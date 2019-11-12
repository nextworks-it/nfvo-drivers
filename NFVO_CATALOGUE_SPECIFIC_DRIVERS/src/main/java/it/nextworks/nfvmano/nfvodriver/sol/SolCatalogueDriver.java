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
package it.nextworks.nfvmano.nfvodriver.sol;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import it.nextworks.nfvmano.libs.fivegcatalogueclient.Catalogue;
import it.nextworks.nfvmano.libs.fivegcatalogueclient.CatalogueType;
import it.nextworks.nfvmano.libs.fivegcatalogueclient.FiveGCatalogueClient;
import it.nextworks.nfvmano.libs.fivegcatalogueclient.sol005.nsdmanagement.elements.KeyValuePairs;
import it.nextworks.nfvmano.libs.ifa.common.elements.KeyValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.MecAppPackageManagementConsumerInterface;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.NsdManagementConsumerInterface;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.VnfPackageManagementConsumerInterface;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.DeleteNsdRequest;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.DeleteNsdResponse;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.DeletePnfdRequest;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.DeletePnfdResponse;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.DeleteVnfPackageRequest;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.DisableNsdRequest;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.DisableVnfPackageRequest;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.EnableNsdRequest;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.EnableVnfPackageRequest;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.FetchOnboardedVnfPackageArtifactsRequest;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.OnBoardVnfPackageRequest;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.OnBoardVnfPackageResponse;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.OnboardAppPackageRequest;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.OnboardAppPackageResponse;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.OnboardNsdRequest;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.OnboardPnfdRequest;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.QueryNsdResponse;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.QueryOnBoadedAppPkgInfoResponse;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.QueryOnBoardedVnfPkgInfoResponse;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.QueryPnfdResponse;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.UpdateNsdRequest;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.UpdatePnfdRequest;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.AlreadyExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.common.messages.GeneralizedQueryRequest;
import it.nextworks.nfvmano.libs.ifa.common.messages.SubscribeRequest;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsDf;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsLevel;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsProfile;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.Nsd;
import it.nextworks.nfvmano.libs.descriptors.templates.DescriptorTemplate;
import it.nextworks.nfvmano.nfvodriver.NfvoCatalogueAbstractDriver;
import it.nextworks.nfvmano.nfvodriver.NfvoCatalogueDriverType;
import it.nextworks.nfvmano.nfvodriver.NfvoCatalogueNotificationInterface;

public class SolCatalogueDriver extends NfvoCatalogueAbstractDriver {
	
	private static final Logger log = LoggerFactory.getLogger(SolCatalogueDriver.class);

	private String user;
	private String password;
	private String project;
	private String catalogueId;
	private FiveGCatalogueClient nsdApi;

	//Map with the return nsd id and the list of triplets used as key for the SOL underneath
	private Map<String, List<NsdDfIlKey>> nsIdToTriplet = new HashMap<>();
	private Map<NsdDfIlKey, String> tripletToSolNs = new HashMap<>();

	public SolCatalogueDriver(String nfvoAddress,
			String user,
			String password,
			String project,
			String catalogueId,
			NfvoCatalogueNotificationInterface nfvoCatalogueNotificationManager) {
		super(NfvoCatalogueDriverType.SOL_005, nfvoAddress, nfvoCatalogueNotificationManager);
		this.catalogueId = catalogueId;
		this.user = user;
		this.password = password;
		this.project = project;

		Catalogue catalogue = new Catalogue(
				catalogueId,
				nfvoAddress,
				false,
				user,
				password);

		this.nsdApi=new FiveGCatalogueClient(CatalogueType.FIVEG_CATALOGUE, catalogue);

		
	}

