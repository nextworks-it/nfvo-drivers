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
import it.nextworks.osm.ApiClient;
import it.nextworks.osm.openapi.NsInstancesApi;


public class OsmLcmDriver extends NfvoLcmAbstractDriver {

	private NsInstancesApi nsInstancesApi;

	public OsmLcmDriver(String nfvoAddress, String username, String password, NfvoLcmNotificationInterface nfvoNotificationsManager) {
		super(NfvoLcmDriverType.OSM, nfvoAddress, nfvoNotificationsManager);
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(nfvoAddress);
		apiClient.setUsername(username);
		apiClient.setPassword(password);
		nsInstancesApi= new NsInstancesApi(apiClient);

		// TODO Auto-generated constructor stub
	}

	@Override
	public String createNsIdentifier(CreateNsIdentifierRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {

		return null;
	}

	@Override
	public String instantiateNs(InstantiateNsRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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



}
