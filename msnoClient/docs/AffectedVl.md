
# AffectedVl

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**nsVirtualLinkInstanceId** | **String** | Identifier of the VL Instance.  | 
**nsVirtualLinkDescId** | **String** | Identifier of the VLD in the NSD for this VL.  | 
**vlProfileId** | **String** | Identifier of the VLD in the NSD for this VL.  | 
**changeType** | [**ChangeTypeEnum**](#ChangeTypeEnum) | Signals the type of change. Permitted values: - ADD - DELETE - MODIFY - ADD_LINK_PORT - REMOVE_LINK_PORT  |  [optional]
**changeResult** | [**ChangeResultEnum**](#ChangeResultEnum) | Signals the result of change identified by the \&quot;changeType\&quot; attribute. Permitted values: - COMPLETED - ROLLED_BACK - FAILED  |  [optional]


<a name="ChangeTypeEnum"></a>
## Enum: ChangeTypeEnum
Name | Value
---- | -----
ADD | &quot;ADD&quot;
DELETE | &quot;DELETE&quot;
MODIFY | &quot;MODIFY&quot;
ADD_LINK_PORT | &quot;ADD_LINK_PORT&quot;
REMOVE_LINK_PORT | &quot;REMOVE_LINK_PORT&quot;


<a name="ChangeResultEnum"></a>
## Enum: ChangeResultEnum
Name | Value
---- | -----
COMPLETED | &quot;COMPLETED&quot;
ROLLED_BACK | &quot;ROLLED_BACK&quot;
FAILED | &quot;FAILED&quot;