	@Override
	public File fetchOnboardedApplicationPackage(String onboardedAppPkgId)
			throws MethodNotImplementedException, NotExistingEntityException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}

	@Override
	public QueryOnBoadedAppPkgInfoResponse queryApplicationPackage(GeneralizedQueryRequest request)
			throws MethodNotImplementedException, NotExistingEntityException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}

	@Override
	public String subscribeMecAppPackageInfo(SubscribeRequest request,
			MecAppPackageManagementConsumerInterface consumer)
			throws MethodNotImplementedException, MalformattedElementException, FailedOperationException {
		throw new MethodNotImplementedException();
	}

	@Override
	public void unsubscribeMecAppPackageInfo(String subscriptionId)
			throws MethodNotImplementedException, NotExistingEntityException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}

	@Override
	public OnboardAppPackageResponse onboardAppPackage(OnboardAppPackageRequest request)
			throws MethodNotImplementedException, AlreadyExistingEntityException, FailedOperationException,
			MalformattedElementException {
		throw new MethodNotImplementedException();
	}

	@Override
	public void enableAppPackage(String onboardedAppPkgId) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}

	@Override
	public void disableAppPackage(String onboardedAppPkgId) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}

	@Override
	public void deleteAppPackage(String onboardedAppPkgId) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}

	@Override
	public void abortAppPackageDeletion(String onboardedAppPkgId) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}

	@Override
	public String onboardNsd(OnboardNsdRequest request) throws MethodNotImplementedException,
			MalformattedElementException, AlreadyExistingEntityException, FailedOperationException {
		log.debug("Processig request to onboard a new NSD.");
		String contentType = "multipart/form-data";
		KeyValuePairs keyValuePair = new KeyValuePairs();
		keyValuePair.putAll(request.getUserDefinedData());
		String authorization = null;
		Nsd nsd = request.getNsd();
		if (nsd == null) throw new MalformattedElementException("NSD for onboarding is null");
		ArrayList<NsdDfIlKey> nsdDfIlKeys = new ArrayList<>();
		for (NsDf df : nsd.getNsDf()) { // need to generate a sol nsd for each ns Profile in ifa descriptor
				for (NsLevel nsIl : df.getNsInstantiationLevel()) {

					DescriptorTemplate dt = IfaToSolTranslator.translateIfaToSolNsd(nsd, df, nsIl);
					if(dt!=null){
						try {
							File nsFile = new File(this.getNsdFile(dt));
							String nsId = nsdApi.uploadNetworkService(nsFile.getAbsolutePath(), this.project, contentType, keyValuePair, authorization );
							NsdDfIlKey nsdDfIlKey = new NsdDfIlKey(nsd.getNsdIdentifier(),df.getNsDfId(), nsIl.getNsLevelId() );
							nsdDfIlKeys.add(nsdDfIlKey);
							tripletToSolNs.put(nsdDfIlKey, nsId);
						} catch (IOException e) {
							log.error(e.getMessage());
							log.error(e.getStackTrace().toString());
							throw new FailedOperationException(e.getMessage());
						}
					}else{
						log.error("Error during IFA to SOL translation");
						throw  new FailedOperationException("Error during IFA to SOL translation");
					}


				}
			}
		UUID uuid = UUID.randomUUID();
		String randomUUIDString = uuid.toString();
		nsIdToTriplet.put(randomUUIDString, nsdDfIlKeys);
		return randomUUIDString;
	}

	@Override
	public void enableNsd(EnableNsdRequest request) throws MethodNotImplementedException, MalformattedElementException,
			NotExistingEntityException, FailedOperationException {
		log.warn("NSD enabling not supported on OSM.");
	}

	@Override
	public void disableNsd(DisableNsdRequest request) throws MethodNotImplementedException,
			MalformattedElementException, NotExistingEntityException, FailedOperationException {
		log.warn("NSD disabling not supported on OSM.");
	}

	@Override
	public String updateNsd(UpdateNsdRequest request)
			throws MethodNotImplementedException, MalformattedElementException, AlreadyExistingEntityException,
			NotExistingEntityException, FailedOperationException {
		throw new MethodNotImplementedException();
	}

	@Override
	public DeleteNsdResponse deleteNsd(DeleteNsdRequest request) throws MethodNotImplementedException,
			MalformattedElementException, NotExistingEntityException, FailedOperationException {
		log.debug("Processig request to delete an NSD.");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryNsdResponse queryNsd(GeneralizedQueryRequest request) throws MethodNotImplementedException,
			MalformattedElementException, NotExistingEntityException, FailedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String subscribeNsdInfo(SubscribeRequest request, NsdManagementConsumerInterface consumer)
			throws MethodNotImplementedException, MalformattedElementException, FailedOperationException {
		throw new MethodNotImplementedException();
	}

	@Override
	public void unsubscribeNsdInfo(String subscriptionId) throws MethodNotImplementedException,
			MalformattedElementException, NotExistingEntityException, FailedOperationException {
		throw new MethodNotImplementedException();
	}

	@Override
	public String onboardPnfd(OnboardPnfdRequest request) throws MethodNotImplementedException,
			MalformattedElementException, AlreadyExistingEntityException, FailedOperationException {
		throw new MethodNotImplementedException();
	}

	@Override
	public String updatePnfd(UpdatePnfdRequest request)
			throws MethodNotImplementedException, MalformattedElementException, NotExistingEntityException,
			AlreadyExistingEntityException, FailedOperationException {
		throw new MethodNotImplementedException();
	}

	@Override
	public DeletePnfdResponse deletePnfd(DeletePnfdRequest request) throws MethodNotImplementedException,
			MalformattedElementException, NotExistingEntityException, FailedOperationException {
		throw new MethodNotImplementedException();
	}

	@Override
	public QueryPnfdResponse queryPnfd(GeneralizedQueryRequest request) throws MethodNotImplementedException,
			MalformattedElementException, NotExistingEntityException, FailedOperationException {
		throw new MethodNotImplementedException();
	}

	@Override
	public OnBoardVnfPackageResponse onBoardVnfPackage(OnBoardVnfPackageRequest request)
			throws MethodNotImplementedException, AlreadyExistingEntityException, FailedOperationException,
			MalformattedElementException {
		throw new MethodNotImplementedException();
	}

	@Override
	public void enableVnfPackage(EnableVnfPackageRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}

	@Override
	public void disableVnfPackage(DisableVnfPackageRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}

	@Override
	public void deleteVnfPackage(DeleteVnfPackageRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}

	@Override
	public QueryOnBoardedVnfPkgInfoResponse queryVnfPackageInfo(GeneralizedQueryRequest request)
			throws MethodNotImplementedException, NotExistingEntityException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}

	@Override
	public String subscribeVnfPackageInfo(SubscribeRequest request, VnfPackageManagementConsumerInterface consumer)
			throws MethodNotImplementedException, MalformattedElementException, FailedOperationException {
		throw new MethodNotImplementedException();
	}

	@Override
	public void unsubscribeVnfPackageInfo(String subscriptionId)
			throws MethodNotImplementedException, NotExistingEntityException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}

	@Override
	public File fetchOnboardedVnfPackage(String onboardedVnfPkgInfoId)
			throws MethodNotImplementedException, NotExistingEntityException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}

	@Override
	public List<File> fetchOnboardedVnfPackageArtifacts(FetchOnboardedVnfPackageArtifactsRequest request)
			throws MethodNotImplementedException, NotExistingEntityException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}

	@Override
	public void abortVnfPackageDeletion(String onboardedVnfPkgInfoId) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}

	@Override
	public void queryVnfPackageSubscription(GeneralizedQueryRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		throw new MethodNotImplementedException();
	}


	private String getNsdFile (DescriptorTemplate template) throws IOException {
		File nsdFile = File.createTempFile("nsd", null);
		log.debug("Using file: "+nsdFile.getAbsolutePath()+" to store NSD: "+template.getId());
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));

		String obtainedNsd = mapper.writeValueAsString(template);
		log.debug("Obtained NSD:"+obtainedNsd);
		BufferedWriter writer = new BufferedWriter(new FileWriter(nsdFile));
		writer.write(obtainedNsd);

		writer.close();

		return nsdFile.getAbsolutePath();


	}
}

class NsdDfIlKey{


	private String nsdId;
	private String instantiationLevel;
	private String deploymentFlavor;

	public NsdDfIlKey(String nsdId, String instantiationLevel, String deploymentFlavor) {
		this.nsdId = nsdId;
		this.instantiationLevel = instantiationLevel;
		this.deploymentFlavor = deploymentFlavor;
	}

	public String getNsdId() {
		return nsdId;
	}

	public String getInstantiationLevel() {
		return instantiationLevel;
	}

	public String getDeploymentFlavor() {
		return deploymentFlavor;
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof NsdDfIlKey){
			NsdDfIlKey key = (NsdDfIlKey)o;
			return nsdId.equals(key.getNsdId())&&instantiationLevel.equals(key.getInstantiationLevel())&&deploymentFlavor.equals(key.getDeploymentFlavor());
		}else return false;
	}
}
