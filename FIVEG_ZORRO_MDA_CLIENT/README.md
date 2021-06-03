# mda-client

FastAPI
- API version: 0.1.0
  - Build date: 2021-06-03T17:13:21.498+02:00[Europe/Rome]

No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)


*Automatically generated by the [Swagger Codegen](https://github.com/swagger-api/swagger-codegen)*


## Requirements

Building the API client library requires:
1. Java 1.7+
2. Maven/Gradle

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn clean install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn clean deploy
```

Refer to the [OSSRH Guide](http://central.sonatype.org/pages/ossrh-guide.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
  <groupId>io.swagger</groupId>
  <artifactId>mda-client</artifactId>
  <version>1.4</version>
  <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "io.swagger:mda-client:1.4"
```

### Others

At first generate the JAR by executing:

```shell
mvn clean package
```

Then manually install the following JARs:

* `target/mda-client-1.4.jar`
* `target/lib/*.jar`

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java
import io.swagger.client.mda.*;
import io.swagger.client.mda.auth.*;
import io.swagger.client.mda.model.*;
import io.swagger.client.mda.api.DefaultApi;

import java.io.File;
import java.util.*;

public class DefaultApiExample {

    public static void main(String[] args) {
        
        DefaultApi apiInstance = new DefaultApi();
        Object configId = null; // Object | 
        try {
            apiInstance.deleteConfigIdSettingsConfigIdDelete(configId);
        } catch (ApiException e) {
            System.err.println("Exception when calling DefaultApi#deleteConfigIdSettingsConfigIdDelete");
            e.printStackTrace();
        }
    }
}
import io.swagger.client.mda.*;
import io.swagger.client.mda.auth.*;
import io.swagger.client.mda.model.*;
import io.swagger.client.mda.api.DefaultApi;

import java.io.File;
import java.util.*;

public class DefaultApiExample {

    public static void main(String[] args) {
        
        DefaultApi apiInstance = new DefaultApi();
        Object configId = null; // Object | 
        try {
            ResponseConfigModel result = apiInstance.disableConfigIdSettingsConfigIdDisablePut(configId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DefaultApi#disableConfigIdSettingsConfigIdDisablePut");
            e.printStackTrace();
        }
    }
}
import io.swagger.client.mda.*;
import io.swagger.client.mda.auth.*;
import io.swagger.client.mda.model.*;
import io.swagger.client.mda.api.DefaultApi;

import java.io.File;
import java.util.*;

public class DefaultApiExample {

    public static void main(String[] args) {
        
        DefaultApi apiInstance = new DefaultApi();
        Object configId = null; // Object | 
        try {
            ResponseConfigModel result = apiInstance.enableConfigIdSettingsConfigIdEnablePut(configId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DefaultApi#enableConfigIdSettingsConfigIdEnablePut");
            e.printStackTrace();
        }
    }
}
import io.swagger.client.mda.*;
import io.swagger.client.mda.auth.*;
import io.swagger.client.mda.model.*;
import io.swagger.client.mda.api.DefaultApi;

import java.io.File;
import java.util.*;

public class DefaultApiExample {

    public static void main(String[] args) {
        
        DefaultApi apiInstance = new DefaultApi();
        try {
            List<ResponseConfigModel> result = apiInstance.getAllConfigsSettingsGet();
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DefaultApi#getAllConfigsSettingsGet");
            e.printStackTrace();
        }
    }
}
import io.swagger.client.mda.*;
import io.swagger.client.mda.auth.*;
import io.swagger.client.mda.model.*;
import io.swagger.client.mda.api.DefaultApi;

import java.io.File;
import java.util.*;

public class DefaultApiExample {

    public static void main(String[] args) {
        
        DefaultApi apiInstance = new DefaultApi();
        Object configId = null; // Object | 
        try {
            ResponseConfigModel result = apiInstance.getConfigIdSettingsConfigIdGet(configId);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DefaultApi#getConfigIdSettingsConfigIdGet");
            e.printStackTrace();
        }
    }
}
import io.swagger.client.mda.*;
import io.swagger.client.mda.auth.*;
import io.swagger.client.mda.model.*;
import io.swagger.client.mda.api.DefaultApi;

import java.io.File;
import java.util.*;

public class DefaultApiExample {

    public static void main(String[] args) {
        
        DefaultApi apiInstance = new DefaultApi();
        ConfigModel body = new ConfigModel(); // ConfigModel | 
        try {
            ResponseConfigModel result = apiInstance.setParamSettingsPost(body);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DefaultApi#setParamSettingsPost");
            e.printStackTrace();
        }
    }
}
import io.swagger.client.mda.*;
import io.swagger.client.mda.auth.*;
import io.swagger.client.mda.model.*;
import io.swagger.client.mda.api.DefaultApi;

import java.io.File;
import java.util.*;

public class DefaultApiExample {

    public static void main(String[] args) {
        
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
    }
}
```

## Documentation for API Endpoints

All URIs are relative to */*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*DefaultApi* | [**deleteConfigIdSettingsConfigIdDelete**](docs/DefaultApi.md#deleteConfigIdSettingsConfigIdDelete) | **DELETE** /settings/{config_id} | Delete Config Id
*DefaultApi* | [**disableConfigIdSettingsConfigIdDisablePut**](docs/DefaultApi.md#disableConfigIdSettingsConfigIdDisablePut) | **PUT** /settings/{config_id}/disable | Disable Config Id
*DefaultApi* | [**enableConfigIdSettingsConfigIdEnablePut**](docs/DefaultApi.md#enableConfigIdSettingsConfigIdEnablePut) | **PUT** /settings/{config_id}/enable | Enable Config Id
*DefaultApi* | [**getAllConfigsSettingsGet**](docs/DefaultApi.md#getAllConfigsSettingsGet) | **GET** /settings | Get All Configs
*DefaultApi* | [**getConfigIdSettingsConfigIdGet**](docs/DefaultApi.md#getConfigIdSettingsConfigIdGet) | **GET** /settings/{config_id} | Get Config Id
*DefaultApi* | [**setParamSettingsPost**](docs/DefaultApi.md#setParamSettingsPost) | **POST** /settings | Set Param
*DefaultApi* | [**updateConfigIdSettingsConfigIdPut**](docs/DefaultApi.md#updateConfigIdSettingsConfigIdPut) | **PUT** /settings/{config_id} | Update Config Id

## Documentation for Models

 - [ConfigModel](docs/ConfigModel.md)
 - [ContextModel](docs/ContextModel.md)
 - [HTTPValidationError](docs/HTTPValidationError.md)
 - [MetricModel](docs/MetricModel.md)
 - [ResponseConfigModel](docs/ResponseConfigModel.md)
 - [ResponseErrorModel](docs/ResponseErrorModel.md)
 - [ResponseMetricModel](docs/ResponseMetricModel.md)
 - [UpdateConfigModel](docs/UpdateConfigModel.md)
 - [ValidationError](docs/ValidationError.md)

## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

