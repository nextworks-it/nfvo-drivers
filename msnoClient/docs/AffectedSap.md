
# AffectedSap

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**sapInstanceId** | **String** | Identifier of the nested NS instance.  | 
**sapdId** | **String** | Identifier of the NSD of the nested NS instance.  | 
**sapName** | **String** | Human readable name for the SAP.  |  [optional]
**changeType** | [**ChangeTypeEnum**](#ChangeTypeEnum) | Signals the type of lifecycle change. Permitted values: - ADD - REMOVE - MODIFY  |  [optional]
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



