package it.nextworks.nfvmano.nfvodriver.elicensing;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.swagger.client.elma.model.RegistrationResponse;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ElicensingService {

    private static final Logger log = LoggerFactory.getLogger(ElicensingService.class);

    private Map<String, CompletableFuture<RegistrationResponse>> pendingResponses = new HashMap<>();


    public void registerPendingResponse(String id, CompletableFuture<RegistrationResponse> response){
        log.debug("Register pending elicense response:"+ id);
        pendingResponses.put(id, response);
    }


    public void processPendingResponse(String id, RegistrationResponse response){
        log.debug("Received pending elicense response:"+ id);
        if(pendingResponses.containsKey(id)){
            pendingResponses.get(id).complete(response);
        }

    }

}
