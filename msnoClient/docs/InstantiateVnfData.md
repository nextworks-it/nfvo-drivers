
# InstantiateVnfData

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**vnfdId** | **String** | Information sufficient to identify the VNFD which defines the VNF to be instantiated.  | 
**vnfFlavourId** | **String** | Identifier of the VNF deployment flavor to be instantiated.  | 
**vnfInstantiationLevelId** | **String** | Identifier of the instantiation level of the deployment flavor to be instantiated. If not present, the default instantiation level as declared in the VNFD is instantiated.  |  [optional]
**vnfInstanceName** | **String** | Human-readable name of the VNF instance to be created.  |  [optional]
**vnfInstanceDescription** | **String** | Human-readable description of the VNF instance to be created.  |  [optional]
**extVirtualLinks** | [**List&lt;ExtVirtualLinkData&gt;**](ExtVirtualLinkData.md) | Information about external VLs to connect the VNF to.  |  [optional]
**extManagedVirtualLinks** | [**List&lt;ExtManagedVirtualLinkData&gt;**](ExtManagedVirtualLinkData.md) | Information about internal VLs that are managed by other entities than the VNFM.  |  [optional]
**localizationLanguage** | **String** | Localization language of the VNF to be instantiated. The value shall comply with the format defined in IETF RFC 5646 [16].  |  [optional]
**additionalParams** | [**KeyValuePairs**](KeyValuePairs.md) | Additional input parameters for the instantiation process, specific to the VNF being instantiated.  |  [optional]



