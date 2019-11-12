
# NsLcmOpOccLinks

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**self** | [**Link**](Link.md) | URI of this resource.  | 
**nsInstance** | [**Link**](Link.md) | Link to the NS instance that the operation applies to.  | 
**cancel** | [**Link**](Link.md) | Link to the task resource that represents the \&quot;cancel\&quot; operation for this LCM operation occurrence, if cancelling is currently allowed.  |  [optional]
**retry** | [**Link**](Link.md) | Link to the task resource that represents the \&quot;cancel\&quot; operation for this LCM operation occurrence, if cancelling is currently allowed.  |  [optional]
**rollback** | [**Link**](Link.md) | Link to the task resource that represents the \&quot;rollback\&quot; operation for this LCM operation occurrence, if rolling back is currently allowed.  |  [optional]
**_continue** | [**Link**](Link.md) | Link to the task resource that represents the \&quot;continue\&quot; operation for this LCM operation occurrence, if rolling back is currently allowed.  |  [optional]
**fail** | [**Link**](Link.md) | Link to the task resource that represents the \&quot;fail\&quot; operation for this LCM operation occurrence, if rolling back is currently allowed.  |  [optional]



