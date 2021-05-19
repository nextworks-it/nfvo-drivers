package it.nextworks.nfvmano.nfvodriver.elicensing;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ElicensingService {

    private static final Logger log = LoggerFactory.getLogger(ElicensingService.class);

    private Map<String, CompletableFuture<ElicensingOperationResponse>> pendingResponses = new HashMap<>();


    public void registerPendingResponse(String id, CompletableFuture<ElicensingOperationResponse> response){
        log.debug("Register pending elicense response:"+ id);
        pendingResponses.put(id, response);
    }


    public void processPendingResponse(String id, ElicensingOperationResponse response){
        log.debug("Received pending elicense response:"+ id);
        if(pendingResponses.containsKey(id)){
            pendingResponses.get(id).complete(response);
        }

    }

}
