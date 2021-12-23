package it.nextworks.nfvmano.nfvodriver.osm10;


import io.swagger.client.model.*;
import it.nextworks.nfvmano.libs.ifa.common.elements.Filter;
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
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.HealNsRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.InstantiateNsRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.ScaleNsRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.TerminateNsRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.UpdateNsRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.*;
import it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsInfo;
import it.nextworks.nfvmano.libs.ifa.records.vnfinfo.VnfInfo;
import it.nextworks.nfvmano.libs.ifasol.catalogues.interfaces.enums.NsdFormat;
import it.nextworks.nfvmano.libs.ifasol.catalogues.interfaces.messages.QueryNsdResponse;
import it.nextworks.nfvmano.nfvodriver.*;
import it.nextworks.nfvmano.nfvodriver.elicensing.ElicenseManagementProviderInterface;
import it.nextworks.nfvmano.nfvodriver.monitoring.MonitoringManager;
import it.nextworks.nfvmano.nfvodriver.osm.IfaOsmLcmTranslator;
import it.nextworks.nfvmano.nfvodriver.osm.MonitoringInfo;
import it.nextworks.nfvmano.nfvodriver.osm.OAuthSimpleClient;

import it.nextworks.nfvmano.nfvodriver.osm.OsmNsLcmOperationStatus;
import it.nextworks.openapi.msno.model.NsLcmOperationStateType;
import it.nextworks.osm.ApiClient;
import it.nextworks.osm.ApiException;
import it.nextworks.osm.openapi.NsInstancesApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Osm10LcmDriver extends NfvoLcmAbstractDriver {

	private static final Logger log = LoggerFactory.getLogger(Osm10LcmDriver.class);

	private NsInstancesApi nsInstancesApi;

	private String version = "v1";
	private String accept = "*/*";
	private String contentType = "application/json";
	private String authorization = null;		//TODO: this is to be fixed - it should be the token

	private String callbackUri;
	private String nfvoAddress;
	private boolean enablePolling = true;
	private String username;
	private String password;
	private String project;
	private final OAuthSimpleClient oAuthSimpleClient;
	private String vimAccountId;
	private Map<String, CreateNsIdentifierRequest> requestMap = new HashMap<>();

	private final List<MonitoringInfo> monitoringQueue = new ArrayList<>();
	private NfvoLcmOperationPollingManager timeoNfvoOperationPollingManager;

	private NfvoCatalogueService nfvoCatalogueService;
	private MonitoringManager monitoringManager;
	private ElicenseManagementProviderInterface elicenseMgr;
	public Osm10LcmDriver(String nfvoAddress,
						  NfvoLcmNotificationInterface nfvoNotificationsManager,
						  NfvoLcmOperationPollingManager timeoNfvoOperationPollingManager,
						  String callbackUri, int timeout,
						  boolean enablePolling,
						  String username,
						  String password,
						  String project,
						  String vimAccountId,
						  NfvoCatalogueService nfvoCatalogueService,
						  MonitoringManager monitoringManager, ElicenseManagementProviderInterface elicenseMgr) {
		super(NfvoLcmDriverType.OSM10, nfvoAddress, nfvoNotificationsManager);
		this.timeoNfvoOperationPollingManager = timeoNfvoOperationPollingManager;
		this.nfvoAddress = nfvoAddress;
		ApiClient ac = new ApiClient();
		ac.setConnectTimeout(timeout);
		ac.setReadTimeout(timeout);
		ac.setWriteTimeout(timeout);
		String url = nfvoAddress;
		ac = ac.setBasePath(url);
		this.nsInstancesApi = new NsInstancesApi();
		this.enablePolling= enablePolling;
		this.username= username;
		this.password= password;
		this.project = project;
		this.oAuthSimpleClient = new OAuthSimpleClient(nfvoAddress+"/osm/admin/v1/tokens", username, password, project);
		log.debug("OSM10 driver configured with base path: " + nsInstancesApi.getApiClient().getBasePath());
		this.callbackUri = callbackUri;
		this.vimAccountId= vimAccountId;
		this.nfvoCatalogueService=nfvoCatalogueService;
		this.monitoringManager = monitoringManager;
		this.elicenseMgr = elicenseMgr;
	}

	/*
	public setApiEndpoint() {
		ApiClient ac = new ApiClient();
		String url = "http://" + this.nfvoAddress + "/nslcm/v1";
		ac.setBasePath(url);
		restClient.setApiClient(ac);
	}
	*/
	
	public String createNsIdentifier(CreateNsIdentifierRequest request) 
			throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {
		if (request == null) throw new MalformattedElementException("Create NS identifier request is null.");
		request.isValid();
		log.debug("Building create NS identifier request in SOL 005 format");
		CreateNsRequest body = IfaOsm10LcmTranslator.getCreateNsRequest(request, UUID.fromString(vimAccountId), UUID.fromString(request.getNsdId()));
		log.debug("Create NS request {}", body.toString());
		try {

			nsInstancesApi.setApiClient(getClient());
			ObjectId nsInstance = nsInstancesApi.addNSinstance(body);;

			log.debug("NS instance response {}", nsInstance.toString());
			String nsId = nsInstance.getId().toString();
			log.debug("Created NS instance with ID " + nsId);
			return nsId;
		} catch ( it.nextworks.osm.ApiException e) {
			log.error("Error creating new instance {}", e.getMessage());
			throw new FailedOperationException("Failure when interacting with NFVO: " + e.getMessage());
		}
	}
	
	public String instantiateNs(InstantiateNsRequest request) 
			throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {
		if (request == null) throw new MalformattedElementException("Instantiate NS request is null.");
		//request.isValid();
		log.debug("Building instantiate NS request in SOL 005 format");
		String nsInstanceId = request.getNsInstanceId();

		nsInstancesApi.setApiClient(getClient());

		//Now we can instantiate NSD
		try {
			NsInstance nsInstance = nsInstancesApi.getNSinstance(nsInstanceId);
			if(elicenseMgr!=null){
				Map<String, String> elicensingMetadata = new HashMap<>();
				elicensingMetadata.putAll(request.getAdditionalParamForNs());
				elicensingMetadata.put("NS_ID", nsInstanceId);
				elicensingMetadata.put("NSD_ID", nsInstance.getNsd().getId());

				elicenseMgr.activateElicenseManagement(elicensingMetadata);
			}
			io.swagger.client.model.InstantiateNsRequest instantiateNsRequest =
					IfaOsm10LcmTranslator.getInstantiateNsRequest(request,nsInstance.getName(), nsInstance.getNsd().getIdentifier(),UUID.fromString(vimAccountId));

			ObjectId objOperationId = nsInstancesApi.instantiateNSinstance(nsInstanceId, instantiateNsRequest);
			UUID operationId = objOperationId.getId();
			if(timeoNfvoOperationPollingManager!=null)
				timeoNfvoOperationPollingManager.addOperation(operationId.toString(), OperationStatus.SUCCESSFULLY_DONE, request.getNsInstanceId(), "NS_INSTANTIATION");
			String nsdInfoId = "";
			String nsdName= "";
			log.debug("Created instance " + nsInstanceId+" from descriptor " + nsInstance.getNsd().getId() +" with UUID: " + nsInstance.getNsd().getIdentifier());
			if(monitoringManager!=null)
				monitoringQueue.add(new MonitoringInfo(request.getNsInstanceId(),nsInstance.getNsd().getId(),null,null,operationId.toString()));
			return operationId.toString(); //TODO if return the same id of the request?
		} catch (it.nextworks.osm.ApiException e) {
			log.error("Cannot instantiate Network service.\n" + e.getResponseBody());
			throw new FailedOperationException();
		}
	}
	
	public String scaleNs(ScaleNsRequest request) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}
	
	public UpdateNsResponse updateNs(UpdateNsRequest request) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}
	
	public QueryNsResponse queryNs(GeneralizedQueryRequest request) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {
		if (request == null) throw new MalformattedElementException("Query NS request is null.");
		request.isValid();
		try {
			String nsInstanceId = request.getFilter().getParameters().get("NS_ID");
			log.debug("Building terminate NS request in SOL 005 format");
			NsInstance nsInstance = nsInstancesApi.getNSinstance(nsInstanceId);
			if (nsInstance == null) throw new NotExistingEntityException("NS instance not found");
			List<NsInfo> queryNsResult = new ArrayList<NsInfo>();

			InstantiationState status;
			if(nsInstance.getNsStatus()==NsStatus.INSTANTIATED){
				status = InstantiationState.INSTANTIATED;
			}else if(nsInstance.getNsStatus()==NsStatus.NOT_INSTANTIATED){
				status = InstantiationState.NOT_INSTANTIATED;
			}else {
				status = InstantiationState.FAILED;
			}
			NsInfo nsInfo = new NsInfo(nsInstanceId, nsInstance.getName(), nsInstance.getDescription(),
					nsInstance.getNsd().getId(), "default", null, null, status, null, null, null, null  );
			queryNsResult.add(nsInfo);
			QueryNsResponse response = new QueryNsResponse(ResponseCode.OK, queryNsResult);
			return response;
		} catch (Exception e) {
			throw new FailedOperationException("Failure when interacting with NFVO: " + e.getMessage());
		}
	}

	public String terminateNs(TerminateNsRequest request) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {
		if (request == null) throw new MalformattedElementException("Terminate NS request is null.");
		request.isValid();
		log.debug("Building terminate NS request in SOL 005 format");
		io.swagger.client.model.TerminateNsRequest requestTranslate = IfaOsmLcmTranslator.getTerminateNsRequest(request);

		try {
			nsInstancesApi.setApiClient(getClient());
			if(elicenseMgr!=null){
				elicenseMgr.terminateElicenseManagement();
			}
			ObjectId objOperationId = nsInstancesApi.terminateNSinstance(request.getNsInstanceId(), requestTranslate);
			String operationId = objOperationId.getId().toString();
			if(timeoNfvoOperationPollingManager!=null)
				timeoNfvoOperationPollingManager.addOperation(operationId, OperationStatus.SUCCESSFULLY_DONE, request.getNsInstanceId(), "NS_TERMINATION");
			log.debug("Added polling task for NFVO operation " + operationId);

			return operationId;
		} catch (it.nextworks.osm.ApiException e) {
			log.debug("Cannot terminare NS " + request.getNsInstanceId());
			throw new FailedOperationException(e.getMessage());
		}
	}
	
	public void deleteNsIdentifier(String nsInstanceIdentifier) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException {
		/*
		if (nsInstanceIdentifier == null) throw new FailedOperationException("Delete NS ID request with null ID.");
		log.debug("Building SOL 005 request to remove NS ID.");
		try {
			restClient.nsInstancesNsInstanceIdDelete(nsInstanceIdentifier, version, authorization);
		} catch (ApiException e) {
			throw new FailedOperationException("Failure when interacting with NFVO: " + e.getMessage());
		}*/
	}
	
	public String healNs(HealNsRequest request) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}
	
	public OperationStatus getOperationStatus(String operationId) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {
		try {
			nsInstancesApi.setApiClient(getClient());
			NsLcmOpOcc nsLcmOpOcc = nsInstancesApi.getNSLCMOpOcc(operationId);
			OsmNsLcmOperationStatus osmOperationStatus = OsmNsLcmOperationStatus.valueOf(nsLcmOpOcc.getOperationState());
			//activate monitoring if needed
			if(OsmNsLcmOperationStatus.COMPLETED==osmOperationStatus){
				MonitoringInfo monitoringInfo = getMonitoringInfoByOpId(monitoringQueue,operationId);
				if(monitoringInfo != null){
					//creating associations with VnfInfos for this nsInstanceId and update content of nsInfo
					List<VnfInfo> vnfInfoList = getVnfInfos(monitoringInfo.getNsInstanceId(),monitoringInfo.getNsInfo());

					//activate monitoring
					manageMonitoring(monitoringInfo.getNsInstanceId(),monitoringInfo.getIfaNsdId(),monitoringInfo.getIfaNsdVersion(),vnfInfoList);
				}
			}
			return IfaOsmLcmTranslator.getOperationSatus(osmOperationStatus);
		} catch (ApiException e) {
			log.error("Cannot retrieve operational status");
			throw new FailedOperationException(e.getMessage());
		}

	}



	/**
	 * This function activate the monitoring for the osm ns instance.
	 * It retrieve the Nsd related to this instance from OsmCatalogue and
	 * passes it to the MonitoringManager
	 * @param nsInstanceId
	 * @param ifaNsdId
	 * @param version
	 * @throws FailedOperationException
	 */
	private void manageMonitoring(String nsInstanceId, String ifaNsdId, String version, List<VnfInfo> vnfInfoList) throws FailedOperationException {
		//Going to retrieve nsd id and version in order to get the complete ifa nsd from the catalogue
		HashMap<String,String> parameters = new HashMap<>();
		parameters.put("NSD_ID",ifaNsdId);
		parameters.put("NSD_VERSION",version);
		GeneralizedQueryRequest generalizedQueryRequest = new GeneralizedQueryRequest(new Filter(parameters),null);
		QueryNsdResponse queryNsdResponse  = null;
		try {
			queryNsdResponse = nfvoCatalogueService.queryNsd(generalizedQueryRequest);
		} catch (Exception e) {
			log.error("Cannot query the NSD.");
			e.printStackTrace();
		}
		if(queryNsdResponse == null){
			log.error("No entry in repository for " + ifaNsdId);
			throw new FailedOperationException();
		}
		try {
			if(queryNsdResponse.getNsdFormat()== NsdFormat.SOL006){
				//monitoringManager.activateNsMonitoring(nsInstanceId,
				//		((QueryNsdIfaResponse)queryNsdResponse).getQueryResult().get(0).getNsd(),
				//		vnfInfoList);
			}

		} catch (Exception e) {
			log.error("Error while activating ns monitoring");
			throw new FailedOperationException();
		}
	}
	/**
	 * This function retrieve the VnfInstanceInfo the are related to this osm
	 * ns instance and build the list of VnfInfo.
	 * @param osmNsInstanceId
	 * @return List<VnfInfo>
	 * @throws FailedOperationException
	 */
	protected List<VnfInfo> getVnfInfos(String osmNsInstanceId, NsInfo nsInfo) throws FailedOperationException {
		int i=0;
		List<VnfInfo> vnfInfos = new ArrayList<>();
		try{
			nsInstancesApi.setApiClient(getClient());
			//Multiple vnf with the same NsrIdRef can be present
			for(VnfInstanceInfo vnfInstanceInfo : nsInstancesApi.getVnfInstances()){
				if(vnfInstanceInfo.getNsrIdRef().equals(osmNsInstanceId)) {
					//This vnf is an instance of the ns. Need to create a new VnfInfo
					HashMap<String,String> metadata = new HashMap<>();
					// ip on mgmt net
					metadata.put("IP",vnfInstanceInfo.geIpAddress());
					VnfInfo vnfInfo = new VnfInfo(
							vnfInstanceInfo.getId().toString(),
							null,
							null,
							vnfInstanceInfo.getVnfdId(),
							null,
							null,
							null,
							null,
							null,
							null,
							null,
							InstantiationState.INSTANTIATED,
							metadata,
							null);
					vnfInfos.add(vnfInfo);
					//what index represent? a local counter or the index of vnf within the nsd?
					//add(uuid_of_vnf_instance,index,id_of_referred_vnfd_package)
					nsInfo.addVnfInfo(vnfInstanceInfo.getId().toString(),i++,vnfInstanceInfo.getVnfdRef());
				}
			}
		} catch (FailedOperationException | it.nextworks.osm.ApiException e) {
			log.error("Cannot retrieve Vnf Instance Infos");
			throw new FailedOperationException();
		}
		return vnfInfos;
	}


	private MonitoringInfo getMonitoringInfoByOpId(List<MonitoringInfo> monitoringQueue, String operationId) {
		for(int i=0; i<monitoringQueue.size(); i++){
			if(monitoringQueue.get(i).getOperationId().equals(operationId))
				return monitoringQueue.remove(i);
		}
		return null;
	}
	public String subscribeNsLcmEvents(SubscribeRequest request, NsLcmConsumerInterface consumer) throws MethodNotImplementedException, MalformattedElementException, FailedOperationException {
		throw new MethodNotImplementedException();
	}
	
	public void unsubscribeNsLcmEvents(String subscriptionId) throws MethodNotImplementedException, MalformattedElementException, NotExistingEntityException, FailedOperationException {
		throw new MethodNotImplementedException();
	}
	
	public void queryNsSubscription(GeneralizedQueryRequest request) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}
	



	private it.nextworks.osm.ApiClient getClient() throws FailedOperationException {

		ApiClient apiClient = new ApiClient();
		apiClient.setHttpClient(OAuthSimpleClient.getUnsafeOkHttpClient());
		apiClient.setBasePath(this.getNfvoAddress()+"/osm");
		apiClient.setUsername(username);
		apiClient.setPassword(password);
		apiClient.setAccessToken(oAuthSimpleClient.getToken());
		apiClient.setDebugging(true);
		//HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		//interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		//apiClient.getHttpClient().interceptors().add(interceptor);
		//apiClient.setDebugging(true);
		return apiClient;
	}

}
