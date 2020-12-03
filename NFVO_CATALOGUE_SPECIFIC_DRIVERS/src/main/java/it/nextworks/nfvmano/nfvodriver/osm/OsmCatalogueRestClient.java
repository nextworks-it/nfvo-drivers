package it.nextworks.nfvmano.nfvodriver.osm;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.client.model.CreateNsdInfoRequest;
import io.swagger.client.model.CreateVnfPkgInfoRequest;
import io.swagger.client.model.NsdInfo;
import io.swagger.client.model.ObjectId;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.OnBoardVnfPackageRequest;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.OnBoardVnfPackageResponse;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.OnboardNsdRequest;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.AlreadyExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsDf;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.Nsd;
import it.nextworks.nfvmano.libs.ifa.descriptors.vnfd.VnfDf;
import it.nextworks.nfvmano.libs.ifa.descriptors.vnfd.Vnfd;
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
    private final FileUtilities fileUtilities;

    //Map the NSD ID IFA to the list of OSM NSD UUIDs generated after translation
    private final Map<String,List<UUID>> NsdIfaIdToNsdInfoId;

    private final Map<UUID,String> NsdInfoIdToOsmNsdId;

    //maybe we need a map to id of a particular nsd and its instantiation level logic

    public OsmCatalogueRestClient(String nfvoAddress, String username, String password, OAuthSimpleClient oAuthSimpleClient){
        this.oAuthSimpleClient = oAuthSimpleClient;
        this.nfvoAddress = nfvoAddress;
        this.username = username;
        this.password = password;
        this.nsPackagesApi = new NsPackagesApi();
        this.vnfPackagesApi = new VnfPackagesApi();
        this.fileUtilities = new FileUtilities(System.getProperty("java.io.tmpdir"));
        this.NsdIfaIdToNsdInfoId = new HashMap<>();
        this.NsdInfoIdToOsmNsdId = new HashMap<>();
    }

    //******************************** NSD methods ********************************//

    public String onboardNsd(OnboardNsdRequest request) throws MethodNotImplementedException,
            MalformattedElementException, AlreadyExistingEntityException, FailedOperationException, ApiException {

        Nsd nsd = request.getNsd();
        if(nsd == null) throw new MalformattedElementException("NSD for onboarding is empty");

        //contains all the OSM NSD UUIDs generated from the IFA NSD
        List<UUID> nsdInfoIds = new ArrayList<>();
        for (NsDf df : nsd.getNsDf()) { // Generate a OSM NSD for each DF

            //call the translation process that return nsd tar file
            File compressFilePath = IfaOsmTranslator.createPackageForNsd(nsd,df);
            UUID nsdInfoId;
            //this post request create a new ns descriptor resource
            try {
                nsPackagesApi.setApiClient(getClient());
                //this will create a new NSD resource
                ObjectId response = nsPackagesApi.addNSD(new CreateNsdInfoRequest());
                nsdInfoId = response.getId();
                NsdInfoIdToOsmNsdId.put(nsdInfoId,IfaOsmTranslator.getOsmNsdId(nsd,df));
            }
            catch (ApiException e){
                log.error("Creation NSD resource failed",e);
                log.error("Error during creation of NSD resource!", e.getResponseBody());
                throw new FailedOperationException(e.getMessage());
            }
            try{
                nsPackagesApi.updateNSDcontent(nsdInfoId.toString(), compressFilePath);
            }
            catch (ApiException e){
                //the resource pointed by nsdInfoId has not been modified due to exception, need to delete it
                nsPackagesApi.deleteNSD(nsdInfoId.toString());
                throw new FailedOperationException("Error on NSD onboarding!" + e.getResponseBody());
            }
            nsdInfoIds.add(nsdInfoId);
        }
        NsdIfaIdToNsdInfoId.put(nsd.getNsdIdentifier(),nsdInfoIds);
        return nsd.getNsdIdentifier(); //TODO validate this return
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
            vnfd = (Vnfd) mapper.readValue(Paths.get("/home/nextworks/Desktop/vnfd.json").toFile(), Vnfd.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(vnfd == null) throw new MalformattedElementException("VNFD for onboarding is empty");
        //maybe a vnfd for each couple of vnfd,vnfDf?

        for(VnfDf vnfdDf : vnfd.getDeploymentFlavour()){

            File compressFilePath = IfaOsmTranslator.createPackageForVnfd(vnfd,vnfdDf);
            String vnfdInfoId = "";

            //create a new Vnfd resource
            try {
                vnfPackagesApi.setApiClient(getClient());
                ObjectId response = vnfPackagesApi.addVnfPkg(new CreateVnfPkgInfoRequest());
                vnfdInfoId = response.getId().toString();
            } catch (FailedOperationException e) {
                log.error("Failed to set Api client",e);
            } catch (ApiException e) {
                log.error("Creation VNFD resource failed",e);
            }
            //
            try {
                vnfPackagesApi.uploadVnfPkgContent(vnfdInfoId,compressFilePath);
            } catch (ApiException e) {
                //the resource pointed by nsdInfoId has not been modified due to exception, need to delete it
                try {
                    vnfPackagesApi.deleteVnfPkg(vnfdInfoId);
                } catch (ApiException apiException) {
                    log.error("Can't delete vnfdInfoId resource", apiException);
                }
                throw new FailedOperationException("Error on VNFD onboarding!" + e.getResponseBody());
            }
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
