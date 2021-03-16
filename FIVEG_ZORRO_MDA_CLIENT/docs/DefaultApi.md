# DefaultApi

All URIs are relative to */*

Method | HTTP request | Description
------------- | ------------- | -------------
[**deleteConfigIdSettingsConfigIdDelete**](DefaultApi.md#deleteConfigIdSettingsConfigIdDelete) | **DELETE** /settings/{config_id} | Delete Config Id
[**disableConfigIdSettingsConfigIdDisablePut**](DefaultApi.md#disableConfigIdSettingsConfigIdDisablePut) | **PUT** /settings/{config_id}/disable | Disable Config Id
[**enableConfigIdSettingsConfigIdEnablePut**](DefaultApi.md#enableConfigIdSettingsConfigIdEnablePut) | **PUT** /settings/{config_id}/enable | Enable Config Id
[**getAllConfigsSettingsGet**](DefaultApi.md#getAllConfigsSettingsGet) | **GET** /settings | Get All Configs
[**getConfigIdSettingsConfigIdGet**](DefaultApi.md#getConfigIdSettingsConfigIdGet) | **GET** /settings/{config_id} | Get Config Id
[**setParamSettingsPost**](DefaultApi.md#setParamSettingsPost) | **POST** /settings | Set Param
[**updateConfigIdSettingsConfigIdPut**](DefaultApi.md#updateConfigIdSettingsConfigIdPut) | **PUT** /settings/{config_id} | Update Config Id

<a name="deleteConfigIdSettingsConfigIdDelete"></a>
# **deleteConfigIdSettingsConfigIdDelete**
> deleteConfigIdSettingsConfigIdDelete(configId)

Delete Config Id

### Example
```java
// Import classes:
//import ApiException;
//import DefaultApi;


DefaultApi apiInstance = new DefaultApi();
Object configId = null; // Object | 
try {
    apiInstance.deleteConfigIdSettingsConfigIdDelete(configId);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#deleteConfigIdSettingsConfigIdDelete");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **configId** | [**Object**](.md)|  |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="disableConfigIdSettingsConfigIdDisablePut"></a>
# **disableConfigIdSettingsConfigIdDisablePut**
> ResponseConfigModel disableConfigIdSettingsConfigIdDisablePut(configId)

Disable Config Id

### Example
```java
// Import classes:
//import ApiException;
//import DefaultApi;


DefaultApi apiInstance = new DefaultApi();
Object configId = null; // Object | 
try {
    ResponseConfigModel result = apiInstance.disableConfigIdSettingsConfigIdDisablePut(configId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#disableConfigIdSettingsConfigIdDisablePut");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **configId** | [**Object**](.md)|  |

### Return type

[**ResponseConfigModel**](ResponseConfigModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="enableConfigIdSettingsConfigIdEnablePut"></a>
# **enableConfigIdSettingsConfigIdEnablePut**
> ResponseConfigModel enableConfigIdSettingsConfigIdEnablePut(configId)

Enable Config Id

### Example
```java
// Import classes:
//import ApiException;
//import DefaultApi;


DefaultApi apiInstance = new DefaultApi();
Object configId = null; // Object | 
try {
    ResponseConfigModel result = apiInstance.enableConfigIdSettingsConfigIdEnablePut(configId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#enableConfigIdSettingsConfigIdEnablePut");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **configId** | [**Object**](.md)|  |

### Return type

[**ResponseConfigModel**](ResponseConfigModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getAllConfigsSettingsGet"></a>
# **getAllConfigsSettingsGet**
> List&lt;ResponseConfigModel&gt; getAllConfigsSettingsGet()

Get All Configs

### Example
```java
// Import classes:
//import ApiException;
//import DefaultApi;


DefaultApi apiInstance = new DefaultApi();
try {
    List<ResponseConfigModel> result = apiInstance.getAllConfigsSettingsGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#getAllConfigsSettingsGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;ResponseConfigModel&gt;**](ResponseConfigModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="getConfigIdSettingsConfigIdGet"></a>
# **getConfigIdSettingsConfigIdGet**
> ResponseConfigModel getConfigIdSettingsConfigIdGet(configId)

Get Config Id

### Example
```java
// Import classes:
//import ApiException;
//import DefaultApi;


DefaultApi apiInstance = new DefaultApi();
Object configId = null; // Object | 
try {
    ResponseConfigModel result = apiInstance.getConfigIdSettingsConfigIdGet(configId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#getConfigIdSettingsConfigIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **configId** | [**Object**](.md)|  |

### Return type

[**ResponseConfigModel**](ResponseConfigModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="setParamSettingsPost"></a>
# **setParamSettingsPost**
> ResponseConfigModel setParamSettingsPost(body)

Set Param

### Example
```java
// Import classes:
//import ApiException;
//import DefaultApi;


DefaultApi apiInstance = new DefaultApi();
ConfigModel body = new ConfigModel(); // ConfigModel | 
try {
    ResponseConfigModel result = apiInstance.setParamSettingsPost(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#setParamSettingsPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**ConfigModel**](ConfigModel.md)|  |

### Return type

[**ResponseConfigModel**](ResponseConfigModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateConfigIdSettingsConfigIdPut"></a>
# **updateConfigIdSettingsConfigIdPut**
> ResponseConfigModel updateConfigIdSettingsConfigIdPut(body, configId)

Update Config Id

### Example
```java
// Import classes:
//import ApiException;
//import DefaultApi;


DefaultApi apiInstance = new DefaultApi();
UpdateConfigModel body = new UpdateConfigModel(); // UpdateConfigModel | 
Object configId = null; // Object | 
try {
    ResponseConfigModel result = apiInstance.updateConfigIdSettingsConfigIdPut(body, configId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#updateConfigIdSettingsConfigIdPut");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**UpdateConfigModel**](UpdateConfigModel.md)|  |
 **configId** | [**Object**](.md)|  |

### Return type

[**ResponseConfigModel**](ResponseConfigModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

