
# ScaleByStepData

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**aspectId** | **String** | Identifier of (reference to) the aspect of the VNF that is requested to be scaled, as declared in the VNFD.  | 
**numberOfSteps** | **Integer** | Number of scaling steps. It shall be a positive number. Defaults to 1. The VNF provider defines in the VNFD whether or not a particular VNF supports performing more than one step at a time. Such a property in the VNFD applies for all instances of a particular VNF.  |  [optional]
**additionalParams** | [**KeyValuePairs**](KeyValuePairs.md) | Additional parameters passed by the NFVO as input to the scaling process, specific to the VNF instance being scaled.  |  [optional]



