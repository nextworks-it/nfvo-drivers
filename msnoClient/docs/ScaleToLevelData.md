
# ScaleToLevelData

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**vnfInstantiationLevelId** | **String** | Identifier of the target instantiation level of the current deployment flavor to which the VNF is requested to be scaled.  |  [optional]
**vnfScaleInfo** | [**List&lt;VnfScaleInfo&gt;**](VnfScaleInfo.md) | For each scaling aspect of the current deployment flavor, indicates the target scale level to which the VNF is to be scaled.  |  [optional]
**additionalParams** | [**KeyValuePairs**](KeyValuePairs.md) | Additional parameters passed by the NFVO as input to the scaling process, specific to the VNF being scaled.  |  [optional]



