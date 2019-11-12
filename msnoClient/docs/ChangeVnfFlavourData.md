
# ChangeVnfFlavourData

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**vnfInstanceId** | **String** | Identifier of the VNF instance to be modified.  | 
**newFlavourId** | **String** | Identifier of the VNF deployment flavor to be instantiated.  | 
**instantiationLevelId** | **String** | Identifier of the instantiation level of the deployment flavor to be instantiated. If not present, the default instantiation level as declared in the VNFD is instantiated.  |  [optional]
**extVirtualLinks** | [**List&lt;ExtVirtualLinkData&gt;**](ExtVirtualLinkData.md) | Information about external VLs to connect the VNF to.  |  [optional]
**extManagedVirtualLinks** | [**List&lt;ExtManagedVirtualLinkData&gt;**](ExtManagedVirtualLinkData.md) | information about internal VLs that are managed by NFVO.  |  [optional]
**additionalParams** | [**KeyValuePairs**](KeyValuePairs.md) | Additional input parameters for the flavor change process, specific to the VNF being modified, as declared in the VNFD as part of \&quot;ChangeVnfFlavourOpConfig\&quot;.  |  [optional]



