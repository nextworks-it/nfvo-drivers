# DefaultApi

All URIs are relative to *http://localhost/nslcm/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiVersionsGet**](DefaultApi.md#apiVersionsGet) | **GET** /api-versions | Retrieve API version information
[**nsInstancesGet**](DefaultApi.md#nsInstancesGet) | **GET** /ns_instances | Query multiple NS instances.
[**nsInstancesNsInstanceIdDelete**](DefaultApi.md#nsInstancesNsInstanceIdDelete) | **DELETE** /ns_instances/{nsInstanceId} | Delete NS instance resource.
[**nsInstancesNsInstanceIdGet**](DefaultApi.md#nsInstancesNsInstanceIdGet) | **GET** /ns_instances/{nsInstanceId} | Read an individual NS instance resource.
[**nsInstancesNsInstanceIdHealPost**](DefaultApi.md#nsInstancesNsInstanceIdHealPost) | **POST** /ns_instances/{nsInstanceId}/heal | Heal a NS instance.
[**nsInstancesNsInstanceIdInstantiatePost**](DefaultApi.md#nsInstancesNsInstanceIdInstantiatePost) | **POST** /ns_instances/{nsInstanceId}/instantiate | Instantiate a NS.
[**nsInstancesNsInstanceIdScalePost**](DefaultApi.md#nsInstancesNsInstanceIdScalePost) | **POST** /ns_instances/{nsInstanceId}/scale | Scale a NS instance.
[**nsInstancesNsInstanceIdTerminatePost**](DefaultApi.md#nsInstancesNsInstanceIdTerminatePost) | **POST** /ns_instances/{nsInstanceId}/terminate | Terminate a NS instance.
[**nsInstancesNsInstanceIdUpdatePost**](DefaultApi.md#nsInstancesNsInstanceIdUpdatePost) | **POST** /ns_instances/{nsInstanceId}/update | Updates a NS instance.
[**nsInstancesPost**](DefaultApi.md#nsInstancesPost) | **POST** /ns_instances | Create a NS instance resource.
[**nsLcmOpOccsGet**](DefaultApi.md#nsLcmOpOccsGet) | **GET** /ns_lcm_op_occs | Query multiple NS LCM operation occurrences.
[**nsLcmOpOccsNsLcmOpOccIdContinuePost**](DefaultApi.md#nsLcmOpOccsNsLcmOpOccIdContinuePost) | **POST** /ns_lcm_op_occs/{nsLcmOpOccId}/continue | Continue a NS lifecycle management operation occurrence.
[**nsLcmOpOccsNsLcmOpOccIdGet**](DefaultApi.md#nsLcmOpOccsNsLcmOpOccIdGet) | **GET** /ns_lcm_op_occs/{nsLcmOpOccId} | Read an individual NS LCM operation occurrence resource.
[**nsLcmOpOccsNsLcmOpOccIdRetryPost**](DefaultApi.md#nsLcmOpOccsNsLcmOpOccIdRetryPost) | **POST** /ns_lcm_op_occs/{nsLcmOpOccId}/retry | Retry a NS lifecycle management operation occurrence.
[**nsLcmOpOccsNsLcmOpOccIdRollbackPost**](DefaultApi.md#nsLcmOpOccsNsLcmOpOccIdRollbackPost) | **POST** /ns_lcm_op_occs/{nsLcmOpOccId}/rollback | Rollback a NS lifecycle management operation occurrence.
[**nslcmV1NsLcmOpOccsNsLcmOpOccIdCancelPost**](DefaultApi.md#nslcmV1NsLcmOpOccsNsLcmOpOccIdCancelPost) | **POST** /nslcm/v1/ns_lcm_op_occs/{nsLcmOpOccId}/cancel | Cancel a NS lifecycle management operation occurrence.
[**nslcmV1NsLcmOpOccsNsLcmOpOccIdFailPost**](DefaultApi.md#nslcmV1NsLcmOpOccsNsLcmOpOccIdFailPost) | **POST** /nslcm/v1/ns_lcm_op_occs/{nsLcmOpOccId}/fail | Mark a NS lifecycle management operation occurrence as failed.
[**subscriptionsGet**](DefaultApi.md#subscriptionsGet) | **GET** /subscriptions | Query multiple subscriptions.
[**subscriptionsPost**](DefaultApi.md#subscriptionsPost) | **POST** /subscriptions | Subscribe to NS lifecycle change notifications.
[**subscriptionsSubscriptionIdDelete**](DefaultApi.md#subscriptionsSubscriptionIdDelete) | **DELETE** /subscriptions/{subscriptionId} | Terminate a subscription.
[**subscriptionsSubscriptionIdGet**](DefaultApi.md#subscriptionsSubscriptionIdGet) | **GET** /subscriptions/{subscriptionId} | Read an individual subscription resource.


<a name="apiVersionsGet"></a>
# **apiVersionsGet**
> ApiVersionInformation apiVersionsGet(version)

Retrieve API version information

The GET method reads API version information. This method shall follow the provisions specified in table 4.6.3.3.3.2-1 for request and response data structures, and response codes. URI query parameters are not supported. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
try {
    ApiVersionInformation result = apiInstance.apiVersionsGet(version);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#apiVersionsGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **String**| Version of the API requested to use when responding to this request.  | [optional]

### Return type

[**ApiVersionInformation**](ApiVersionInformation.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="nsInstancesGet"></a>
# **nsInstancesGet**
> List&lt;InlineResponse200&gt; nsInstancesGet(version, accept, authorization, filter, allFields, fields, excludeFields, excludeDefault, nextpageOpaqueMarker)

Query multiple NS instances.

Query NS Instances. The GET method queries information about multiple NS instances. This method shall support the URI query parameters, request and response data structures, and response codes, as specified in the Tables 6.4.2.3.2-1 and 6.4.2.3.2-2. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
String accept = "accept_example"; // String | Content-Types that are acceptable for the response. Reference: IETF RFC 7231 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235. 
String filter = "filter_example"; // String | Attribute-based filtering expression according to clause 4.3.2. The NFVO shall support receiving this parameter as part of the URI query string. The OSS/BSS may supply this parameter. All attribute names that appear in the NsInstance and in data types referenced from it shall be supported by the NFVO in the filter expression. 
String allFields = "allFields_example"; // String | Include all complex attributes in the response. See clause 4.3.3 for details. The NFVO shall support this parameter. 
String fields = "fields_example"; // String | \"Complex attributes to be included into the response. See clause 4.3.3 for details. The NFVO should support this parameter.\" 
String excludeFields = "excludeFields_example"; // String | \"Complex attributes to be excluded from the response. See clause 4.3.3 for details. The NFVO should support this parameter.\" 
String excludeDefault = "excludeDefault_example"; // String | \"Indicates to exclude the following complex attributes from the response. See clause 4.3.3 for details. The NFVO shall support this parameter. The following attributes shall be excluded from the NsInstance structure in the response body if this parameter is provided, or none of the parameters \"all_fields,\" \"fields\", \"exclude_fields\", \"exclude_default\" are provided: - vnfInstances - pnfInfo - virtualLinkInfo - vnffgInfo - sapInfo - nsScaleStatus - additionalAffinityOrAntiAffinityRules\" 
String nextpageOpaqueMarker = "nextpageOpaqueMarker_example"; // String | Marker to obtain the next page of a paged response. Shall be supported by the NFVO if the NFVO supports alternative 2 (paging) according to clause 4.7.2.1 for this resource. 
try {
    List<InlineResponse200> result = apiInstance.nsInstancesGet(version, accept, authorization, filter, allFields, fields, excludeFields, excludeDefault, nextpageOpaqueMarker);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#nsInstancesGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **accept** | **String**| Content-Types that are acceptable for the response. Reference: IETF RFC 7231  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235.  | [optional]
 **filter** | **String**| Attribute-based filtering expression according to clause 4.3.2. The NFVO shall support receiving this parameter as part of the URI query string. The OSS/BSS may supply this parameter. All attribute names that appear in the NsInstance and in data types referenced from it shall be supported by the NFVO in the filter expression.  | [optional]
 **allFields** | **String**| Include all complex attributes in the response. See clause 4.3.3 for details. The NFVO shall support this parameter.  | [optional]
 **fields** | **String**| \&quot;Complex attributes to be included into the response. See clause 4.3.3 for details. The NFVO should support this parameter.\&quot;  | [optional]
 **excludeFields** | **String**| \&quot;Complex attributes to be excluded from the response. See clause 4.3.3 for details. The NFVO should support this parameter.\&quot;  | [optional]
 **excludeDefault** | **String**| \&quot;Indicates to exclude the following complex attributes from the response. See clause 4.3.3 for details. The NFVO shall support this parameter. The following attributes shall be excluded from the NsInstance structure in the response body if this parameter is provided, or none of the parameters \&quot;all_fields,\&quot; \&quot;fields\&quot;, \&quot;exclude_fields\&quot;, \&quot;exclude_default\&quot; are provided: - vnfInstances - pnfInfo - virtualLinkInfo - vnffgInfo - sapInfo - nsScaleStatus - additionalAffinityOrAntiAffinityRules\&quot;  | [optional]
 **nextpageOpaqueMarker** | **String**| Marker to obtain the next page of a paged response. Shall be supported by the NFVO if the NFVO supports alternative 2 (paging) according to clause 4.7.2.1 for this resource.  | [optional]

### Return type

[**List&lt;InlineResponse200&gt;**](InlineResponse200.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="nsInstancesNsInstanceIdDelete"></a>
# **nsInstancesNsInstanceIdDelete**
> nsInstancesNsInstanceIdDelete(nsInstanceId, version, authorization)

Delete NS instance resource.

Delete NS Identifier This method deletes an individual NS instance resource. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String nsInstanceId = "nsInstanceId_example"; // String | Identifier of the NS instance. 
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235. 
try {
    apiInstance.nsInstancesNsInstanceIdDelete(nsInstanceId, version, authorization);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#nsInstancesNsInstanceIdDelete");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsInstanceId** | **String**| Identifier of the NS instance.  |
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235.  | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="nsInstancesNsInstanceIdGet"></a>
# **nsInstancesNsInstanceIdGet**
> NsInstance nsInstancesNsInstanceIdGet(nsInstanceId, version, accept, contentType, authorization)

Read an individual NS instance resource.

The GET method retrieves information about a NS instance by reading an individual NS instance resource. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String nsInstanceId = "nsInstanceId_example"; // String | Identifier of the NS instance. 
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
String accept = "accept_example"; // String | Content-Types that are acceptable for the response. Reference: IETF RFC 7231 
String contentType = "contentType_example"; // String | The MIME type of the body of the request. Reference: IETF RFC 7231 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235. 
try {
    NsInstance result = apiInstance.nsInstancesNsInstanceIdGet(nsInstanceId, version, accept, contentType, authorization);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#nsInstancesNsInstanceIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsInstanceId** | **String**| Identifier of the NS instance.  |
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **accept** | **String**| Content-Types that are acceptable for the response. Reference: IETF RFC 7231  |
 **contentType** | **String**| The MIME type of the body of the request. Reference: IETF RFC 7231  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235.  | [optional]

### Return type

[**NsInstance**](NsInstance.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="nsInstancesNsInstanceIdHealPost"></a>
# **nsInstancesNsInstanceIdHealPost**
> NsInstance2 nsInstancesNsInstanceIdHealPost(nsInstanceId, accept, contentType, version, body, authorization)

Heal a NS instance.

The POST method requests to heal a NS instance resource. This method shall follow the provisions specified in the Tables 6.4.7.3.1-1 and 6.4.7.3.1-2 for URI query parameters, request and response data structures, and response codes. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String nsInstanceId = "nsInstanceId_example"; // String | Identifier of the NS instance to be healed. 
String accept = "accept_example"; // String | Content-Types that are acceptable for the response. Reference: IETF RFC 7231 
String contentType = "contentType_example"; // String | The MIME type of the body of the request. Reference: IETF RFC 7231 
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
HealNsRequest body = new HealNsRequest(); // HealNsRequest | Parameters for the heal NS operation, as defined in clause 6.5.2.12. 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235 
try {
    NsInstance2 result = apiInstance.nsInstancesNsInstanceIdHealPost(nsInstanceId, accept, contentType, version, body, authorization);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#nsInstancesNsInstanceIdHealPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsInstanceId** | **String**| Identifier of the NS instance to be healed.  |
 **accept** | **String**| Content-Types that are acceptable for the response. Reference: IETF RFC 7231  |
 **contentType** | **String**| The MIME type of the body of the request. Reference: IETF RFC 7231  |
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **body** | [**HealNsRequest**](HealNsRequest.md)| Parameters for the heal NS operation, as defined in clause 6.5.2.12.  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235  | [optional]

### Return type

[**NsInstance2**](NsInstance2.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="nsInstancesNsInstanceIdInstantiatePost"></a>
# **nsInstancesNsInstanceIdInstantiatePost**
> NsInstance2 nsInstancesNsInstanceIdInstantiatePost(nsInstanceId, accept, contentType, version, body, authorization)

Instantiate a NS.

The POST method requests to instantiate a NS instance resource. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String nsInstanceId = "nsInstanceId_example"; // String | Identifier of the NS instance to be instantiated. 
String accept = "accept_example"; // String | Content-Types that are acceptable for the response. Reference: IETF RFC 7231 
String contentType = "contentType_example"; // String | The MIME type of the body of the request. Reference: IETF RFC 7231 
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
InstantiateNsRequest body = new InstantiateNsRequest(); // InstantiateNsRequest | Parameters for the instantiate NS operation, as defined in clause 6.5.2.10. 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235 
try {
    NsInstance2 result = apiInstance.nsInstancesNsInstanceIdInstantiatePost(nsInstanceId, accept, contentType, version, body, authorization);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#nsInstancesNsInstanceIdInstantiatePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsInstanceId** | **String**| Identifier of the NS instance to be instantiated.  |
 **accept** | **String**| Content-Types that are acceptable for the response. Reference: IETF RFC 7231  |
 **contentType** | **String**| The MIME type of the body of the request. Reference: IETF RFC 7231  |
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **body** | [**InstantiateNsRequest**](InstantiateNsRequest.md)| Parameters for the instantiate NS operation, as defined in clause 6.5.2.10.  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235  | [optional]

### Return type

[**NsInstance2**](NsInstance2.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="nsInstancesNsInstanceIdScalePost"></a>
# **nsInstancesNsInstanceIdScalePost**
> NsInstance2 nsInstancesNsInstanceIdScalePost(nsInstanceId, accept, contentType, version, body, authorization)

Scale a NS instance.

The POST method requests to scale a NS instance resource. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String nsInstanceId = "nsInstanceId_example"; // String | Identifier of the NS instance to be scaled. 
String accept = "accept_example"; // String | Content-Types that are acceptable for the response. Reference: IETF RFC 7231 
String contentType = "contentType_example"; // String | The MIME type of the body of the request. Reference: IETF RFC 7231 
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
ScaleNsRequest body = new ScaleNsRequest(); // ScaleNsRequest | Parameters for the scale NS operation, as defined in clause 6.5.2.13. 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235 
try {
    NsInstance2 result = apiInstance.nsInstancesNsInstanceIdScalePost(nsInstanceId, accept, contentType, version, body, authorization);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#nsInstancesNsInstanceIdScalePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsInstanceId** | **String**| Identifier of the NS instance to be scaled.  |
 **accept** | **String**| Content-Types that are acceptable for the response. Reference: IETF RFC 7231  |
 **contentType** | **String**| The MIME type of the body of the request. Reference: IETF RFC 7231  |
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **body** | [**ScaleNsRequest**](ScaleNsRequest.md)| Parameters for the scale NS operation, as defined in clause 6.5.2.13.  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235  | [optional]

### Return type

[**NsInstance2**](NsInstance2.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="nsInstancesNsInstanceIdTerminatePost"></a>
# **nsInstancesNsInstanceIdTerminatePost**
> NsInstance2 nsInstancesNsInstanceIdTerminatePost(nsInstanceId, accept, contentType, version, body, authorization)

Terminate a NS instance.

Terminate NS task. The POST method terminates a NS instance. This method can only be used with a NS instance in the INSTANTIATED state. Terminating a NS instance does not delete the NS instance identifier, but rather transitions the NS into the NOT_INSTANTIATED state. This method shall support the URI query parameters, request and response data structures, and response codes, as specified in the Tables 6.4.8.3.1-1 and 6.8.8.3.1-2. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String nsInstanceId = "nsInstanceId_example"; // String | The identifier of the NS instance to be terminated. 
String accept = "accept_example"; // String | Content-Types that are acceptable for the response. Reference: IETF RFC 7231 
String contentType = "contentType_example"; // String | The MIME type of the body of the request. Reference: IETF RFC 7231 
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
TerminateNsRequest body = new TerminateNsRequest(); // TerminateNsRequest | The terminate NS request parameters, as defined in  clause 6.5.2.14. 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235 
try {
    NsInstance2 result = apiInstance.nsInstancesNsInstanceIdTerminatePost(nsInstanceId, accept, contentType, version, body, authorization);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#nsInstancesNsInstanceIdTerminatePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsInstanceId** | **String**| The identifier of the NS instance to be terminated.  |
 **accept** | **String**| Content-Types that are acceptable for the response. Reference: IETF RFC 7231  |
 **contentType** | **String**| The MIME type of the body of the request. Reference: IETF RFC 7231  |
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **body** | [**TerminateNsRequest**](TerminateNsRequest.md)| The terminate NS request parameters, as defined in  clause 6.5.2.14.  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235  | [optional]

### Return type

[**NsInstance2**](NsInstance2.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="nsInstancesNsInstanceIdUpdatePost"></a>
# **nsInstancesNsInstanceIdUpdatePost**
> NsInstance2 nsInstancesNsInstanceIdUpdatePost(nsInstanceId, accept, contentType, version, body, authorization)

Updates a NS instance.

Scale NS instance. The POST method requests to scale a NS instance resource. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String nsInstanceId = "nsInstanceId_example"; // String | Identifier of the NS instance to be updated. 
String accept = "accept_example"; // String | Content-Types that are acceptable for the response. Reference: IETF RFC 7231 
String contentType = "contentType_example"; // String | The MIME type of the body of the request. Reference: IETF RFC 7231 
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
UpdateNsRequest body = new UpdateNsRequest(); // UpdateNsRequest | Parameters for the update NS operation, as defined in clause 6.5.2.11. 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235 
try {
    NsInstance2 result = apiInstance.nsInstancesNsInstanceIdUpdatePost(nsInstanceId, accept, contentType, version, body, authorization);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#nsInstancesNsInstanceIdUpdatePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsInstanceId** | **String**| Identifier of the NS instance to be updated.  |
 **accept** | **String**| Content-Types that are acceptable for the response. Reference: IETF RFC 7231  |
 **contentType** | **String**| The MIME type of the body of the request. Reference: IETF RFC 7231  |
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **body** | [**UpdateNsRequest**](UpdateNsRequest.md)| Parameters for the update NS operation, as defined in clause 6.5.2.11.  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235  | [optional]

### Return type

[**NsInstance2**](NsInstance2.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="nsInstancesPost"></a>
# **nsInstancesPost**
> NsInstance nsInstancesPost(version, accept, contentType, body, authorization)

Create a NS instance resource.

The POST method creates a new NS instance resource. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
String accept = "accept_example"; // String | Content-Types that are acceptable for the response. Reference: IETF RFC 7231 
String contentType = "contentType_example"; // String | The MIME type of the body of the request. Reference: IETF RFC 7231 
CreateNsRequest body = new CreateNsRequest(); // CreateNsRequest | The NS creation parameters, as defined in clause 6.5.2.7. 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235. 
try {
    NsInstance result = apiInstance.nsInstancesPost(version, accept, contentType, body, authorization);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#nsInstancesPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **accept** | **String**| Content-Types that are acceptable for the response. Reference: IETF RFC 7231  |
 **contentType** | **String**| The MIME type of the body of the request. Reference: IETF RFC 7231  |
 **body** | [**CreateNsRequest**](CreateNsRequest.md)| The NS creation parameters, as defined in clause 6.5.2.7.  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235.  | [optional]

### Return type

[**NsInstance**](NsInstance.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="nsLcmOpOccsGet"></a>
# **nsLcmOpOccsGet**
> List&lt;InlineResponse2001&gt; nsLcmOpOccsGet(accept, version, filter, fields, excludeFields, excludeDefault, nextpageOpaqueMarker, authorization)

Query multiple NS LCM operation occurrences.

Get Operation Status. The client can use this method to query status information about multiple NS lifecycle management operation occurrences. This method shall follow the provisions specified in the Tables 6.4.9.3.2-1 and 6.4.9.3.2-2 for URI query parameters, request and response data structures, and response codes. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String accept = "accept_example"; // String | Content-Types that are acceptable for the response. Reference: IETF RFC 7231 
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
String filter = "filter_example"; // String | Attribute-based filtering expression according to clause 4.3.2. The NFVO shall support receiving this parameter as part of the URI query string. The OSS/BSS may supply this parameter. All attribute names that appear in the NsLcmOpOcc and in data types referenced from it shall be supported by the NFVO in the filter expression. 
String fields = "fields_example"; // String | Complex attributes to be included into the response. See clause 4.3.3 for details. The NFVO should support this parameter. 
String excludeFields = "excludeFields_example"; // String | Complex attributes to be excluded from the response. See clause 4.3.3 for details. The NFVO should support this parameter. 
String excludeDefault = "excludeDefault_example"; // String | Indicates to exclude the following complex attributes from the response. See clause 4.3.3 for details. The NFVO shall support this parameter. The following attributes shall be excluded from the NsLcmOpOcc structure in the response body if this parameter is provided: - operationParams - changedVnfInfo - error - resourceChanges 
String nextpageOpaqueMarker = "nextpageOpaqueMarker_example"; // String | Marker to obtain the next page of a paged response. Shall be supported by the NFVO if the NFVO supports alternative 2 (paging) according to clause 4.7.2.1 for this resource. 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235 
try {
    List<InlineResponse2001> result = apiInstance.nsLcmOpOccsGet(accept, version, filter, fields, excludeFields, excludeDefault, nextpageOpaqueMarker, authorization);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#nsLcmOpOccsGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **accept** | **String**| Content-Types that are acceptable for the response. Reference: IETF RFC 7231  |
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **filter** | **String**| Attribute-based filtering expression according to clause 4.3.2. The NFVO shall support receiving this parameter as part of the URI query string. The OSS/BSS may supply this parameter. All attribute names that appear in the NsLcmOpOcc and in data types referenced from it shall be supported by the NFVO in the filter expression.  | [optional]
 **fields** | **String**| Complex attributes to be included into the response. See clause 4.3.3 for details. The NFVO should support this parameter.  | [optional]
 **excludeFields** | **String**| Complex attributes to be excluded from the response. See clause 4.3.3 for details. The NFVO should support this parameter.  | [optional]
 **excludeDefault** | **String**| Indicates to exclude the following complex attributes from the response. See clause 4.3.3 for details. The NFVO shall support this parameter. The following attributes shall be excluded from the NsLcmOpOcc structure in the response body if this parameter is provided: - operationParams - changedVnfInfo - error - resourceChanges  | [optional]
 **nextpageOpaqueMarker** | **String**| Marker to obtain the next page of a paged response. Shall be supported by the NFVO if the NFVO supports alternative 2 (paging) according to clause 4.7.2.1 for this resource.  | [optional]
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235  | [optional]

### Return type

[**List&lt;InlineResponse2001&gt;**](InlineResponse2001.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="nsLcmOpOccsNsLcmOpOccIdContinuePost"></a>
# **nsLcmOpOccsNsLcmOpOccIdContinuePost**
> nsLcmOpOccsNsLcmOpOccIdContinuePost(nsLcmOpOccId, version, authorization)

Continue a NS lifecycle management operation occurrence.

The POST method initiates continuing an NS lifecycle operation if that operation has experienced a temporary failure, i.e. the related \&quot;NS LCM operation occurrence\&quot; is in \&quot;FAILED_TEMP\&quot; state. This method shall follow the provisions specified in the Tables 6.4.13.3.1-1 and 6.4.13.3.1-2 for URI query parameters, request and response data structures, and response codes. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String nsLcmOpOccId = "nsLcmOpOccId_example"; // String | Identifier of a NS lifecycle management operation occurrence to be continued. 
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235 
try {
    apiInstance.nsLcmOpOccsNsLcmOpOccIdContinuePost(nsLcmOpOccId, version, authorization);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#nsLcmOpOccsNsLcmOpOccIdContinuePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsLcmOpOccId** | **String**| Identifier of a NS lifecycle management operation occurrence to be continued.  |
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235  | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="nsLcmOpOccsNsLcmOpOccIdGet"></a>
# **nsLcmOpOccsNsLcmOpOccIdGet**
> NsLcmOpOcc nsLcmOpOccsNsLcmOpOccIdGet(nsLcmOpOccId, accept, contentType, version, authorization)

Read an individual NS LCM operation occurrence resource.

The client can use this method to retrieve status information about a NS lifecycle management operation occurrence by reading an individual \&quot;NS LCM operation occurrence\&quot; resource. This method shall follow the provisions specified in the Tables 6.4.10.3.2-1 and 6.4.10.3.2-2 for URI query parameters, request and response data structures, and response codes. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String nsLcmOpOccId = "nsLcmOpOccId_example"; // String | Identifier of a NS lifecycle management operation occurrence. 
String accept = "accept_example"; // String | Content-Types that are acceptable for the response. Reference: IETF RFC 7231 
String contentType = "contentType_example"; // String | The MIME type of the body of the request. Reference: IETF RFC 7231 
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235 
try {
    NsLcmOpOcc result = apiInstance.nsLcmOpOccsNsLcmOpOccIdGet(nsLcmOpOccId, accept, contentType, version, authorization);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#nsLcmOpOccsNsLcmOpOccIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsLcmOpOccId** | **String**| Identifier of a NS lifecycle management operation occurrence.  |
 **accept** | **String**| Content-Types that are acceptable for the response. Reference: IETF RFC 7231  |
 **contentType** | **String**| The MIME type of the body of the request. Reference: IETF RFC 7231  |
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235  | [optional]

### Return type

[**NsLcmOpOcc**](NsLcmOpOcc.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="nsLcmOpOccsNsLcmOpOccIdRetryPost"></a>
# **nsLcmOpOccsNsLcmOpOccIdRetryPost**
> nsLcmOpOccsNsLcmOpOccIdRetryPost(nsLcmOpOccId, version, authorization)

Retry a NS lifecycle management operation occurrence.

The POST method initiates retrying a NS lifecycle management operation if that operation has experienced a temporary failure, i.e. the related \&quot;NS LCM operation occurrence\&quot; is in \&quot;FAILED_TEMP\&quot; state. This method shall follow the provisions specified in the Tables 6.4.11.3.1-1 and 6.4.11.3.1-2 for URI query parameters, request and response data structures, and response codes. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String nsLcmOpOccId = "nsLcmOpOccId_example"; // String | Identifier of a NS lifecycle management operation occurrence to be retried. This identifier can be retrieved from the resource referenced by the \"Location\" HTTP header in the response to a POST request triggering a NS LCM operation. It can also be retrieved from the \"nsLcmOpOccId\" attribute in the NsLcmOperationOccurrenceNotification. 
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235 
try {
    apiInstance.nsLcmOpOccsNsLcmOpOccIdRetryPost(nsLcmOpOccId, version, authorization);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#nsLcmOpOccsNsLcmOpOccIdRetryPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsLcmOpOccId** | **String**| Identifier of a NS lifecycle management operation occurrence to be retried. This identifier can be retrieved from the resource referenced by the \&quot;Location\&quot; HTTP header in the response to a POST request triggering a NS LCM operation. It can also be retrieved from the \&quot;nsLcmOpOccId\&quot; attribute in the NsLcmOperationOccurrenceNotification.  |
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235  | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="nsLcmOpOccsNsLcmOpOccIdRollbackPost"></a>
# **nsLcmOpOccsNsLcmOpOccIdRollbackPost**
> nsLcmOpOccsNsLcmOpOccIdRollbackPost(nsLcmOpOccId, version, authorization)

Rollback a NS lifecycle management operation occurrence.

The POST method initiates rolling back a NS lifecycle operation if that operation has experienced a temporary failure, i.e. the related \&quot;NS LCM operation occurrence\&quot; is in \&quot;FAILED_TEMP\&quot; state. This method shall follow the provisions specified in the Tables 6.4.12.3.1-1 and 6.4.12.3.1-2 for URI query parameters, request and response data structures, and response codes. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String nsLcmOpOccId = "nsLcmOpOccId_example"; // String | Identifier of a NS lifecycle management operation occurrence to be rolled back. This identifier can be retrieved from the resource referenced by the \"Location\" HTTP header in the response to a POST request triggering a NS LCM operation. It can also be retrieved from the \"nsLcmOpOccId\" attribute in the NsLcmOperationOccurrenceNotification. 
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235 
try {
    apiInstance.nsLcmOpOccsNsLcmOpOccIdRollbackPost(nsLcmOpOccId, version, authorization);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#nsLcmOpOccsNsLcmOpOccIdRollbackPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsLcmOpOccId** | **String**| Identifier of a NS lifecycle management operation occurrence to be rolled back. This identifier can be retrieved from the resource referenced by the \&quot;Location\&quot; HTTP header in the response to a POST request triggering a NS LCM operation. It can also be retrieved from the \&quot;nsLcmOpOccId\&quot; attribute in the NsLcmOperationOccurrenceNotification.  |
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235  | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="nslcmV1NsLcmOpOccsNsLcmOpOccIdCancelPost"></a>
# **nslcmV1NsLcmOpOccsNsLcmOpOccIdCancelPost**
> nslcmV1NsLcmOpOccsNsLcmOpOccIdCancelPost(nsLcmOpOccId, accept, contentType, version, body, authorization)

Cancel a NS lifecycle management operation occurrence.

The POST method initiates canceling an ongoing NS lifecycle management operation while it is being executed or rolled back, i.e. the related \&quot;NS LCM operation occurrence\&quot; is either in \&quot;PROCESSING\&quot; or \&quot;ROLLING_BACK\&quot; state. This method shall follow the provisions specified in the Tables 6.4.15.3.1-1 and 6.4.15.3.1-2 for URI query parameters, request and response data structures, and response codes. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String nsLcmOpOccId = "nsLcmOpOccId_example"; // String | Identifier of a NS lifecycle management operation occurrence to be canceled. This identifier can be retrieved from the resource referenced by the \"Location\" HTTP header in the response to a POST request triggering a NS LCM operation. It can also be retrieved from the \"nsLcmOpOccId\" attribute in the NsLcmOperationOccurrenceNotification. 
String accept = "accept_example"; // String | Content-Types that are acceptable for the response. Reference: IETF RFC 7231 
String contentType = "contentType_example"; // String | The MIME type of the body of the request. Reference: IETF RFC 7231 
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
CancelMode body = new CancelMode(); // CancelMode | The POST request to this resource shall include a CancelMode structure in the payload body to choose between \"graceful\" and \"forceful\" cancellation. 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235 
try {
    apiInstance.nslcmV1NsLcmOpOccsNsLcmOpOccIdCancelPost(nsLcmOpOccId, accept, contentType, version, body, authorization);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#nslcmV1NsLcmOpOccsNsLcmOpOccIdCancelPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsLcmOpOccId** | **String**| Identifier of a NS lifecycle management operation occurrence to be canceled. This identifier can be retrieved from the resource referenced by the \&quot;Location\&quot; HTTP header in the response to a POST request triggering a NS LCM operation. It can also be retrieved from the \&quot;nsLcmOpOccId\&quot; attribute in the NsLcmOperationOccurrenceNotification.  |
 **accept** | **String**| Content-Types that are acceptable for the response. Reference: IETF RFC 7231  |
 **contentType** | **String**| The MIME type of the body of the request. Reference: IETF RFC 7231  |
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **body** | [**CancelMode**](CancelMode.md)| The POST request to this resource shall include a CancelMode structure in the payload body to choose between \&quot;graceful\&quot; and \&quot;forceful\&quot; cancellation.  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235  | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="nslcmV1NsLcmOpOccsNsLcmOpOccIdFailPost"></a>
# **nslcmV1NsLcmOpOccsNsLcmOpOccIdFailPost**
> NsLcmOpOcc nslcmV1NsLcmOpOccsNsLcmOpOccIdFailPost(nsLcmOpOccId, accept, version, authorization)

Mark a NS lifecycle management operation occurrence as failed.

The POST method marks a NS lifecycle management operation occurrence as \&quot;finally failed\&quot; if that operation occurrence is in \&quot;FAILED_TEMP\&quot; state. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String nsLcmOpOccId = "nsLcmOpOccId_example"; // String | Identifier of a NS lifecycle management operation occurrence to be marked as \"failed\". This identifier can be retrieved from the resource referenced by he \"Location\" HTTP header in the response to a POST request triggering a NS LCM operation. It can also be retrieved from the \"nsLcmOpOccId\" attribute in the NsLcmOperationOccurrenceNotification. 
String accept = "accept_example"; // String | Content-Types that are acceptable for the response. Reference: IETF RFC 7231 
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235 
try {
    NsLcmOpOcc result = apiInstance.nslcmV1NsLcmOpOccsNsLcmOpOccIdFailPost(nsLcmOpOccId, accept, version, authorization);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#nslcmV1NsLcmOpOccsNsLcmOpOccIdFailPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nsLcmOpOccId** | **String**| Identifier of a NS lifecycle management operation occurrence to be marked as \&quot;failed\&quot;. This identifier can be retrieved from the resource referenced by he \&quot;Location\&quot; HTTP header in the response to a POST request triggering a NS LCM operation. It can also be retrieved from the \&quot;nsLcmOpOccId\&quot; attribute in the NsLcmOperationOccurrenceNotification.  |
 **accept** | **String**| Content-Types that are acceptable for the response. Reference: IETF RFC 7231  |
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235  | [optional]

### Return type

[**NsLcmOpOcc**](NsLcmOpOcc.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="subscriptionsGet"></a>
# **subscriptionsGet**
> List&lt;LccnSubscription&gt; subscriptionsGet(version, accept, authorization, filter, nextpageOpaqueMarker)

Query multiple subscriptions.

Query Subscription Information. The GET method queries the list of active subscriptions of the functional block that invokes the method. It can be used e.g. for resynchronization after error situations. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
String accept = "accept_example"; // String | Content-Types that are acceptable for the response. Reference: IETF RFC 7231 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235. 
String filter = "filter_example"; // String | Attribute-based filtering expression according to clause 4.3.2. The NFVO shall support receiving this parameter as part of the URI query string. The OSS/BSS may supply this parameter. All attribute names that appear in the LccnSubscription and in data types referenced from it shall be supported by the NFVO in the filter expression. 
String nextpageOpaqueMarker = "nextpageOpaqueMarker_example"; // String | Marker to obtain the next page of a paged response. Shall be supported by the NFVO if the NFVO supports alternative 2 (paging) according to clause 4.7.2.1 for this resource. 
try {
    List<LccnSubscription> result = apiInstance.subscriptionsGet(version, accept, authorization, filter, nextpageOpaqueMarker);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#subscriptionsGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **accept** | **String**| Content-Types that are acceptable for the response. Reference: IETF RFC 7231  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235.  | [optional]
 **filter** | **String**| Attribute-based filtering expression according to clause 4.3.2. The NFVO shall support receiving this parameter as part of the URI query string. The OSS/BSS may supply this parameter. All attribute names that appear in the LccnSubscription and in data types referenced from it shall be supported by the NFVO in the filter expression.  | [optional]
 **nextpageOpaqueMarker** | **String**| Marker to obtain the next page of a paged response. Shall be supported by the NFVO if the NFVO supports alternative 2 (paging) according to clause 4.7.2.1 for this resource.  | [optional]

### Return type

[**List&lt;LccnSubscription&gt;**](LccnSubscription.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="subscriptionsPost"></a>
# **subscriptionsPost**
> LccnSubscription subscriptionsPost(version, accept, contentType, body, authorization)

Subscribe to NS lifecycle change notifications.

The POST method creates a new subscription. This method shall support the URI query parameters, request and response data structures, and response codes, as specified in the Tables 6.4.16.3.1-1 and 6.4.16.3.1-2. Creation of two subscription resources with the same callbackURI and the same filter can result in performance degradation and will provide duplicates of notifications to the OSS, and might make sense only in very rare use cases. Consequently, the NFVO may either allow creating a subscription resource if another subscription resource with the same filter and callbackUri already exists (in which case it shall return the \&quot;201 Created\&quot; response code), or may decide to not create a duplicate subscription resource (in which case it shall return a \&quot;303 See Other\&quot; response code referencing the existing subscription resource with the same filter and callbackUri). 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
String accept = "accept_example"; // String | Content-Types that are acceptable for the response. Reference: IETF RFC 7231 
String contentType = "contentType_example"; // String | The MIME type of the body of the request. Reference: IETF RFC 7231 
LccnSubscriptionRequest body = new LccnSubscriptionRequest(); // LccnSubscriptionRequest | Details of the subscription to be created, as defined in clause 6.5.2.2. 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235. 
try {
    LccnSubscription result = apiInstance.subscriptionsPost(version, accept, contentType, body, authorization);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#subscriptionsPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **accept** | **String**| Content-Types that are acceptable for the response. Reference: IETF RFC 7231  |
 **contentType** | **String**| The MIME type of the body of the request. Reference: IETF RFC 7231  |
 **body** | [**LccnSubscriptionRequest**](LccnSubscriptionRequest.md)| Details of the subscription to be created, as defined in clause 6.5.2.2.  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235.  | [optional]

### Return type

[**LccnSubscription**](LccnSubscription.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="subscriptionsSubscriptionIdDelete"></a>
# **subscriptionsSubscriptionIdDelete**
> subscriptionsSubscriptionIdDelete(subscriptionId, version, authorization)

Terminate a subscription.

The DELETE method terminates an individual subscription. This method shall support the URI query parameters, request and response data structures, and response codes, as specified in the Tables 6.4.17.3.5-1 and 6.4.17.3.5-2. 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String subscriptionId = "subscriptionId_example"; // String | Identifier of this subscription. 
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235. 
try {
    apiInstance.subscriptionsSubscriptionIdDelete(subscriptionId, version, authorization);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#subscriptionsSubscriptionIdDelete");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **subscriptionId** | **String**| Identifier of this subscription.  |
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235.  | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="subscriptionsSubscriptionIdGet"></a>
# **subscriptionsSubscriptionIdGet**
> LccnSubscription subscriptionsSubscriptionIdGet(subscriptionId, version, accept, authorization)

Read an individual subscription resource.

The GET method retrieves information about a subscription by reading an individual subscription resource. This method shall support the URI query parameters, request and response data structures, and response codes, as specified in the Tables 6.4.17.3.2-1 and 6.4.17.3.2-2 

### Example
```java
// Import classes:
//import it.nextworks.openapi.ApiException;
//import it.nextworks.openapi.msno.DefaultApi;


DefaultApi apiInstance = new DefaultApi();
String subscriptionId = "subscriptionId_example"; // String | Identifier of this subscription. 
String version = "version_example"; // String | Version of the API requested to use when responding to this request. 
String accept = "accept_example"; // String | Content-Types that are acceptable for the response. Reference: IETF RFC 7231 
String authorization = "authorization_example"; // String | The authorization token for the request. Reference: IETF RFC 7235. 
try {
    LccnSubscription result = apiInstance.subscriptionsSubscriptionIdGet(subscriptionId, version, accept, authorization);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#subscriptionsSubscriptionIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **subscriptionId** | **String**| Identifier of this subscription.  |
 **version** | **String**| Version of the API requested to use when responding to this request.  |
 **accept** | **String**| Content-Types that are acceptable for the response. Reference: IETF RFC 7231  |
 **authorization** | **String**| The authorization token for the request. Reference: IETF RFC 7235.  | [optional]

### Return type

[**LccnSubscription**](LccnSubscription.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

