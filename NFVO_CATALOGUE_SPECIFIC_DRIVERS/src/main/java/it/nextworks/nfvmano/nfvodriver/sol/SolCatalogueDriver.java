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
import it.nextworks.nfvmano.libs.fivegcatalogueclient.invoker.nsd.ApiClient;
import it.nextworks.nfvmano.libs.fivegcatalogueclient.sol005.nsdmanagement.elements.NsdInfo;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.AlreadyExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.fivegcatalogueclient.Catalogue;
import it.nextworks.nfvmano.libs.fivegcatalogueclient.CatalogueType;
import it.nextworks.nfvmano.libs.fivegcatalogueclient.FiveGCatalogueClient;
import it.nextworks.nfvmano.libs.fivegcatalogueclient.sol005.nsdmanagement.elements.KeyValuePairs;
import it.nextworks.nfvmano.libs.fivegcatalogueclient.sol005.vnfpackagemanagement.elements.VnfPkgInfo;
import it.nextworks.nfvmano.libs.ifa.common.elements.KeyValuePair;
import it.nextworks.nfvmano.libs.ifasol.catalogues.interfaces.enums.NsdFormat;
import it.nextworks.nfvmano.libs.ifasol.catalogues.interfaces.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import it.nextworks.nfvmano.libs.ifasol.catalogues.interfaces.MecAppPackageManagementConsumerInterface;
import it.nextworks.nfvmano.libs.ifasol.catalogues.interfaces.NsdManagementConsumerInterface;
import it.nextworks.nfvmano.libs.ifasol.catalogues.interfaces.VnfPackageManagementConsumerInterface;


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
import org.springframework.web.client.RestTemplate;

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

		//this.nsdApi=new FiveGCatalogueClient(CatalogueType.FIVEG_CATALOGUE, catalogue);

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder
                .errorHandler(new NsdRestTemplateErrorHandler())
                .build();
        ApiClient client = new ApiClient(restTemplate, catalogue);
        this.nsdApi= new FiveGCatalogueClient(CatalogueType.FIVEG_CATALOGUE, catalogue, client);
		
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
		log.debug("Processing request to onboard a new NSD.");
		if(request.getNsdFormat()!= NsdFormat.IFA)
			throw new MethodNotImplementedException("NSD Format not supported");
		String contentType = "multipart/form-data";
		KeyValuePairs keyValuePair = new KeyValuePairs();
		keyValuePair.putAll(request.getUserDefinedData());
		String authorization = null;
		Nsd nsd = ((OnboardNsdIfaRequest) request).getNsd();
		if (nsd == null) throw new MalformattedElementException("NSD for onboarding is null");
		ArrayList<NsdDfIlKey> nsdDfIlKeys = new ArrayList<>();
		for (NsDf df : nsd.getNsDf()) { // need to generate a sol nsd for each ns Profile in ifa descriptor
				for (NsLevel nsIl : df.getNsInstantiationLevel()) {

					String compressFilePath = IfaToSolTranslator.createCsarPackageForNsdDfIl(nsd, df, nsIl, this);
					if(keyValuePair.containsKey("NSD_INVARIANT_ID")){

						String nsdSolId = IfaToSolTranslator.getNsDescriptorId(nsd, df, nsIl);
						keyValuePair.put("NSD_ID", nsdSolId );
						log.debug("Adding NSD_ID to KeyPair map:" +nsdSolId);
					}

					File nsFile = new File(compressFilePath);
					try {

						String nsId = nsdApi.uploadNetworkService(nsFile.getAbsolutePath(), this.project, contentType, keyValuePair, authorization );
					} catch (IOException e) {
						log.error("Error during NS upload!",e);
						throw new FailedOperationException(e.getMessage());
					} catch (HttpClientErrorException e){
						HttpStatus status = e.getStatusCode();
						if (status == HttpStatus.CONFLICT) {
							throw new AlreadyExistingEntityException("NSD already onboarded");
						}else{
							log.error("Error onboarding NSD", e);
							throw new FailedOperationException("Nsd onboarding failed"+e.getMessage());

						}
					}
					/*DescriptorTemplate dt = IfaToSolTranslator.translateIfaToSolNsd(nsd, df, nsIl);
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
					}*/


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
		String authorization = null;
		GeneralizedQueryRequest solRequest = IfaToSolTranslator.translateQueryNsdRequest(request);


		try{

			List<NsdInfo> nsdInfos = nsdApi.getNsdInfoList(this.project, authorization, solRequest.getFilter().getParameters().get("NSD_ID"));

			return IfaToSolTranslator.translateQueryNsdInfoResponse(request, nsdInfos);
		}catch(Exception e){
			log.error("Error querying NSD", e);
			throw new FailedOperationException(e);
		}




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
		String authorization = null;
		if(request.getFilter().getParameters().containsKey("VNFD_ID")){
			String vnfdId = request.getFilter().getParameters().get("VNFD_ID");

			List<VnfPkgInfo> vnfPkgInfos = nsdApi.getVnfPackageInfoList(this.project,authorization,vnfdId);
			return IfaToSolTranslator.translateQueryVnfPackageInfo(request, vnfPkgInfos);

		}else throw new MethodNotImplementedException("Unsupported query filter");


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






    public DescriptorTemplate getVNFD(String vnfPkgId){
	    return nsdApi.getVNFD(vnfPkgId, this.project, null);
    }


}


