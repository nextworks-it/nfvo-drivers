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
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.QueryNsdResponse;
import it.nextworks.nfvmano.libs.ifa.common.elements.Filter;
import it.nextworks.nfvmano.libs.ifa.common.enums.InstantiationState;
import it.nextworks.nfvmano.libs.ifa.common.enums.OperationStatus;
import it.nextworks.nfvmano.libs.ifa.common.enums.ResponseCode;
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
import it.nextworks.nfvmano.libs.ifa.records.nsinfo.NsInfo;
import it.nextworks.nfvmano.libs.ifa.records.vnfinfo.VnfInfo;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.nsDescriptor.ConstituentVNFD;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.nsDescriptor.OsmNSPackage;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.OsmVNFPackage;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.ScalingGroupDescriptor;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.VNFDescriptor;
import it.nextworks.nfvmano.nfvodriver.*;
import it.nextworks.nfvmano.nfvodriver.monitoring.MonitoringManager;
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
import java.util.*;


public class OsmLcmDriver extends NfvoLcmAbstractDriver {
	private static final Logger log = LoggerFactory.getLogger(OsmLcmDriver.class);
	private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    private final String password;
    private final String username;
    private final NsInstancesApi nsInstancesApi;
	private final NsPackagesApi nsPackagesApi;
	private final VnfPackagesApi vnfPackagesApi;
    private final NfvoLcmOperationPollingManager nfvoLcmOperationPollingManager;
	private final OAuthSimpleClient oAuthSimpleClient;
	private final UUID vimId;
	private String project;
	private NfvoCatalogueService nfvoCatalogueService;
	private final MonitoringManager monitoringManager;

	//for each ifa nsd there are multiple nsd package in osm

	// map the creation of a ns instance resource request (that contains nsd ifa) to a random UUID, mapped then in ifaNsdIdToOsmNsdPackageId
	private final Map<UUID,CreateNsIdentifierRequest> ifaNsdIdFromUUID;
	// map the UUID of ifa nsd create randomly to the corresponding osm nsd package UUID
	private final Map<UUID,UUID> ifaNsdIdToOsmNsdPackageId;
	// map the UUID of an osm ns instace to the UUID of its osm nsd package
	private final Map<UUID,UUID> osmNsIdToOsmNsdPackageId;
	// map the UUID of an ns instance to its current IL
	private final Map<UUID,String> currentIlMapping;

	// map the osm ns instance id to its info
	private final Map<String, NsInfo> nsInstancesIdToNsInfo = new HashMap<>();
	// map the osm ns instance id to its VnfInfo
	private final Map<String, List<VnfInfo>> nsInstancesIdToVnfInfo = new HashMap<>();

	private final List<MonitoringInfo> monitoringQueue = new ArrayList<>();
	//private Map<UUID, UUID> instanceIdToNsdIdMapping;
    //private Map<UUID, UUID> instanceIdToNsdInfoIdMapping;

	public OsmLcmDriver(String nfvoAddress,
						String username,
						String password,
						String project,
						NfvoLcmOperationPollingManager nfvoLcmOperationPollingManager,
						NfvoLcmNotificationInterface nfvoNotificationsManager,
						UUID vimId,
						NfvoCatalogueService nfvoCatalogueService) {
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
		this.nfvoCatalogueService = nfvoCatalogueService;
		monitoringManager = new MonitoringManager("http://10.30.8.49:8989/prom-manager","http://10.30.8.49:3000",nfvoAddress);
		this.oAuthSimpleClient = new OAuthSimpleClient(nfvoAddress+"/osm/admin/v1/tokens", username, password, project);
	}

	//******************************** Onboarding ********************************//

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
		a new NS Instance resource because we don't know the DF of the NSD
		present into the CreateNsIdentifierRequest. So just initialize the
		list of the package in OSM corresponding to this NSD Ifa id
		 */
		log.debug("Initializing map of Osm nsd packages for Ifa Nsd:  "+request.getNsdId());
		UUID randomId = UUID.randomUUID();
		ifaNsdIdFromUUID.put(randomId,request);

		//fare mappa
		// <UUID,UUID>
		// <UUID,String> request.getNsdId()

