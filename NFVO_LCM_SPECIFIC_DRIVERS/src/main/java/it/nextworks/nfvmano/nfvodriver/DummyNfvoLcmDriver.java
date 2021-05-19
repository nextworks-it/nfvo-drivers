/*
* Copyright 2018 Nextworks s.r.l.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package it.nextworks.nfvmano.nfvodriver;

import it.nextworks.nfvmano.libs.ifa.common.elements.Filter;
import it.nextworks.nfvmano.libs.ifa.common.enums.InstantiationState;
import it.nextworks.nfvmano.libs.ifa.common.enums.OperationStatus;
import it.nextworks.nfvmano.libs.ifa.common.enums.ResponseCode;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.*;
import it.nextworks.nfvmano.libs.ifa.common.messages.GeneralizedQueryRequest;
import it.nextworks.nfvmano.libs.ifa.common.messages.SubscribeRequest;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.Nsd;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.NsLcmConsumerInterface;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.elements.ScaleNsData;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.*;
import it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsInfo;
import it.nextworks.nfvmano.libs.ifa.records.vnfinfo.VnfInfo;
import it.nextworks.nfvmano.nfvodriver.elicensing.ElicenseManagementProviderInterface;
import it.nextworks.nfvmano.nfvodriver.monitoring.MonitoringManager;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.QueryNsdResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.nextworks.nfvmano.nfvodriver.*;

import java.util.*;

public class DummyNfvoLcmDriver extends NfvoLcmAbstractDriver {
	
	private Map<String, NsInfo> nsInstances = new HashMap<>();
	private Map<String, OperationStatus> operations = new HashMap<>();
	private List<String> subscriptions = new ArrayList<>();
	private ElicenseManagementProviderInterface eLicensingManager = null;
	private MonitoringManager monitoringManager = null;
	private NfvoCatalogueService nfvoCatalogueService;
	
	private NfvoLcmOperationPollingManager nfvoOperationPollingManager;
	Map<String, List<VnfInfo>> nsVnfInfos = new HashMap<>();
	private static final Logger log = LoggerFactory.getLogger(DummyNfvoLcmDriver.class);


	public DummyNfvoLcmDriver(String nfvoAddress,
							  NfvoLcmNotificationInterface nfvoNotificationManager,
							  NfvoLcmOperationPollingManager nfvoOperationPollingManager,
							  ElicenseManagementProviderInterface eLicensingManager,
							  MonitoringManager monitoringManager,
							  NfvoCatalogueService nfvoCatalogueService) {
		super(NfvoLcmDriverType.DUMMY, nfvoAddress, nfvoNotificationManager);
		this.nfvoOperationPollingManager = nfvoOperationPollingManager;
		this.eLicensingManager = eLicensingManager;
		this.monitoringManager= monitoringManager;
		this.nfvoCatalogueService= nfvoCatalogueService;
	}

	@Override
	public String createNsIdentifier(CreateNsIdentifierRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		request.isValid();
		log.debug("Generating new NS identifier for:" +request.getNsdId());
		UUID nsInfoUuid = UUID.randomUUID();

		String nsdInfoId = request.getNsdId();

		String nsInfoId = nsInfoUuid.toString();
		log.debug("Generated NS identifier " + nsInfoId);
		NsInfo nsInfo = new NsInfo(nsInfoId, 
				request.getNsName(), 				//nsName 
				request.getNsDescription(),			//description 
				request.getNsdId(),					//nsdId 
				null, 								//flavourId 
				null, 								//vnfInfoId
				null, 								//nestedNsInfoId
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
            if(eLicensingManager!=null){
                Map<String, String> elicenseMetadata = new HashMap<>();
                elicenseMetadata.putAll(request.getAdditionalParamForNs());
                elicenseMetadata.put("NS_ID", nsInstanceId);
                elicenseMetadata.put("NSD_ID", nsInfo.getNsdId());

                eLicensingManager.activateElicenseManagement(elicenseMetadata);
            }
			nsInfo.setFlavourId(request.getFlavourId());
			nsInfo.setInstantiationLevel(request.getNsInstantiationLevelId());
			nsInfo.setNsState(InstantiationState.INSTANTIATED);
			Map<String, String > nsInfoConfParams = request.getConfigurationParameterList();
			nsInfoConfParams.putAll(request.getAdditionalParamForNs());
			nsInfo.setConfigurationParameters(nsInfoConfParams);
			log.debug("Adding NsInfo with configuration parameters:"+nsInfoConfParams);
			nsInstances.put(nsInstanceId, nsInfo);
			List<VnfInfo> currentVnfInfos = new ArrayList<>();
			int i=0;
			for(String vnfdId : retrieveNSD(nsInfo.getNsdId()).getVnfdId()){
				log.debug("Generating VnfInfo for:"+vnfdId);
				VnfInfo info = new VnfInfo(Integer.toString(i),vnfdId+"_"+i, "", vnfdId,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						InstantiationState.INSTANTIATED,
						null,
						null);
				currentVnfInfos.add(info);
				nsInfo.addVnfInfo(info.getVnfInstanceId(), i, vnfdId);
			}
			nsVnfInfos.put(nsInstanceId,currentVnfInfos);
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
			if(monitoringManager!=null){
				try {
					monitoringManager.activateNsMonitoring(nsInstances.get(nsId),retrieveNSD(nsInstances.get(nsId).getNsdId()),nsVnfInfos.get(nsId));

				} catch (AlreadyExistingEntityException e) {
					log.error("", e);
					throw new FailedOperationException(e);
				}
			}
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
			if(eLicensingManager!=null){
				eLicensingManager.terminateElicenseManagement();
			}
			if(monitoringManager!=null){
				log.debug("Deactivating monitoring for instance");
				monitoringManager.deactivateNsMonitoring(nsInstances.get(nsInstanceId).getNsInstanceId());
			}

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


	private Nsd retrieveNSD(String nsdInfoId) throws  FailedOperationException{
		HashMap<String,String> parameters = new HashMap<>();
		parameters.put("NSD_INFO_ID",nsdInfoId);
		GeneralizedQueryRequest generalizedQueryRequest = new GeneralizedQueryRequest(new Filter(parameters),null);
		QueryNsdResponse queryNsdResponse  = null;
		try {
			queryNsdResponse = nfvoCatalogueService.queryNsd(generalizedQueryRequest);
			return queryNsdResponse.getQueryResult().get(0).getNsd();
		} catch (Exception e) {
			log.error("",e);
			throw new FailedOperationException(e);
		}
	}


}
