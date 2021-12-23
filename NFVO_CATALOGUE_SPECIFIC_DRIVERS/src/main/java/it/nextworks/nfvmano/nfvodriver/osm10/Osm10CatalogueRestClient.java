package it.nextworks.nfvmano.nfvodriver.osm10;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.swagger.client.model.*;
import it.nextworks.nfvmano.libs.ifa.common.enums.UsageState;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.*;
import it.nextworks.nfvmano.libs.ifa.common.messages.GeneralizedQueryRequest;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.*;
import it.nextworks.nfvmano.libs.ifa.descriptors.onboardedvnfpackage.OnboardedVnfPkgInfo;
import it.nextworks.nfvmano.libs.ifa.descriptors.vnfd.VnfDf;
import it.nextworks.nfvmano.libs.ifa.descriptors.vnfd.Vnfd;
import it.nextworks.nfvmano.libs.ifasol.catalogues.interfaces.enums.NsdFormat;
import it.nextworks.nfvmano.libs.ifasol.catalogues.interfaces.messages.*;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.nsDescriptor.NSDescriptor;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.*;
import it.nextworks.nfvmano.nfvodriver.dummy.FileUtilities;
import it.nextworks.nfvmano.nfvodriver.file.NsdFileRegistryService;
import it.nextworks.nfvmano.nfvodriver.file.VnfdFileRegistryService;
import it.nextworks.osm.ApiClient;
import it.nextworks.osm.ApiException;
import it.nextworks.osm.openapi.NsPackagesApi;
import it.nextworks.osm.openapi.VnfPackagesApi;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * REST client to interact with OSM NFVO.
 *
 * @author nextworks
 *
 */

public class Osm10CatalogueRestClient {

    private static final Logger log = LoggerFactory.getLogger(Osm10CatalogueRestClient.class);
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
    private final Map<Nsd,List<UUID>> nsdIfaIdToNsdInfoIds;
    private final Map<UUID,String> nsdInfoIdToOsmNsdId;

    //this map the osm vnfd id to the osm vnfd UUID
    private final Map<String,UUID> vnfdIdToVnfdUUID;
    //to be deleted
    private final Map<String, VNFDescriptor> vnfdIdToOsmVnfd;


    public Osm10CatalogueRestClient(String nfvoAddress, String username, String password, OAuthSimpleClient oAuthSimpleClient,
                                    NsdFileRegistryService nsdFileRegistryService,
                                    VnfdFileRegistryService vnfdFileRegistryService){
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

        this.nsdIfaIdToNsdInfoIds = new HashMap<>();
        this.nsdInfoIdToOsmNsdId = new HashMap<>();

        this.vnfdIdToVnfdUUID = new HashMap<>();
        this.vnfdIdToOsmVnfd = new HashMap<>();
    }


    //******************************** NSD onboarding methods ********************************//

