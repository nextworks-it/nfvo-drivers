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

import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.elements.NsdInfo;
import it.nextworks.nfvmano.libs.ifa.common.elements.Filter;
import it.nextworks.nfvmano.libs.ifa.common.enums.OperationalState;
import it.nextworks.nfvmano.libs.ifa.common.enums.UsageState;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.AlreadyExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.MecAppPackageManagementConsumerInterface;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.NsdManagementConsumerInterface;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.VnfPackageManagementConsumerInterface;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.*;
import it.nextworks.nfvmano.libs.ifa.common.messages.GeneralizedQueryRequest;
import it.nextworks.nfvmano.libs.ifa.common.messages.SubscribeRequest;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.Nsd;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.SecurityParameters;
import it.nextworks.nfvmano.nfvodriver.NfvoCatalogueAbstractDriver;
import it.nextworks.nfvmano.nfvodriver.NfvoCatalogueDriverType;
import it.nextworks.nfvmano.nfvodriver.timeo.TimeoCatalogueRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public class DummyNfvoCatalogueDriver extends NfvoCatalogueAbstractDriver {

	private Map<String, Nsd> nsds = new HashMap<>();
	private static final Logger log = LoggerFactory.getLogger(DummyNfvoCatalogueDriver.class);

	public DummyNfvoCatalogueDriver(String nfvoAddress) {
		super(NfvoCatalogueDriverType.DUMMY, nfvoAddress, null);
		String uuidNSD = onBoardFakeNsd();
		log.info("On boarded NSD with ID "+uuidNSD);
	}


	private String onBoardFakeNsd() {
		Nsd nsd = new Nsd("nsVS",
				"designer",
				"0.1",
				"nsdName",
				"nsdInvariantId",
				new ArrayList<String>(),
				new ArrayList<String>(),
				new ArrayList<String>(),
				null);
		try {
			OnboardNsdRequest request = new OnboardNsdRequest(nsd, new HashMap<>());
			String uuid = onboardNsd(request);
			return uuid;
		}
		catch (Exception e) {
			log.error("Error on boarding Fake nsd");
			return null;
		}
	}


	@Override
	public File fetchOnboardedApplicationPackage(String onboardedAppPkgId)
			throws MethodNotImplementedException, NotExistingEntityException, MalformattedElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryOnBoadedAppPkgInfoResponse queryApplicationPackage(GeneralizedQueryRequest request)
			throws MethodNotImplementedException, NotExistingEntityException, MalformattedElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String subscribeMecAppPackageInfo(SubscribeRequest request,
			MecAppPackageManagementConsumerInterface consumer)
			throws MethodNotImplementedException, MalformattedElementException, FailedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unsubscribeMecAppPackageInfo(String subscriptionId)
			throws MethodNotImplementedException, NotExistingEntityException, MalformattedElementException {
		// TODO Auto-generated method stub

	}

	@Override
	public OnboardAppPackageResponse onboardAppPackage(OnboardAppPackageRequest request)
			throws MethodNotImplementedException, AlreadyExistingEntityException, FailedOperationException,
			MalformattedElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void enableAppPackage(String onboardedAppPkgId) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableAppPackage(String onboardedAppPkgId) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAppPackage(String onboardedAppPkgId) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		// TODO Auto-generated method stub

	}

	@Override
	public void abortAppPackageDeletion(String onboardedAppPkgId) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		// TODO Auto-generated method stub

	}

	@Override
	public String onboardNsd(OnboardNsdRequest request) throws MethodNotImplementedException,
			MalformattedElementException, AlreadyExistingEntityException, FailedOperationException {
		
		String uuid = UUID.randomUUID().toString();
		nsds.put(uuid, request.getNsd());
		log.debug("Onboarded Nsd with ID " + request.getNsd().getNsdIdentifier() + " and version " + request.getNsd().getVersion());
		return uuid;
	}

	@Override
	public void enableNsd(EnableNsdRequest request) throws MethodNotImplementedException, MalformattedElementException,
			NotExistingEntityException, FailedOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableNsd(DisableNsdRequest request) throws MethodNotImplementedException,
			MalformattedElementException, NotExistingEntityException, FailedOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public String updateNsd(UpdateNsdRequest request)
			throws MethodNotImplementedException, MalformattedElementException, AlreadyExistingEntityException,
			NotExistingEntityException, FailedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeleteNsdResponse deleteNsd(DeleteNsdRequest request) throws MethodNotImplementedException,
			MalformattedElementException, NotExistingEntityException, FailedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryNsdResponse queryNsd(GeneralizedQueryRequest request) throws MethodNotImplementedException,
			MalformattedElementException, NotExistingEntityException, FailedOperationException {
		// TODO Auto-generated method stub

		Filter filter = request.getFilter();
		Map<String, String> params = filter.getParameters();
		String p1 = "NSD_ID";
		String p2 = "NSD_VERSION";


		List<NsdInfo> queryResult = new ArrayList<>();

		log.debug("Querying NSD");

		if (params.containsKey(p1) && params.containsKey(p2)) {
			String nsdId = params.get("NSD_ID");
			String nsdVersion = params.get("NSD_VERSION");

			log.debug("Querying NSD with ID " + nsdId + " and version " + nsdVersion);

			for (Map.Entry<String, Nsd> nsdEntry : nsds.entrySet()) {
				Nsd nsd = nsdEntry.getValue();
				if (nsd.getNsdIdentifier().equals(nsdId) && nsd.getVersion().equals(nsdVersion)) {
					log.debug("NSD found");
					queryResult.add(new NsdInfo(nsdEntry.getKey(),
							nsdId,
							nsd.getNsdName(),
							nsdVersion,
							nsd.getDesigner(),
							nsd,
							new ArrayList<>(),
							new ArrayList<>(),
							null,
							OperationalState.ENABLED,
							UsageState.IN_USE,
							false,
							new HashMap<>()));
					QueryNsdResponse response = new QueryNsdResponse(queryResult);
					return response;
				}
			}
			log.debug("NSD not found");
			return null;
		} else return null;
	}

	@Override
	public String subscribeNsdInfo(SubscribeRequest request, NsdManagementConsumerInterface consumer)
			throws MethodNotImplementedException, MalformattedElementException, FailedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unsubscribeNsdInfo(String subscriptionId) throws MethodNotImplementedException,
			MalformattedElementException, NotExistingEntityException, FailedOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public String onboardPnfd(OnboardPnfdRequest request) throws MethodNotImplementedException,
			MalformattedElementException, AlreadyExistingEntityException, FailedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updatePnfd(UpdatePnfdRequest request)
			throws MethodNotImplementedException, MalformattedElementException, NotExistingEntityException,
			AlreadyExistingEntityException, FailedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeletePnfdResponse deletePnfd(DeletePnfdRequest request) throws MethodNotImplementedException,
			MalformattedElementException, NotExistingEntityException, FailedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryPnfdResponse queryPnfd(GeneralizedQueryRequest request) throws MethodNotImplementedException,
			MalformattedElementException, NotExistingEntityException, FailedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OnBoardVnfPackageResponse onBoardVnfPackage(OnBoardVnfPackageRequest request)
			throws MethodNotImplementedException, AlreadyExistingEntityException, FailedOperationException,
			MalformattedElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void enableVnfPackage(EnableVnfPackageRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableVnfPackage(DisableVnfPackageRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteVnfPackage(DeleteVnfPackageRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		// TODO Auto-generated method stub

	}

	@Override
	public QueryOnBoardedVnfPkgInfoResponse queryVnfPackageInfo(GeneralizedQueryRequest request)
			throws MethodNotImplementedException, NotExistingEntityException, MalformattedElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String subscribeVnfPackageInfo(SubscribeRequest request, VnfPackageManagementConsumerInterface consumer)
			throws MethodNotImplementedException, MalformattedElementException, FailedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unsubscribeVnfPackageInfo(String subscriptionId)
			throws MethodNotImplementedException, NotExistingEntityException, MalformattedElementException {
		// TODO Auto-generated method stub

	}

	@Override
	public File fetchOnboardedVnfPackage(String onboardedVnfPkgInfoId)
			throws MethodNotImplementedException, NotExistingEntityException, MalformattedElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<File> fetchOnboardedVnfPackageArtifacts(FetchOnboardedVnfPackageArtifactsRequest request)
			throws MethodNotImplementedException, NotExistingEntityException, MalformattedElementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void abortVnfPackageDeletion(String onboardedVnfPkgInfoId) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		// TODO Auto-generated method stub

	}

	@Override
	public void queryVnfPackageSubscription(GeneralizedQueryRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		// TODO Auto-generated method stub

	}

}
