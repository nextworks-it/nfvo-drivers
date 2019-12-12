
# ExtVirtualLinkData

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**extVirtualLinkId** | **String** | The identifier of the external VL instance, if provided.  |  [optional]
**vimId** | **String** | Identifier of the VIM that manages this resource. This attribute shall only be supported and present if VNFrelated resource management in direct mode is applicable.  |  [optional]
**resourceProviderId** | **String** | Identifies the entity responsible for the management of this resource. This attribute shall only be supported and present if VNF-related resource management in indirect mode is applicable. The identification scheme is outside the scope of the present document.  |  [optional]
**resourceId** | **String** | The identifier of the resource in the scope of the VIM or the resource provider.  | 
**extCps** | [**List&lt;VnfExtCpData&gt;**](VnfExtCpData.md) | External CPs of the VNF to be connected to this external VL.  | 
**extLinkPorts** | [**List&lt;ExtLinkPortData&gt;**](ExtLinkPortData.md) | Externally provided link ports to be used to connect external connection points to this external VL.  |  [optional]



