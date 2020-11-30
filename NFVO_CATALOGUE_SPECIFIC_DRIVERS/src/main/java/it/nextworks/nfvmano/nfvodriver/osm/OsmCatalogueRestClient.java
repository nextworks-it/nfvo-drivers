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
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // second argument is a list if for each couple of <nsd,df> we need to generate
    // an osm nsd descriptor TODO verify this
    private final Map<String,List<String>> NsdIfaIdToNsdInfoId;

    private final Map<String,String> NsdInfoIdToOsmNsdId;

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

        List<String> nsdInfoIds = new ArrayList<>();
        for (NsDf df : nsd.getNsDf()) { // Generate a OSM NSD for each DF

            //call the translation process that return nsd tar file
            File compressFilePath = IfaOsmTranslator.createPackageForNsd(nsd,df);

            /*
             * TODO after vnfd translation
             * Since we know the scaling policy for a vnf only at NSD-onboarding-time
             * we need a data structure that tell us which are the modification to do
             * on each vnfd in this nsd
             */

            //File compressFilePath = Paths.get("/home/nextworks/Desktop/osm_export.tar.gz").toFile(); //for testing
            String nsdInfoId = "";
            //this post request create a new ns descriptor resource
            try {
                nsPackagesApi.setApiClient(getClient());
                //this will create a new NSD resource
                ObjectId response = nsPackagesApi.addNSD(new CreateNsdInfoRequest());
                nsdInfoId = response.getId().toString();
                NsdInfoIdToOsmNsdId.put(nsdInfoId,IfaOsmTranslator.getOsmNsdId(nsd,df));
            }
            catch (ApiException e){
                log.error("Creation NSD resource failed",e);
                log.error("Error during creation of NSD resource!", e.getResponseBody());
                throw new FailedOperationException(e.getMessage());
            }
            try{
                nsPackagesApi.updateNSDcontent(nsdInfoId, compressFilePath);
            }
            catch (ApiException e){
                //the resource pointed by nsdInfoId has not been modified due to exception, need to delete it
                nsPackagesApi.deleteNSD(nsdInfoId);
                throw new FailedOperationException("Error on NSD onboarding!" + e.getResponseBody());
            }
            nsdInfoIds.add(nsdInfoId);
        }
        NsdIfaIdToNsdInfoId.put(nsd.getNsdIdentifier(),nsdInfoIds);

        //TODO what to be returned?
        return null;
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
        try{
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

        /*
        FROM DummyNFVO
        OnboardedVnfPkgInfo pkgInfo = createVnfPkgInfoFromVnfd(vnfd);
        vnfPkgInfos.add(pkgInfo);
        OnBoardVnfPackageResponse response = new OnBoardVnfPackageResponse(pkgInfo.getOnboardedVnfPkgInfoId(), vnfd.getVnfdId());
        return response;
        */
        return null;
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
