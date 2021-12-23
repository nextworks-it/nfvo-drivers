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
package it.nextworks.nfvmano.nfvodriver.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.nextworks.nfvmano.libs.ifasol.catalogues.interfaces.MecAppPackageManagementConsumerInterface;
import it.nextworks.nfvmano.libs.ifasol.catalogues.interfaces.NsdManagementConsumerInterface;
import it.nextworks.nfvmano.libs.ifasol.catalogues.interfaces.VnfPackageManagementConsumerInterface;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.elements.NsdInfo;
import it.nextworks.nfvmano.libs.ifasol.catalogues.interfaces.enums.NsdFormat;
import it.nextworks.nfvmano.libs.ifasol.catalogues.interfaces.messages.*;
import it.nextworks.nfvmano.libs.ifa.common.elements.Filter;
import it.nextworks.nfvmano.libs.ifa.common.enums.OperationalState;
import it.nextworks.nfvmano.libs.ifa.common.enums.UsageState;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.*;
import it.nextworks.nfvmano.libs.ifa.common.messages.GeneralizedQueryRequest;
import it.nextworks.nfvmano.libs.ifa.common.messages.SubscribeRequest;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.Nsd;
import it.nextworks.nfvmano.libs.ifa.descriptors.onboardedvnfpackage.OnboardedVnfPkgInfo;
import it.nextworks.nfvmano.libs.ifa.descriptors.vnfd.Vnfd;
import it.nextworks.nfvmano.nfvodriver.NfvoCatalogueAbstractDriver;
import it.nextworks.nfvmano.nfvodriver.NfvoCatalogueDriverType;
import it.nextworks.nfvmano.nfvodriver.dummy.FileUtilities;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class DummyFileNfvoCatalogueDriver extends NfvoCatalogueAbstractDriver {


	private static final Logger log = LoggerFactory.getLogger(DummyFileNfvoCatalogueDriver.class);
	private NsdFileRegistryService nsdFileRegistryService;
	private VnfdFileRegistryService vnfdFileRegistryService;
	private String tmpDir;
	private FileUtilities fileUtilities;

	public DummyFileNfvoCatalogueDriver(NsdFileRegistryService nsdFileRegistryService, VnfdFileRegistryService vnfdFileRegistryService, String tmpDir) {
		super(NfvoCatalogueDriverType.DUMMY_FILE, null, null);
		this.nsdFileRegistryService = nsdFileRegistryService;
		this.tmpDir = tmpDir;
		fileUtilities = new FileUtilities(tmpDir);
		this.vnfdFileRegistryService= vnfdFileRegistryService;

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
		if(request.getNsdFormat()!= NsdFormat.IFA)
			throw new MethodNotImplementedException("NSD Format not supported");
		return nsdFileRegistryService.storeNsd(((OnboardNsdIfaRequest)request).getNsd());
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


		Filter filter = request.getFilter();
		Map<String, String> params = filter.getParameters();
		String p1 = "NSD_ID";
		String p2 = "NSD_VERSION";



		log.debug("Querying NSD");

		if (params.containsKey(p1) && params.containsKey(p2)) {
			String nsdId = params.get("NSD_ID");
			String nsdVersion = params.get("NSD_VERSION");


			log.debug("Querying NSD with ID " + nsdId + " and version " + nsdVersion);
			NsdInfo nsdInfo = nsdFileRegistryService.queryNsd(nsdId, nsdVersion);
			List<NsdInfo> nsdInfos = new ArrayList<>();
			nsdInfos.add(nsdInfo);
			return new QueryNsdIfaResponse(nsdInfos);


		} else if(params.size()==1 &&params.containsKey("NSD_INFO_ID")){
			String nsdInfoId = params.get("NSD_INFO_ID");

			log.debug("Querying nsd using nsdInfoId:"+nsdInfoId);
			NsdInfo nsdInfo = nsdFileRegistryService.queryNsd(nsdInfoId);
			List<NsdInfo> nsdInfos = new ArrayList<>();
			nsdInfos.add(nsdInfo);
			return new QueryNsdIfaResponse(nsdInfos);

		}else  throw new MalformattedElementException("Only nsdId and nsdVersion or nsdInfoId query implemented");
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


		log.debug("Received request to onboard a new VNF package");
		String folder = null;
		String vnfPackagePath = request.getVnfPackagePath();
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
			String vnfdInfoId = vnfdFileRegistryService.storeVnfd(request, vnfd);
			log.debug("Cleaning local directory");
			fileUtilities.removeFileAndFolder(downloadedFile, folder);


			OnBoardVnfPackageResponse response = new OnBoardVnfPackageResponse(vnfdInfoId, vnfd.getVnfdId());
			return response;
		} catch (ArchiveException e) {
			throw new FailedOperationException(e.getMessage());
		} catch (IOException e) {
			throw new FailedOperationException(e.getMessage());
		}

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
		log.debug("Received query to retrieve VnfPkgInfo");
		Map<String, String> parameters = request.getFilter().getParameters();

		/*
		"VNF_PACKAGE_PRODUCT_NAME" && "VNF_PACKAGE_SW_VERSION" && "VNF_PACKAGE_PROVIDER"
		 */
		if(parameters.size()==3 && parameters.containsKey("VNF_PACKAGE_PRODUCT_NAME") &&
				parameters.containsKey("VNF_PACKAGE_SW_VERSION") && parameters.containsKey("VNF_PACKAGE_PROVIDER")){
			OnboardedVnfPkgInfo vnfPkgInfo = vnfdFileRegistryService.queryVnf(parameters.get("VNF_PACKAGE_PRODUCT_NAME"), parameters.get("VNF_PACKAGE_PROVIDER"),
					parameters.get("VNF_PACKAGE_SW_VERSION"));
			List<OnboardedVnfPkgInfo> pkgInfos = new ArrayList<>();
			pkgInfos.add(vnfPkgInfo);
			return new QueryOnBoardedVnfPkgInfoIfaResponse(pkgInfos);

		}else if(parameters.size()==1  && parameters.containsKey("VNFD_ID") ){
			OnboardedVnfPkgInfo vnfPkgInfo = vnfdFileRegistryService.queryVnf(parameters.get("VNFD_ID"));
			List<OnboardedVnfPkgInfo> pkgInfos = new ArrayList<>();
			pkgInfos.add(vnfPkgInfo);
			return new QueryOnBoardedVnfPkgInfoIfaResponse(pkgInfos);
		}else{
			throw new MalformattedElementException("Specified filter params not supported");
		}

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
