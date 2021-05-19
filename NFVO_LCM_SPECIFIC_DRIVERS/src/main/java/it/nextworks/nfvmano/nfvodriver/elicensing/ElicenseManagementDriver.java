package it.nextworks.nfvmano.nfvodriver.elicensing;


import io.swagger.client.elma.ApiException;
import io.swagger.client.elma.model.RegistrationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import io.swagger.client.elma.ApiClient;
import io.swagger.client.elma.api.DefaultApi;
import io.swagger.client.elma.model.CheckLicensing;
import io.swagger.client.elma.model.Domains;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ElicenseManagementDriver implements ElicenseManagementProviderInterface {

    private String elicensingManagerAddress =null;
    private Map<String, String> metadata=new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(ElicenseManagementDriver.class);
    private DefaultApi api = new DefaultApi();
    private String domainId;
    private ElicensingService service;

    public ElicenseManagementDriver(String elicensingManagerAddress,String domainId, ElicensingService service){
        this.elicensingManagerAddress=elicensingManagerAddress;
        api.setApiClient(new ApiClient().setBasePath(elicensingManagerAddress).setDebugging(true));
        this.domainId=domainId;
        this.service= service;

    }

    @Override
    public void activateElicenseManagement(Map<String, String> metadata) throws FailedOperationException {
        log.debug("Requesting elicense management creation."+metadata);
        CheckLicensing body = new CheckLicensing();
        Domains d = new Domains();
        d.domainDID(domainId);
        d.addNsi(metadata.get("NSD_ID"), "nst",  metadata.get("NS_ID"), "tenant");
        body.addDomainsItem(d);
        body.setProductID(metadata.get("product_id"));
        try {
            RegistrationResponse response = api.elicensemanagercoreElmcFrontOperationsCheckLicensing(body);
            CompletableFuture<ElicensingOperationResponse> elicensingResponse = new CompletableFuture();

            service.registerPendingResponse(metadata.get("product_id"), elicensingResponse);
            log.debug("WAITING FOR ELICENSE VERIFICATiON");
            ElicensingOperationResponse operationResponse = elicensingResponse.get();
            if(operationResponse.getStatusCode()==HttpStatus.OK){
                log.debug("Correctly verified elicense.");
            }else{
                throw new FailedOperationException("Elicense verification failed:"+operationResponse.getDetails());
            }
        } catch (ApiException e) {
            log.error("Error activating license",e);
            throw new FailedOperationException(e);
        } catch (InterruptedException e) {
            log.error("Error activating license",e);
            throw new FailedOperationException(e);
        } catch (ExecutionException e) {
            log.error("Error activating license",e);
            throw new FailedOperationException(e);
        }
    }

    @Override
    public void terminateElicenseManagement() throws FailedOperationException {

    }
}
