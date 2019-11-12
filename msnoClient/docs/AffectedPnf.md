
# AffectedPnf

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**pnfId** | **String** | Identifier of the affected PNF. This identifier is allocated by the OSS/BSS.  | 
**pnfdId** | **String** | Identifier of the PNFD on which the PNF is based.  | 
**pnfProfileId** | **String** | Identifier of the VNF profile of the NSD.  | 
**pnfName** | **String** | Name of the PNF.  |  [optional]
**cpInstanceId** | **List&lt;String&gt;** | Identifier of the CP in the scope of the PNF.  | 
**changeType** | [**ChangeTypeEnum**](#ChangeTypeEnum) | Signals the type of change. Permitted values: - ADD - REMOVE - MODIFY  |  [optional]
**changeResult** | [**ChangeResultEnum**](#ChangeResultEnum) | Signals the result of change identified by the \&quot;changeType\&quot; attribute. Permitted values: - COMPLETED - ROLLED_BACK - FAILED  |  [optional]


<a name="ChangeTypeEnum"></a>
## Enum: ChangeTypeEnum
Name | Value
---- | -----
ADD | &quot;ADD&quot;
REMOVE | &quot;REMOVE&quot;
MODIFY | &quot;MODIFY&quot;


<a name="ChangeResultEnum"></a>
## Enum: ChangeResultEnum
Name | Value
---- | -----
COMPLETED | &quot;COMPLETED&quot;
ROLLED_BACK | &quot;ROLLED_BACK&quot;
FAILED | &quot;FAILED&quot;



