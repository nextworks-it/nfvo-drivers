package it.nextworks.nfvmano.nfvodriver.osm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.swagger.client.model.CreateNsdInfoRequest;
import io.swagger.client.model.CreateVnfPkgInfoRequest;
import io.swagger.client.model.NsdInfo;
import io.swagger.client.model.ObjectId;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.OnBoardVnfPackageRequest;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.OnBoardVnfPackageResponse;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.OnboardNsdRequest;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.*;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.*;
import it.nextworks.nfvmano.libs.ifa.descriptors.vnfd.VnfDf;
import it.nextworks.nfvmano.libs.ifa.descriptors.vnfd.Vnfd;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.nsDescriptor.NSDescriptor;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.*;
import it.nextworks.nfvmano.nfvodriver.dummy.FileUtilities;
import it.nextworks.osm.ApiClient;
import it.nextworks.osm.ApiException;
import it.nextworks.osm.openapi.NsPackagesApi;
import it.nextworks.osm.openapi.VnfPackagesApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
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

    //Map the NSD ID IFA to the list of OSM NSD UUIDs generated after translation
    private final Map<UUID,List<UUID>> NsdIfaIdToNsdInfoIds;
    private final Map<UUID,String> NsdInfoIdToOsmNsdId;

    private final Map<String,UUID> vnfdIdToVnfdUUID;
    private final Map<String, VNFDescriptor> vnfdIdToOsmVnfd;


    public OsmCatalogueRestClient(String nfvoAddress, String username, String password, OAuthSimpleClient oAuthSimpleClient){
        this.oAuthSimpleClient = oAuthSimpleClient;
        this.nfvoAddress = nfvoAddress;
        this.username = username;
        this.password = password;
        this.nsPackagesApi = new NsPackagesApi();
        this.vnfPackagesApi = new VnfPackagesApi();
        this.fileUtilities = new FileUtilities(System.getProperty("java.io.tmpdir"));

        this.NsdIfaIdToNsdInfoIds = new HashMap<>();
        this.NsdInfoIdToOsmNsdId = new HashMap<>();

        this.vnfdIdToVnfdUUID = new HashMap<>();
        this.vnfdIdToOsmVnfd = new HashMap<>();
    }

    //******************************** NSD methods ********************************//

    /**
     * Takes the onboarding request and generate the NSDs package
     * @param request
     * @return String, the UUID of the onboarded NSD
     */
    public String onboardNsd(OnboardNsdRequest request) throws MethodNotImplementedException,
            MalformattedElementException, AlreadyExistingEntityException, FailedOperationException, ApiException {

        Nsd nsd = request.getNsd();
        if(nsd == null) throw new MalformattedElementException("NSD for onboarding is empty");

        //contains all the OSM NSD UUIDs generated from the IFA NSD
        List<UUID> nsdInfoIds = new ArrayList<>();
        for (NsDf df : nsd.getNsDf()) { // Generate a OSM NSD for each DF
            Map<String,String> vnfProfileId;

            HashMap<String,Boolean> useTemplateVNFDs = new HashMap<>();
            if(df.getNsInstantiationLevel().size()> 1){
                //there is at least one scaling rule to add
                if(!updateVNFDs(nsd,df,useTemplateVNFDs)){
                    // Error during the update of vnfd package with scaling rule
                    throw new FailedOperationException("Cannot update VNFD package with autoscaling rule");
                }
            }
            // Now create and onboard NSD
            //call the translation process that return nsd tar file
            File compressFilePath = IfaOsmTranslator.createPackageForNsd(nsd,df,useTemplateVNFDs);
            NSDescriptor nsdOsm = IfaOsmTranslator.getGeneratedOsmNsd(); //TODO validate if this could change (maybe no more useful)
            UUID nsdInfoId;
            //this post request create a new ns descriptor resource
            try {
                nsPackagesApi.setApiClient(getClient());
                //this will create a new NSD resource
                ObjectId response = nsPackagesApi.addNSD(new CreateNsdInfoRequest());
                nsdInfoId = response.getId();
                log.debug("Created NSD resource with UUID: " + nsdInfoId.toString());
                NsdInfoIdToOsmNsdId.put(nsdInfoId,IfaOsmTranslator.getOsmNsdId(nsd,df));
            }
            catch (ApiException e){
                log.error("Creation NSD resource failed",e);
                log.error("Error during creation of NSD resource!", e.getResponseBody());
                throw new FailedOperationException(e.getMessage());
            }
            try{
                nsPackagesApi.updateNSDcontent(nsdInfoId.toString(), compressFilePath);
                log.debug("Updated the NSD resource with UUID: " + nsdInfoId + "\n  with the NSD of id: " + nsdOsm.getId());
            }
            catch (ApiException e){
                //the resource pointed by nsdInfoId has not been modified due to exception, need to delete it
                nsPackagesApi.deleteNSD(nsdInfoId.toString());
                log.debug("Deleted NSD Resource with UUID: " + nsdInfoId);
                throw new FailedOperationException("Error on NSD onboarding!" + e.getResponseBody());
            }
            nsdInfoIds.add(nsdInfoId);
        }

        //NsdIfaIdToNsdInfoIds.put(UUID.fromString(nsd.getNsdIdentifier()),nsdInfoIds);
        return nsd.getNsdIdentifier(); //TODO validate this return
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
    private boolean updateVNFDs(Nsd nsd, NsDf df, HashMap<String, Boolean> useTemplateVNFDs) {
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

            boolean deleteResource = false;
            //add autoscaling for each vnfd that are in this nsd
            if (addAutoscalingRules(df, defaultIL, vnfDescriptor, vnfProfileIdToVnfId)) {
                //Vnfd modified. We need to upload it on OSM
                File compressVnfdFile = IfaOsmTranslator.updateVnfPackage(vnfDescriptor, nsdIdWithFlavour);
                if (compressVnfdFile != null) {
                    try {
                        vnfPackagesApi.uploadVnfPkgContent(vnfdInfoId.toString(), compressVnfdFile);
                    } catch (ApiException e1) {
                        log.error("Cannot onboard updated VNFD");
                        deleteResource = true;
                    }
                    log.debug("Onboarded VNFD " + vnfDescriptor.getId() + "_" + nsdIdWithFlavour);
                    useTemplateVNFDs.put(vnfdIfaId,false);
                } else {
                    log.error("Cannot compress file");
                    deleteResource = true;
                }
            } else {
                //autoscaling rule not needed for this vnfd
                useTemplateVNFDs.put(vnfdIfaId,true);
                //delete package resource
                try {
                    vnfPackagesApi.deleteVnfPkg(vnfdInfoId.toString());
                    log.error("Cannot onboard new VNFD");
                } catch (ApiException e) {
                    e.printStackTrace();
                    log.error("Cannot delete package resource");
                }
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
                return false;
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
     * @return boolean that represent if the scaling rules have been added
     */
    private boolean addAutoscalingRules(NsDf df, NsLevel defaultIL, VNFDescriptor vnfDescriptor, HashMap<String, String> vnfProfileIdToVnfId) {
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

        if(!toAdd) return false;

        //There at least a scaling rule to add
        List<ScalingGroupDescriptor> scalingGroupDescriptorList = new ArrayList<>();
        for(NsLevel nsLevel : df.getNsInstantiationLevel()){
            for(VnfToLevelMapping vnfToLevelMapping : nsLevel.getVnfToLevelMapping()){
                if(vnfProfileIdToVnfId.get(vnfToLevelMapping.getVnfProfileId()).equals(vnfDescriptor.getId())){

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
        return true;
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

    //******************************** VNFD methods ********************************//

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
        /*try{
            String downloadedFile = fileUtilities.downloadFile(vnfPackagePath);
            folder = fileUtilities.extractFile(downloadedFile);
            File jsonFile = fileUtilities.findJsonFileInDir(folder);

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
        }*/
        ObjectMapper mapper = new ObjectMapper();
        try {
            vnfd = (Vnfd) mapper.readValue(Paths.get(request.getName()).toFile(), Vnfd.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(vnfd == null) throw new MalformattedElementException("VNFD for onboarding is empty");
        //maybe a vnfd for each couple of vnfd,vnfDf?
        for(VnfDf vnfdDf : vnfd.getDeploymentFlavour()){
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
            vnfdIdToOsmVnfd.put(vnfd.getVnfdId()+"_"+vnfdDf.getFlavourId(),vnfdOsm);
        }
        UUID randomUUID = UUID.randomUUID();
        //maybe a map
        OnBoardVnfPackageResponse onBoardVnfPackageResponse = new OnBoardVnfPackageResponse(randomUUID.toString(),vnfd.getVnfdId());
        return onBoardVnfPackageResponse;
    }

    private String getOsmVnfdId(String vnfdId, String flavourId) {
        return vnfdId +"_"+flavourId;
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


}
