
# AffectedVnffg

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**vnffgInstanceId** | **String** | Identifier of the VNFFG instance.  | 
**vnffgdId** | **String** | Identifier of the VNFFGD of the VNFFG instance.  | 
**changeType** | [**ChangeTypeEnum**](#ChangeTypeEnum) | Signals the type of change. Permitted values: - ADD - DELETE - MODIFY  |  [optional]
**changeResult** | [**ChangeResultEnum**](#ChangeResultEnum) | Signals the result of change identified by the \&quot;changeType\&quot; attribute. Permitted values: - COMPLETED - ROLLED_BACK - FAILED  |  [optional]


<a name="ChangeTypeEnum"></a>
## Enum: ChangeTypeEnum
Name | Value
---- | -----
ADD | &quot;ADD&quot;
DELETE | &quot;DELETE&quot;
MODIFY | &quot;MODIFY&quot;


<a name="ChangeResultEnum"></a>
## Enum: ChangeResultEnum
Name | Value
---- | -----
COMPLETED | &quot;COMPLETED&quot;
ROLLED_BACK | &quot;ROLLED_BACK&quot;
FAILED | &quot;FAILED&quot;