		return randomId.toString();
	}

	//******************************** Instantiation ********************************//

	private String createNsResource(CreateNsIdentifierRequest request, UUID nsdPackageUUID) throws FailedOperationException {
		CreateNsRequest nsRequest = IfaOsmLcmTranslator.getCreateNsRequest(request,vimId,nsdPackageUUID);
		try {
			nsInstancesApi.setApiClient(getClient());

			ObjectId response = nsInstancesApi.addNSinstance(nsRequest);
			String nsInstanceId = response.getId().toString();
			log.debug("Created NsIdentifier: "+nsInstanceId);

			//TODO validate: vnfInfoId correspond to the uuid of the vnf instance in osm
			// validate tenantId for monotoringGui
			NsInfo nsInfo = new NsInfo(nsInstanceId,
					request.getNsName(), 				//nsName
					request.getNsDescription(),			//description
					request.getNsdId(),					//nsdId
					null, 								//flavourId
					new ArrayList<>(), 								//vnfInfoId
					null, 								//nestedNsInfoId
					InstantiationState.NOT_INSTANTIATED,//nsState
					null,								//nsScaleStatus
					null,								//additionalAffinityOrAntiAffinityRule
					request.getTenantId(),				//tenantId
					null);		//configurationParameters)
			// maybe add also vnfInstances that contains VnfInfo (requested from NsMonitoringManager)
			nsInstancesIdToNsInfo.put(nsInstanceId, nsInfo);
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
		// At instantion request time we know also the DF for the NSD Ifa, so we have to trigger also the creation
		// of a new NS Instance Resource

		//if in the catalogue side we don't add a new nsd info to the query, at this point the ns instance id will be
		// the random id generated during storing of nsd ifa
		CreateNsIdentifierRequest nsIdentifierRequest = ifaNsdIdFromUUID.get(UUID.fromString(request.getNsInstanceId()));
		String ifaNsdId = nsIdentifierRequest.getNsdId();
		String ifaNsdFlavourId = request.getFlavourId();

		log.debug("Received instantiation request for NS Instance Id: "+ ifaNsdId +" with DF: " + ifaNsdFlavourId);

		NsdInfo nsdPackage = getOsmNsdPackage(ifaNsdId +"_"+ ifaNsdFlavourId);

		if(nsdPackage == null){
			log.error("Osm nsd package with id: " +ifaNsdId+"_"+ifaNsdFlavourId+" doesn't exists");
			throw new NotExistingEntityException();
		}

		UUID nsdPackageId = nsdPackage.getIdentifier();

		// add the reference to NSD package for this Ifa NSD
		// Note: every time we switch to a new DF, for the same NS, the random uuid associated to the ifa ns will change
		//	the reference in this hashmap to a new osm nsd package. So, the correct behaviour is to terminate the current service
		//	and create the new one. So far the basic assumption is to have only one DF running at a given instant of time
		// TODO: if changing DF, terminate the current DF
		ifaNsdIdToOsmNsdPackageId.put(UUID.fromString(request.getNsInstanceId()),nsdPackageId);

		//now we have to create NS Resource
		CreateNsIdentifierRequest createNsIdentifierRequest = new CreateNsIdentifierRequest(nsdPackage.getId(), nsdPackage.getId(),nsIdentifierRequest.getNsDescription(),nsIdentifierRequest.getTenantId());
		String osmNsInstanceId = createNsResource(createNsIdentifierRequest,nsdPackageId);

		//this is the UUID of the NS Instance resource associated to the NSD <nsdId_nsdDfId>(full_01_df_vCDN)
		if(osmNsInstanceId == null){
			log.error("Cannot create a new NSD resource");
			throw new FailedOperationException();
		}

		//Now we can instantiate NSD
		try {
			io.swagger.client.model.InstantiateNsRequest instantiateNsRequest = IfaOsmLcmTranslator.getInstantiateNsRequest(request,nsdPackage,vimId);
			nsInstancesApi.setApiClient(getClient());
			ObjectId objOperationId = nsInstancesApi.instantiateNSinstance(osmNsInstanceId, instantiateNsRequest);
			UUID operationId = objOperationId.getId();
			if(nfvoLcmOperationPollingManager!=null)
				nfvoLcmOperationPollingManager.addOperation(operationId.toString(), OperationStatus.SUCCESSFULLY_DONE, request.getNsInstanceId(), "NS_INSTANTIATION");
			osmNsIdToOsmNsdPackageId.put(UUID.fromString(osmNsInstanceId),nsdPackageId);
			log.debug("Created instance " + nsdPackage.getId() +" from descriptor " + nsdPackage.getName() +" with UUID: " + osmNsInstanceId);
			//return operationId.toString();
			currentIlMapping.put(UUID.fromString(osmNsInstanceId),"default");
			NsInfo nsInfo = nsInstancesIdToNsInfo.get(osmNsInstanceId);
			nsInfo.setNsState(InstantiationState.INSTANTIATED);
			//this will manage the monitoring when the ns is instantiated
			monitoringQueue.add(new MonitoringInfo(osmNsInstanceId,ifaNsdId,nsdPackage.getVersion(),nsInfo,operationId.toString()));
			return osmNsInstanceId; //TODO if return the same id of the request?
		} catch (ApiException e) {
			log.error(e.getMessage());
			log.error(e.getResponseBody());
			throw new FailedOperationException(e.getMessage());
		}
	}

	/**
	 * This function retrieve the VnfInstanceInfo the are related to this osm
	 * ns instance and build the list of VnfInfo.
	 * @param osmNsInstanceId
	 * @return List<VnfInfo>
	 * @throws FailedOperationException
	 */
	protected List<VnfInfo> getVnfInfos(String osmNsInstanceId, NsInfo nsInfo) throws FailedOperationException {
		int i=0;
		List<VnfInfo> vnfInfos = new ArrayList<>();
		//non serve prendere l'id della vnf con il vnfpackages, sta in vnfinstanceinfo
		try{
			nsInstancesApi.setApiClient(getClient());
			for(VnfInstanceInfo vnfInstanceInfo : nsInstancesApi.getVnfInstances()){
				if(vnfInstanceInfo.getNsrIdRef().equals(osmNsInstanceId)) {
					//This vnf is an instance of the ns. Need to create a new VnfInfo
					HashMap<String,String> metadata = new HashMap<>();
					metadata.put("IP",vnfInstanceInfo.geIpAddress());
					VnfInfo vnfInfo = new VnfInfo(
							vnfInstanceInfo.getId().toString(),
							null,
							null,
							vnfInstanceInfo.getVnfdId(),
							null,
							null,
							null,
							null,
							null,
							null,
							null,
							null,
							metadata,
							null);
					vnfInfos.add(vnfInfo);
					//what index represent? a local counter or the index of vnf within the nsd?
					//add(uuid_of_vnf_instance,index,id_of_referred_vnfd_package)
					nsInfo.addVnfInfo(vnfInstanceInfo.getId().toString(),i++,vnfInstanceInfo.getVnfdRef());
				}
			}
		} catch (FailedOperationException | ApiException e) {
			log.error("Cannot retrieve Vnf Instance Infos");
			throw new FailedOperationException();
		}
		return vnfInfos;
	}

	/**
	 * This function activate the monitoring for the osm ns instance.
	 * It retrieve the Nsd related to this instance from OsmCatalogue and
	 * passes it to the MonitoringManager
	 * @param nsInstanceId
	 * @param ifaNsdId
	 * @param version
	 * @throws FailedOperationException
	 */
	private void manageMonitoring(String nsInstanceId, String ifaNsdId, String version, List<VnfInfo> vnfInfoList) throws FailedOperationException {
		//Going to retrieve nsd id and version in order to get the complete ifa nsd from the catalogue
		HashMap<String,String> parameters = new HashMap<>();
		parameters.put("NSD_ID",ifaNsdId);
		parameters.put("NSD_VERSION",version);
		GeneralizedQueryRequest generalizedQueryRequest = new GeneralizedQueryRequest(new Filter(parameters),null);
		QueryNsdResponse queryNsdResponse  = null;
		try {
			queryNsdResponse = nfvoCatalogueService.queryNsd(generalizedQueryRequest);
		} catch (MethodNotImplementedException e) {
			e.printStackTrace();
		} catch (MalformattedElementException e) {
			e.printStackTrace();
		} catch (NotExistingEntityException e) {
			e.printStackTrace();
		}
		if(queryNsdResponse == null){
			log.error("There is no entry in repoistory for " + ifaNsdId);
			throw new FailedOperationException();
		}
		try {
			//activateNsMonitoring(NsInfo, Nsd)
			monitoringManager.activateNsMonitoring(nsInstancesIdToNsInfo.get(nsInstanceId),
					queryNsdResponse.getQueryResult().get(0).getNsd(),
					vnfInfoList);
		} catch (Exception e) {
			log.error("Error while activating ns monitoring");
			throw new FailedOperationException();
		}
	}

	/*private NSDescriptor getNsdDescriptorFromString(String nsdYaml) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(TEMP_DIR+ File.separator+"osmNsd.yaml");
			out.print(nsdYaml);
			out.close();
			ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
			yamlReader.findAndRegisterModules();
			File tempFile = new File(TEMP_DIR+ File.separator+"osmNsd.yaml");
			OsmNSPackage nsDescriptorPackage = null;
			nsDescriptorPackage = yamlReader.readValue(tempFile, OsmNSPackage.class);
			tempFile.delete();
			return nsDescriptorPackage.getNsdCatalog().getNsds().get(0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
			log.info("Cannot generate OsmNsdPackage from string");
		}
		return null;
	}*/

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

	//******************************** Scaling ********************************//

	@Override
	public String scaleNs(ScaleNsRequest request) throws MethodNotImplementedException, NotExistingEntityException,
			FailedOperationException, MalformattedElementException {
		log.debug("Received scaling request for NS Instance "+request.getNsInstanceId());

		String ifaNsdUuid = request.getNsInstanceId();
		String osmNsPackageId = ifaNsdIdToOsmNsdPackageId.get(UUID.fromString(ifaNsdUuid)).toString();
		String osmNsInstanceId = getAssociatedNsInstance(osmNsIdToOsmNsdPackageId,osmNsPackageId);

		//get OSM NSD UUID from request.getNsInstanceId()
		if(osmNsPackageId == null){
			log.debug("There is no nsd package associated to id " + ifaNsdUuid);
			throw new FailedOperationException();
		}
		//Check if there is an active osm ns instance for this osm nsd
		if(osmNsInstanceId == null){
			log.debug("Cannot perform scaling action for osm nsd package. No corresponding instance found!");
			throw new FailedOperationException();
		}

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

		if(constituentVNFDList == null){
			log.error("Cannot retrieve constituent VNF list for OSM NSD " + osmNsPackageId);
			throw new FailedOperationException();
		}

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
			throw new FailedOperationException();
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
		String currentIl = currentIlMapping.get(UUID.fromString(osmNsInstanceId));
		if(currentIl.equals(instantiationLevelId)) {
			log.error("Cannot scale to the same instantiation level");
			throw new FailedOperationException();
		}
		boolean isFinalIlDefault = true;
		//Now for each consistuent VNF that has the same IL id we need to trigger a scaling action
		for(ConstituentVNFD constituentVNFD : constituentVNFDList){
			String indexConstituentVnf = constituentVNFD.getMemberVNFIndex().toString();
			VNFDescriptor vnfDescriptor = vnfDescriptorList.get(constituentVNFD.getVnfdIdentifierReference());
			//check if this vnf is within this scaling group
			String defaultScalingGroup = isScaleActionRequired(vnfDescriptor,instantiationLevelId);
			if(defaultScalingGroup != null) {
				// we build the defaultScalingGroup as the name of the default IL in the nsd plus the string "-default"
				// in order to know which is the default IL of the Nsd. So if instantiationLevelId is the default
				// IL in the nsd, then in the VNFD descriptor will be present a scaling group descriptor
				// with id "instantiationLevelId-default"
				if(!currentIl.equals("default")){
					//a scale-in is needed
					io.swagger.client.model.ScaleNsRequest scaleInNsRequest = IfaOsmLcmTranslator
							.getScaleInNsRequest(indexConstituentVnf,currentIl);
					try{
						nsInstancesApi.setApiClient(getClient());
						nsInstancesApi.scaleNSinstance(osmNsInstanceId,scaleInNsRequest);
					} catch (ApiException e) {
						log.error("An error occured when try to scale-in the ns instance: " + request.getNsInstanceId());
						log.debug(e.getResponseBody());
						throw new FailedOperationException();
					}
					if(!defaultScalingGroup.contains(instantiationLevelId)){
						// we need also a scale out-action
						io.swagger.client.model.ScaleNsRequest scaleOutNsRequest = IfaOsmLcmTranslator
								.getScaleOutNsRequest(indexConstituentVnf,instantiationLevelId);
						try{
							nsInstancesApi.setApiClient(getClient());
							nsInstancesApi.scaleNSinstance(osmNsInstanceId,scaleOutNsRequest);
						} catch (ApiException e) {
							log.error("An error occured when try to scale-out the ns instance: " + request.getNsInstanceId());
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
						nsInstancesApi.scaleNSinstance(osmNsInstanceId,scaleOutNsRequest);
					} catch (ApiException e) {
						log.error("An error occured when try to scale-out the ns instance: " + request.getNsInstanceId());
						log.debug(e.getResponseBody());
						throw new FailedOperationException();
					}
					isFinalIlDefault = false;
				}
			}
		}
		if(isFinalIlDefault) currentIlMapping.put(UUID.fromString(osmNsInstanceId),"default");
		else currentIlMapping.put(UUID.fromString(osmNsInstanceId),instantiationLevelId);
		ArrayOfNsLcmOpOcc arrayOfNsLcmOpOcc = null;
		try {
			arrayOfNsLcmOpOcc = nsInstancesApi.getNSLCMOpOccs();
		} catch (ApiException e) {
			e.printStackTrace();
		}
		// TODO un polling per ogni operazione opppure è sufficiente solo l'ultimo scaling?
		// se uno per ogni operazione, conta il numero di operazioni e prendi l'id degli ultimi n elementi della lista.
		NsLcmOpOcc nsLcmOpOcc;
		if(arrayOfNsLcmOpOcc != null && arrayOfNsLcmOpOcc.size() > 0)
			nsLcmOpOcc = arrayOfNsLcmOpOcc.get(arrayOfNsLcmOpOcc.size()-1);
		else{
			log.error("Cannot retrieve list of lcm operations");
			throw new FailedOperationException();
		}
		if(nfvoLcmOperationPollingManager!=null)
			nfvoLcmOperationPollingManager.addOperation(nsLcmOpOcc.getId().toString(), OperationStatus.SUCCESSFULLY_DONE, request.getNsInstanceId(), "NS_SCALING");
		return request.getNsInstanceId();
	}

	/**
	 * Check if this vnf belongs to this instantiation level.
	 * @param vnfDescriptor
	 * @param instantiationLevelId
	 * @return Name of the default scaling group descriptor in this vnf
	 */
	private String isScaleActionRequired(VNFDescriptor vnfDescriptor, String instantiationLevelId) {
		//no scaling-group within this vnfd
		if(vnfDescriptor.getScalingGroupDescriptor() == null)
			return null;
		String defaaultScalingGroup = null;
		boolean toScale = false;
		for(ScalingGroupDescriptor scalingGroupDescriptor : vnfDescriptor.getScalingGroupDescriptor()){
			if(scalingGroupDescriptor.getName().contains("default"))
				defaaultScalingGroup = scalingGroupDescriptor.getName();
			if(scalingGroupDescriptor.getName().contains(instantiationLevelId))
				toScale = true;
			if(toScale && defaaultScalingGroup != null)
				break;
		}
		//there is a scaling-group within this vnfd with the same name of the next IL
		if(toScale)
			return defaaultScalingGroup;
		else
			return null;
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
		log.debug("Received NS query");
		Map<String, String> filter = request.getFilter().getParameters();

		//this is the uuid of the generic ns without df info
		String ifaNsdUuid = filter.get("NS_ID");
		//this is the uuid of the ns package in osm associated to the generic uuid
		String osmNsPackageId = ifaNsdIdToOsmNsdPackageId.get(UUID.fromString(ifaNsdUuid)).toString();
		//this is the uuid of the ns instance in osm
		String osmNsInstanceId = getAssociatedNsInstance(osmNsIdToOsmNsdPackageId,osmNsPackageId);

		if (osmNsInstanceId == null) {
			log.error("Received NFV NS instance query without NS instance ID");
			throw new MalformattedElementException("NS instance queries are supported only with NS ID.");
		}
		if (nsInstancesIdToNsInfo.containsKey(osmNsInstanceId)) {
			List<NsInfo> queryNsResult = new ArrayList<>();
			queryNsResult.add(nsInstancesIdToNsInfo.get(osmNsInstanceId));
			return new QueryNsResponse(ResponseCode.OK, queryNsResult);
		} else {
			log.error("NS instance " + osmNsInstanceId + " not found.");
			throw new NotExistingEntityException("NS instance " + osmNsInstanceId + " not found.");
		}
	}

	/**
	 * This method scan the value of ifaNsdIdToOsmNsdPackageId and returns the key
	 * associated to the value that match with nsId
	 * @param ifaNsdIdToOsmNsdPackageId
	 * @param nsId
	 * @return String
	 */
	private String getAssociatedNsInstance(Map<UUID, UUID> ifaNsdIdToOsmNsdPackageId, String nsId) {
		for (Map.Entry<UUID, UUID> entry : ifaNsdIdToOsmNsdPackageId.entrySet()) {
			if(entry.getValue().toString().equals(nsId)){
				return entry.getKey().toString();
			}
		}
		return null;
	}

	@Override
	public String terminateNs(TerminateNsRequest request) throws MethodNotImplementedException,
			NotExistingEntityException, FailedOperationException, MalformattedElementException {
		log.debug("Received termination request for "+request.getNsInstanceId());

		String ifaNsdUuid = request.getNsInstanceId();

		String osmNsPackageId = ifaNsdIdToOsmNsdPackageId.get(UUID.fromString(ifaNsdUuid)).toString();
		//Check if there is an osm nsd package associated to the uuid
		if(osmNsPackageId == null){
			log.error("There is no nsd package associated to id " + ifaNsdUuid);
			throw new FailedOperationException();
		}

		String osmNsInstanceId = getAssociatedNsInstance(osmNsIdToOsmNsdPackageId,osmNsPackageId);
		//Check if there is an active osm ns instance for this osm nsd
		if(osmNsInstanceId == null){
			log.error("Cannot perform scaling action for osm nsd package. No corresponding instance found!");
			throw new FailedOperationException();
		}

		io.swagger.client.model.TerminateNsRequest requestTranslate = IfaOsmLcmTranslator.getTerminateNsRequest(request);

		try {
			nsInstancesApi.setApiClient(getClient());
		    ObjectId objOperationId = nsInstancesApi.terminateNSinstance(osmNsInstanceId, requestTranslate);
			String operationId = objOperationId.getId().toString();
			if(nfvoLcmOperationPollingManager!=null)
				nfvoLcmOperationPollingManager.addOperation(operationId, OperationStatus.SUCCESSFULLY_DONE, request.getNsInstanceId(), "NS_TERMINATION");
			log.debug("Added polling task for NFVO operation " + operationId);
			NsInfo nsInfo = nsInstancesIdToNsInfo.get(osmNsInstanceId);
			nsInfo.setNsState(InstantiationState.NOT_INSTANTIATED);
			nsInstancesIdToNsInfo.put(osmNsInstanceId, nsInfo);
			//TODO check this
			monitoringManager.deactivateNsMonitoring(osmNsInstanceId);
			//deleteNsIdentifier(request.getNsInstanceId());
			return operationId;
		} catch (ApiException e) {
			log.debug("Cannot terminare NS " + osmNsInstanceId);
			throw new FailedOperationException(e.getMessage());
		}
	}

	@Override
	public void deleteNsIdentifier(String nsInstanceIdentifier)
			throws MethodNotImplementedException, NotExistingEntityException, FailedOperationException {
		//This method can't be called while ns instance is not in terminated state
		log.debug("Received delete request for "+ nsInstanceIdentifier);
		String osmNsPackageId = ifaNsdIdToOsmNsdPackageId.get(UUID.fromString(nsInstanceIdentifier)).toString();
		//Check if there is an osm nsd package associated to the uuid
		if(osmNsPackageId == null){
			log.debug("There is no nsd package associated to id " + nsInstanceIdentifier);
			throw new FailedOperationException();
		}
		String osmNsInstanceId = getAssociatedNsInstance(osmNsIdToOsmNsdPackageId,osmNsPackageId);
		//Check if there is an active osm ns instance for this osm nsd
		if(osmNsInstanceId == null){
			log.debug("Cannot perform scaling action for osm nsd package. No corresponding instance found!");
			throw new FailedOperationException();
		}
		try {
			nsInstancesApi.setApiClient(getClient());
			nsInstancesApi.deleteNSinstance(osmNsInstanceId);
			osmNsIdToOsmNsdPackageId.remove(UUID.fromString(osmNsInstanceId));
			if (nsInstancesIdToNsInfo.containsKey(nsInstanceIdentifier)) {
				nsInstancesIdToNsInfo.remove(nsInstanceIdentifier);
				log.debug("NS instance ID " + nsInstanceIdentifier + " removed from internal DB");
			} else {
				log.error("NS instance "+ nsInstanceIdentifier + " not found");
				throw new NotExistingEntityException("NS instance "+ nsInstanceIdentifier + " not found");
			}
		} catch (ApiException e) {
			e.printStackTrace();
		}
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
			//activate monitoring if needed
			if(OsmNsLcmOperationStatus.COMPLETED==osmOperationStatus){
				MonitoringInfo monitoringInfo = getMonitoringInfoByOpId(monitoringQueue,operationId);
				if(monitoringInfo != null){
					//creating associations with VnfInfos for this nsInstanceId and update content of nsInfo
					List<VnfInfo> vnfInfoList = getVnfInfos(monitoringInfo.getNsInstanceId(),monitoringInfo.getNsInfo());
					nsInstancesIdToVnfInfo.put(monitoringInfo.getNsInstanceId(),vnfInfoList);
					nsInstancesIdToNsInfo.put(monitoringInfo.getNsInstanceId(),monitoringInfo.getNsInfo());
					//activate monitoring
					manageMonitoring(monitoringInfo.getNsInstanceId(),monitoringInfo.getIfaNsdId(),monitoringInfo.getIfaNsdVersion(),vnfInfoList);
				}
			}
			return IfaOsmLcmTranslator.getOperationSatus(osmOperationStatus);
		} catch (ApiException e) {
			log.error("Cannot retrieve operational status");
			throw new FailedOperationException(e.getMessage());
		}

	}

	// for testing purpose
	public boolean getOptionStatus(String id){
		try {
			nsInstancesApi.setApiClient(getClient());
			ArrayOfNsLcmOpOcc nsLcmOpOccs = nsInstancesApi.getNSLCMOpOccs();
			for (NsLcmOpOcc nsLcmOpOcc : nsLcmOpOccs) {
				if (nsLcmOpOcc.getNsInstanceId().toString().equals(id)) {
					OsmNsLcmOperationStatus osmOperationStatus = OsmNsLcmOperationStatus.valueOf(nsLcmOpOcc.getOperationState());
					if (OsmNsLcmOperationStatus.COMPLETED == osmOperationStatus)
						return true;
				}
			}
		} catch (FailedOperationException e) {
			e.printStackTrace();
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return false;
	}

	private MonitoringInfo getMonitoringInfoByOpId(List<MonitoringInfo> monitoringQueue, String operationId) {
		for(int i=0; i<monitoringQueue.size(); i++){
			if(monitoringQueue.get(i).getOperationId().equals(operationId))
				return monitoringQueue.remove(i);
		}
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

    // for testing purpose
	public ArrayOfVnfInstanceInfo getVnfInstances(){
		try{
			nsInstancesApi.setApiClient(getClient());
			return nsInstancesApi.getVnfInstances();

			//nsPackagesApi.setApiClient(getClient());
			//return nsPackagesApi.getNSDs().get(0);
		} catch (FailedOperationException e) {
			e.printStackTrace();
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return null;
	}

}
