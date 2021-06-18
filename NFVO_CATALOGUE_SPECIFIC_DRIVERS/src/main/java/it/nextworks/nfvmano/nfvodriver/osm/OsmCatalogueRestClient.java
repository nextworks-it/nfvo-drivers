package it.nextworks.nfvmano.nfvodriver.osm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.swagger.client.model.*;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.*;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.*;
import it.nextworks.nfvmano.libs.ifa.common.messages.GeneralizedQueryRequest;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.*;
import it.nextworks.nfvmano.libs.ifa.descriptors.onboardedvnfpackage.OnboardedVnfPkgInfo;
import it.nextworks.nfvmano.libs.ifa.descriptors.vnfd.VnfDf;
import it.nextworks.nfvmano.libs.ifa.descriptors.vnfd.Vnfd;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.nsDescriptor.NSDescriptor;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.*;
import it.nextworks.nfvmano.nfvodriver.dummy.FileUtilities;
import it.nextworks.nfvmano.nfvodriver.file.NsdFileRegistryService;
import it.nextworks.nfvmano.nfvodriver.file.VnfdFileRegistryService;
import it.nextworks.osm.ApiClient;
import it.nextworks.osm.ApiException;
import it.nextworks.osm.openapi.NsPackagesApi;
import it.nextworks.osm.openapi.VnfPackagesApi;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * REST client to interact with OSM NFVO.
 *
 * @author nextworks
 *
 */

public class OsmCatalogueRestClient {

    private static final Logger log = LoggerFactory.getLogger(OsmCatalogueRestClient.class);
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    private final OAuthSimpleClient oAuthSimpleClient;
    private final String nfvoAddress;
    private final String username;
    private final String password;
    private final NsPackagesApi nsPackagesApi;
    private final VnfPackagesApi vnfPackagesApi;
    private final FileUtilities fileUtilities; //don't delete. Used in download of vnf package

    private final NsdFileRegistryService nsdFileRegistryService;
    private final VnfdFileRegistryService vnfdFileRegistryService;

    // map the uuid generated from storing nsd to the ifa nds name
    private final Map<String,String> storedIfaNsdName;
    // map the uuid generated from storing nsd to the ifa nds name
    private final Map<String,String> storedIfaVnfdName;

    //map the vnfd ifa id to the vnfd ifa
    private final Map<String,Vnfd> ifaVnfdIdToIfaVnfd;

    //Map the NSD ID IFA to the list of OSM NSD UUIDs generated after translation
    private final Map<Nsd,List<UUID>> NsdIfaIdToNsdInfoIds;
    private final Map<UUID,String> NsdInfoIdToOsmNsdId;

    private boolean useFlavorInVnfdId = false;
    //this map the osm vnfd id to the osm vnfd UUID
    private final Map<String,UUID> vnfdIdToVnfdUUID;
    //to be deleted
    private final Map<String, VNFDescriptor> vnfdIdToOsmVnfd;


    public OsmCatalogueRestClient(String nfvoAddress, String username, String password, OAuthSimpleClient oAuthSimpleClient,
                                  NsdFileRegistryService nsdFileRegistryService,
                                  VnfdFileRegistryService vnfdFileRegistryService, boolean useFlavorInVnfdId){
        this.oAuthSimpleClient = oAuthSimpleClient;
        this.nfvoAddress = nfvoAddress;
        this.username = username;
        this.password = password;
        this.nsPackagesApi = new NsPackagesApi();
        this.vnfPackagesApi = new VnfPackagesApi();
        this.fileUtilities = new FileUtilities(System.getProperty("java.io.tmpdir"));

        this.nsdFileRegistryService = nsdFileRegistryService;
        this.vnfdFileRegistryService = vnfdFileRegistryService;

        this.storedIfaNsdName = new HashMap<>();
        this.storedIfaVnfdName = new HashMap<>();

        this.ifaVnfdIdToIfaVnfd = new HashMap<>();

        this.NsdIfaIdToNsdInfoIds = new HashMap<>();
        this.NsdInfoIdToOsmNsdId = new HashMap<>();

        this.vnfdIdToVnfdUUID = new HashMap<>();
        this.vnfdIdToOsmVnfd = new HashMap<>();
        this.useFlavorInVnfdId=useFlavorInVnfdId;
    }


    //******************************** NSD onboarding methods ********************************//

