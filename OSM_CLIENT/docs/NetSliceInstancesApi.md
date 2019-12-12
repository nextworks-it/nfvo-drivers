# NetSliceInstancesApi

All URIs are relative to *https://osm.etsi.org/nbapi/v1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**actionOnNSI**](NetSliceInstancesApi.md#actionOnNSI) | **POST** /nsilcm/v1/netslice_instances/{netsliceInstanceId}/action | Execute an action on a NetSlice instance
[**addNSI**](NetSliceInstancesApi.md#addNSI) | **POST** /nsilcm/v1/netslice_instances | Create a new NetSlice instance resource
[**createNSIContent**](NetSliceInstancesApi.md#createNSIContent) | **POST** /nsilcm/v1/netslice_instances_content | Create a new NetSlice instance
[**deleteNSI**](NetSliceInstancesApi.md#deleteNSI) | **DELETE** /nsilcm/v1/netslice_instances/{netsliceInstanceId} | Delete an individual NetSlice instance resource
[**deleteNSIContent**](NetSliceInstancesApi.md#deleteNSIContent) | **DELETE** /nsilcm/v1/netslice_instances_content/{netsliceInstanceContentId} | Delete an individual NS instance resource
[**getNSI**](NetSliceInstancesApi.md#getNSI) | **GET** /nsilcm/v1/netslice_instances/{netsliceInstanceId} | Read an individual NetSlice instance resource
[**getNSIContent**](NetSliceInstancesApi.md#getNSIContent) | **GET** /nsilcm/v1/netslice_instances_content/{netsliceInstanceContentId} | Read an individual NetSlice instance resource
[**getNSIs**](NetSliceInstancesApi.md#getNSIs) | **GET** /nsilcm/v1/netslice_instances | Query information about multiple NetSlice instances
[**getNSIsContent**](NetSliceInstancesApi.md#getNSIsContent) | **GET** /nsilcm/v1/netslice_instances_content | Query information about multiple NetSlice instances
[**getNsiLcmOpOcc**](NetSliceInstancesApi.md#getNsiLcmOpOcc) | **GET** /nsilcm/v1/nsi_lcm_op_occs/{nsiLcmOpOccId} | Query information about an individual NetSlice LCM Operation Occurrence
[**getNsiLcmOpOccs**](NetSliceInstancesApi.md#getNsiLcmOpOccs) | **GET** /nsilcm/v1/nsi_lcm_op_occs | Query information about multiple NetSlice LCM Operation Occurrences
[**instantiateNSI**](NetSliceInstancesApi.md#instantiateNSI) | **POST** /nsilcm/v1/netslice_instances/{netsliceInstanceId}/instantiate | Instantiate a NetSlice
[**terminateNSI**](NetSliceInstancesApi.md#terminateNSI) | **POST** /nsilcm/v1/netslice_instances/{netsliceInstanceId}/terminate | Terminate a NetSlice instance

<a name="actionOnNSI"></a>
# **actionOnNSI**
> ObjectId actionOnNSI(netsliceInstanceId, body)

Execute an action on a NetSlice instance

Execute an action on a NetSlice instance. The NetSlice instance must have been created and must be in INSTANTIATED state. 

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceInstancesApi apiInstance = new NetSliceInstancesApi();
String netsliceInstanceId = "netsliceInstanceId_example"; // String | NetSlice Instance ID
NsiActionRequest body = new NsiActionRequest(); // NsiActionRequest | 
try {
    ObjectId result = apiInstance.actionOnNSI(netsliceInstanceId, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceInstancesApi#actionOnNSI");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **netsliceInstanceId** | **String**| NetSlice Instance ID |
 **body** | [**NsiActionRequest**](NsiActionRequest.md)|  | [optional]

### Return type

[**ObjectId**](ObjectId.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/yaml
 - **Accept**: application/json, application/yaml

<a name="addNSI"></a>
# **addNSI**
> ObjectId addNSI(body)

Create a new NetSlice instance resource

Create a new NetSlice instance resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceInstancesApi apiInstance = new NetSliceInstancesApi();
CreateNsiRequest body = new CreateNsiRequest(); // CreateNsiRequest | 
try {
    ObjectId result = apiInstance.addNSI(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceInstancesApi#addNSI");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CreateNsiRequest**](CreateNsiRequest.md)|  | [optional]

### Return type

[**ObjectId**](ObjectId.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/yaml
 - **Accept**: application/json, application/yaml

<a name="createNSIContent"></a>
# **createNSIContent**
> CreateNsiContentResponse createNSIContent(body)

Create a new NetSlice instance

Create a new NetSlice instance

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceInstancesApi apiInstance = new NetSliceInstancesApi();
CreateNsiContentRequest body = new CreateNsiContentRequest(); // CreateNsiContentRequest | 
try {
    CreateNsiContentResponse result = apiInstance.createNSIContent(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceInstancesApi#createNSIContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CreateNsiContentRequest**](CreateNsiContentRequest.md)|  | [optional]

### Return type

[**CreateNsiContentResponse**](CreateNsiContentResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/yaml
 - **Accept**: application/json, application/yaml

<a name="deleteNSI"></a>
# **deleteNSI**
> deleteNSI(netsliceInstanceId)

Delete an individual NetSlice instance resource

Delete an individual NetSlice instance resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceInstancesApi apiInstance = new NetSliceInstancesApi();
String netsliceInstanceId = "netsliceInstanceId_example"; // String | NetSlice Instance ID
try {
    apiInstance.deleteNSI(netsliceInstanceId);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceInstancesApi#deleteNSI");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **netsliceInstanceId** | **String**| NetSlice Instance ID |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="deleteNSIContent"></a>
# **deleteNSIContent**
> ObjectId deleteNSIContent(netsliceInstanceContentId)

Delete an individual NS instance resource

Delete an individual NS instance resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceInstancesApi apiInstance = new NetSliceInstancesApi();
String netsliceInstanceContentId = "netsliceInstanceContentId_example"; // String | NetSlice Instance Content ID
try {
    ObjectId result = apiInstance.deleteNSIContent(netsliceInstanceContentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceInstancesApi#deleteNSIContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **netsliceInstanceContentId** | **String**| NetSlice Instance Content ID |

### Return type

[**ObjectId**](ObjectId.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getNSI"></a>
# **getNSI**
> NetSliceInstance getNSI(netsliceInstanceId)

Read an individual NetSlice instance resource

Read an individual NetSlice instance resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceInstancesApi apiInstance = new NetSliceInstancesApi();
String netsliceInstanceId = "netsliceInstanceId_example"; // String | NetSlice Instance ID
try {
    NetSliceInstance result = apiInstance.getNSI(netsliceInstanceId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceInstancesApi#getNSI");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **netsliceInstanceId** | **String**| NetSlice Instance ID |

### Return type

[**NetSliceInstance**](NetSliceInstance.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getNSIContent"></a>
# **getNSIContent**
> NetSliceInstance getNSIContent(netsliceInstanceContentId)

Read an individual NetSlice instance resource

Read an individual NetSlice instance resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceInstancesApi apiInstance = new NetSliceInstancesApi();
String netsliceInstanceContentId = "netsliceInstanceContentId_example"; // String | NetSlice Instance Content ID
try {
    NetSliceInstance result = apiInstance.getNSIContent(netsliceInstanceContentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceInstancesApi#getNSIContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **netsliceInstanceContentId** | **String**| NetSlice Instance Content ID |

### Return type

[**NetSliceInstance**](NetSliceInstance.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getNSIs"></a>
# **getNSIs**
> ArrayOfNetSliceInstance getNSIs()

Query information about multiple NetSlice instances

Query information about multiple NetSlice isntances

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceInstancesApi apiInstance = new NetSliceInstancesApi();
try {
    ArrayOfNetSliceInstance result = apiInstance.getNSIs();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceInstancesApi#getNSIs");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ArrayOfNetSliceInstance**](ArrayOfNetSliceInstance.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getNSIsContent"></a>
# **getNSIsContent**
> ArrayOfNetSliceInstance getNSIsContent()

Query information about multiple NetSlice instances

Query information about multiple NetSlice isntances

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceInstancesApi apiInstance = new NetSliceInstancesApi();
try {
    ArrayOfNetSliceInstance result = apiInstance.getNSIsContent();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceInstancesApi#getNSIsContent");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ArrayOfNetSliceInstance**](ArrayOfNetSliceInstance.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getNsiLcmOpOcc"></a>
# **getNsiLcmOpOcc**
> NsiLcmOpOcc getNsiLcmOpOcc(nsiLcmOpOccId)

Query information about an individual NetSlice LCM Operation Occurrence

Query information about an individual NetSlice LCM Operation Occurrence

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceInstancesApi apiInstance = new NetSliceInstancesApi();
String nsiLcmOpOccId = "nsiLcmOpOccId_example"; // String | NetSlice LCM Operation Occurrence ID
try {
    NsiLcmOpOcc result = apiInstance.getNsiLcmOpOcc(nsiLcmOpOccId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceInstancesApi#getNsiLcmOpOcc");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsiLcmOpOccId** | **String**| NetSlice LCM Operation Occurrence ID |

### Return type

[**NsiLcmOpOcc**](NsiLcmOpOcc.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getNsiLcmOpOccs"></a>
# **getNsiLcmOpOccs**
> ArrayOfNsiLcmOpOcc getNsiLcmOpOccs()

Query information about multiple NetSlice LCM Operation Occurrences

Query information about multiple NetSlice LCM Operation Occurrences

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceInstancesApi apiInstance = new NetSliceInstancesApi();
try {
    ArrayOfNsiLcmOpOcc result = apiInstance.getNsiLcmOpOccs();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceInstancesApi#getNsiLcmOpOccs");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ArrayOfNsiLcmOpOcc**](ArrayOfNsiLcmOpOcc.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="instantiateNSI"></a>
# **instantiateNSI**
> ObjectId instantiateNSI(netsliceInstanceId, body)

Instantiate a NetSlice

Instantiate a NetSlice. The precondition is that the NetSlice instance must have been created and must be in NOT_INSTANTIATED state. As a result of the success of this operation, the NFVO creates a \&quot;NetSlice Lifecycle Operation Occurrence\&quot; resource for the request, and the NS instance state becomes INSTANTIATED. 

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceInstancesApi apiInstance = new NetSliceInstancesApi();
String netsliceInstanceId = "netsliceInstanceId_example"; // String | NetSlice Instance ID
InstantiateNsiRequest body = new InstantiateNsiRequest(); // InstantiateNsiRequest | 
try {
    ObjectId result = apiInstance.instantiateNSI(netsliceInstanceId, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceInstancesApi#instantiateNSI");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **netsliceInstanceId** | **String**| NetSlice Instance ID |
 **body** | [**InstantiateNsiRequest**](InstantiateNsiRequest.md)|  | [optional]

### Return type

[**ObjectId**](ObjectId.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/yaml
 - **Accept**: application/json, application/yaml

<a name="terminateNSI"></a>
# **terminateNSI**
> ObjectId terminateNSI(netsliceInstanceId, body)

Terminate a NetSlice instance

Terminate a NetSlice instance. The precondition is that the NetSlice instance must have been created and must be in INSTANTIATED state. As a result of the success of this operation, the NFVO creates a \&quot;NetSlice Lifecycle Operation Occurrence\&quot; resource for the request, and the NetSlice instance state becomes NOT_INSTANTIATED. 

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceInstancesApi apiInstance = new NetSliceInstancesApi();
String netsliceInstanceId = "netsliceInstanceId_example"; // String | NetSlice Instance ID
TerminateNsiRequest body = new TerminateNsiRequest(); // TerminateNsiRequest | 
try {
    ObjectId result = apiInstance.terminateNSI(netsliceInstanceId, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceInstancesApi#terminateNSI");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **netsliceInstanceId** | **String**| NetSlice Instance ID |
 **body** | [**TerminateNsiRequest**](TerminateNsiRequest.md)|  | [optional]

### Return type

[**ObjectId**](ObjectId.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/yaml
 - **Accept**: application/json, application/yaml

