
# AffectedNs

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**nsInstanceId** | **String** | Identifier of the nested NS instance.  | 
**nsdId** | **String** | Identifier of the NSD of the nested NS instance.  | 
**changeType** | [**ChangeTypeEnum**](#ChangeTypeEnum) | Signals the type of lifecycle change. Permitted values: - ADD - REMOVE - INSTANTIATE - SCALE - UPDATE - HEAL - TERMINATE  | 
**changeResult** | [**ChangeResultEnum**](#ChangeResultEnum) | Signals the result of change identified by the \&quot;changeType\&quot; attribute. Permitted values: - COMPLETED - ROLLED_BACK - FAILED - PARTIALLY_COMPLETED  | 


<a name="ChangeTypeEnum"></a>
## Enum: ChangeTypeEnum
Name | Value
---- | -----
ADD | &quot;ADD&quot;
REMOVE | &quot;REMOVE&quot;
INSTANTIATE | &quot;INSTANTIATE&quot;
SCALE | &quot;SCALE&quot;
UPDATE | &quot;UPDATE&quot;
HEAL | &quot;HEAL&quot;
TERMINATE | &quot;TERMINATE&quot;


<a name="ChangeResultEnum"></a>
## Enum: ChangeResultEnum
Name | Value
---- | -----
COMPLETED | &quot;COMPLETED&quot;
ROLLED_BACK | &quot;ROLLED_BACK&quot;
FAILED | &quot;FAILED&quot;
PARTIALLY_COMPLETED | &quot;PARTIALLY_COMPLETED&quot;



