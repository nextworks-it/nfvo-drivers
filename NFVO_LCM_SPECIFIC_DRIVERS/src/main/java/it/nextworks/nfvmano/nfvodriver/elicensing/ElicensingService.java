package it.nextworks.nfvmano.nfvodriver.elicensing;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Service;

@Service
public class ElicensingService {


    private Map<String, CompletableFuture<ElicensingOperationResponse>> pendingResponses = new HashMap<>();


    public void registerPendingResponse(String id, CompletableFuture<ElicensingOperationResponse> response){
        pendingResponses.put(id, response);
    }


    public void processPendingResponse(String id, ElicensingOperationResponse response){

        if(pendingResponses.containsKey(id)){
            pendingResponses.get(id).complete(response);
        }

    }

}
