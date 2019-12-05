# NetSliceTemplatesApi

All URIs are relative to *https://osm.etsi.org/nbapi/v1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addNST**](NetSliceTemplatesApi.md#addNST) | **POST** /nst/v1/netslice_templates | Create a new NetSlice template resource
[**deleteNST**](NetSliceTemplatesApi.md#deleteNST) | **DELETE** /nst/v1/netslice_templates/{netsliceTemplateId} | Delete an individual NetSlice template resource
[**deleteNstIdContent**](NetSliceTemplatesApi.md#deleteNstIdContent) | **DELETE** /nst/v1/netslice_templates_content/{netsliceTemplateContentId} | Delete an individual NetSlice Template resource
[**getNST**](NetSliceTemplatesApi.md#getNST) | **GET** /nst/v1/netslice_templates/{netsliceTemplateId} | Read information about an individual NetSlice template resource
[**getNSTcontent**](NetSliceTemplatesApi.md#getNSTcontent) | **GET** /nst/v1/netslice_templates/{netsliceTemplateId}/nst_content | Fetch the content of a NST
[**getNSTs**](NetSliceTemplatesApi.md#getNSTs) | **GET** /nst/v1/netslice_templates | Query information about multiple NetSlice template resources
[**getNstArtifact**](NetSliceTemplatesApi.md#getNstArtifact) | **GET** /nst/v1/netslice_templates/{netsliceTemplateId}/artifacts/{artifactPath} | Fetch individual NetSlice Template artifact
[**getNstContent**](NetSliceTemplatesApi.md#getNstContent) | **GET** /nst/v1/netslice_templates_content | Query information about multiple NetSlice Template resources
[**getNstIdContent**](NetSliceTemplatesApi.md#getNstIdContent) | **GET** /nst/v1/netslice_templates_content/{netsliceTemplateContentId} | Read information about an individual NetSlice Template resource
[**getNstNst**](NetSliceTemplatesApi.md#getNstNst) | **GET** /nst/v1/netslice_templates/{netsliceTemplateId}/nst | Read NST of an on-boarded NetSlice Template
[**updateNSTcontent**](NetSliceTemplatesApi.md#updateNSTcontent) | **PUT** /nst/v1/netslice_templates/{netsliceTemplateId}/nst_content | Upload the content of a NST
[**updateNstIdContent**](NetSliceTemplatesApi.md#updateNstIdContent) | **PUT** /nst/v1/netslice_templates_content/{netsliceTemplateContentId} | Modify an individual NetSlice Template resource
[**uploadNstContent**](NetSliceTemplatesApi.md#uploadNstContent) | **POST** /nst/v1/netslice_templates_content | Upload a NetSlice package by providing the content of the NetSlice package

<a name="addNST"></a>
# **addNST**
> ObjectId addNST(body)

Create a new NetSlice template resource

Create a new NetSlice template resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceTemplatesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceTemplatesApi apiInstance = new NetSliceTemplatesApi();
Map<String, Object> body = new Map(); // Map<String, Object> | 
try {
    ObjectId result = apiInstance.addNST(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceTemplatesApi#addNST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**Map&lt;String, Object&gt;**](Map.md)|  | [optional]

### Return type

[**ObjectId**](ObjectId.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/yaml
 - **Accept**: application/json, application/yaml

<a name="deleteNST"></a>
# **deleteNST**
> deleteNST(netsliceTemplateId)

Delete an individual NetSlice template resource

Delete an individual NetSlice template resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceTemplatesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceTemplatesApi apiInstance = new NetSliceTemplatesApi();
String netsliceTemplateId = "netsliceTemplateId_example"; // String | NetSlice Template ID
try {
    apiInstance.deleteNST(netsliceTemplateId);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceTemplatesApi#deleteNST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **netsliceTemplateId** | **String**| NetSlice Template ID |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="deleteNstIdContent"></a>
# **deleteNstIdContent**
> deleteNstIdContent(netsliceTemplateContentId)

Delete an individual NetSlice Template resource

Delete an individual NetSlice Template resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceTemplatesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceTemplatesApi apiInstance = new NetSliceTemplatesApi();
String netsliceTemplateContentId = "netsliceTemplateContentId_example"; // String | NetSlice Template ID
try {
    apiInstance.deleteNstIdContent(netsliceTemplateContentId);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceTemplatesApi#deleteNstIdContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **netsliceTemplateContentId** | **String**| NetSlice Template ID |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getNST"></a>
# **getNST**
> NstInfo getNST(netsliceTemplateId)

Read information about an individual NetSlice template resource

Read information about an individual NetSlice template resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceTemplatesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceTemplatesApi apiInstance = new NetSliceTemplatesApi();
String netsliceTemplateId = "netsliceTemplateId_example"; // String | NetSlice Template ID
try {
    NstInfo result = apiInstance.getNST(netsliceTemplateId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceTemplatesApi#getNST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **netsliceTemplateId** | **String**| NetSlice Template ID |

### Return type

[**NstInfo**](NstInfo.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getNSTcontent"></a>
# **getNSTcontent**
> File getNSTcontent(netsliceTemplateId)

Fetch the content of a NST

Fetch the content of a NST

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceTemplatesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceTemplatesApi apiInstance = new NetSliceTemplatesApi();
String netsliceTemplateId = "netsliceTemplateId_example"; // String | NetSlice Template ID
try {
    File result = apiInstance.getNSTcontent(netsliceTemplateId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceTemplatesApi#getNSTcontent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **netsliceTemplateId** | **String**| NetSlice Template ID |

### Return type

[**File**](File.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/zip, application/json

<a name="getNSTs"></a>
# **getNSTs**
> ArrayOfNstInfo getNSTs()

Query information about multiple NetSlice template resources

Query information about multiple NetSlice template resources

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceTemplatesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceTemplatesApi apiInstance = new NetSliceTemplatesApi();
try {
    ArrayOfNstInfo result = apiInstance.getNSTs();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceTemplatesApi#getNSTs");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ArrayOfNstInfo**](ArrayOfNstInfo.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getNstArtifact"></a>
# **getNstArtifact**
> File getNstArtifact(netsliceTemplateId, artifactPath)

Fetch individual NetSlice Template artifact

Fetch individual NetSlice Template artifact

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceTemplatesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceTemplatesApi apiInstance = new NetSliceTemplatesApi();
String netsliceTemplateId = "netsliceTemplateId_example"; // String | NetSlice Template ID
String artifactPath = "artifactPath_example"; // String | Artifact Path
try {
    File result = apiInstance.getNstArtifact(netsliceTemplateId, artifactPath);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceTemplatesApi#getNstArtifact");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **netsliceTemplateId** | **String**| NetSlice Template ID |
 **artifactPath** | **String**| Artifact Path |

### Return type

[**File**](File.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/octet-stream, application/json

<a name="getNstContent"></a>
# **getNstContent**
> ArrayOfNstInfo getNstContent()

Query information about multiple NetSlice Template resources

Query information about multiple NetSlice Template resources

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceTemplatesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceTemplatesApi apiInstance = new NetSliceTemplatesApi();
try {
    ArrayOfNstInfo result = apiInstance.getNstContent();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceTemplatesApi#getNstContent");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ArrayOfNstInfo**](ArrayOfNstInfo.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml, application/octet-stream

<a name="getNstIdContent"></a>
# **getNstIdContent**
> NstInfo getNstIdContent(netsliceTemplateContentId)

Read information about an individual NetSlice Template resource

Read information about an individual NetSlice Template resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceTemplatesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceTemplatesApi apiInstance = new NetSliceTemplatesApi();
String netsliceTemplateContentId = "netsliceTemplateContentId_example"; // String | NetSlice Template ID
try {
    NstInfo result = apiInstance.getNstIdContent(netsliceTemplateContentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceTemplatesApi#getNstIdContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **netsliceTemplateContentId** | **String**| NetSlice Template ID |

### Return type

[**NstInfo**](NstInfo.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getNstNst"></a>
# **getNstNst**
> String getNstNst(netsliceTemplateId)

Read NST of an on-boarded NetSlice Template

Read NST of an on-boarded NetSlice Template

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceTemplatesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceTemplatesApi apiInstance = new NetSliceTemplatesApi();
String netsliceTemplateId = "netsliceTemplateId_example"; // String | NetSlice Template ID
try {
    String result = apiInstance.getNstNst(netsliceTemplateId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceTemplatesApi#getNstNst");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **netsliceTemplateId** | **String**| NetSlice Template ID |

### Return type

**String**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json

<a name="updateNSTcontent"></a>
# **updateNSTcontent**
> updateNSTcontent(netsliceTemplateId, body)

Upload the content of a NST

Upload the content of a NST

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceTemplatesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceTemplatesApi apiInstance = new NetSliceTemplatesApi();
String netsliceTemplateId = "netsliceTemplateId_example"; // String | NetSlice Template ID
Object body = null; // Object | 
try {
    apiInstance.updateNSTcontent(netsliceTemplateId, body);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceTemplatesApi#updateNSTcontent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **netsliceTemplateId** | **String**| NetSlice Template ID |
 **body** | [**Object**](Object.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/zip
 - **Accept**: application/json

<a name="updateNstIdContent"></a>
# **updateNstIdContent**
> updateNstIdContent(netsliceTemplateContentId, body)

Modify an individual NetSlice Template resource

Modify an individual NetSlice Template resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceTemplatesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceTemplatesApi apiInstance = new NetSliceTemplatesApi();
String netsliceTemplateContentId = "netsliceTemplateContentId_example"; // String | NetSlice Template ID
NstInfoModifications body = new NstInfoModifications(); // NstInfoModifications | 
try {
    apiInstance.updateNstIdContent(netsliceTemplateContentId, body);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceTemplatesApi#updateNstIdContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **netsliceTemplateContentId** | **String**| NetSlice Template ID |
 **body** | [**NstInfoModifications**](NstInfoModifications.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/yaml
 - **Accept**: application/json

<a name="uploadNstContent"></a>
# **uploadNstContent**
> ObjectId uploadNstContent(body)

Upload a NetSlice package by providing the content of the NetSlice package

Upload a NetSlice package by providing the content of the NetSlice package

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NetSliceTemplatesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NetSliceTemplatesApi apiInstance = new NetSliceTemplatesApi();
Object body = null; // Object | 
try {
    ObjectId result = apiInstance.uploadNstContent(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NetSliceTemplatesApi#uploadNstContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**Object**](Object.md)|  | [optional]

### Return type

[**ObjectId**](ObjectId.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/zip
 - **Accept**: application/json, application/yaml

