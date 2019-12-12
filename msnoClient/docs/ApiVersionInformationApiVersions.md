
# ApiVersionInformationApiVersions

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**version** | **String** | Identifies a supported version. The value of the version attribute shall be a version identifier as specified in clause 4.6.1.  | 
**isDeprecated** | **Boolean** | If such information is available, this attribute indicates whether use of the version signaled by the version attribute is deprecated (true) or not (false). A deprecated version is still supported by the API producer but is recommended not to be used any longer. When a version is no longer supported, it does not appear in the response body.  |  [optional]
**retirementDate** | **String** | The date and time after which the API version will no longer be supported. This attribute may be included if the value of the isDeprecated attribute is set to true and shall be absent otherwise.  |  [optional]