    /**
     * Takes the onboarding request and generate the NSDs package
     * @param request
     * @return String, the UUID of the onboarded NSD
     */
    public String onboardNsdSol(OnboardNsdSolRequest request) throws MethodNotImplementedException,
            MalformattedElementException, AlreadyExistingEntityException, FailedOperationException, ApiException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            log.debug("onboardNsd", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));
        } catch (JsonProcessingException e) {
            log.error("Error processing NSD", e);
            throw new MalformattedElementException(e.getMessage());
        }
        nsPackagesApi.setApiClient(getClient());
        ObjectId response = nsPackagesApi.addNSD(new CreateNsdInfoRequest());
        UUID nsdInfoId = response.getId();
        File compressFilePath = IfaOsmTranslator.createPackageForNsd(request.getNsd());
        nsPackagesApi.updateNSDcontent(nsdInfoId.toString(), compressFilePath);
        return nsdInfoId.toString();
    }

    public String onboardNsd(OnboardNsdRequest request) throws MethodNotImplementedException,
            MalformattedElementException, AlreadyExistingEntityException, FailedOperationException, ApiException {
        if(request.getNsdFormat()== NsdFormat.IFA){
            return onboardNsdIfa((OnboardNsdIfaRequest) request);
        } else if (request.getNsdFormat()==NsdFormat.SOL006){
            return onboardNsdSol((OnboardNsdSolRequest) request);
        }else   throw new MethodNotImplementedException("NSD Format not supported");
    }
    /**
     * Takes the onboarding request and generate the NSDs package
     * @param request
     * @return String, the UUID of the onboarded NSD
     */
    public String onboardNsdIfa(OnboardNsdIfaRequest request) throws MethodNotImplementedException,
            MalformattedElementException, AlreadyExistingEntityException, FailedOperationException, ApiException {
        boolean onboarded = false;
        if(request.getNsdFormat()!= NsdFormat.IFA)
            throw new MethodNotImplementedException("NSD Format not supported");
        Nsd nsd = ((OnboardNsdIfaRequest)request).getNsd();
        if(nsd == null){
            log.error("NSD for onboarding is empty");
            throw new MalformattedElementException();
        }
        //contains all the OSM NSD UUIDs generated from the IFA NSD
        List<UUID> nsdInfoIds = new ArrayList<>();

        // Generate a OSM NSD for each DF
        for (NsDf df : nsd.getNsDf()) {
            NsdInfo nsdInfo = null;
            //check if the nsd is already onboarded first locally, then in osm
            if(nsdInfoIdToOsmNsdId.containsValue(IfaOsmTranslator.getOsmNsdId(nsd,df))){
                log.debug("NSD already present locally. Skipping onboarding.");
                continue;
            } else if((nsdInfo = checkNSDExistence(IfaOsmTranslator.getOsmNsdId(nsd,df)))!= null){
                log.debug("NSD is already present in OSM, but not locally. Updating associations...");
                nsdInfoIdToOsmNsdId.put(nsdInfo.getIdentifier(), IfaOsmTranslator.getOsmNsdId(nsd,df));
                onboarded = true;
                continue;
            }
            //No nsd found. Need to onboard it. Check if the associated VNFDs to this <nsd,df> are present locally and then in OSM
            if (!checkVNFDExistence(nsd, df)) {
                log.debug("VNFDs associated to this <NSD,DF> aren't in the NFVO Catalogue. Skipping the onboarding of this NS");
                continue;
            }
            //VNFD presernt. Check if onboarded VNFDs require autoscaling rules.
            HashMap<String, Boolean> useTemplateVNFDs = new HashMap<>();
            if (df.getNsInstantiationLevel().size() > 1) {
                //there is at least one scaling rule to add
                if (!updateVNFDs(nsd, df, useTemplateVNFDs)) {
                    // Error during the update of vnfd package with scaling rule
                    log.error("Cannot update VNFD package with autoscaling rule");
                    throw new FailedOperationException();
                }
            } else{
                // this <nsd,df> use all default VNFDs (with no autoscaling rule)
                for (String vnfdIfaId : nsd.getVnfdId()) {
                    useTemplateVNFDs.put(vnfdIfaId,true);
                }
            }
            // Now create and onboard NSD
            //call the translation process that return nsd tar file
            File compressFilePath = IfaOsmTranslator.createPackageForNsd(nsd, df, useTemplateVNFDs);
            NSDescriptor nsdOsm = IfaOsmTranslator.getGeneratedOsmNsd();
            UUID nsdInfoId;
            //this post request create a new ns descriptor resource
            try {
                nsPackagesApi.setApiClient(getClient());
                //this will create a new NSD resource
                ObjectId response = nsPackagesApi.addNSD(new CreateNsdInfoRequest());
                nsdInfoId = response.getId();
                log.debug("Created NSD resource with UUID: " + nsdInfoId.toString());
                nsdInfoIdToOsmNsdId.put(nsdInfoId, IfaOsmTranslator.getOsmNsdId(nsd, df));
            } catch (ApiException e) {
                log.error("Error during creation of NSD resource. \n" + e.getResponseBody());
                throw new FailedOperationException(e.getMessage());
            }
            try {
                nsPackagesApi.updateNSDcontent(nsdInfoId.toString(), compressFilePath);
                log.debug("Updated the NSD resource with UUID: " + nsdInfoId + "\n  with the NSD of id: " + nsdOsm.getId());
            } catch (ApiException e) {
                //the resource pointed by nsdInfoId has not been modified due to exception, need to delete it
                nsPackagesApi.deleteNSD(nsdInfoId.toString());
                log.debug("Deleted NSD Resource with UUID: " + nsdInfoId);
                throw new FailedOperationException("Error on NSD onboarding!" + e.getResponseBody());
            }
            nsdInfoIds.add(nsdInfoId);
            onboarded = true;
        }

        if(onboarded){
            //this store the nsd with random uuid name for the json file
            String uuidStoredNsd = nsdFileRegistryService.storeNsd(nsd);
            storedIfaNsdName.put(uuidStoredNsd,nsd.getNsdIdentifier());
        }
        else{
            log.debug("No NSD has been onboarded.");
            return nsd.getNsdIdentifier();
        }
        //saving nsd descriptor named with nsd_id
        //storeIfaNsd(nsd,folderIfaDescriptors);

        //NsdIfaIdToNsdInfoIds.put(UUID.fromString(nsd.getNsdIdentifier()),nsdInfoIds);
        return nsd.getNsdIdentifier(); //TODO validate this return
    }

    private NsdInfo checkNSDExistence(String osmNsdId) {
        try{
            nsPackagesApi.setApiClient(getClient());
            List<NsdInfo> nsdInfoList = nsPackagesApi.getNSDs();
            for(NsdInfo nsdInfo : nsdInfoList){
                if(nsdInfo.getId().equals(osmNsdId))
                    return nsdInfo;
            }
            return null;
        } catch (FailedOperationException e) {
            log.error("Setting api client failed: "+ e);
        } catch (ApiException e) {
            log.error("Retrieve NSDs failed: "+ e);
        }
        return null;
    }

    /**
     * Check for each VNFDs of this <nsd,df>, if it is present first locally, then in OSM.
     * @param nsd
     * @param df
     * @return true if found.
     */
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
                log.debug("OSM VNFD ID: " + osmVnfdId);
                log.debug("Internal VNFD ID: " + vnfdIdToVnfdUUID.get(osmVnfdId).toString());
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


        String nsdId = request.getFilter().getParameters().get("NSD_ID");
        String nsdVersion = request.getFilter().getParameters().get("NSD_VERSION");
        List< it.nextworks.nfvmano.libs.descriptors.sol006.NsdInfo> nsdInfoList = new ArrayList<>();

        nsPackagesApi.setApiClient(getClient());
        try {
            List<NsdInfo> osmNsdInfoList = nsPackagesApi.getNSDs();
            for(NsdInfo nsdInfo : osmNsdInfoList){

                it.nextworks.nfvmano.libs.descriptors.sol006.NsdInfo newNsdInfo=
                        new it.nextworks.nfvmano.libs.descriptors.sol006.NsdInfo();
                newNsdInfo.setId(nsdInfo.getId());
                newNsdInfo.setIdentifier(nsdInfo.getIdentifier());
                newNsdInfo.setName(nsdInfo.getName());
                newNsdInfo.setDescription(nsdInfo.getDescription());
                newNsdInfo.setVersion(nsdInfo.getVersion());
                File nsdConent = nsPackagesApi.getNSDcontent(nsdInfo.getIdentifier().toString());
                String targetDir = TEMP_DIR+"/"+nsdInfo.getIdentifier();
                File dir =  new File(targetDir);
                dir.delete();
                try{
                    decompressFile(nsdConent, targetDir);
                    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    mapper.findAndRegisterModules();

                    String nsdContentFile = targetDir+"/"+nsdInfo.getId()+"/"+nsdInfo.getId()+".yaml";
                    JsonNode node = mapper.readTree(new File(nsdContentFile));

                    it.nextworks.nfvmano.libs.descriptors.sol006.Nsd nsdContent = mapper.treeToValue(node, it.nextworks.nfvmano.libs.descriptors.sol006.OsmNsSol6Package.class).getOsmNsdCatalogue().getNsds().get(0);
                    newNsdInfo.setNsd(nsdContent);
                }catch (Exception e){
                    log.error("Failed to retrieve NSD content for:"+nsdInfo.getIdentifier());
                }

                if(nsdId!=null){
                    if(newNsdInfo.getId().equals(nsdId)){
                        if(nsdVersion!=null){
                            if(newNsdInfo.getVersion().equals(nsdVersion))
                                nsdInfoList.add(newNsdInfo);
                        }else nsdInfoList.add(newNsdInfo);
                    }
                }else nsdInfoList.add(newNsdInfo);
            }
        } catch (ApiException e) {
            throw new FailedOperationException(e);
        }

        QueryNsdResponse queryNsdResponse = new QueryNsdSolResponse(nsdInfoList);
        log.debug("Retrieved NSD " + nsdId);
        return queryNsdResponse;
    }


    private void decompressFile(File input, String target) throws IOException {
        Path source = Paths.get(input.getAbsolutePath());
        // Where is the decompression
        Path targetDir = Paths.get(target);

        if (Files.notExists(source)) {
            throw new IOException(" The file you want to extract does not exist ");
        }

        //InputStream Input stream , The following four streams will tar.gz Read into memory and operate
        //BufferedInputStream Buffered input stream
        //GzipCompressorInputStream Decompress the input stream
        //TarArchiveInputStream Explain tar Packet input stream
        try (InputStream fi = Files.newInputStream(source);
             BufferedInputStream bi = new BufferedInputStream(fi);
             GzipCompressorInputStream gzi = new GzipCompressorInputStream(bi);
             TarArchiveInputStream ti = new TarArchiveInputStream(gzi)) {

            ArchiveEntry entry;
            while ((entry = ti.getNextEntry()) != null) {

                // Get the unzip file directory , And determine whether the file is damaged
                Path newPath = zipSlipProtect(entry, targetDir);

                if (entry.isDirectory()) {
                    // Create a directory for extracting files
                    Files.createDirectories(newPath);
                } else {
                    // Verify the existence of the extracted file directory again
                    Path parent = newPath.getParent();
                    if (parent != null) {
                        if (Files.notExists(parent)) {
                            Files.createDirectories(parent);
                        }
                    }
                    //  Input the extracted file into TarArchiveInputStream, Output to disk newPath Catalog
                    Files.copy(ti, newPath, StandardCopyOption.REPLACE_EXISTING);

                }
            }
        }

    }


    private  Path zipSlipProtect(ArchiveEntry entry,Path targetDir)
            throws IOException {

        Path targetDirResolved = targetDir.resolve(entry.getName());
        Path normalizePath = targetDirResolved.normalize();

        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException(" The compressed file has been damaged : " + entry.getName());
        }

        return normalizePath;
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
        return vnfdId +"_"+flavourId;
    }

    public QueryOnBoardedVnfPkgInfoResponse queryVnfPackageInfo(GeneralizedQueryRequest request) throws NotExistingEntityException {
        List<OnboardedVnfPkgInfo> onboardedVnfPkgInfoList = new ArrayList<>();
        //this is the id of the VNFD IFA requested
        String vnfdId = request.getFilter().getParameters().get("VNFD_ID");

        try {
            vnfPackagesApi.setApiClient(getClient());
            List<VnfPkgInfo> packageInfos = vnfPackagesApi.getVnfPkgs();
            List<it.nextworks.nfvmano.libs.descriptors.sol006.VnfPkgInfo> solPkgInfos = new ArrayList<>();
            for(VnfPkgInfo info : packageInfos){

                it.nextworks.nfvmano.libs.descriptors.sol006.VnfPkgInfo osmPkgInfo = new it.nextworks.nfvmano.libs.descriptors.sol006.VnfPkgInfo();
                osmPkgInfo.setDescription(info.getDescription());
                osmPkgInfo.setId(info.getId());
                osmPkgInfo.setIdentifier(info.getIdentifier());
                osmPkgInfo.setName(info.getName());
                File vnfConent = vnfPackagesApi.getVnfPkgContent(info.getIdentifier().toString());
                String targetDir = TEMP_DIR+"/"+info.getIdentifier();
                File dir =  new File(targetDir);
                dir.delete();
                try{
                    decompressFile(vnfConent, targetDir);
                    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    mapper.findAndRegisterModules();

                    String vnfdContentFile =getYamlFileFromFolder(new File(targetDir));
                    JsonNode node = mapper.readTree(new File(vnfdContentFile));

                    it.nextworks.nfvmano.libs.descriptors.sol006.Vnfd vnfdContent = mapper.treeToValue(node, it.nextworks.nfvmano.libs.descriptors.sol006.OsmVnfPackage.class).getVnfd();
                    osmPkgInfo.setVnfd(vnfdContent);
                }catch (Exception e){
                    log.error("Failed to retrieve VNF package content for:"+info.getIdentifier(), e);
                }
                if(vnfdId!=null){

                    if(info.getId().equals(vnfdId)){


                        solPkgInfos.add(osmPkgInfo);
                    }
                }else solPkgInfos.add(osmPkgInfo);

            }
            return new QueryOnBoardedVnfPkgInfoSolResponse(solPkgInfos);
        } catch (ApiException | FailedOperationException e) {
            throw new NotExistingEntityException(e);
        }

    }

    //******************************** PNFD ********************************//

    public String onboardPnfd(OnboardPnfdRequest request) {
        Pnfd pnfd = ((OnboardPnfdIfaRequest)request).getPnfd();
        File compressFilePath = IfaOsmTranslator.createPackageForPnfd(pnfd);
        return null;
    }


    private String getYamlFileFromFolder(File targetFolder) throws FailedOperationException {
        if(targetFolder.listFiles().length==1){
            File subdir = targetFolder.listFiles()[0];
            File[] matches = subdir.listFiles(new FilenameFilter()
            {
                public boolean accept(File dir, String name)
                {
                    return name.endsWith(".yaml");
                }
            });
            return matches[0].getAbsolutePath();
        }else throw new FailedOperationException("unknown content structure");
    }
    //******************************** Client API ********************************//

    private ApiClient getClient() throws FailedOperationException {

        ApiClient apiClient = new ApiClient();
        apiClient.setHttpClient(OAuthSimpleClient.getUnsafeOkHttpClient());
        apiClient.setBasePath(nfvoAddress+"/osm/");
        apiClient.setUsername(username);
        apiClient.setPassword(password);
        apiClient.setAccessToken(oAuthSimpleClient.getToken());
        apiClient.setDebugging(true);
        apiClient.setConnectTimeout(0);
        apiClient.setWriteTimeout(0);
        apiClient.setReadTimeout(0);
        return apiClient;
    }

}
