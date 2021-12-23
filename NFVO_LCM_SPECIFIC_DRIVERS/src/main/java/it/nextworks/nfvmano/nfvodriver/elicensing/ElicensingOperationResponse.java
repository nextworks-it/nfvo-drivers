package it.nextworks.nfvmano.nfvodriver.elicensing;

import org.springframework.http.HttpStatus;
public class ElicensingOperationResponse {

    private String operationId;
    private String productId;
    private HttpStatus statusCode;

    private String responseType;

    private String details;


    public String getOperationId() {
        return operationId;
    }

    public String getProductId() {
        return productId;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getResponseType() {
        return responseType;
    }

    public String getDetails() {
        return details;
    }
}
