package it.nextworks.nfvmano.nfvodriver;

import it.nextworks.nfvmano.nfvodriver.elicensing.ElicensingOperationResponse;
import it.nextworks.nfvmano.nfvodriver.elicensing.ElicensingService;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/portal/elicensing")
public class ElicesingRestController {


    @Autowired
    private ElicensingService elicensingService;

    @ApiOperation(value = "Onboard a set of translation rules.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "the operation was succesfully proccesed.", response = String.class),

    })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> receiveElicensingOperationResponse(@RequestBody ElicensingOperationResponse response) {

        elicensingService.processPendingResponse(response.getProductId(), response);
        return new ResponseEntity<String>(response.getProductId(),HttpStatus.CREATED);
    }



}
