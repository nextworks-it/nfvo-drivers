
# NsInstanceLinks

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**self** | [**Link**](Link.md) | URI of this resource.  | 
**nestedNsInstances** | [**List&lt;Link&gt;**](Link.md) | Links to resources related to this notification.  |  [optional]
**instantiate** | [**Link**](Link.md) | Link to the \&quot;instantiate\&quot; task resource, if the related operation is possible based on the current status of this NS instance resource (i.e. NS instance in NOT_INSTANTIATED state).  |  [optional]
**terminate** | [**Link**](Link.md) | Link to the \&quot;terminate\&quot; task resource, if the related operation is possible based on the current status of this NS instance resource (i.e. NS instance is in INSTANTIATED state).  |  [optional]
**update** | [**Link**](Link.md) | Link to the \&quot;update\&quot; task resource, if the related operation is possible based on the current status of this NS instance resource (i.e. NS instance is in INSTANTIATED state).  |  [optional]
**scale** | [**Link**](Link.md) | Link to the \&quot;scale\&quot; task resource, if the related operation is supported for this NS instance, and is possible based on the current status of this NS instance resource (i.e. NS instance is in INSTANTIATED state).  |  [optional]
**heal** | [**Link**](Link.md) | Link to the \&quot;heal\&quot; task resource, if the related operation is supported for this NS instance, and is possible based on the current status of this NS instance resource (i.e. NS instance is in INSTANTIATED state).  |  [optional]



