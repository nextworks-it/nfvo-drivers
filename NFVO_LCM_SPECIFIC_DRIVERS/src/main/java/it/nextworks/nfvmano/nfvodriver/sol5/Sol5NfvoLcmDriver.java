package it.nextworks.nfvmano.nfvodriver.sol5;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.nextworks.nfvmano.libs.ifa.common.enums.OperationStatus;
import it.nextworks.nfvmano.libs.ifa.common.enums.ResponseCode;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.common.messages.GeneralizedQueryRequest;
import it.nextworks.nfvmano.libs.ifa.common.messages.SubscribeRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.NsLcmConsumerInterface;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.CreateNsIdentifierRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.HealNsRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.InstantiateNsRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.QueryNsResponse;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.ScaleNsRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.TerminateNsRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.UpdateNsRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.UpdateNsResponse;
import it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsInfo;
import it.nextworks.nfvmano.nfvodriver.NfvoLcmAbstractDriver;
import it.nextworks.nfvmano.nfvodriver.NfvoLcmDriverType;
import it.nextworks.nfvmano.nfvodriver.NfvoLcmNotificationInterface;
import it.nextworks.nfvmano.nfvodriver.NfvoLcmOperationPollingManager;
import it.nextworks.openapi.ApiClient;
import it.nextworks.openapi.ApiException;
import it.nextworks.openapi.ApiResponse;
import it.nextworks.openapi.msno.DefaultApi;
import it.nextworks.openapi.msno.model.CreateNsRequest;
import it.nextworks.openapi.msno.model.NsInstance;
import it.nextworks.openapi.msno.model.NsInstance2;
import it.nextworks.openapi.msno.model.NsLcmOpOcc;
import it.nextworks.openapi.msno.model.NsLcmOperationStateType;

public class Sol5NfvoLcmDriver extends NfvoLcmAbstractDriver {

	private static final Logger log = LoggerFactory.getLogger(Sol5NfvoLcmDriver.class);
	
	private DefaultApi restClient;
	
	private String version = "v1";
	private String accept = "application/json";
	private String contentType = "application/json";
	private String authorization = null;		//TODO: this is to be fixed - it should be the token
	
	private String callbackUri;
	
	@Autowired
	private NfvoLcmOperationPollingManager timeoNfvoOperationPollingManager;
	
	public Sol5NfvoLcmDriver(String nfvoAddress,
			NfvoLcmNotificationInterface nfvoNotificationsManager,
			NfvoLcmOperationPollingManager timeoNfvoOperationPollingManager,
			String callbackUri) {
		super(NfvoLcmDriverType.SOL_5, nfvoAddress, nfvoNotificationsManager);
		this.timeoNfvoOperationPollingManager = timeoNfvoOperationPollingManager;
		ApiClient ac = new ApiClient();
		String url = "http://" + nfvoAddress + "/nslcm/v1";
		ac.setBasePath(url);
		this.callbackUri = callbackUri;
	}
	
	public String createNsIdentifier(CreateNsIdentifierRequest request) 
			throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {
		if (request == null) throw new MalformattedElementException("Create NS identifier request is null.");
		request.isValid();
		log.debug("Building create NS identifier request in SOL 005 format");
		CreateNsRequest body = IfaSolLcmTranslator.buildSolCreateNsRequest(request);
		try {
			NsInstance nsInstance = restClient.nsInstancesPost(version, accept, contentType, body, authorization);
			String nsId = nsInstance.getId();
			log.debug("Created NS instance with ID " + nsId);
			return nsId;
		} catch (ApiException e) {
			log.error("Error creating new instance {}", e.getMessage());
			throw new FailedOperationException("Failure when interacting with NFVO: " + e.getMessage());
		}
	}
	
