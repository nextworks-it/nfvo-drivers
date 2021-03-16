# DefaultApi

All URIs are relative to */*

Method | HTTP request | Description
------------- | ------------- | -------------
[**elicensemanagercoreElmcFrontOperationsCheckLicensing**](DefaultApi.md#elicensemanagercoreElmcFrontOperationsCheckLicensing) | **POST** /checkLicensing | Entrypoint to inform of a new ProductOffering (PO) that has been added to a monitored domain
[**elicensemanagercoreElmcFrontOperationsHealth**](DefaultApi.md#elicensemanagercoreElmcFrontOperationsHealth) | **GET** /health | Entrypoint for heath tests

<a name="elicensemanagercoreElmcFrontOperationsCheckLicensing"></a>
# **elicensemanagercoreElmcFrontOperationsCheckLicensing**
> RegistrationResponse elicensemanagercoreElmcFrontOperationsCheckLicensing(body)

Entrypoint to inform of a new ProductOffering (PO) that has been added to a monitored domain

### Example
```java
// Import classes:
//import ApiException;
//import DefaultApi;


DefaultApi apiInstance = new DefaultApi();
CheckLicensing body = new CheckLicensing(); // CheckLicensing | ProductOffering id and domains where it applys
try {
    RegistrationResponse result = apiInstance.elicensemanagercoreElmcFrontOperationsCheckLicensing(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#elicensemanagercoreElmcFrontOperationsCheckLicensing");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CheckLicensing**](CheckLicensing.md)| ProductOffering id and domains where it applys |

### Return type

[**RegistrationResponse**](RegistrationResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="elicensemanagercoreElmcFrontOperationsHealth"></a>
# **elicensemanagercoreElmcFrontOperationsHealth**
> elicensemanagercoreElmcFrontOperationsHealth()

Entrypoint for heath tests

### Example
```java
// Import classes:
//import ApiException;
//import DefaultApi;


DefaultApi apiInstance = new DefaultApi();
try {
    apiInstance.elicensemanagercoreElmcFrontOperationsHealth();
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#elicensemanagercoreElmcFrontOperationsHealth");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