    /**
     * Takes the onboarding request and generate the NSDs package
     * @param request
     * @return String, the UUID of the onboarded NSD
     */
    public String onboardNsd(OnboardNsdRequest request) throws MethodNotImplementedException,
            MalformattedElementException, AlreadyExistingEntityException, FailedOperationException, ApiException {

        boolean onboarded = false;
        Nsd nsd = request.getNsd();
        if(nsd == null){
            log.error("NSD for onboarding is empty");
            throw new MalformattedElementException();
        }

        //contains all the OSM NSD UUIDs generated from the IFA NSD
        List<UUID> nsdInfoIds = new ArrayList<>();
        for (NsDf df : nsd.getNsDf()) { // Generate a OSM NSD for each DF
            //Map<String,String> vnfProfileId;
            //check if the associated vnfds to this nsd,df are in the NFVO
            if (!checkVNFDExistence(nsd, df)) {
                log.debug("VNFDs associated to this <NSD,DF> aren't in the NFVO Catalogue. Skipping the onboarding of this NS");
            } else {
                HashMap<String, Boolean> useTemplateVNFDs = new HashMap<>();
                if (df.getNsInstantiationLevel().size() > 1) {
                    //there is at least one scaling rule to add
                    if (!updateVNFDs(nsd, df, useTemplateVNFDs)) {
                        // Error during the update of vnfd package with scaling rule
                        log.error("Cannot update VNFD package with autoscaling rule");
                        throw new FailedOperationException();
                    }
                }
                // Now create and onboard NSD
                //call the translation process that return nsd tar file
                File compressFilePath = IfaOsmTranslator.createPackageForNsd(nsd, df, useTemplateVNFDs,useFlavorInVnfdId );
                NSDescriptor nsdOsm = IfaOsmTranslator.getGeneratedOsmNsd(); //TODO validate if this could change (maybe no more useful)
                UUID nsdInfoId=null;
                //this post request create a new ns descriptor resource
                try {
                    boolean alreadyOnboarded = false;
                    nsPackagesApi.setApiClient(getClient());
                    String nsdIdOsm = IfaOsmTranslator.getOsmNsdId(nsd, df);
                    log.debug("Searching if onboarded: "+nsdIdOsm);
                    for( NsdInfo curNsdInfo : nsPackagesApi.getNSDs()){
                        log.debug("Found onboarded: "+curNsdInfo.getId());
                        if(curNsdInfo.getId().equals(nsdIdOsm)){
                            log.debug("Found !!, skipping onboarding");
                            alreadyOnboarded=true;
                            nsdInfoId = curNsdInfo.getIdentifier();
                            break;
                        }
                    }
                    //this will create a new NSD resource
                    if(!alreadyOnboarded){
                        ObjectId response = nsPackagesApi.addNSD(new CreateNsdInfoRequest());
                        nsdInfoId = response.getId();
                        log.debug("Created NSD resource with UUID: " + nsdInfoId.toString());
                        nsPackagesApi.updateNSDcontent(nsdInfoId.toString(), compressFilePath);
                        log.debug("Updated the NSD resource with UUID: " + nsdInfoId + "\n  with the NSD of id: " + nsdOsm.getId());
                    }else{
                        log.debug("NSD already onboarded: " + nsdInfoId );
                    }
                    NsdInfoIdToOsmNsdId.put(nsdInfoId, IfaOsmTranslator.getOsmNsdId(nsd, df));

                } catch (ApiException e) {
                    log.error("Creation NSD resource failed", e);
                    log.error("Error during creation of NSD resource!", e.getResponseBody());
                    if(nsdInfoId!=null){
                        nsPackagesApi.deleteNSD(nsdInfoId.toString());
                    }
                    throw new FailedOperationException(e.getMessage());
                }

                nsdInfoIds.add(nsdInfoId);
                onboarded = true;
            }
        }

        if(onboarded){
            //this store the nsd with random uuid name for the json file
            String uuidStoredNsd = nsdFileRegistryService.storeNsd(nsd);
            storedIfaNsdName.put(uuidStoredNsd,nsd.getNsdIdentifier());
        }
        else{
            log.debug("No NS has been onboarded. VNFDs not present in NFVO Catalogue");
            throw new FailedOperationException("VNFDs for this NS not found in NFVO Catalogue.");
        }
        //saving nsd descriptor named with nsd_id
        //storeIfaNsd(nsd,folderIfaDescriptors);

        //NsdIfaIdToNsdInfoIds.put(UUID.fromString(nsd.getNsdIdentifier()),nsdInfoIds);
        return nsd.getNsdIdentifier(); //TODO validate this return
    }

