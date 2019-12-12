# NsInstancesApi

All URIs are relative to *https://osm.etsi.org/nbapi/v1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**actionOnNSinstance**](NsInstancesApi.md#actionOnNSinstance) | **POST** /nslcm/v1/ns_instances/{nsInstanceId}/action | Execute an action on a NS instance
[**addNSinstance**](NsInstancesApi.md#addNSinstance) | **POST** /nslcm/v1/ns_instances | Create a new NS instance resource
[**createNSinstanceContent**](NsInstancesApi.md#createNSinstanceContent) | **POST** /nslcm/v1/ns_instances_content | Create a new NS instance
[**deleteNSinstance**](NsInstancesApi.md#deleteNSinstance) | **DELETE** /nslcm/v1/ns_instances/{nsInstanceId} | Delete an individual NS instance resource
[**deleteNSinstanceContent**](NsInstancesApi.md#deleteNSinstanceContent) | **DELETE** /nslcm/v1/ns_instances_content/{nsInstanceContentId} | Delete an individual NS instance resource
[**getNSLCMOpOcc**](NsInstancesApi.md#getNSLCMOpOcc) | **GET** /nslcm/v1/ns_lcm_op_occs/{nsLcmOpOccId} | Query information about an individual NS LCM Operation Occurrence
[**getNSLCMOpOccs**](NsInstancesApi.md#getNSLCMOpOccs) | **GET** /nslcm/v1/ns_lcm_op_occs | Query information about multiple NS LCM Operation Occurrences
[**getNSinstance**](NsInstancesApi.md#getNSinstance) | **GET** /nslcm/v1/ns_instances/{nsInstanceId} | Read an individual NS instance resource
[**getNSinstanceContent**](NsInstancesApi.md#getNSinstanceContent) | **GET** /nslcm/v1/ns_instances_content/{nsInstanceContentId} | Read an individual NS instance resource
[**getNSinstances**](NsInstancesApi.md#getNSinstances) | **GET** /nslcm/v1/ns_instances | Query information about multiple NS instances
[**getNSinstancesContent**](NsInstancesApi.md#getNSinstancesContent) | **GET** /nslcm/v1/ns_instances_content | Query information about multiple NS instances
[**getVnfInstance**](NsInstancesApi.md#getVnfInstance) | **GET** /nslcm/v1/vnf_instances/{vnfInstanceId} | Query information about an individual VNF Instance
[**getVnfInstances**](NsInstancesApi.md#getVnfInstances) | **GET** /nslcm/v1/vnf_instances | Query information about multiple VNF Instances
[**instantiateNSinstance**](NsInstancesApi.md#instantiateNSinstance) | **POST** /nslcm/v1/ns_instances/{nsInstanceId}/instantiate | Instantiate a NS
[**scaleNSinstance**](NsInstancesApi.md#scaleNSinstance) | **POST** /nslcm/v1/ns_instances/{nsInstanceId}/scale | Scale a NS instance
[**terminateNSinstance**](NsInstancesApi.md#terminateNSinstance) | **POST** /nslcm/v1/ns_instances/{nsInstanceId}/terminate | Terminate a NS instance

<a name="actionOnNSinstance"></a>
# **actionOnNSinstance**
> ObjectId actionOnNSinstance(nsInstanceId, body)

Execute an action on a NS instance

Execute an action on a NS instance. The NS instance must have been created and must be in INSTANTIATED state. 

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsInstancesApi apiInstance = new NsInstancesApi();
String nsInstanceId = "nsInstanceId_example"; // String | NS Instance ID
NSinstanceActionRequest body = new NSinstanceActionRequest(); // NSinstanceActionRequest | 
try {
    ObjectId result = apiInstance.actionOnNSinstance(nsInstanceId, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsInstancesApi#actionOnNSinstance");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsInstanceId** | **String**| NS Instance ID |
 **body** | [**NSinstanceActionRequest**](NSinstanceActionRequest.md)|  | [optional]

### Return type

[**ObjectId**](ObjectId.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/yaml
 - **Accept**: application/json, application/yaml

<a name="addNSinstance"></a>
# **addNSinstance**
> ObjectId addNSinstance(body)

Create a new NS instance resource

Create a new NS instance resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsInstancesApi apiInstance = new NsInstancesApi();
CreateNsRequest body = new CreateNsRequest(); // CreateNsRequest | 
try {
    ObjectId result = apiInstance.addNSinstance(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsInstancesApi#addNSinstance");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CreateNsRequest**](CreateNsRequest.md)|  | [optional]

### Return type

[**ObjectId**](ObjectId.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/yaml
 - **Accept**: application/json, application/yaml

<a name="createNSinstanceContent"></a>
# **createNSinstanceContent**
> CreateNSinstanceContentResponse createNSinstanceContent(body)

Create a new NS instance

Create a new NS instance

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsInstancesApi apiInstance = new NsInstancesApi();
CreateNSinstanceContentRequest body = new CreateNSinstanceContentRequest(); // CreateNSinstanceContentRequest | 
try {
    CreateNSinstanceContentResponse result = apiInstance.createNSinstanceContent(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsInstancesApi#createNSinstanceContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CreateNSinstanceContentRequest**](CreateNSinstanceContentRequest.md)|  | [optional]

### Return type

[**CreateNSinstanceContentResponse**](CreateNSinstanceContentResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/yaml
 - **Accept**: application/json, application/yaml

<a name="deleteNSinstance"></a>
# **deleteNSinstance**
> deleteNSinstance(nsInstanceId)

Delete an individual NS instance resource

Delete an individual NS instance resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsInstancesApi apiInstance = new NsInstancesApi();
String nsInstanceId = "nsInstanceId_example"; // String | NS Instance ID
try {
    apiInstance.deleteNSinstance(nsInstanceId);
} catch (ApiException e) {
    System.err.println("Exception when calling NsInstancesApi#deleteNSinstance");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsInstanceId** | **String**| NS Instance ID |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="deleteNSinstanceContent"></a>
# **deleteNSinstanceContent**
> ObjectId deleteNSinstanceContent(nsInstanceContentId)

Delete an individual NS instance resource

Delete an individual NS instance resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsInstancesApi apiInstance = new NsInstancesApi();
String nsInstanceContentId = "nsInstanceContentId_example"; // String | NS Instance Content ID
try {
    ObjectId result = apiInstance.deleteNSinstanceContent(nsInstanceContentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsInstancesApi#deleteNSinstanceContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsInstanceContentId** | **String**| NS Instance Content ID |

### Return type

[**ObjectId**](ObjectId.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getNSLCMOpOcc"></a>
# **getNSLCMOpOcc**
> NsLcmOpOcc getNSLCMOpOcc(nsLcmOpOccId)

Query information about an individual NS LCM Operation Occurrence

Query information about an individual NS LCM Operation Occurrence

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsInstancesApi apiInstance = new NsInstancesApi();
String nsLcmOpOccId = "nsLcmOpOccId_example"; // String | NS LCM Operation Occurrence ID
try {
    NsLcmOpOcc result = apiInstance.getNSLCMOpOcc(nsLcmOpOccId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsInstancesApi#getNSLCMOpOcc");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsLcmOpOccId** | **String**| NS LCM Operation Occurrence ID |

### Return type

[**NsLcmOpOcc**](NsLcmOpOcc.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getNSLCMOpOccs"></a>
# **getNSLCMOpOccs**
> ArrayOfNsLcmOpOcc getNSLCMOpOccs()

Query information about multiple NS LCM Operation Occurrences

Query information about multiple NS LCM Operation Occurrences

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsInstancesApi apiInstance = new NsInstancesApi();
try {
    ArrayOfNsLcmOpOcc result = apiInstance.getNSLCMOpOccs();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsInstancesApi#getNSLCMOpOccs");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ArrayOfNsLcmOpOcc**](ArrayOfNsLcmOpOcc.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getNSinstance"></a>
# **getNSinstance**
> NsInstance getNSinstance(nsInstanceId)

Read an individual NS instance resource

Read an individual NS instance resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsInstancesApi apiInstance = new NsInstancesApi();
String nsInstanceId = "nsInstanceId_example"; // String | NS Instance ID
try {
    NsInstance result = apiInstance.getNSinstance(nsInstanceId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsInstancesApi#getNSinstance");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsInstanceId** | **String**| NS Instance ID |

### Return type

[**NsInstance**](NsInstance.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getNSinstanceContent"></a>
# **getNSinstanceContent**
> NsInstance getNSinstanceContent(nsInstanceContentId)

Read an individual NS instance resource

Read an individual NS instance resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsInstancesApi apiInstance = new NsInstancesApi();
String nsInstanceContentId = "nsInstanceContentId_example"; // String | NS Instance Content ID
try {
    NsInstance result = apiInstance.getNSinstanceContent(nsInstanceContentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsInstancesApi#getNSinstanceContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsInstanceContentId** | **String**| NS Instance Content ID |

### Return type

[**NsInstance**](NsInstance.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getNSinstances"></a>
# **getNSinstances**
> ArrayOfNsInstance getNSinstances()

Query information about multiple NS instances

Query information about multiple NS isntances

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsInstancesApi apiInstance = new NsInstancesApi();
try {
    ArrayOfNsInstance result = apiInstance.getNSinstances();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsInstancesApi#getNSinstances");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ArrayOfNsInstance**](ArrayOfNsInstance.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getNSinstancesContent"></a>
# **getNSinstancesContent**
> ArrayOfNsInstance getNSinstancesContent()

Query information about multiple NS instances

Query information about multiple NS isntances

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsInstancesApi apiInstance = new NsInstancesApi();
try {
    ArrayOfNsInstance result = apiInstance.getNSinstancesContent();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsInstancesApi#getNSinstancesContent");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ArrayOfNsInstance**](ArrayOfNsInstance.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getVnfInstance"></a>
# **getVnfInstance**
> VnfInstanceInfo getVnfInstance(vnfInstanceId)

Query information about an individual VNF Instance

Query information about an individual VNF Instance

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsInstancesApi apiInstance = new NsInstancesApi();
String vnfInstanceId = "vnfInstanceId_example"; // String | VNF Instance ID
try {
    VnfInstanceInfo result = apiInstance.getVnfInstance(vnfInstanceId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsInstancesApi#getVnfInstance");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **vnfInstanceId** | **String**| VNF Instance ID |

### Return type

[**VnfInstanceInfo**](VnfInstanceInfo.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getVnfInstances"></a>
# **getVnfInstances**
> ArrayOfVnfInstanceInfo getVnfInstances()

Query information about multiple VNF Instances

Query information about multiple VNF Instances

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsInstancesApi apiInstance = new NsInstancesApi();
try {
    ArrayOfVnfInstanceInfo result = apiInstance.getVnfInstances();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsInstancesApi#getVnfInstances");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ArrayOfVnfInstanceInfo**](ArrayOfVnfInstanceInfo.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="instantiateNSinstance"></a>
# **instantiateNSinstance**
> ObjectId instantiateNSinstance(nsInstanceId, body)

Instantiate a NS

Instantiate a NS. The precondition is that the NS instance must have been created and must be in NOT_INSTANTIATED state. As a result of the success of this operation, the NFVO creates a \&quot;NS Lifecycle Operation Occurrence\&quot; resource for the request, and the NS instance state becomes INSTANTIATED. 

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsInstancesApi apiInstance = new NsInstancesApi();
String nsInstanceId = "nsInstanceId_example"; // String | NS Instance ID
InstantiateNsRequest body = new InstantiateNsRequest(); // InstantiateNsRequest | 
try {
    ObjectId result = apiInstance.instantiateNSinstance(nsInstanceId, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsInstancesApi#instantiateNSinstance");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsInstanceId** | **String**| NS Instance ID |
 **body** | [**InstantiateNsRequest**](InstantiateNsRequest.md)|  | [optional]

### Return type

[**ObjectId**](ObjectId.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/yaml
 - **Accept**: application/json, application/yaml

<a name="scaleNSinstance"></a>
# **scaleNSinstance**
> scaleNSinstance(nsInstanceId, body)

Scale a NS instance

Scale a NS instance. The precondition is that the NS instance must have been created and must be in INSTANTIATED state. As a result of the success of this operation, the NFVO creates a \&quot;NS Lifecycle Operation Occurrence\&quot; resource for the request, and the NS instance state remains INSTANTIATED. 

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsInstancesApi apiInstance = new NsInstancesApi();
String nsInstanceId = "nsInstanceId_example"; // String | NS Instance ID
Map<String, Object> body = new Map(); // Map<String, Object> | 
try {
    apiInstance.scaleNSinstance(nsInstanceId, body);
} catch (ApiException e) {
    System.err.println("Exception when calling NsInstancesApi#scaleNSinstance");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsInstanceId** | **String**| NS Instance ID |
 **body** | [**Map&lt;String, Object&gt;**](Map.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/yaml
 - **Accept**: application/json

<a name="terminateNSinstance"></a>
# **terminateNSinstance**
> ObjectId terminateNSinstance(nsInstanceId, body)

Terminate a NS instance

Terminate a NS instance. The precondition is that the NS instance must have been created and must be in INSTANTIATED state. As a result of the success of this operation, the NFVO creates a \&quot;NS Lifecycle Operation Occurrence\&quot; resource for the request, and the NS instance state becomes NOT_INSTANTIATED. 

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsInstancesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsInstancesApi apiInstance = new NsInstancesApi();
String nsInstanceId = "nsInstanceId_example"; // String | NS Instance ID
TerminateNsRequest body = new TerminateNsRequest(); // TerminateNsRequest | 
try {
    ObjectId result = apiInstance.terminateNSinstance(nsInstanceId, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsInstancesApi#terminateNSinstance");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsInstanceId** | **String**| NS Instance ID |
 **body** | [**TerminateNsRequest**](TerminateNsRequest.md)|  | [optional]

### Return type

[**ObjectId**](ObjectId.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/yaml
 - **Accept**: application/json, application/yaml

