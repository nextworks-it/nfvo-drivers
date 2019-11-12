
# AffectedVnf

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**vnfInstanceId** | **String** | Identifier of the VNF instance.  | 
**vnfdId** | **String** | Identifier of the VNFD of the VNF Instance.  | 
**vnfProfileId** | **String** | Identifier of the VNF profile of the NSD.  | 
**vnfName** | **String** | Name of the VNF Instance.  |  [optional]
**changeType** | [**ChangeTypeEnum**](#ChangeTypeEnum) | Signals the type of change Permitted values: - ADD - REMOVE - INSTANTIATE - TERMINATE - SCALE - CHANGE_FLAVOUR - HEAL - OPERATE - MODIFY_INFORMATION - CHANGE_EXTERNAL_VNF_CONNECTIVITY  |  [optional]
**changeResult** | [**ChangeResultEnum**](#ChangeResultEnum) | Signals the result of change identified by the \&quot;changeType\&quot; attribute. Permitted values: - COMPLETED - ROLLED_BACK - FAILED  |  [optional]
**changedInfo** | [**AffectedVnfChangedInfo**](AffectedVnfChangedInfo.md) |  |  [optional]


<a name="ChangeTypeEnum"></a>
## Enum: ChangeTypeEnum
Name | Value
---- | -----
ADD | &quot;ADD&quot;
REMOVE | &quot;REMOVE&quot;
INSTANTIATE | &quot;INSTANTIATE&quot;
TERMINATE | &quot;TERMINATE&quot;
SCALE | &quot;SCALE&quot;
CHANGE_FLAVOUR | &quot;CHANGE_FLAVOUR&quot;
HEAL | &quot;HEAL&quot;
OPERATE | &quot;OPERATE&quot;
MODIFY_INFORMATION | &quot;MODIFY_INFORMATION&quot;
CHANGE_EXTERNAL_VNF_CONNECTIVITY | &quot;CHANGE_EXTERNAL_VNF_CONNECTIVITY&quot;


<a name="ChangeResultEnum"></a>
## Enum: ChangeResultEnum
Name | Value
---- | -----
COMPLETED | &quot;COMPLETED&quot;
ROLLED_BACK | &quot;ROLLED_BACK&quot;
FAILED | &quot;FAILED&quot;