    private boolean checkVNFDExistence(Nsd nsd, NsDf df) {
        for (String vnfdIfaId : nsd.getVnfdId()) {
            String osmVnfdId = null;
            try {
                //try to check locally
                osmVnfdId = getOsmVnfdId(vnfdIfaId, IfaOsmTranslator.getFlavourFromVnfdId(df, vnfdIfaId));
                if(!vnfdIdToVnfdUUID.containsKey(osmVnfdId)){
                    //try to check in OSM.
                    vnfPackagesApi.setApiClient(getClient());
                    ArrayOfVnfPkgInfo arrayOfVnfPkgInfo = vnfPackagesApi.getVnfPkgs();
                    for(VnfPkgInfo vnfPkgInfo : arrayOfVnfPkgInfo){
                        if(vnfPkgInfo.getId().equals(osmVnfdId))
                            //let backend know the mapping
                            vnfdIdToVnfdUUID.put(osmVnfdId,vnfPkgInfo.getIdentifier());
                    }
                }
            } catch (NotExistingEntityException e) {
                log.error("Cannot obtain id of Osm vnfd from Ifa constituent vnfd");
                e.printStackTrace();
                return false;
            } catch (ApiException e) {
                return false;
            } catch (FailedOperationException e) {
                log.error("Cannot set api client.");
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * Update the content of a VNFD template with scaling rule.
     * This function actually creates a new vnf descriptor in OSM
     * starting by the already onboarded VNFD template.
     *
     * Id of the new VNFD created.
     * @param nsd
     * @param df
     * @param useTemplateVNFDs
     * @return String
     */
    private boolean updateVNFDs(Nsd nsd, NsDf df, HashMap<String, Boolean> useTemplateVNFDs) throws FailedOperationException {
        String nsdIdWithFlavour = nsd.getNsdIdentifier()+"_"+df.getNsDfId();
        //We need to upload the content of each constituent vnfd within this nsd by providing the scaling rule

        //get default IL
        NsLevel defaultIL = null;
        try {
            defaultIL = df.getDefaultInstantiationLevel();
        } catch (NotExistingEntityException e) {
            e.printStackTrace();
            log.error("Cannot retrieve default IL from IFA NSD");
            return false;
        }

        HashMap<String,String> vnfProfileIdToVnfId = generateVnfProfileIdMapping(nsd,df);
        for (String vnfdIfaId : nsd.getVnfdId()) {
            String osmVnfdId = null;
            //this is the id of vnfd template
            try {
                osmVnfdId = getOsmVnfdId(vnfdIfaId, IfaOsmTranslator.getFlavourFromVnfdId(df, vnfdIfaId));
            } catch (NotExistingEntityException e) {
                log.error("Cannot obtain id of Osm vnfd from Ifa constituent vnfd");
                e.printStackTrace();
                return false;
            }
            String yamlDescriptor = null;
            try {
                //this is the yaml descriptor of the vnfd template
                yamlDescriptor = vnfPackagesApi.getVnfPkgVnfd(vnfdIdToVnfdUUID.get(osmVnfdId).toString());
            } catch (ApiException e) {
                e.printStackTrace();
                log.error("Cannot retrieve descriptor for UUID " + vnfdIdToVnfdUUID.get(osmVnfdId).toString());
                return false;
            }
            VNFDescriptor vnfDescriptor = getNewVNFDescriptorFromYamlString(yamlDescriptor);
            if (vnfDescriptor == null) {
                log.error("Cannot find a vnf package with id " + osmVnfdId);
                return false;
            }

            //creating new  VNFD resource
            try {
                vnfPackagesApi.setApiClient(getClient());
            } catch (FailedOperationException e) {
                e.printStackTrace();
                return false;
            }
            ObjectId response = null;
            UUID vnfdInfoId = null;
            try {
                response = vnfPackagesApi.addVnfPkg(new CreateVnfPkgInfoRequest());
                vnfdInfoId = response.getId();
                log.debug("Created a new VNFD Resource with UUID: " + vnfdInfoId);
            } catch (ApiException e) {
                e.printStackTrace();
                return false;
            }
            //TODO Check the onboarding of new nsd when it is already present the vnfd containig the scaling rule
            boolean deleteResource = false;
            int result = addAutoscalingRules(df, defaultIL, vnfDescriptor, vnfProfileIdToVnfId,nsdIdWithFlavour);
            //add autoscaling for each vnfd that are in this nsd
            if (result==1) {
                //Vnfd modified. We need to upload it on OSM
                File compressVnfdFile = IfaOsmTranslator.updateVnfPackage(vnfDescriptor, nsdIdWithFlavour);
                if (compressVnfdFile != null) {
                    try {
                        vnfPackagesApi.uploadVnfPkgContent(vnfdInfoId.toString(), compressVnfdFile);
                        log.debug("Onboarded VNFD " + vnfDescriptor.getId() + "_" + nsdIdWithFlavour);
                        useTemplateVNFDs.put(vnfdIfaId,false);
                    } catch (ApiException e1) {
                        log.error("Cannot onboard updated VNFD. Using default template.");
                        useTemplateVNFDs.put(vnfdIfaId,true);
                        //delete package resource
                        try {
                            vnfPackagesApi.deleteVnfPkg(vnfdInfoId.toString());
                            return false;
                        } catch (ApiException e) {
                            log.error("Cannot delete package resource");
                            throw new FailedOperationException();
                        }
                    }
                } else {
                    log.error("Cannot compress file");
                    deleteResource = true;
                }
            } else if(result == 0){
                //autoscaling rule not needed for this vnfd
                useTemplateVNFDs.put(vnfdIfaId,true);
                deleteResource = true;
            }
            else{
                //vnfd modified already present
                useTemplateVNFDs.put(vnfdIfaId,false);
                deleteResource = true;
            }
            if (deleteResource) {
                //delete package resource
                try {
                    vnfPackagesApi.deleteVnfPkg(vnfdInfoId.toString());
                    log.error("Cannot onboard new VNFD");
                } catch (ApiException e) {
                    e.printStackTrace();
                    log.error("Cannot delete package resource");
                }
            }
        }
        return true;
    }

    private VNFDescriptor getNewVNFDescriptorFromYamlString(String vnfDescriptor) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(TEMP_DIR+ File.separator+"osmVnfd.yaml");
            out.print(vnfDescriptor);
            out.close();
            ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
            yamlReader.findAndRegisterModules();
            File tempFile = new File(TEMP_DIR+ File.separator+"osmVnfd.yaml");
            OsmVNFPackage vnfDescriptorPackage = null;
            vnfDescriptorPackage = yamlReader.readValue(tempFile, OsmVNFPackage.class);
            tempFile.delete();
            return vnfDescriptorPackage.getVnfdCatalog().getVnfd().get(0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
            log.info("Cannot generate OsmVNFPackage from string");
        }
        return null;
    }

    /**
     * Generates an hashmap with the association
     *      vnf_profile_id_Ifa - vnf_id_Osm
     * @param nsd the ifa nsd
     * @param df the df of this nsd
     * @return hashmap that contains associations
     */
    private HashMap<String, String> generateVnfProfileIdMapping(Nsd nsd, NsDf df) {
        HashMap<String,String> map = new HashMap<>();

        for(VnfProfile vnfProfile : df.getVnfProfile()){
            map.put(vnfProfile.getVnfProfileId(),vnfProfile.getVnfdId()+"_"+vnfProfile.getFlavourId());
        }
        return map;
    }

    /**
     * Takes Nsd Ifa descriptor and generates the autoscaling rule for each constituent VNF
     * To be moved in IfaOsmTranslator
     * @param df
     * @param vnfDescriptor
     * @param vnfProfileIdToVnfId
     * @return int
     */
    private int addAutoscalingRules(NsDf df, NsLevel defaultIL, VNFDescriptor vnfDescriptor, HashMap<String, String> vnfProfileIdToVnfId, String nsdId) throws FailedOperationException {
        //-1 -> modified vnfd already exist
        // 0 -> scaling rule not required
        // 1 -> scaling rule added

        //adding a scaling rules to this vnfDescriptor
        HashMap<String,Integer> defaultNumberOfInstances = new HashMap<>();
        /*assumption the default instantiation level has always the min number of instances*/
        for(VnfToLevelMapping vnfToLevelMapping : defaultIL.getVnfToLevelMapping()){
            defaultNumberOfInstances.put(vnfToLevelMapping.getVnfProfileId(),vnfToLevelMapping.getNumberOfInstances());
        }
        //check if there are scaling group to add
        boolean toAdd = false;
        int i=0;
        for(NsLevel nsLevel : df.getNsInstantiationLevel()){
            if(!nsLevel.getNsLevelId().equals(defaultIL.getNsLevelId())){
                for(VnfToLevelMapping vnfToLevelMapping : nsLevel.getVnfToLevelMapping()){
                    if(vnfProfileIdToVnfId.get(vnfToLevelMapping.getVnfProfileId()).equals(vnfDescriptor.getId())
                            && vnfToLevelMapping.getNumberOfInstances() > defaultNumberOfInstances.get(vnfToLevelMapping.getVnfProfileId())){
                        //check if the vnf is in the IL and if the number of istances is different from default IL
                        toAdd = true;
                        break;
                    }
                }
            }
            if(toAdd) break;
        }
        if(!toAdd) return 0;
        //check if the modified vnfd is already present in osm
        String vnfdId = vnfDescriptor.getId().concat("_"+nsdId);
        //try to check locally
        if(!vnfdIdToVnfdUUID.containsKey(vnfdId)){
            //try to check in OSM.
            vnfPackagesApi.setApiClient(getClient());
            ArrayOfVnfPkgInfo arrayOfVnfPkgInfo = null;
            try {
                arrayOfVnfPkgInfo = vnfPackagesApi.getVnfPkgs();
            } catch (ApiException e) {
                log.error("Cannot retrieve the list of VNFD.");
                throw new FailedOperationException();
            }
            for(VnfPkgInfo vnfPkgInfo : arrayOfVnfPkgInfo){
                if(vnfPkgInfo.getId() != null && vnfPkgInfo.getId().equals(vnfdId)){
                    //let backend know the mapping
                    vnfdIdToVnfdUUID.put(vnfdId,vnfPkgInfo.getIdentifier());
                    return -1;
                }
            }
        }
        else return -1;

        //There at least a scaling rule to add
        List<ScalingGroupDescriptor> scalingGroupDescriptorList = new ArrayList<>();
        for(NsLevel nsLevel : df.getNsInstantiationLevel()){
            for(VnfToLevelMapping vnfToLevelMapping : nsLevel.getVnfToLevelMapping()){
                if(vnfProfileIdToVnfId.get(vnfToLevelMapping.getVnfProfileId()).equals(vnfDescriptor.getId())){
                    if(vnfDescriptor.getVduList().size() >= 2){
                        log.error("Scaling group definition for multi-vdu vnf is not yet supported");
                        throw new FailedOperationException();
                    }
                    //add new scaling group
                    ScalingGroupDescriptor scalingGroupDescriptor = new ScalingGroupDescriptor();
                    //encoding of the default instantantiation level in the name of the scaling-group
                    if(nsLevel.getNsLevelId().equals(defaultIL.getNsLevelId()))
                        scalingGroupDescriptor.setName(nsLevel.getNsLevelId()+"-default");
                    else scalingGroupDescriptor.setName(nsLevel.getNsLevelId());
                    scalingGroupDescriptor.setMinInstanceCount(0);
                    scalingGroupDescriptor.setMaxInstanceCount(vnfToLevelMapping.getNumberOfInstances()+1);

                    List<ScalingPolicy> scalingPolicyList = new ArrayList<>();
                    ScalingPolicy scalingPolicy = new ScalingPolicy();
                    scalingPolicy.setName(nsLevel.getNsLevelId());
                    scalingPolicy.setScalingType("manual");
                    scalingPolicy.setEnabled(true);
                    scalingPolicy.setThresholdTime(1);
                    scalingPolicy.setCooldownTime(10);
                    scalingPolicyList.add(scalingPolicy);
                    scalingGroupDescriptor.setScalingPolicies(scalingPolicyList);

                    //TODO this will work only if there is always a single vdu
                    List<VduReference> vduReferenceList = new ArrayList<>();
                    VduReference vduReference = new VduReference();
                    vduReference.setVduIdRef(vnfDescriptor.getVduList().get(0).getId());
                    vduReference.setCount(vnfToLevelMapping.getNumberOfInstances()-defaultNumberOfInstances.get(vnfToLevelMapping.getVnfProfileId()));
                    vduReferenceList.add(vduReference);
                    scalingGroupDescriptor.setVduList(vduReferenceList);
                    scalingGroupDescriptorList.add(scalingGroupDescriptor);
                }
            }
        }
        vnfDescriptor.setScalingGroupDescriptor(scalingGroupDescriptorList);
        return 1;
    }

    /**
     * Print the list of instantiated NS (for testing purpose)
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public void getNSDs(){
        try{
            nsPackagesApi.setApiClient(getClient());
            List<NsdInfo> nsdInfoList = nsPackagesApi.getNSDs();
            for(NsdInfo nsdInfo : nsdInfoList){
                System.out.println(nsdInfo);
            }
        } catch (FailedOperationException e) {
            log.error("Setting api client failed: "+ e);
        } catch (ApiException e) {
            log.error("Api exception: " + e);
        }
    }

    public QueryNsdResponse queryNsd(GeneralizedQueryRequest request) throws NotExistingEntityException, FailedOperationException {
        List<it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.elements.NsdInfo> nsdInfoList = new ArrayList<>();
        //this is the id of the NSD IFA requested
        String nsdId = request.getFilter().getParameters().get("NSD_ID");
        String nsdVersion = request.getFilter().getParameters().get("NSD_VERSION");
        it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.elements.NsdInfo nsdInfo;
        //here the nsdInfoId of nsdInfo is a combination of nsdId + nsdVersion to uuid
        if(nsdVersion == null) nsdInfo = nsdFileRegistryService.queryNsd(nsdId);
        else nsdInfo = nsdFileRegistryService.queryNsd(nsdId,nsdVersion);
        //workaround because if we answer with nsdInfo, then at instantiation time OsmLcmDriver will receive as id the id corresponding to
        //the random uuid generated when storing the nsd
        it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.elements.NsdInfo newNsdInfo = new it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.elements.NsdInfo(
                storedIfaNsdName.get(nsdInfo.getNsdInfoId()),
                nsdInfo.getNsdId(),
                nsdInfo.getName(),
                nsdInfo.getVersion(),
                nsdInfo.getDesigner(),
                nsdInfo.getNsd(),
                nsdInfo.getOnboardedVnfPkgInfoId(),
                nsdInfo.getPnfdInfoId(),
                nsdInfo.getPreviousNsdVersionId(),
                nsdInfo.getOperationalState(),
                nsdInfo.getUsageState(),
                nsdInfo.isDeletionPending(),
                nsdInfo.getUserDefinedData()
        );
        nsdInfoList.add(newNsdInfo);
        QueryNsdResponse queryNsdResponse = new QueryNsdResponse(nsdInfoList);
        log.debug("Retrieved NSD " + nsdId);
        return queryNsdResponse;
    }

    //******************************** VNFD onboarding methods ********************************//

    /**
     * Takes the onboarding request and generate the VNFDs package
     * @param request
     * @return OnBoardVnfPackageResponse
     */

    public OnBoardVnfPackageResponse onboardVnfPackage(OnBoardVnfPackageRequest request) throws MalformattedElementException, FailedOperationException {
        log.debug("Getting VNF package");
        String vnfPackagePath = request.getVnfPackagePath();
        log.debug("Retrieving VNFD from VNF package");
        String folder = null;
        Vnfd vnfd = null;
        File cloudInit = null;
        try{
            String downloadedFile = fileUtilities.downloadFile(vnfPackagePath);
            folder = fileUtilities.extractFile(downloadedFile);
            File jsonFile = fileUtilities.findJsonFileInDir(folder);
            //TODO cloud-config to be replaced with the name specified in start script instantiation of ifa vnfd
            cloudInit = new File(TEMP_DIR+File.separator+folder,"cloud-config.txt");
            if(cloudInit.exists()){
                //need to not delete cloud init
                Files.move(Paths.get(cloudInit.getAbsolutePath()),Paths.get(TEMP_DIR+File.separator+"cloud-config.txt"), StandardCopyOption.REPLACE_EXISTING);
            }
            Charset encoding = null;
            String json = FileUtils.readFileToString(jsonFile, encoding);
            log.debug("VNFD json: \n" + json);

            ObjectMapper mapper = new ObjectMapper();
            vnfd = (Vnfd) mapper.readValue(json, Vnfd.class);
            log.debug("VNFD correctly parsed.");

            log.debug("Cleaning local directory");
            fileUtilities.removeFileAndFolder(downloadedFile, folder);

        } catch (ArchiveException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*ObjectMapper mapper = new ObjectMapper();
        try {
            vnfd = (Vnfd) mapper.readValue(Paths.get(request.getName()).toFile(), Vnfd.class);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        if(vnfd == null) throw new MalformattedElementException("VNFD for onboarding is empty");
        try {
            String uuidStoredVnfd = vnfdFileRegistryService.storeVnfd(request,vnfd);
            storedIfaVnfdName.put(uuidStoredVnfd,vnfd.getVnfdId());
            ifaVnfdIdToIfaVnfd.put(vnfd.getVnfdId(),vnfd);
        } catch (AlreadyExistingEntityException e) {
            log.debug("VNFD ifa already present.");
            //TODO se è già presente necessità di essere refreshato?
        }
        //maybe a vnfd for each couple of vnfd,vnfDf?
        for(VnfDf vnfdDf : vnfd.getDeploymentFlavour()){
            if(vnfdIdToVnfdUUID.containsKey(vnfd.getVnfdId()+"_"+vnfdDf.getFlavourId())){
                log.debug("This VNFD is already present in the NFVO Catalgoue, skipping onboarding.");
            }
            else{
                boolean skipOnboard = false;
                //check in osm
                vnfPackagesApi.setApiClient(getClient());
                String osmVnfdId = getOsmVnfdId(vnfd.getVnfdId(),vnfdDf.getFlavourId());
                ArrayOfVnfPkgInfo arrayOfVnfPkgInfo = null;
                try {
                    arrayOfVnfPkgInfo = vnfPackagesApi.getVnfPkgs();
                } catch (ApiException e) {
                    log.error("Cannot retrieve the list of VNFDs.");
                    throw new FailedOperationException();
                }
                for(VnfPkgInfo vnfPkgInfo : arrayOfVnfPkgInfo){
                    if(vnfPkgInfo.getId().equals(osmVnfdId)) {
                        //let backend know the mapping
                        vnfdIdToVnfdUUID.put(osmVnfdId, vnfPkgInfo.getIdentifier());
                        skipOnboard = true;
                        log.debug("VNFD : " + osmVnfdId + " already present. Skipping onboarding");
                    }
                }
                if(!skipOnboard){
                    File compressFilePath = IfaOsmTranslator.createPackageForVnfd(vnfd,vnfdDf);
                    VNFDescriptor vnfdOsm = IfaOsmTranslator.getGeneratedOsmVnfd(); //TODO validate if this could change
                    UUID vnfdInfoId = null;
                    //create a new Vnfd resource
                    try {
                        vnfPackagesApi.setApiClient(getClient());
                        ObjectId response = vnfPackagesApi.addVnfPkg(new CreateVnfPkgInfoRequest());
                        vnfdInfoId = response.getId();
                        log.debug("Created a new VNFD Resource with UUID: " + vnfdInfoId);
                    } catch (FailedOperationException e) {
                        log.error("Failed to set Api client",e);
                        throw new FailedOperationException(e.getMessage());
                    } catch (ApiException e) {
                        log.error("Creation VNFD resource failed",e);
                        throw new FailedOperationException(e.getResponseBody());
                    }
                    try {
                        vnfPackagesApi.uploadVnfPkgContent(vnfdInfoId.toString(),compressFilePath);
                        log.debug("Updated the VNFD resource with UUID: " + vnfdInfoId + "\n  with the VNFD of id: " + vnfdOsm.getId());
                    } catch (ApiException e) {
                        //TODO to log error
                        //the resource pointed by nsdInfoId has not been modified due to exception, need to delete it
                        try {
                            vnfPackagesApi.deleteVnfPkg(vnfdInfoId.toString());
                            log.debug("Deleted VNFD Resource with UUID: " + vnfdInfoId);
                        } catch (ApiException apiException) {
                            log.error("Can't delete vnfdInfoId resource", apiException);
                            throw new FailedOperationException(apiException.getResponseBody());
                        }
                        throw new FailedOperationException("Error on VNFD onboarding!" + e.getResponseBody());
                    }
                    vnfdIdToVnfdUUID.put(vnfd.getVnfdId()+"_"+vnfdDf.getFlavourId(),vnfdInfoId);
                }
            }
        }

        UUID randomUUID = UUID.randomUUID();
        //maybe a map
        OnBoardVnfPackageResponse onBoardVnfPackageResponse = new OnBoardVnfPackageResponse(randomUUID.toString(),vnfd.getVnfdId());
        //CHECK WHAT RETURN IF VNFD ALREADY PRESENT
        return onBoardVnfPackageResponse;
    }

    private String getOsmVnfdId(String vnfdId, String flavourId) {

        //return vnfdId +"_"+flavourId;
        return vnfdId;


    }

    public QueryOnBoardedVnfPkgInfoResponse queryVnfPackageInfo(GeneralizedQueryRequest request) throws NotExistingEntityException {
        List<OnboardedVnfPkgInfo> onboardedVnfPkgInfoList = new ArrayList<>();
        //this is the id of the VNFD IFA requested
        String vnfdId = request.getFilter().getParameters().get("VNFD_ID");
        OnboardedVnfPkgInfo onboardedVnfPkgInfo = vnfdFileRegistryService.queryVnf(vnfdId);
        OnboardedVnfPkgInfo newOnboardedVnfPkgInfo = new OnboardedVnfPkgInfo(
                storedIfaVnfdName.get(onboardedVnfPkgInfo.getVnfdId()),
                onboardedVnfPkgInfo.getVnfdId(),
                onboardedVnfPkgInfo.getVnfProvider(),
                onboardedVnfPkgInfo.getVnfProductName(),
                onboardedVnfPkgInfo.getVnfSoftwareVersion(),
                onboardedVnfPkgInfo.getVnfdVersion(),
                onboardedVnfPkgInfo.getChecksum(),
                onboardedVnfPkgInfo.getVnfd(),
                onboardedVnfPkgInfo.getSoftwareImage(),
                onboardedVnfPkgInfo.getAdditionalArtifact(),
                onboardedVnfPkgInfo.getOperationalState(),
                onboardedVnfPkgInfo.getUsageState(),
                onboardedVnfPkgInfo.isDeletionPending(),
                onboardedVnfPkgInfo.getUserDefinedData()
        );
        onboardedVnfPkgInfoList.add(newOnboardedVnfPkgInfo);
        QueryOnBoardedVnfPkgInfoResponse queryOnBoardedVnfPkgInfoResponse = new QueryOnBoardedVnfPkgInfoResponse(onboardedVnfPkgInfoList);
        return queryOnBoardedVnfPkgInfoResponse;
    }
    //******************************** Client API ********************************//

    private ApiClient getClient() throws FailedOperationException {

        ApiClient apiClient = new ApiClient();
        apiClient.setHttpClient(OAuthSimpleClient.getUnsafeOkHttpClient());
        apiClient.setBasePath(nfvoAddress+"/osm/");
        apiClient.setUsername(username);
        apiClient.setPassword(password);
        apiClient.setAccessToken(oAuthSimpleClient.getToken());
        return apiClient;
    }

    //******************************** Test Functions ********************************//

    /*public List<KeyPair> getKeyPair(String packageId) throws FailedOperationException {
        String yamlDescriptor = null;
        try {
            nsPackagesApi.setApiClient(getClient());
            //this is the yaml descriptor of the vnfd template
            yamlDescriptor = nsPackagesApi.getNsPkgNsd(packageId);
        } catch (ApiException e) {
           throw new FailedOperationException();
        }
        NSDescriptor nsDescriptor = getNewNSDescriptorFromYamlString(yamlDescriptor);
        return nsDescriptor.getKeyPairs();
    }

    private NSDescriptor getNewNSDescriptorFromYamlString(String nsDescriptor) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(TEMP_DIR+ File.separator+"osmNsd.yaml");
            out.print(nsDescriptor);
            out.close();
            ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
            yamlReader.findAndRegisterModules();
            File tempFile = new File(TEMP_DIR+ File.separator+"osmNsd.yaml");
            OsmNSPackage vnfDescriptorPackage = null;
            vnfDescriptorPackage = yamlReader.readValue(tempFile, OsmNSPackage.class);
            tempFile.delete();
            return vnfDescriptorPackage.getNsdCatalog().getNsds().get(0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
            log.info("Cannot generate OsmVNFPackage from string");
        }
        return null;
    }*/
}
