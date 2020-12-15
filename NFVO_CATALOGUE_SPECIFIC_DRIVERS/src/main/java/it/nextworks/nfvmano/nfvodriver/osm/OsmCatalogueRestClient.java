package it.nextworks.nfvmano.nfvodriver.osm;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.nsDescriptor.ConstituentVNFD;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.nsDescriptor.NSDescriptor;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.ScalingGroupDescriptor;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.ScalingPolicy;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.VNFDescriptor;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.vnfDescriptor.VduReference;
import it.nextworks.nfvmano.nfvodriver.dummy.FileUtilities;
import it.nextworks.osm.ApiClient;
import it.nextworks.osm.ApiException;
import it.nextworks.osm.openapi.NsPackagesApi;
import it.nextworks.osm.openapi.VnfPackagesApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
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

    public String onboardNsd(OnboardNsdRequest request) throws MethodNotImplementedException,
            MalformattedElementException, AlreadyExistingEntityException, FailedOperationException, ApiException {

        Nsd nsd = request.getNsd();
        if(nsd == null) throw new MalformattedElementException("NSD for onboarding is empty");

        //contains all the OSM NSD UUIDs generated from the IFA NSD
        List<UUID> nsdInfoIds = new ArrayList<>();
        for (NsDf df : nsd.getNsDf()) { // Generate a OSM NSD for each DF
            Map<String,String> vnfProfileId;
            //call the translation process that return nsd tar file
            File compressFilePath = IfaOsmTranslator.createPackageForNsd(nsd,df);
            NSDescriptor nsdOsm = IfaOsmTranslator.getGeneratedOsmNsd(); //TODO validate if this could change
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

            //Now we need to upload the content of each constituent vnfd within this OSM NSD
            //providing the scaling rule
            try {
                vnfPackagesApi.setApiClient(getClient());
                ObjectId response = vnfPackagesApi.addVnfPkg(new CreateVnfPkgInfoRequest());
                UUID vnfdInfoId = response.getId();
                log.debug("Created a new VNFD Resource with UUID: " + vnfdInfoId);
                if(df.getNsInstantiationLevel().size()> 1){
                    HashMap<String,String> vnfProfileIdToVnfId = generateVnfProfileIdMapping(nsd,df);
                    for (ConstituentVNFD constituentVNFD : nsdOsm.getConstituentVNFDs()) {
                        VNFDescriptor vnfDescriptor = vnfdIdToOsmVnfd.get(constituentVNFD.getVnfdIdentifierReference());
                        //add autoscaling for each vnfd that are in this nsd
                        if(addAutoscalingRules(nsd,df,vnfDescriptor,vnfProfileIdToVnfId)){
                            //Vnfd modified. We need to upload it on OSM
                            File compressVnfdFile = IfaOsmTranslator.updateVnfPackage(vnfDescriptor,nsdOsm.getId());
                            //vnfpkg id deve essere lo UUID
                            if(compressVnfdFile != null){
                                try{
                                    vnfPackagesApi.uploadVnfPkgContent(vnfdInfoId.toString(),compressVnfdFile);
                                }
                                catch (ApiException e1){
                                    log.debug("Cannot onboard new VNFD", e1);
                                    vnfPackagesApi.deleteVnfPkg(vnfdInfoId.toString());
                                }
                                log.debug("Updated the VNFD resource " + vnfdInfoId + " with the VNFD " + vnfDescriptor.getId()+"_"+nsdOsm.getId());
                                //vnfPackagesApi.uploadVnfPkgContent(vnfdIdToVnfdUUID.get(vnfDescriptor.getId()).toString(),compressVnfdFile);
                                //log.debug("Update the package content of VNFD with id: " + vnfDescriptor.getId());
                            }
                        }
                    }
                }
            }
            catch (ApiException e){
                System.out.println(e.getResponseBody());
                //nsPackagesApi.deleteNSD(nsdInfoId.toString());
            }
            nsdInfoIds.add(nsdInfoId);
        }

        // Now change the vnfd refernce in the NSD
        //TODO
        //NsdIfaIdToNsdInfoIds.put(UUID.fromString(nsd.getNsdIdentifier()),nsdInfoIds);
        return nsd.getNsdIdentifier(); //TODO validate this return
    }

    private HashMap<String, String> generateVnfProfileIdMapping(Nsd nsd, NsDf df) {
        HashMap<String,String> map = new HashMap<>();

        for(VnfProfile vnfProfile : df.getVnfProfile()){
            map.put(vnfProfile.getVnfProfileId(),vnfProfile.getVnfdId()+"_"+vnfProfile.getFlavourId());
        }
        return map;
    }

    private boolean addAutoscalingRules(Nsd nsd, NsDf df, VNFDescriptor vnfDescriptor,HashMap<String,String> vnfProfileIdToVnfId) {
        //adding a scaling rules to this vnfDescriptor

        NsLevel defaultInstantiationLevel = null;
        HashMap<String,Integer> defaultNumberOfInstances = new HashMap<>();
        try {
            //assumption the default instantiation level has always the min number of instances
            defaultInstantiationLevel = df.getDefaultInstantiationLevel();
            for(VnfToLevelMapping vnfToLevelMapping : defaultInstantiationLevel.getVnfToLevelMapping()){
                defaultNumberOfInstances.put(vnfToLevelMapping.getVnfProfileId(),vnfToLevelMapping.getNumberOfInstances());
            }
        } catch (NotExistingEntityException e) {
            e.printStackTrace();
            return false;
        }

        //check if there are scaling group to add
        boolean toAdd = false;
        int i=0;
        for(NsLevel nsLevel : df.getNsInstantiationLevel()){
            if(!nsLevel.getNsLevelId().equals(defaultInstantiationLevel.getNsLevelId())){
                for(VnfToLevelMapping vnfToLevelMapping : nsLevel.getVnfToLevelMapping()){
                    if(vnfProfileIdToVnfId.get(vnfToLevelMapping.getVnfProfileId()).equals(vnfDescriptor.getId())){
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
                    if(nsLevel.getNsLevelId().equals(defaultInstantiationLevel.getNsLevelId()))
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

    public OnBoardVnfPackageResponse onboardVnfPackage(OnBoardVnfPackageRequest request) throws MalformattedElementException, FailedOperationException {
        log.debug("Getting VNF package");
        String vnfPackagePath = request.getVnfPackagePath();
        log.debug("Retrieving VNFD from VNF package");
        String folder = null;
        Vnfd vnfd = null;
        /*try{ //TODO how to test this?
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
            vnfd = (Vnfd) mapper.readValue(Paths.get("/home/nextworks/Desktop/vnfd.json").toFile(), Vnfd.class);
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
        return null; //TODO what to be retured?
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
