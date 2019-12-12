
# ExtManagedVirtualLinkInfo2

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **String** | Identifier of the externally-managed internal VL and the related externally-managed VL information instance. The identifier is assigned by the NFV-MANO entity that manages this VL instance.  | 
**vnfVirtualLinkDescId** | **String** | Identifier of the VNF Virtual Link Descriptor (VLD) in the VNFD.  | 
**networkResource** | [**ResourceHandle**](ResourceHandle.md) | Reference to the VirtualNetwork resource.  |  [optional]
**vnfLinkPorts** | [**List&lt;VnfLinkPortInfo2&gt;**](VnfLinkPortInfo2.md) | Link ports of this VL.  |  [optional]



