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
package it.nextworks.nfvmano.nfvodriver.dummy;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import it.nextworks.nfvmano.libs.ifa.descriptors.onboardedvnfpackage.OnboardedVnfPkgInfo;
import it.nextworks.nfvmano.libs.ifa.descriptors.vnfd.Vnfd;
import it.nextworks.nfvmano.nfvodriver.NfvoCatalogueAbstractDriver;
import it.nextworks.nfvmano.nfvodriver.NfvoCatalogueDriverType;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class DummyNfvoCatalogueDriver extends NfvoCatalogueAbstractDriver {

	private Map<String, Nsd> nsds = new HashMap<>();
	private static final Logger log = LoggerFactory.getLogger(DummyNfvoCatalogueDriver.class);
	private FileUtilities fileUtilities;
	private long lastVnfPkgInfoId;
	private ArrayList<OnboardedVnfPkgInfo> vnfPkgInfos = new ArrayList<>();

	public DummyNfvoCatalogueDriver(String nfvoAddress, String tmpDir) {
		super(NfvoCatalogueDriverType.DUMMY, nfvoAddress, null);
		//String uuidNSD = onBoardFakeNsd();
		this.fileUtilities = new FileUtilities(tmpDir);
		//log.info("On boarded NSD with ID "+uuidNSD);
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

	private OnBoardVnfPackageResponse fakeOnBoardVnfPackage(OnBoardVnfPackageRequest request)
			throws MethodNotImplementedException, AlreadyExistingEntityException, FailedOperationException,
			MalformattedElementException {
		RestTemplate restTemplate = new RestTemplate();
		String vnfPackagePath = request.getVnfPackagePath();
        log.debug("Getting VNF package");


        log.debug("Retrieving VNFD from VNF package");
		String folder = null;
		try {
			String downloadedFile = fileUtilities.downloadFile(vnfPackagePath);
			folder = fileUtilities.extractFile(downloadedFile);
			File jsonFile = fileUtilities.findJsonFileInDir(folder);
			Charset encoding = null;
			String json = FileUtils.readFileToString(jsonFile, encoding);
			log.debug("VNFD json: \n" + json);

			ObjectMapper mapper = new ObjectMapper();
			Vnfd vnfd = (Vnfd) mapper.readValue(json, Vnfd.class);
			log.debug("VNFD correctly parsed.");

			log.debug("Cleaning local directory");
			fileUtilities.removeFileAndFolder(downloadedFile, folder);
			OnboardedVnfPkgInfo pkgInfo = createVnfPkgInfoFromVnfd(vnfd);
			vnfPkgInfos.add(pkgInfo);
			OnBoardVnfPackageResponse response = new OnBoardVnfPackageResponse(pkgInfo.getOnboardedVnfPkgInfoId(), vnfd.getVnfdId());
			return response;
		} catch (ArchiveException e) {
			throw new FailedOperationException(e.getMessage());
		} catch (IOException e) {
			throw new FailedOperationException(e.getMessage());
		}




	}

	private OnboardedVnfPkgInfo createVnfPkgInfoFromVnfd(Vnfd vnfd){
		String vnfPkgInfoId = (new Long(lastVnfPkgInfoId)).toString();
		OnboardedVnfPkgInfo pkgInfo = new OnboardedVnfPkgInfo(vnfPkgInfoId,
				vnfd.getVnfdId(),
				vnfd.getVnfProvider(),
				vnfd.getVnfProductName(),
				vnfd.getVnfSoftwareVersion(),
				vnfd.getVnfdVersion(),
				"",
				vnfd,
				null,
				null,
				OperationalState.ENABLED,
				UsageState.NOT_IN_USE,
				false,
				null);
		lastVnfPkgInfoId++;
		return pkgInfo;
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
		String p3 = "NSD_INFO_ID";


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
		} else if(params.containsKey(p1) ){
			String nsdId = params.get(p1);
			log.debug("Querying NSD with ID " + nsdId);
			for (Map.Entry<String, Nsd> nsdEntry : nsds.entrySet()) {
				Nsd nsd = nsdEntry.getValue();
				if (nsd.getNsdIdentifier().equals(nsdId)) {
					log.debug("NSD found");
					queryResult.add(new NsdInfo(nsdEntry.getKey(),
							nsdId,
							nsd.getNsdName(),
							"1.0",
							nsd.getDesigner(),
							nsd,
							new ArrayList<>(),
							new ArrayList<>(),
							null,
							OperationalState.ENABLED,
							UsageState.IN_USE,
							false,
							new HashMap<>()));

				}
			}
			QueryNsdResponse response = new QueryNsdResponse(queryResult);
			return response;

		}else if(params.containsKey(p3) ){
			String nsdInfoId = params.get(p3);
			log.debug("Querying NSD with INFO ID " + nsdInfoId);
			if(this.nsds.containsKey(nsdInfoId)){
				log.debug("NSD found");
				Nsd cNsd = this.nsds.get(nsdInfoId);
				queryResult.add(new NsdInfo(nsdInfoId,
						cNsd.getNsdIdentifier(),
						cNsd.getNsdName(),
						cNsd.getVersion(),
						cNsd.getDesigner(),
						cNsd,
						new ArrayList<>(),
						new ArrayList<>(),
						null,
						OperationalState.ENABLED,
						UsageState.IN_USE,
						false,
						new HashMap<>()));
			}
			QueryNsdResponse response = new QueryNsdResponse(queryResult);
			return response;
		}else{
			log.debug("FILTER NOT SUPPORTED");
			throw new MalformattedElementException("Filter not supported");
		}
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
		return fakeOnBoardVnfPackage(request);
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
		Filter  filter =request.getFilter();
		Map<String, String> params = filter.getParameters();
		if(params.containsKey("VNF_PACKAGE_PRODUCT_NAME") && params.containsKey("VNF_PACKAGE_SW_VERSION")&& params.containsKey("VNF_PACKAGE_PROVIDER")){

			String vnfPackageProductName = params.get("VNF_PACKAGE_PRODUCT_NAME");
			String vnfPackageSwVersion = params.get("VNF_PACKAGE_SW_VERSION");
			String vnfPackageProvider = params.get("VNF_PACKAGE_PROVIDER");
			List<OnboardedVnfPkgInfo> filtered  = vnfPkgInfos.stream()
					.filter(pkgInfo -> pkgInfo.getVnfProductName().equals(vnfPackageProductName)&&
							pkgInfo.getVnfSoftwareVersion().equals(vnfPackageSwVersion)&&
							pkgInfo.getVnfProvider().equals(vnfPackageProvider))
					.collect(Collectors.toList());
			return new QueryOnBoardedVnfPkgInfoResponse(filtered);
		}else if(params.containsKey("VNF_PACKAGE_ID")){
			String vnfPackageId = params.get("VNF_PACKAGE_ID");
			List<OnboardedVnfPkgInfo> filtered  = vnfPkgInfos.stream()
					.filter(pkgInfo -> pkgInfo.getOnboardedVnfPkgInfoId().equals(vnfPackageId)		)
					.collect(Collectors.toList());
			return new QueryOnBoardedVnfPkgInfoResponse(filtered);

		}else if(params.containsKey("VNFD_ID")){
			String vnfdId = params.get("VNFD_ID");
			List<OnboardedVnfPkgInfo> filtered  = vnfPkgInfos.stream()
					.filter(pkgInfo -> pkgInfo.getVnfdId().equals(vnfdId)		)
					.collect(Collectors.toList());
			return new QueryOnBoardedVnfPkgInfoResponse(filtered);

		}else throw  new MalformattedElementException("Unsupported VNF Package filter");

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
