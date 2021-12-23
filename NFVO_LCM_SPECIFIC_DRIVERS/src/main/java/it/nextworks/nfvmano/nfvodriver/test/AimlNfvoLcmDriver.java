package it.nextworks.nfvmano.nfvodriver.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.nextworks.nfvmano.libs.ifa.common.enums.InstantiationState;
import it.nextworks.nfvmano.libs.ifa.common.enums.OperationStatus;
import it.nextworks.nfvmano.libs.ifa.common.enums.ResponseCode;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.common.messages.GeneralizedQueryRequest;
import it.nextworks.nfvmano.libs.ifa.common.messages.SubscribeRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.NsLcmConsumerInterface;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.*;
import it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsInfo;
import it.nextworks.nfvmano.nfvodriver.NfvoLcmAbstractDriver;
import it.nextworks.nfvmano.nfvodriver.NfvoLcmDriverType;
import it.nextworks.nfvmano.nfvodriver.NfvoLcmNotificationInterface;
import it.nextworks.nfvmano.nfvodriver.NfvoLcmOperationPollingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AimlNfvoLcmDriver extends NfvoLcmAbstractDriver {

    private Map<String, NsInfo> nsInstances = new HashMap<>();
    private Map<String, OperationStatus> operations = new HashMap<>();
    private List<String> subscriptions = new ArrayList<>();

    private String sharedNSInfoId= null;

    private NfvoLcmOperationPollingManager nfvoOperationPollingManager;

    private static final Logger log = LoggerFactory.getLogger(AimlNfvoLcmDriver.class);

    public AimlNfvoLcmDriver(String nfvoAddress,
                                NfvoLcmNotificationInterface nfvoNotificationManager,
                                NfvoLcmOperationPollingManager nfvoOperationPollingManager) {
        super(NfvoLcmDriverType.AIML, nfvoAddress, nfvoNotificationManager);
        this.nfvoOperationPollingManager = nfvoOperationPollingManager;

    }

    @Override
    public String createNsIdentifier(CreateNsIdentifierRequest request) throws MethodNotImplementedException,
            NotExistingEntityException, FailedOperationException, MalformattedElementException {
        request.isValid();
        log.debug("Generating new NS identifier");
        UUID nsInfoUuid = UUID.randomUUID();
        String nsInfoId = nsInfoUuid.toString();
        Map<String, String> vnfPlacement = new HashMap<>();
        vnfPlacement.put("vnf.placement.vTDA", "EDGE");
        vnfPlacement.put("vnf.placement.vCIM", "EDGE");
        List<String> nestedIds = new ArrayList<>();

        if(request.getNsName().contains("shared")){
            sharedNSInfoId = generateNsdInfo("nsCIM-TDA", request.getTenantId(), vnfPlacement);
            nestedIds.add(generateNsdInfo("nsDENMgenerator", request.getTenantId(), null));
        }else{
            nestedIds.add(generateNsdInfo("nsBEV", request.getTenantId(), null));
        }
        nestedIds.add(sharedNSInfoId);
        log.debug("Generated NS identifier " + nsInfoId);
        NsInfo nsInfo = new NsInfo(nsInfoId,
                request.getNsName(), 				//nsName
                request.getNsDescription(),			//description
                request.getNsdId(),					//nsdId
                null, 								//flavourId
                null, 								//vnfInfoId
                nestedIds, 								//nestedNsInfoId
                InstantiationState.NOT_INSTANTIATED,//nsState
                null,								//nsScaleStatus
                null,								//additionalAffinityOrAntiAffinityRule
                request.getTenantId(),				//tenantId
                null);		//configurationParameters)



        nsInstances.put(nsInfoId, nsInfo);

        return nsInfoId;
    }

    @Override
    public String instantiateNs(InstantiateNsRequest request) throws MethodNotImplementedException,
            NotExistingEntityException, FailedOperationException, MalformattedElementException {
        request.isValid();
        ObjectMapper mapper = new ObjectMapper();
        try{
            log.debug("Received InstantiateNsRequest: "+mapper.writeValueAsString(request));
        }catch (Exception e){
            log.error("Cannot deserialize InstantiateNsRequest",e);
        }

        String nsInstanceId = request.getNsInstanceId();
        log.debug("Received request to instantiate NS instance " + nsInstanceId);
        if (nsInstances.containsKey(nsInstanceId)) {
            log.debug("Generating new operation identifier");
            UUID operationUuid = UUID.randomUUID();
            String operationId = operationUuid.toString();
            log.debug("Generated operation identifier " + operationId);
            operations.put(operationId, OperationStatus.SUCCESSFULLY_DONE);
            NsInfo nsInfo = nsInstances.get(nsInstanceId);
            for(String nestedNsi : nsInfo.getNestedNsInfoId()){
                if(nestedNsi != null) {
                    NsInfo nestedNsInfo = nsInstances.get(nestedNsi);

                    nestedNsInfo.setNsState(InstantiationState.INSTANTIATED);

                    nsInstances.put(nestedNsi, nestedNsInfo);
                }

            }
            nsInfo.setFlavourId(request.getFlavourId());
            nsInfo.setInstantiationLevel(request.getNsInstantiationLevelId());
            nsInfo.setNsState(InstantiationState.INSTANTIATED);
            //nsInfo.setConfigurationParameters(request.getConfigurationParameterList());
            nsInstances.put(nsInstanceId, nsInfo);
            nfvoOperationPollingManager.addOperation(operationId, OperationStatus.SUCCESSFULLY_DONE, request.getNsInstanceId(), "NS_INSTANTIATION");
            log.debug("Added polling task for NFVO operation " + operationId);
            return operationId;
        } else {
            log.error("NS instance "+ nsInstanceId + " not found");
            throw new NotExistingEntityException("NS instance "+ nsInstanceId + " not found");
        }
    }

    @Override
    public String scaleNs(ScaleNsRequest request) throws MethodNotImplementedException, NotExistingEntityException,
            FailedOperationException, MalformattedElementException {
        request.isValid();
        String nsInstanceId = request.getNsInstanceId();
        log.debug("Received request to scale NS instance " + nsInstanceId);
        if (nsInstances.containsKey(nsInstanceId)) {
            log.debug("Generating new operation identifier");
            UUID operationUuid = UUID.randomUUID();
            String operationId = operationUuid.toString();
            operations.put(operationId, OperationStatus.SUCCESSFULLY_DONE);
            nfvoOperationPollingManager.addOperation(operationId, OperationStatus.SUCCESSFULLY_DONE, request.getNsInstanceId(), "NS_SCALING");
            log.debug("Added polling task for NFVO operation " + operationId);
            return operationId;
        }
        else {
            log.error("NS instance "+ nsInstanceId + " not found");
            throw new MethodNotImplementedException("Scale NS not supported");
        }
    }

    @Override
    public UpdateNsResponse updateNs(UpdateNsRequest request) throws MethodNotImplementedException,
            NotExistingEntityException, FailedOperationException, MalformattedElementException {
        throw new MethodNotImplementedException("Update NS not supported");
    }

    @Override
    public QueryNsResponse queryNs(GeneralizedQueryRequest request) throws MethodNotImplementedException,
            NotExistingEntityException, FailedOperationException, MalformattedElementException {
        request.isValid();
        log.debug("Received NS query");
        Map<String, String> filter = request.getFilter().getParameters();
        String nsId = filter.get("NS_ID");
        if (nsId == null) {
            log.error("Received NFV NS instance query without NS instance ID");
            throw new MalformattedElementException("NS instance queries are supported only with NS ID.");
        }
        if (nsInstances.containsKey(nsId)) {
            List<NsInfo> queryNsResult = new ArrayList<NsInfo>();
            queryNsResult.add(nsInstances.get(nsId));
            return new QueryNsResponse(ResponseCode.OK, queryNsResult);
        } else {
            log.error("NS instance " + nsId + " not found.");
            throw new NotExistingEntityException("NS instance " + nsId + " not found.");
        }
    }

    @Override
    public String terminateNs(TerminateNsRequest request) throws MethodNotImplementedException,
            NotExistingEntityException, FailedOperationException, MalformattedElementException {
        request.isValid();
        String nsInstanceId = request.getNsInstanceId();
        log.debug("Received request to terminate NS instance " + nsInstanceId);
        if (nsInstances.containsKey(nsInstanceId)) {
            log.debug("Generating new operation identifier");
            UUID operationUuid = UUID.randomUUID();
            String operationId = operationUuid.toString();
            log.debug("Generated operation identifier " + operationId);
            operations.put(operationId, OperationStatus.SUCCESSFULLY_DONE);
            NsInfo nsInfo = nsInstances.get(nsInstanceId);
            nsInfo.setNsState(InstantiationState.NOT_INSTANTIATED);
            nsInstances.put(nsInstanceId, nsInfo);
            nfvoOperationPollingManager.addOperation(operationId, OperationStatus.SUCCESSFULLY_DONE, request.getNsInstanceId(), "NS_TERMINATION");
            log.debug("Added polling task for NFVO operation " + operationId);
            return operationId;
        } else {
            log.error("NS instance "+ nsInstanceId + " not found");
            throw new NotExistingEntityException("NS instance "+ nsInstanceId + " not found");
        }
    }

    @Override
    public void deleteNsIdentifier(String nsInstanceIdentifier)
            throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException {
        log.debug("Received request to remove NS instance ID " + nsInstanceIdentifier);
        if (nsInstances.containsKey(nsInstanceIdentifier)) {
            nsInstances.remove(nsInstanceIdentifier);
            log.debug("NS instance ID " + nsInstanceIdentifier + " removed from internal DB");
        } else {
            log.error("NS instance "+ nsInstanceIdentifier + " not found");
            throw new NotExistingEntityException("NS instance "+ nsInstanceIdentifier + " not found");
        }
    }

    @Override
    public String healNs(HealNsRequest request) throws MethodNotImplementedException, NotExistingEntityException,
            FailedOperationException, MalformattedElementException {
        throw new MethodNotImplementedException("Heal NS not supported");
    }

    @Override
    public OperationStatus getOperationStatus(String operationId) throws MethodNotImplementedException,
            NotExistingEntityException, FailedOperationException, MalformattedElementException {
        log.debug("Received query about status of operation " + operationId);
        if (operations.containsKey(operationId)) return operations.get(operationId);
        else throw new NotExistingEntityException("Operation "+ operationId + " not found");
    }

    @Override
    public String subscribeNsLcmEvents(SubscribeRequest request, NsLcmConsumerInterface consumer)
            throws MethodNotImplementedException, MalformattedElementException, FailedOperationException {
        log.debug("Generating new subscription identifier");
        UUID subscriptionUuid = UUID.randomUUID();
        String subscriptionId = subscriptionUuid.toString();
        log.debug("Generated subscription identifier " + subscriptionId);
        subscriptions.add(subscriptionId);
        return subscriptionId;
    }

    @Override
    public void unsubscribeNsLcmEvents(String subscriptionId) throws MethodNotImplementedException,
            MalformattedElementException, NotExistingEntityException, FailedOperationException {
        if (subscriptions.contains(subscriptionId)) {
            subscriptions.remove(subscriptionId);
        } else throw new NotExistingEntityException("Subscription not found");

    }

    @Override
    public void queryNsSubscription(GeneralizedQueryRequest request) throws MethodNotImplementedException,
            NotExistingEntityException, FailedOperationException, MalformattedElementException {
        throw new MethodNotImplementedException("Subscriptions not supported");

    }

    private String generateNsdInfo(String nsdId, String tenant, Map<String, String> configurationParameters  ){
        UUID nestedNsInfoUuid = UUID.randomUUID();
        String nestedNsInfoId = nestedNsInfoUuid.toString();
        log.debug("Generated NS identifier " + nestedNsInfoId);

        String currentDf = nsdId+"_df";
        String currentIl = nsdId+"_il_small";
        NsInfo nsInfo = new NsInfo(nestedNsInfoId,
                nsdId, 				//nsName
                nsdId,			//description
                nsdId,					//nsdId
                currentDf, 								//flavourId
                null, 								//vnfInfoId
                null, 								//nestedNsInfoId
                InstantiationState.NOT_INSTANTIATED,//nsState
                null,								//nsScaleStatus
                null,								//additionalAffinityOrAntiAffinityRule
                tenant,				//tenantId
                configurationParameters);		//configurationParameters)
        nsInfo.setInstantiationLevel(currentIl);
        nsInstances.put(nestedNsInfoId, nsInfo);
        return nestedNsInfoId;
    }
}
