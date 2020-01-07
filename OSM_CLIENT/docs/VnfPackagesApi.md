# VnfPackagesApi

All URIs are relative to *https://osm.etsi.org/nbapi/v1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addVnfPkg**](VnfPackagesApi.md#addVnfPkg) | **POST** /vnfpkgm/v1/vnf_packages | Create a new VNF package resource
[**deleteVnfPkg**](VnfPackagesApi.md#deleteVnfPkg) | **DELETE** /vnfpkgm/v1/vnf_packages/{vnfPkgId} | Delete an individual VNF package resource
[**deleteVnfPkgsIdContent**](VnfPackagesApi.md#deleteVnfPkgsIdContent) | **DELETE** /vnfpkgm/v1/vnf_packages_content/{packageContentId} | Delete an individual VNF package resource
[**getVnfPkg**](VnfPackagesApi.md#getVnfPkg) | **GET** /vnfpkgm/v1/vnf_packages/{vnfPkgId} | Read information about an individual VNF package resource
[**getVnfPkgArtifact**](VnfPackagesApi.md#getVnfPkgArtifact) | **GET** /vnfpkgm/v1/vnf_packages/{vnfPkgId}/artifacts/{artifactPath} | Fetch individual VNF package artifact
[**getVnfPkgContent**](VnfPackagesApi.md#getVnfPkgContent) | **GET** /vnfpkgm/v1/vnf_packages/{vnfPkgId}/package_content | Fetch an on-boarded VNF package
[**getVnfPkgVnfd**](VnfPackagesApi.md#getVnfPkgVnfd) | **GET** /vnfpkgm/v1/vnf_packages/{vnfPkgId}/vnfd | Read VNFD of an on-boarded VNF package
[**getVnfPkgs**](VnfPackagesApi.md#getVnfPkgs) | **GET** /vnfpkgm/v1/vnf_packages | Query information about multiple VNF package resources
[**getVnfPkgsContent**](VnfPackagesApi.md#getVnfPkgsContent) | **GET** /vnfpkgm/v1/vnf_packages_content | Query information about multiple VNF package resources
[**getVnfPkgsIdContent**](VnfPackagesApi.md#getVnfPkgsIdContent) | **GET** /vnfpkgm/v1/vnf_packages_content/{packageContentId} | Read information about an individual VNF package resource
[**updateVnfPkg**](VnfPackagesApi.md#updateVnfPkg) | **PATCH** /vnfpkgm/v1/vnf_packages/{vnfPkgId} | Modify an individual VNF package resource
[**updateVnfPkgsIdContent**](VnfPackagesApi.md#updateVnfPkgsIdContent) | **PUT** /vnfpkgm/v1/vnf_packages_content/{packageContentId} | Modify an individual VNF package resource
[**uploadVnfPkgContent**](VnfPackagesApi.md#uploadVnfPkgContent) | **PUT** /vnfpkgm/v1/vnf_packages/{vnfPkgId}/package_content | Upload a VNF package by providing the content of the VNF package
[**uploadVnfPkgsContent**](VnfPackagesApi.md#uploadVnfPkgsContent) | **POST** /vnfpkgm/v1/vnf_packages_content | Upload a VNF package by providing the content of the VNF package

<a name="addVnfPkg"></a>
# **addVnfPkg**
> ObjectId addVnfPkg(body)

Create a new VNF package resource

Create a new VNF package resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.VnfPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


VnfPackagesApi apiInstance = new VnfPackagesApi();
Map<String, Object> body = new Map(); // Map<String, Object> | 
try {
    ObjectId result = apiInstance.addVnfPkg(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling VnfPackagesApi#addVnfPkg");
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

<a name="deleteVnfPkg"></a>
# **deleteVnfPkg**
> deleteVnfPkg(vnfPkgId)

Delete an individual VNF package resource

Delete an individual VNF package resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.VnfPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


VnfPackagesApi apiInstance = new VnfPackagesApi();
String vnfPkgId = "vnfPkgId_example"; // String | VNF Package ID
try {
    apiInstance.deleteVnfPkg(vnfPkgId);
} catch (ApiException e) {
    System.err.println("Exception when calling VnfPackagesApi#deleteVnfPkg");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **vnfPkgId** | **String**| VNF Package ID |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="deleteVnfPkgsIdContent"></a>
# **deleteVnfPkgsIdContent**
> deleteVnfPkgsIdContent(packageContentId)

Delete an individual VNF package resource

Delete an individual VNF package resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.VnfPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


VnfPackagesApi apiInstance = new VnfPackagesApi();
String packageContentId = "packageContentId_example"; // String | VNF Package Content ID
try {
    apiInstance.deleteVnfPkgsIdContent(packageContentId);
} catch (ApiException e) {
    System.err.println("Exception when calling VnfPackagesApi#deleteVnfPkgsIdContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **packageContentId** | **String**| VNF Package Content ID |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getVnfPkg"></a>
# **getVnfPkg**
> VnfPkgInfo getVnfPkg(vnfPkgId)

Read information about an individual VNF package resource

Read information about an individual VNF package resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.VnfPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


VnfPackagesApi apiInstance = new VnfPackagesApi();
String vnfPkgId = "vnfPkgId_example"; // String | VNF Package ID
try {
    VnfPkgInfo result = apiInstance.getVnfPkg(vnfPkgId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling VnfPackagesApi#getVnfPkg");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **vnfPkgId** | **String**| VNF Package ID |

### Return type

[**VnfPkgInfo**](VnfPkgInfo.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getVnfPkgArtifact"></a>
# **getVnfPkgArtifact**
> File getVnfPkgArtifact(vnfPkgId, artifactPath)

Fetch individual VNF package artifact

Fetch individual VNF package artifact

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.VnfPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


VnfPackagesApi apiInstance = new VnfPackagesApi();
String vnfPkgId = "vnfPkgId_example"; // String | VNF Package ID
String artifactPath = "artifactPath_example"; // String | Artifact Path
try {
    File result = apiInstance.getVnfPkgArtifact(vnfPkgId, artifactPath);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling VnfPackagesApi#getVnfPkgArtifact");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **vnfPkgId** | **String**| VNF Package ID |
 **artifactPath** | **String**| Artifact Path |

### Return type

[**File**](File.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/octet-stream, application/json

<a name="getVnfPkgContent"></a>
# **getVnfPkgContent**
> File getVnfPkgContent(vnfPkgId)

Fetch an on-boarded VNF package

Fetch an on-boarded VNF package

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.VnfPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


VnfPackagesApi apiInstance = new VnfPackagesApi();
String vnfPkgId = "vnfPkgId_example"; // String | VNF Package ID
try {
    File result = apiInstance.getVnfPkgContent(vnfPkgId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling VnfPackagesApi#getVnfPkgContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **vnfPkgId** | **String**| VNF Package ID |

### Return type

[**File**](File.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/zip, application/json

<a name="getVnfPkgVnfd"></a>
# **getVnfPkgVnfd**
> String getVnfPkgVnfd(vnfPkgId)

Read VNFD of an on-boarded VNF package

Read VNFD of an on-boarded VNF package

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.VnfPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


VnfPackagesApi apiInstance = new VnfPackagesApi();
String vnfPkgId = "vnfPkgId_example"; // String | VNF Package ID
try {
    String result = apiInstance.getVnfPkgVnfd(vnfPkgId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling VnfPackagesApi#getVnfPkgVnfd");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **vnfPkgId** | **String**| VNF Package ID |

### Return type

**String**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json

<a name="getVnfPkgs"></a>
# **getVnfPkgs**
> ArrayOfVnfPkgInfo getVnfPkgs()

Query information about multiple VNF package resources

Query information about multiple VNF package resources

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.VnfPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


VnfPackagesApi apiInstance = new VnfPackagesApi();
try {
    ArrayOfVnfPkgInfo result = apiInstance.getVnfPkgs();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling VnfPackagesApi#getVnfPkgs");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ArrayOfVnfPkgInfo**](ArrayOfVnfPkgInfo.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="getVnfPkgsContent"></a>
# **getVnfPkgsContent**
> ArrayOfVnfPkgInfo getVnfPkgsContent()

Query information about multiple VNF package resources

Query information about multiple VNF package resources

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.VnfPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


VnfPackagesApi apiInstance = new VnfPackagesApi();
try {
    ArrayOfVnfPkgInfo result = apiInstance.getVnfPkgsContent();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling VnfPackagesApi#getVnfPkgsContent");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ArrayOfVnfPkgInfo**](ArrayOfVnfPkgInfo.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml, application/octet-stream

<a name="getVnfPkgsIdContent"></a>
# **getVnfPkgsIdContent**
> VnfPkgInfo getVnfPkgsIdContent(packageContentId)

Read information about an individual VNF package resource

Read information about an individual VNF package resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.VnfPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


VnfPackagesApi apiInstance = new VnfPackagesApi();
String packageContentId = "packageContentId_example"; // String | VNF Package Content ID
try {
    VnfPkgInfo result = apiInstance.getVnfPkgsIdContent(packageContentId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling VnfPackagesApi#getVnfPkgsIdContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **packageContentId** | **String**| VNF Package Content ID |

### Return type

[**VnfPkgInfo**](VnfPkgInfo.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, application/yaml

<a name="updateVnfPkg"></a>
# **updateVnfPkg**
> updateVnfPkg(vnfPkgId, body)

Modify an individual VNF package resource

Modify an individual VNF package resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.VnfPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


VnfPackagesApi apiInstance = new VnfPackagesApi();
String vnfPkgId = "vnfPkgId_example"; // String | VNF Package ID
VnfPkgInfoModifications body = new VnfPkgInfoModifications(); // VnfPkgInfoModifications | 
try {
    apiInstance.updateVnfPkg(vnfPkgId, body);
} catch (ApiException e) {
    System.err.println("Exception when calling VnfPackagesApi#updateVnfPkg");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **vnfPkgId** | **String**| VNF Package ID |
 **body** | [**VnfPkgInfoModifications**](VnfPkgInfoModifications.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/yaml
 - **Accept**: application/json

<a name="updateVnfPkgsIdContent"></a>
# **updateVnfPkgsIdContent**
> updateVnfPkgsIdContent(packageContentId, body)

Modify an individual VNF package resource

Modify an individual VNF package resource

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.VnfPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


VnfPackagesApi apiInstance = new VnfPackagesApi();
String packageContentId = "packageContentId_example"; // String | VNF Package Content ID
VnfPkgInfoModifications body = new VnfPkgInfoModifications(); // VnfPkgInfoModifications | 
try {
    apiInstance.updateVnfPkgsIdContent(packageContentId, body);
} catch (ApiException e) {
    System.err.println("Exception when calling VnfPackagesApi#updateVnfPkgsIdContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **packageContentId** | **String**| VNF Package Content ID |
 **body** | [**VnfPkgInfoModifications**](VnfPkgInfoModifications.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json, application/yaml
 - **Accept**: application/json

<a name="uploadVnfPkgContent"></a>
# **uploadVnfPkgContent**
> uploadVnfPkgContent(vnfPkgId, body)

Upload a VNF package by providing the content of the VNF package

Upload a VNF package by providing the content of the VNF package

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.VnfPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


VnfPackagesApi apiInstance = new VnfPackagesApi();
String vnfPkgId = "vnfPkgId_example"; // String | VNF Package ID
Object body = null; // Object | 
try {
    apiInstance.uploadVnfPkgContent(vnfPkgId, body);
} catch (ApiException e) {
    System.err.println("Exception when calling VnfPackagesApi#uploadVnfPkgContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **vnfPkgId** | **String**| VNF Package ID |
 **body** | [**Object**](Object.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/zip
 - **Accept**: application/json

<a name="uploadVnfPkgsContent"></a>
# **uploadVnfPkgsContent**
> ObjectId uploadVnfPkgsContent(body)

Upload a VNF package by providing the content of the VNF package

Upload a VNF package by providing the content of the VNF package

### Example
```java
// Import classes:
//import it.nextworks.osm.ApiClient;
//import it.nextworks.osm.ApiException;
//import it.nextworks.osm.Configuration;
//import it.nextworks.osm.auth.*;
//import it.nextworks.osm.openapi.VnfPackagesApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();


VnfPackagesApi apiInstance = new VnfPackagesApi();
Object body = null; // Object | 
try {
    ObjectId result = apiInstance.uploadVnfPkgsContent(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling VnfPackagesApi#uploadVnfPkgsContent");
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

