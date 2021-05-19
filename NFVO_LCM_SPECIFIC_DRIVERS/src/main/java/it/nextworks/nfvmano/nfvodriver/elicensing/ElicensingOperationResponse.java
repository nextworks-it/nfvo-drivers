package it.nextworks.nfvmano.nfvodriver.elicensing;

public class ElicensingOperationResponse {

    private String operationId;
    private String productId;
    private int statusCode;

    private String responseType;

    private String details;


    public String getOperationId() {
        return operationId;
    }

    public String getProductId() {
        return productId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseType() {
        return responseType;
    }

    public String getDetails() {
        return details;
    }
}
