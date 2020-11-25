package it.nextworks.nfvmano.nfvodriver.osm;

import io.swagger.client.model.*;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.OnBoardVnfPackageRequest;
import it.nextworks.nfvmano.libs.ifa.catalogues.interfaces.messages.OnboardNsdRequest;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.AlreadyExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsDf;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.Nsd;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.nsDescriptor.NSDCatalog;
import it.nextworks.nfvmano.libs.osmr4PlusDataModel.nsDescriptor.OsmNSPackage;
import it.nextworks.osm.ApiClient;
import it.nextworks.osm.ApiException;
import it.nextworks.osm.openapi.NsPackagesApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * REST client to interact with OSM NFVO.
 *
 * @author nextworks
 *
 */

public class OsmCatalogueRestClient {

    private static final Logger log = LoggerFactory.getLogger(OsmCatalogueRestClient.class);
    private OAuthSimpleClient oAuthSimpleClient;
    private final String nfvoAddress;
    private String username;
    private String password;
    private NsPackagesApi nsPackagesApi;
    private Map<UUID, UUID> IdToNsdIdMapping;
    private Map<UUID, UUID> IdToNsdInfoIdMapping;
    private NSDCatalog nsdCatalog;
    private OsmNSPackage osmNSPackage;

    public OsmCatalogueRestClient(String nfvoAddress, String username, String password, OAuthSimpleClient oAuthSimpleClient){
        this.oAuthSimpleClient = oAuthSimpleClient;
        this.nfvoAddress = nfvoAddress;
        this.username = username;
        this.password = password;
        nsPackagesApi = new NsPackagesApi();
        this.IdToNsdIdMapping = new HashMap<>();
        this.IdToNsdInfoIdMapping = new HashMap<>();
        this.nsdCatalog = new NSDCatalog();
        this.osmNSPackage = new OsmNSPackage();
        osmNSPackage.setNsdCatalog(nsdCatalog);
    }

    //******************************** NSD methods ********************************//

    public String onboardNsd(OnboardNsdRequest request) throws MethodNotImplementedException,
            MalformattedElementException, AlreadyExistingEntityException, FailedOperationException, ApiException {

        KeyValuePairs keyValuePair = new KeyValuePairs();
        keyValuePair.putAll(request.getUserDefinedData());

        //UUID nsdIfaId = UUID.nameUUIDFromBytes(request.getNsd());

        Nsd nsd = request.getNsd();
        log.debug("nsd ifa received from request: "+ nsd);
        if(nsd == null) throw new MalformattedElementException("NSD for onboarding is empty");

        for (NsDf df : nsd.getNsDf()) { // Generate a OSM NSD for each DF

            //call the translation process
            String compressFilePath = IfaOsmTranslator.createPackage(nsd,df);
            System.out.println(compressFilePath);
            System.out.println("###########################");

            /*
            //this post request create a new ns descriptor resource
            try {
                nsPackagesApi.setApiClient(getClient());
                ObjectId response = nsPackagesApi.addNSD(new CreateNsdInfoRequest());
                String nsdInfoId = response.getId().toString();
                NsdInfoModifications nsdInfoModifications = new NsdInfoModifications();
                //then we have to make a PATCH request to modify the data of an individual NS descriptor resource
                nsPackagesApi.updateNSD(nsdInfoId, nsdInfoModifications);
                // or maybe PUT the zip content by call
                //nsPackageApi.updateNSDContent(nsdInfoId, Object body)
            }
            catch (ApiException e){
                log.error(e.getMessage());
                log.error(e.getStackTrace().toString());
                throw new FailedOperationException(e.getMessage());
            }*/

        }

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

    public void onboardVnfd(OnBoardVnfPackageRequest request) {

    }


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
