package it.nextworks.nfvmano.nfvodriver.sol5;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import it.nextworks.nfvmano.libs.ifa.common.enums.OperationStatus;
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
import it.nextworks.nfvmano.nfvodriver.NfvoLcmAbstractDriver;
import it.nextworks.nfvmano.nfvodriver.NfvoLcmDriverType;
import it.nextworks.nfvmano.nfvodriver.NfvoLcmNotificationInterface;
import it.nextworks.nfvmano.nfvodriver.NfvoLcmOperationPollingManager;
import it.nextworks.openapi.ApiClient;
import it.nextworks.openapi.ApiException;
import it.nextworks.openapi.msno.DefaultApi;
import it.nextworks.openapi.msno.model.CreateNsRequest;
import it.nextworks.openapi.msno.model.NsInstance;
import it.nextworks.openapi.msno.model.NsInstance2;

public class Sol5NfvoLcmDriver extends NfvoLcmAbstractDriver {

	private static final Logger log = LoggerFactory.getLogger(Sol5NfvoLcmDriver.class);
	
	private DefaultApi restClient;
	
	private String version = "v1";
	private String accept = "application/json";
	private String contentType = "application/json";
	private String authorization = null;		//TODO: this is to be fixed - it should be the token
	
	private String callbackUri;
	
	public Sol5NfvoLcmDriver(String nfvoAddress,
			NfvoLcmNotificationInterface nfvoNotificationsManager,
			NfvoLcmOperationPollingManager timeoNfvoOperationPollingManager,
			String callbackUri) {
		super(NfvoLcmDriverType.SOL_5, nfvoAddress, nfvoNotificationsManager);
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
			NsInstance2 nsInstance = restClient.nsInstancesNsInstanceIdInstantiatePost(nsInstanceId, accept, contentType, version, body, authorization);
			String operationId = "";	//TODO: this must be fixed when the new version of the REST API is available.
			return operationId;
		} catch (ApiException e) {
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
		throw new MethodNotImplementedException();
	}

	public String terminateNs(TerminateNsRequest request) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {
		if (request == null) throw new MalformattedElementException("Instantiate NS request is null.");
		request.isValid();
		log.debug("Building terminate NS request in SOL 005 format");
		it.nextworks.openapi.msno.model.TerminateNsRequest body = IfaSolLcmTranslator.buildSolTerminateNsRequest(request);
		try {
			restClient.nsInstancesNsInstanceIdTerminatePost(request.getNsInstanceId(), accept, contentType, version, body, authorization);
			String operationId = "";	//TODO: this must be fixed when the new version of the REST API is available.
			return operationId;
		} catch (ApiException e) {
			throw new FailedOperationException("Failure when interacting with NFVO: " + e.getMessage());
		}
	}
	
	public void deleteNsIdentifier(String nsInstanceIdentifier) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException {
		throw new MethodNotImplementedException();
	}
	
	public String healNs(HealNsRequest request) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}
	
	public OperationStatus getOperationStatus(String operationId) throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException, MalformattedElementException {
		throw new MethodNotImplementedException();
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

}
