
# ScaleVnfData

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**vnfInstanceid** | **String** | Identifier of the VNF instance being scaled.  | 
**scaleVnfType** | [**ScaleVnfTypeEnum**](#ScaleVnfTypeEnum) | Type of the scale VNF operation requested. Allowed values are: - SCALE_OUT - SCALE_IN - SCALE_TO_INSTANTIATION_LEVEL - SCALE_TO_SCALE_LEVEL(S) The set of types actually supported depends on the capabilities of the VNF being managed.  | 
**scaleToLevelData** | [**ScaleToLevelData**](ScaleToLevelData.md) | The information used for scaling to a given level.  |  [optional]
**scaleByStepData** | [**ScaleByStepData**](ScaleByStepData.md) | The information used for scaling by steps.  |  [optional]


<a name="ScaleVnfTypeEnum"></a>
## Enum: ScaleVnfTypeEnum
Name | Value
---- | -----
OUT | &quot;SCALE_OUT&quot;
IN | &quot;SCALE_IN&quot;
TO_INSTANTIATION_LEVEL | &quot;SCALE_TO_INSTANTIATION_LEVEL&quot;
TO_SCALE_LEVEL_S_ | &quot;SCALE_TO_SCALE_LEVEL(S)&quot;



