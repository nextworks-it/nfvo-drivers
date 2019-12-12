# NsPackagesApi

All URIs are relative to *https://osm.etsi.org/nbapi/v1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addNSD**](NsPackagesApi.md#addNSD) | **POST** /nsd/v1/ns_descriptors | Create a new NS descriptor resource
[**deleteNSD**](NsPackagesApi.md#deleteNSD) | **DELETE** /nsd/v1/ns_descriptors/{nsdInfoId} | Delete an individual NS descriptor resource
[**deleteNSPkgsIdContent**](NsPackagesApi.md#deleteNSPkgsIdContent) | **DELETE** /nsd/v1/ns_descriptors_content/{nsdInfoId} | Delete an individual NS package resource
[**getNSD**](NsPackagesApi.md#getNSD) | **GET** /nsd/v1/ns_descriptors/{nsdInfoId} | Read information about an individual NS descriptor resource
[**getNSDcontent**](NsPackagesApi.md#getNSDcontent) | **GET** /nsd/v1/ns_descriptors/{nsdInfoId}/nsd_content | Fetch the content of a NSD
[**getNSDs**](NsPackagesApi.md#getNSDs) | **GET** /nsd/v1/ns_descriptors | Query information about multiple NS descriptor resources
[**getNsPkgArtifact**](NsPackagesApi.md#getNsPkgArtifact) | **GET** /nsd/v1/ns_descriptors/{nsdInfoId}/artifacts/{artifactPath} | Fetch individual NS package artifact
[**getNsPkgNsd**](NsPackagesApi.md#getNsPkgNsd) | **GET** /nsd/v1/ns_descriptors/{nsdInfoId}/nsd | Read NSD of an on-boarded NS package
[**getNsPkgsContent**](NsPackagesApi.md#getNsPkgsContent) | **GET** /nsd/v1/ns_descriptors_content | Query information about multiple NS package resources
[**getNsPkgsIdContent**](NsPackagesApi.md#getNsPkgsIdContent) | **GET** /nsd/v1/ns_descriptors_content/{nsdInfoId} | Read information about an individual NS package resource
[**updateNSD**](NsPackagesApi.md#updateNSD) | **PATCH** /nsd/v1/ns_descriptors/{nsdInfoId} | Modify the data of an  individual NS descriptor resource
[**updateNSDcontent**](NsPackagesApi.md#updateNSDcontent) | **PUT** /nsd/v1/ns_descriptors/{nsdInfoId}/nsd_content | Upload the content of a NSD
[**updateNsPkgsIdContent**](NsPackagesApi.md#updateNsPkgsIdContent) | **PUT** /nsd/v1/ns_descriptors_content/{nsdInfoId} | Modify an individual NS package resource
[**uploadNsPkgsContent**](NsPackagesApi.md#uploadNsPkgsContent) | **POST** /nsd/v1/ns_descriptors_content | Upload a NS package by providing the content of the NS package

<a name="addNSD"></a>
# **addNSD**
> ObjectId addNSD(body)

Create a new NS descriptor resource

Create a new NS descriptor resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsPackagesApi apiInstance = new NsPackagesApi();
Map<String, Object> body = new Map(); // Map<String, Object> | 
try {
    ObjectId result = apiInstance.addNSD(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsPackagesApi#addNSD");
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

<a name="deleteNSD"></a>
# **deleteNSD**
> deleteNSD(nsdInfoId)

Delete an individual NS descriptor resource

Delete an individual NS descriptor resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsPackagesApi apiInstance = new NsPackagesApi();
String nsdInfoId = "nsdInfoId_example"; // String | NSD Info ID
try {
    apiInstance.deleteNSD(nsdInfoId);
} catch (ApiException e) {
    System.err.println("Exception when calling NsPackagesApi#deleteNSD");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsdInfoId** | **String**| NSD Info ID |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="deleteNSPkgsIdContent"></a>
# **deleteNSPkgsIdContent**
> deleteNSPkgsIdContent(nsdInfoId)

Delete an individual NS package resource

Delete an individual NS package resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsPackagesApi apiInstance = new NsPackagesApi();
String nsdInfoId = "nsdInfoId_example"; // String | NS Package ID
try {
    apiInstance.deleteNSPkgsIdContent(nsdInfoId);
} catch (ApiException e) {
    System.err.println("Exception when calling NsPackagesApi#deleteNSPkgsIdContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsdInfoId** | **String**| NS Package ID |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getNSD"></a>
# **getNSD**
> NsdInfo getNSD(nsdInfoId)

Read information about an individual NS descriptor resource

Read information about an individual NS descriptor resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsPackagesApi apiInstance = new NsPackagesApi();
String nsdInfoId = "nsdInfoId_example"; // String | NSD Info ID
try {
    NsdInfo result = apiInstance.getNSD(nsdInfoId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsPackagesApi#getNSD");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsdInfoId** | **String**| NSD Info ID |

### Return type

[**NsdInfo**](NsdInfo.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getNSDcontent"></a>
# **getNSDcontent**
> File getNSDcontent(nsdInfoId)

Fetch the content of a NSD

Fetch the content of a NSD

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsPackagesApi apiInstance = new NsPackagesApi();
String nsdInfoId = "nsdInfoId_example"; // String | NSD Info ID
try {
    File result = apiInstance.getNSDcontent(nsdInfoId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsPackagesApi#getNSDcontent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsdInfoId** | **String**| NSD Info ID |

### Return type

[**File**](File.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/zip, application/json

<a name="getNSDs"></a>
# **getNSDs**
> ArrayOfNsdInfo getNSDs()

Query information about multiple NS descriptor resources

Query information about multiple NS descriptor resources

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsPackagesApi apiInstance = new NsPackagesApi();
try {
    ArrayOfNsdInfo result = apiInstance.getNSDs();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsPackagesApi#getNSDs");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ArrayOfNsdInfo**](ArrayOfNsdInfo.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getNsPkgArtifact"></a>
# **getNsPkgArtifact**
> File getNsPkgArtifact(nsdInfoId, artifactPath)

Fetch individual NS package artifact

Fetch individual NS package artifact

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsPackagesApi apiInstance = new NsPackagesApi();
String nsdInfoId = "nsdInfoId_example"; // String | NS Package ID
String artifactPath = "artifactPath_example"; // String | Artifact Path
try {
    File result = apiInstance.getNsPkgArtifact(nsdInfoId, artifactPath);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsPackagesApi#getNsPkgArtifact");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsdInfoId** | **String**| NS Package ID |
 **artifactPath** | **String**| Artifact Path |

### Return type

[**File**](File.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/octet-stream, application/json

<a name="getNsPkgNsd"></a>
# **getNsPkgNsd**
> String getNsPkgNsd(nsdInfoId)

Read NSD of an on-boarded NS package

Read NSD of an on-boarded NS package

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsPackagesApi apiInstance = new NsPackagesApi();
String nsdInfoId = "nsdInfoId_example"; // String | NS Package ID
try {
    String result = apiInstance.getNsPkgNsd(nsdInfoId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsPackagesApi#getNsPkgNsd");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsdInfoId** | **String**| NS Package ID |

### Return type

**String**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json

<a name="getNsPkgsContent"></a>
# **getNsPkgsContent**
> ArrayOfNsdInfo getNsPkgsContent()

Query information about multiple NS package resources

Query information about multiple NS package resources

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsPackagesApi apiInstance = new NsPackagesApi();
try {
    ArrayOfNsdInfo result = apiInstance.getNsPkgsContent();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsPackagesApi#getNsPkgsContent");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ArrayOfNsdInfo**](ArrayOfNsdInfo.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml, application/octet-stream

<a name="getNsPkgsIdContent"></a>
# **getNsPkgsIdContent**
> NsdInfo getNsPkgsIdContent(nsdInfoId)

Read information about an individual NS package resource

Read information about an individual NS package resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsPackagesApi apiInstance = new NsPackagesApi();
String nsdInfoId = "nsdInfoId_example"; // String | NS Package ID
try {
    NsdInfo result = apiInstance.getNsPkgsIdContent(nsdInfoId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsPackagesApi#getNsPkgsIdContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsdInfoId** | **String**| NS Package ID |

### Return type

[**NsdInfo**](NsdInfo.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="updateNSD"></a>
# **updateNSD**
> updateNSD(nsdInfoId, body)

Modify the data of an  individual NS descriptor resource

Modify the data of an  individual NS descriptor resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsPackagesApi apiInstance = new NsPackagesApi();
String nsdInfoId = "nsdInfoId_example"; // String | NSD Info ID
NsdInfoModifications body = new NsdInfoModifications(); // NsdInfoModifications | 
try {
    apiInstance.updateNSD(nsdInfoId, body);
} catch (ApiException e) {
    System.err.println("Exception when calling NsPackagesApi#updateNSD");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsdInfoId** | **String**| NSD Info ID |
 **body** | [**NsdInfoModifications**](NsdInfoModifications.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/yaml
 - **Accept**: application/json

<a name="updateNSDcontent"></a>
# **updateNSDcontent**
> updateNSDcontent(nsdInfoId, body)

Upload the content of a NSD

Upload the content of a NSD

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsPackagesApi apiInstance = new NsPackagesApi();
String nsdInfoId = "nsdInfoId_example"; // String | NSD Info ID
Object body = null; // Object | 
try {
    apiInstance.updateNSDcontent(nsdInfoId, body);
} catch (ApiException e) {
    System.err.println("Exception when calling NsPackagesApi#updateNSDcontent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsdInfoId** | **String**| NSD Info ID |
 **body** | [**Object**](Object.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/zip
 - **Accept**: application/json

<a name="updateNsPkgsIdContent"></a>
# **updateNsPkgsIdContent**
> updateNsPkgsIdContent(nsdInfoId, body)

Modify an individual NS package resource

Modify an individual NS package resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsPackagesApi apiInstance = new NsPackagesApi();
String nsdInfoId = "nsdInfoId_example"; // String | NS Package ID
NsdInfoModifications body = new NsdInfoModifications(); // NsdInfoModifications | 
try {
    apiInstance.updateNsPkgsIdContent(nsdInfoId, body);
} catch (ApiException e) {
    System.err.println("Exception when calling NsPackagesApi#updateNsPkgsIdContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsdInfoId** | **String**| NS Package ID |
 **body** | [**NsdInfoModifications**](NsdInfoModifications.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/yaml
 - **Accept**: application/json

<a name="uploadNsPkgsContent"></a>
# **uploadNsPkgsContent**
> ObjectId uploadNsPkgsContent(body)

Upload a NS package by providing the content of the NS package

Upload a NS package by providing the content of the NS package

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.NsPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


NsPackagesApi apiInstance = new NsPackagesApi();
Object body = null; // Object | 
try {
    ObjectId result = apiInstance.uploadNsPkgsContent(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NsPackagesApi#uploadNsPkgsContent");
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

