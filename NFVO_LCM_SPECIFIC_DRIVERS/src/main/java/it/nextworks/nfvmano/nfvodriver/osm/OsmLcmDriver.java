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


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.swagger.client.model.*;
import it.nextworks.nfvmano.libs.ifa.common.enums.OperationStatus;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.common.messages.GeneralizedQueryRequest;
import it.nextworks.nfvmano.libs.ifa.common.messages.SubscribeRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.NsLcmConsumerInterface;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.InstantiateNsRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.ScaleNsRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.TerminateNsRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.*;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.nsDescriptor.ConstituentVNFD;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.nsDescriptor.OsmNSPackage;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.OsmVNFPackage;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.ScalingGroupDescriptor;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.VNFDescriptor;
import it.nextworks.nfvmano.nfvodriver.NfvoLcmAbstractDriver;
import it.nextworks.nfvmano.nfvodriver.NfvoLcmDriverType;
import it.nextworks.nfvmano.nfvodriver.NfvoLcmNotificationInterface;
import it.nextworks.nfvmano.nfvodriver.NfvoLcmOperationPollingManager;
import it.nextworks.osm.ApiClient;
import it.nextworks.osm.ApiException;
import it.nextworks.osm.openapi.NsInstancesApi;
import it.nextworks.osm.openapi.NsPackagesApi;
import it.nextworks.osm.openapi.VnfPackagesApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class OsmLcmDriver extends NfvoLcmAbstractDriver {
	private static final Logger log = LoggerFactory.getLogger(OsmLcmDriver.class);
	private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    private  String password;
    private  String username;
    private NsInstancesApi nsInstancesApi;
	private NsPackagesApi nsPackagesApi;
	private VnfPackagesApi vnfPackagesApi;
    private NfvoLcmOperationPollingManager nfvoLcmOperationPollingManager;
	private OAuthSimpleClient oAuthSimpleClient;
	private UUID vimId;
	private String project;

	//for each ifa nsd there are multiple nsd package in osm

	// map the nsd ifa to a random UUID, mapped then in ifaNsdIdToOsmNsdPackageId
	private Map<UUID,String> ifaNsdIdFromUUID;
	// map the UUID of ifa nsd create randomly to the corresponding osm nsd package UUID
	private Map<UUID,UUID> ifaNsdIdToOsmNsdPackageId;
	// map the UUID of an osm ns instace to the UUID of its osm nsd package
	private Map<UUID,UUID> osmNsIdToOsmNsdPackageId;
	// map the UUID of an ns instance to its current IL
	private Map<UUID,String> currentIlMapping;

	//private Map<UUID, UUID> instanceIdToNsdIdMapping;
    //private Map<UUID, UUID> instanceIdToNsdInfoIdMapping;

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
        this.vnfPackagesApi = new VnfPackagesApi();
        //this.instanceIdToNsdIdMapping = new HashMap<>();
        //this.instanceIdToNsdInfoIdMapping = new HashMap<>();
        this.ifaNsdIdFromUUID = new HashMap<>();
        this.ifaNsdIdToOsmNsdPackageId = new HashMap<>();
        this.osmNsIdToOsmNsdPackageId = new HashMap<>();
        this.currentIlMapping = new HashMap<>();

		this.vimId= vimId;
        this.project =project;
		this.nfvoLcmOperationPollingManager = nfvoLcmOperationPollingManager;
		this.oAuthSimpleClient = new OAuthSimpleClient(nfvoAddress+"/osm/admin/v1/tokens", username, password, project);
	}

	/*
	OSM seems to trigger the instantiation when creating the NS instance resource
	Thus, there is no need to perform two different operations but to be aligned
	with the standard  here we just return an Id which is then mapped to the real Id
	used in OSM.
     */
	@Override
	public String createNsIdentifier(CreateNsIdentifierRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		/*
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
		 */

		/*
		Due to the note above the function, at this point we can't create
		a new NS Istance resource because we don't know the DF of the NSD
		present into the CreateNsIdentifierRequest. So just initialize the
		list of the package in OSM corresponding to this NSD Ifa id
		 */
		log.debug("Initializing map of Osm nsd packages for Ifa Nsd:  "+request.getNsdId());
		UUID randomId = UUID.randomUUID();
		ifaNsdIdFromUUID.put(randomId,request.getNsdId());

		//fare mappa
		// <UUID,UUID>
		// <UUID,String> request.getNsdId()
		return randomId.toString();
	}

	private String createNsResource(CreateNsIdentifierRequest request, UUID nsdPackageUUID) throws FailedOperationException {
		CreateNsRequest nsRequest = IfaOsmLcmTranslator.getCreateNsRequest(request,vimId,nsdPackageUUID);
		try {
			nsInstancesApi.setApiClient(getClient());

			ObjectId response = nsInstancesApi.addNSinstance(nsRequest);
			String nsInstanceId = response.getId().toString();
			log.debug("Created NsIdentifier: "+nsInstanceId);
			//instanceIdToNsdIdMapping.put(response.getId(),nsdPackageUUID);
			return nsInstanceId;
		} catch (ApiException e) {
			log.error(e.getMessage());
			System.out.println(e.getResponseBody());
			throw new FailedOperationException(e.getMessage());
		} catch (FailedOperationException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String instantiateNs(InstantiateNsRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
        /*log.debug("Received instantiation request for NS Instance Id: "+request.getNsInstanceId());
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
		}*/

		// At instantion request time we know also the DF for the NSD Ifa, so we have to trigger also the creation
		// of a new NS Instance Resource
		String ifaNsdId = ifaNsdIdFromUUID.get(UUID.fromString(request.getNsInstanceId()));
		String ifaNsdFlavourId = request.getFlavourId();
		log.debug("Received instantiation request for NS Instance Id: "+ ifaNsdId +" with DF: " + ifaNsdFlavourId);

		NsdInfo nsdPackage = getOsmNsdPackage(ifaNsdId +"_"+ ifaNsdFlavourId);

		if(nsdPackage == null)
			throw new NotExistingEntityException("Osm nsd package with id: "+ifaNsdId+"_"+ifaNsdFlavourId+" doesn't exists");

		UUID nsdPackageId = nsdPackage.getIdentifier();

		// add the reference to NSD package for this Ifa NSD
		ifaNsdIdToOsmNsdPackageId.put(UUID.fromString(request.getNsInstanceId()),nsdPackageId);

		//now we have to create NS Resource
		CreateNsIdentifierRequest createNsIdentifierRequest = new CreateNsIdentifierRequest(nsdPackage.getId(), nsdPackage.getId(),null,null);
		String nsdIstanceId = createNsResource(createNsIdentifierRequest,nsdPackageId);

		//this is the UUID of the NS Instance resource associated to the NSD request.getNsInstanceId()_request.getFlavourId() (full_01_df_vCDN)
		if(nsdIstanceId == null)
			throw new FailedOperationException("Cannot create a new NSD resource");

		//Now we can instantiate NSD
		try {
			io.swagger.client.model.InstantiateNsRequest instantiateNsRequest = IfaOsmLcmTranslator.getInstantiateNsRequest(request,nsdPackage,vimId);
			nsInstancesApi.setApiClient(getClient());
			ObjectId objOperationId = nsInstancesApi.instantiateNSinstance(nsdIstanceId, instantiateNsRequest);
			UUID operationId = objOperationId.getId();
			if(nfvoLcmOperationPollingManager!=null)
				nfvoLcmOperationPollingManager.addOperation(operationId.toString(), OperationStatus.SUCCESSFULLY_DONE, request.getNsInstanceId(), "NS_INSTANTIATION");
			osmNsIdToOsmNsdPackageId.put(UUID.fromString(nsdIstanceId),nsdPackageId);
			log.debug("Created instance " + nsdPackage.getId() +" from descriptor " + nsdPackage.getName() +" with UUID: " + nsdIstanceId);
			//return operationId.toString();
			currentIlMapping.put(UUID.fromString(nsdIstanceId),"default");
			return nsdIstanceId;
		} catch (ApiException e) {
			log.error(e.getMessage());
			log.error(e.getResponseBody());
			throw new FailedOperationException(e.getMessage());
		}
	}

	private NsdInfo getOsmNsdPackage(String nsdOsmId) {
		try{
			nsPackagesApi.setApiClient(getClient());
			List<NsdInfo> nsdInfos = nsPackagesApi.getNSDs();
			for(NsdInfo nsdInfo : nsdInfos){
				if(nsdInfo.getId().equals(nsdOsmId)){
					return nsdInfo;
				}

			}
		} catch (FailedOperationException | ApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*private UUID getNsdInfoId(UUID nsdId) throws NotExistingEntityException, FailedOperationException {

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
    }*/

	@Override
	public String scaleNs(ScaleNsRequest request) throws MethodNotImplementedException, NotExistingEntityException,
			FailedOperationException, MalformattedElementException {
		log.debug("Received scaling request for NS Instance "+request.getNsInstanceId());

		String osmNsPackageId = null;
		//get OSM NSD UUID from request.getNsInstanceId()
		/*if(ifaNsdIdToOsmNsdPackageId.containsKey(UUID.fromString(request.getNsInstanceId())))
			osmNsPackageId = ifaNsdIdToOsmNsdPackageId.get(UUID.fromString(request.getNsInstanceId())).toString();
		else throw new FailedOperationException("Cannot retrieve any osm nsd for ifa nsd with id "+ request.getNsInstanceId());*/

		//Check if there is an active osm ns instance for this osm nsd
		if(!osmNsIdToOsmNsdPackageId.containsKey(UUID.fromString(request.getNsInstanceId())))
			throw new FailedOperationException("Cannot perform scaling action for osm nsd package "+ osmNsPackageId + ". No corresponding instance found!");
		else osmNsPackageId = osmNsIdToOsmNsdPackageId.get(UUID.fromString(request.getNsInstanceId())).toString();

		nsPackagesApi.setApiClient(getClient());
		List<ConstituentVNFD>  constituentVNFDList = null;
		//retrieving constituent vnf for this ns instance to be scaled
		try {
			String NsdDescriptorString = nsPackagesApi.getNsPkgNsd(osmNsPackageId);
			PrintWriter out = new PrintWriter(TEMP_DIR+ File.separator+"osmNsd.yaml");
			out.print(NsdDescriptorString);
			out.close();
			ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
			yamlReader.findAndRegisterModules();
			File tempFile = new File(TEMP_DIR+ File.separator+"osmNsd.yaml");
			OsmNSPackage nsDescriptor = yamlReader.readValue(tempFile,OsmNSPackage.class);
			constituentVNFDList = nsDescriptor.getNsdCatalog().getNsds().get(0).getConstituentVNFDs();
			tempFile.delete();
			//nsPackagesApi.getNSDcontent(nsdPackage.getIdentifier().toString());
		} catch (ApiException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(constituentVNFDList == null)
			throw new FailedOperationException("Cannot retrieve constituent VNF list for OSM NSD " + osmNsPackageId);

		//retrieve the VNFDescriptor of the constituent VNF list
		HashMap<String, VNFDescriptor> vnfDescriptorList = new HashMap<>();
		try{
			for(ConstituentVNFD constituentVNFD : constituentVNFDList){
				String vnfDescriptorString = vnfPackagesApi.getVnfPkgVnfd(getVnfdUUIFromName(constituentVNFD.getVnfdIdentifierReference()));
				PrintWriter out = new PrintWriter(TEMP_DIR+ File.separator+"osmVnfd.yaml");
				out.print(vnfDescriptorString);
				out.close();
				ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
				yamlReader.findAndRegisterModules();
				File tempFile = new File(TEMP_DIR+ File.separator+"osmVnfd.yaml");
				OsmVNFPackage vnfDescriptorPackage = yamlReader.readValue(tempFile,OsmVNFPackage.class);
				vnfDescriptorList.put(constituentVNFD.getVnfdIdentifierReference(),vnfDescriptorPackage.getVnfdCatalog().getVnfd().get(0));
				tempFile.delete();
			}
		} catch (ApiException e) {
			log.debug("Cannot retrieve VNF descriptor " + e);
			//TODO check exception
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String instantiationLevelId = request.getScaleNsData().getScaleNsToLevelData().getNsInstantiationLevel();
		String currentIl = currentIlMapping.get(UUID.fromString(request.getNsInstanceId()));
		if(currentIl.equals(instantiationLevelId)) {
			log.info("Cannot scale to the same instantiation level");
			return request.getNsInstanceId();
		}
		boolean isFinalIlDefault = true;
		//Now for each consistuent VNF that has the same IL id we need to trigger a scaling action
		for(ConstituentVNFD constituentVNFD : constituentVNFDList){
			/**
			 * Assumption: the id of the IL is always present in the vnfd.
			 * 			Is there the possibility this is not true?
			 */
			String indexConstituentVnf = constituentVNFD.getMemberVNFIndex().toString();
			VNFDescriptor vnfDescriptor = vnfDescriptorList.get(constituentVNFD.getVnfdIdentifierReference());
			String defaultScalingGroup = null;
			for(ScalingGroupDescriptor scalingGroupDescriptor : vnfDescriptor.getScalingGroupDescriptor()){
				if(scalingGroupDescriptor.getName().contains("default")){
					defaultScalingGroup = scalingGroupDescriptor.getName();
				}
			}
			// we build the defaultScalingGroup as the name of the default IL in the nsd plus the string "-default"
			// in order to know which is the default IL of the Nsd. So if instantiationLevelId is the default
			// IL in the nsd, then in the VNFD descriptor will be present a scaling group descriptor
			// with id "instantiationLevelId-default"
			if(!currentIl.equals("default")){// && !defaultScalingGroup.contains(instantiationLevelId)){
				//a scale-in is needed
				io.swagger.client.model.ScaleNsRequest scaleInNsRequest = IfaOsmLcmTranslator
						.getScaleInNsRequest(indexConstituentVnf,currentIl);
				try{
					nsInstancesApi.setApiClient(getClient());
					nsInstancesApi.scaleNSinstance(request.getNsInstanceId(),scaleInNsRequest);
				} catch (ApiException e) {
					log.info("An error occured when try to scale-in the ns instance: " + request.getNsInstanceId());
					log.debug(e.getResponseBody());
					throw new FailedOperationException();
				}
				if(!defaultScalingGroup.contains(instantiationLevelId)){
					// we need also a scale out-action
					io.swagger.client.model.ScaleNsRequest scaleOutNsRequest = IfaOsmLcmTranslator
							.getScaleOutNsRequest(indexConstituentVnf,instantiationLevelId);
					try{
						nsInstancesApi.setApiClient(getClient());
						nsInstancesApi.scaleNSinstance(request.getNsInstanceId(),scaleOutNsRequest);
					} catch (ApiException e) {
						log.info("An error occured when try to scale-out the ns instance: " + request.getNsInstanceId());
						log.debug(e.getResponseBody());
						throw new FailedOperationException();
					}
					isFinalIlDefault = false;
				}
			}
			else{
				//only a scale-out is required
				io.swagger.client.model.ScaleNsRequest scaleOutNsRequest = IfaOsmLcmTranslator
						.getScaleOutNsRequest(indexConstituentVnf,instantiationLevelId);
				try{
					nsInstancesApi.setApiClient(getClient());
					nsInstancesApi.scaleNSinstance(request.getNsInstanceId(),scaleOutNsRequest);
				} catch (ApiException e) {
					log.info("An error occured when try to scale-out the ns instance: " + request.getNsInstanceId());
					log.debug(e.getResponseBody());
					throw new FailedOperationException();
				}
				isFinalIlDefault = false;
			}
		}
		if(isFinalIlDefault) currentIlMapping.replace(UUID.fromString(request.getNsInstanceId()),"default");
		else currentIlMapping.replace(UUID.fromString(request.getNsInstanceId()),instantiationLevelId);
		return request.getNsInstanceId();
	}

	private String getVnfdUUIFromName(String vnfdIdentifierReference) {
		try{
			vnfPackagesApi.setApiClient(getClient());
			List<VnfPkgInfo> vnfdInfos = vnfPackagesApi.getVnfPkgs();
			for(VnfPkgInfo vnfdInfo : vnfdInfos){
				if(vnfdInfo.getId().equals(vnfdIdentifierReference)){
					return vnfdInfo.getIdentifier().toString();
				}

			}
		} catch (FailedOperationException | ApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	private NsInstance getActiveDFfromIfaId(String nsdIfaName) {
		try{
			nsInstancesApi.setApiClient(getClient());
			List<NsInstance> nsInfos = nsInstancesApi.getNSinstances();
			for(NsInstance nsInstance: nsInfos){
				if(nsInstance.getName().contains(nsdIfaName)){
					return nsInstance;
				}
			}
		} catch (FailedOperationException | ApiException e) {
			e.printStackTrace();
		}
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
		log.debug("Received termination request for "+request.getNsInstanceId());
		String nsInstanceId= request.getNsInstanceId();

		io.swagger.client.model.TerminateNsRequest requestTranslate = IfaOsmLcmTranslator.getTerminateNsRequest(request);

		try {
			//TODO check if istance is running, otherwise this method will fail
			nsInstancesApi.setApiClient(this.getClient());
		    ObjectId objOperationId = nsInstancesApi.terminateNSinstance(nsInstanceId, requestTranslate);
			String operationId = objOperationId.getId().toString();
			if(nfvoLcmOperationPollingManager!=null)
				nfvoLcmOperationPollingManager.addOperation(operationId, OperationStatus.SUCCESSFULLY_DONE, request.getNsInstanceId(), "NS_TERMINATION");
			log.debug("Added polling task for NFVO operation " + operationId);
			nsInstancesApi.deleteNSinstance(nsInstanceId);
			osmNsIdToOsmNsdPackageId.remove(UUID.fromString(nsInstanceId));
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
		//HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		//interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		//apiClient.getHttpClient().interceptors().add(interceptor);
		//apiClient.setDebugging(true);
        return apiClient;
    }


}
