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
package it.nextworks.nfvmano.nfvodriver.osm;


import io.swagger.client.model.*;
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
import it.nextworks.nfvmano.nfvodriver.sol5.Sol5NfvoLcmDriver;
import it.nextworks.osm.ApiClient;
import it.nextworks.osm.ApiException;
import it.nextworks.osm.auth.HttpBasicAuth;
import it.nextworks.osm.auth.OAuth;
import it.nextworks.osm.openapi.NsInstancesApi;
import it.nextworks.osm.openapi.NsPackagesApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class OsmLcmDriver extends NfvoLcmAbstractDriver {
	private static final Logger log = LoggerFactory.getLogger(OsmLcmDriver.class);
    private  String password;
    private  String username;
    private NsInstancesApi nsInstancesApi;
	private NsPackagesApi nsPackagesApi;
    private NfvoLcmOperationPollingManager nfvoLcmOperationPollingManager;
	private OAuthSimpleClient oAuthSimpleClient;
	private UUID vimId;
	private String project;
	private Map<UUID, UUID> instanceIdToNsdIdMapping;
    private Map<UUID, UUID> instanceIdToNsdInfoIdMapping;
   // private Map<UUID, CreateNsIdentifierRequest> instanceToRequestMapping;

	public OsmLcmDriver(String nfvoAddress,
						String username,
						String password,
						String project,
						NfvoLcmOperationPollingManager nfvoLcmOperationPollingManager,
						NfvoLcmNotificationInterface nfvoNotificationsManager,
						UUID vimId) {
		super(NfvoLcmDriverType.OSM, nfvoAddress, nfvoNotificationsManager);
		this.username = username;
		this.password = password;
		this.project = project;
        this.nsInstancesApi = new NsInstancesApi();
        this.nsPackagesApi = new NsPackagesApi();
        this.instanceIdToNsdIdMapping = new HashMap<>();
        this.instanceIdToNsdInfoIdMapping = new HashMap<>();
        //this.instanceToRequestMapping = new HashMap<>();
		this.vimId= vimId;
        this.project =project;
		this.nfvoLcmOperationPollingManager = nfvoLcmOperationPollingManager;
		this.oAuthSimpleClient = new OAuthSimpleClient(nfvoAddress+"/osm/admin/v1/tokens", username, password, project);


	}

	@Override
	public String createNsIdentifier(CreateNsIdentifierRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {

	    //OSM seems to trigger the instantiation when creating the NS instance resource
        //Thus, there is no need to perform two different operations but to be aligned
        //with the standard  here we just return an Id which is then mapped to the real Id
        //used in OSM.
		log.debug("Creating NS identfier for Nsd: "+request.getNsdId());
        UUID nsdId = UUID.nameUUIDFromBytes(request.getNsdId().getBytes());
        log.debug("NsdId translated to OSM:"+nsdId.toString());
        UUID nsdInfoId = getNsdInfoId(nsdId);
		log.debug("NsdInfoId obtained from OSM:"+nsdInfoId.toString());

        CreateNsRequest requestTranslate = IfaOsmLcmTranslator.getCreateNsRequest(request, vimId, nsdInfoId);
		try {

            nsInstancesApi.setApiClient(getClient());
		    ObjectId response = nsInstancesApi.addNSinstance(requestTranslate);
			String nsInstanceId = response.getId().toString();
			log.debug("Created NsIdentifier: "+nsInstanceId);
            instanceIdToNsdIdMapping.put(response.getId(), nsdId);
            instanceIdToNsdInfoIdMapping.put(response.getId(), nsdInfoId);
            //instanceToRequestMapping.put(response.getId(), request);
			return nsInstanceId;
		} catch (ApiException e) {
			log.error(e.getMessage());
			log.error(e.getStackTrace().toString());
			throw new FailedOperationException(e.getMessage());
		}
	}

	@Override
	public String instantiateNs(InstantiateNsRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
        log.debug("Received instantiation request for NS Instance Id: "+request.getNsInstanceId());

        UUID nsInstanceId = UUID.fromString(request.getNsInstanceId());
        if(!instanceIdToNsdInfoIdMapping.containsKey(nsInstanceId))
            throw new NotExistingEntityException("NS instance with id:"+nsInstanceId+" does not exist");
        UUID nsdInfoId = instanceIdToNsdInfoIdMapping.get(nsInstanceId);
		UUID nsdId = instanceIdToNsdIdMapping.get(nsInstanceId);

		try {

		    io.swagger.client.model.InstantiateNsRequest requestTranslation = IfaOsmLcmTranslator.getInstantiateNsRequest(request,nsdInfoId, nsdId, vimId);
		    nsInstancesApi.setApiClient(getClient());
			ObjectId objOperationId = nsInstancesApi.instantiateNSinstance(nsInstanceId.toString(), requestTranslation);
			UUID operationId = objOperationId.getId();
			if(nfvoLcmOperationPollingManager!=null)
				nfvoLcmOperationPollingManager.addOperation(operationId.toString(), OperationStatus.SUCCESSFULLY_DONE, request.getNsInstanceId(), "NS_INSTANTIATION");
			return operationId.toString();


		} catch (ApiException e) {
			log.error(e.getMessage());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			log.error(sw.toString());
			throw new FailedOperationException(e.getMessage());
		}

	}

	private UUID getNsdInfoId(UUID nsdId) throws NotExistingEntityException, FailedOperationException {

        try {
            nsPackagesApi.setApiClient(getClient());
            List<NsdInfo> nsdInfos = nsPackagesApi.getNSDs();
            for(NsdInfo nsdInfo : nsdInfos){
                if(nsdInfo.getId().equals(nsdId.toString()))
                    return nsdInfo.getIdentifier();

            }
            throw new NotExistingEntityException("Nsd Info id not found: "+nsdId.toString());
        } catch (ApiException e) {
            throw new FailedOperationException(e.getMessage());
        }
    }

	@Override
	public String scaleNs(ScaleNsRequest request) throws MethodNotImplementedException, NotExistingEntityException,
			FailedOperationException, MalformattedElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpdateNsResponse updateNs(UpdateNsRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryNsResponse queryNs(GeneralizedQueryRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String terminateNs(TerminateNsRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		log.debug("Received instantiation request for "+request.getNsInstanceId());
		String nsInstanceId= request.getNsInstanceId();

		io.swagger.client.model.TerminateNsRequest requestTranslate = IfaOsmLcmTranslator.getTerminateNsRequest(request);

		try {
			nsInstancesApi.setApiClient(this.getClient());
		    ObjectId objOperationId = nsInstancesApi.terminateNSinstance(nsInstanceId, requestTranslate);
			String operationId = objOperationId.getId().toString();
			if(nfvoLcmOperationPollingManager!=null)
				nfvoLcmOperationPollingManager.addOperation(operationId, OperationStatus.SUCCESSFULLY_DONE, request.getNsInstanceId(), "NS_TERMINATION");
			log.debug("Added polling task for NFVO operation " + operationId);
			return operationId;
		} catch (ApiException e) {
			log.error(e.getMessage());
			log.error(e.getStackTrace().toString());
			throw new FailedOperationException(e.getMessage());
		}

	}

	@Override
	public void deleteNsIdentifier(String nsInstanceIdentifier)
			throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public String healNs(HealNsRequest request) throws MethodNotImplementedException, NotExistingEntityException,
			FailedOperationException, MalformattedElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OperationStatus getOperationStatus(String operationId) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {

		try {
			nsInstancesApi.setApiClient(getClient());
			NsLcmOpOcc nsLcmOpOcc = nsInstancesApi.getNSLCMOpOcc(operationId);
			OsmNsLcmOperationStatus osmOperationStatus = OsmNsLcmOperationStatus.valueOf(nsLcmOpOcc.getOperationState());
			return IfaOsmLcmTranslator.getOperationSatus(osmOperationStatus);
		} catch (ApiException e) {
			log.error(e.getMessage());
			log.error(e.getStackTrace().toString());
			throw new FailedOperationException(e.getMessage());
		}

	}

	@Override
	public String subscribeNsLcmEvents(SubscribeRequest request, NsLcmConsumerInterface consumer)
			throws MethodNotImplementedException, MalformattedElementException, FailedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unsubscribeNsLcmEvents(String subscriptionId) throws MethodNotImplementedException,
			MalformattedElementException, NotExistingEntityException, FailedOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void queryNsSubscription(GeneralizedQueryRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		// TODO Auto-generated method stub

	}

	private ApiClient getClient() throws FailedOperationException {

        ApiClient apiClient = new ApiClient();
        apiClient.setHttpClient(OAuthSimpleClient.getUnsafeOkHttpClient());
        apiClient.setBasePath(this.getNfvoAddress()+"/osm/");
        apiClient.setUsername(username);
        apiClient.setPassword(password);
        apiClient.setAccessToken(oAuthSimpleClient.getToken());
        return apiClient;
    }



}