	public String instantiateNs(InstantiateNsRequest request) 
			throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {
		if (request == null) throw new MalformattedElementException("Instantiate NS request is null.");
		request.isValid();
		log.debug("Building instantiate NS request in SOL 005 format");
		String nsInstanceId = request.getNsInstanceId();
		it.nextworks.openapi.msno.model.InstantiateNsRequest body = IfaSolLcmTranslator.buildSolInstantiateNsRequest(request);
		try {
			ApiResponse<NsInstance2> nsInstanceResponse = restClient.nsInstancesNsInstanceIdInstantiatePostWithHttpInfo(nsInstanceId, accept, contentType, version, body, authorization);
			String operationId = readOperationIdFromResponse(nsInstanceResponse);
			timeoNfvoOperationPollingManager.addOperation(operationId, OperationStatus.SUCCESSFULLY_DONE, request.getNsInstanceId(), "NS_INSTANTIATION");
			return operationId;
		} catch (ApiException e) {
			log.error("Error instantiating new instance {}", e.getMessage());
			throw new FailedOperationException("Failure when interacting with NFVO: " + e.getMessage());
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
			it.nextworks.openapi.msno.model.NsInstance nsInstance = restClient.nsInstancesNsInstanceIdGet(nsInstanceId, version, accept, contentType, authorization);
			if (nsInstance == null) throw new NotExistingEntityException("NS instance not found");
			List<NsInfo> queryNsResult = new ArrayList<NsInfo>();
			NsInfo nsInfo = IfaSolLcmTranslator.buildIfaNsInfo(nsInstance);
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
		it.nextworks.openapi.msno.model.TerminateNsRequest body = IfaSolLcmTranslator.buildSolTerminateNsRequest(request);
		try {
			ApiResponse<NsInstance2> nsInstanceResponse = restClient.nsInstancesNsInstanceIdTerminatePostWithHttpInfo(request.getNsInstanceId(), accept, contentType, version, body, authorization);
			String operationId = readOperationIdFromResponse(nsInstanceResponse);
			timeoNfvoOperationPollingManager.addOperation(operationId, OperationStatus.SUCCESSFULLY_DONE, request.getNsInstanceId(), "NS_TERMINATION");
			return operationId;
		} catch (ApiException e) {
			throw new FailedOperationException("Failure when interacting with NFVO: " + e.getMessage());
		}
	}
	
	public void deleteNsIdentifier(String nsInstanceIdentifier) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException {
		if (nsInstanceIdentifier == null) throw new FailedOperationException("Delete NS ID request with null ID.");
		log.debug("Building SOL 005 request to remove NS ID.");
		try {
			restClient.nsInstancesNsInstanceIdDelete(nsInstanceIdentifier, version, authorization);
		} catch (ApiException e) {
			throw new FailedOperationException("Failure when interacting with NFVO: " + e.getMessage());
		}
	}
	
	public String healNs(HealNsRequest request) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}
	
	public OperationStatus getOperationStatus(String operationId) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {
		if (operationId == null) throw new MalformattedElementException("Request operation with null ID");
		log.debug("Getting information about LCM operation with ID " + operationId);
		try {
			NsLcmOpOcc operation = restClient.nsLcmOpOccsNsLcmOpOccIdGet(operationId, accept, contentType, version, authorization);
			if (operation == null) throw new NotExistingEntityException("Operation with ID " + operationId + " not found");
			log.debug("Retrieved operation: " + operationId.toString());
			OperationStatus status = translateOperationStatus(operation.getOperationState());
			log.debug("Operation status: " + status.toString());
			return status;
		} catch (ApiException e) {
			throw new FailedOperationException("Failure when interacting with NFVO: " + e.getMessage());
		}
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
	
	private String readOperationIdFromResponse(ApiResponse<?> nsInstanceResponse) throws FailedOperationException {
		log.debug("Reading operation ID from HTTP response: ");
		Map<String, List<String>> headers = nsInstanceResponse.getHeaders();
		List<String> locations = headers.get("Location");
		if (locations != null) {
			String operationUrl = locations.get(0);
			if (operationUrl == null) throw new FailedOperationException("Missing operation ID in location response header."); 
			String opUrls[] = operationUrl.split("/");
			int x = opUrls.length;
			if (x < 1) throw new FailedOperationException("Missing operation ID in location response header.");
			String operationId = opUrls[x-1];
			log.debug("Operation ID: " + operationId);
			return operationId;
		} else {
			log.error("Missing operation ID in location response header.");
			throw new FailedOperationException("Missing operation ID in location response header.");
		}
	}
	
	private OperationStatus translateOperationStatus(NsLcmOperationStateType state) {
		switch (state) {
		case PROCESSING:
			return OperationStatus.PROCESSING;

		case COMPLETED:
			return OperationStatus.SUCCESSFULLY_DONE;
			
		case FAILED:
			return OperationStatus.FAILED;
			
		default: {
			log.error("Unexpected operation status, returning failed");
			return OperationStatus.FAILED;
		}
		}
	}

}
