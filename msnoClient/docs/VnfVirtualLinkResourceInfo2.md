
# VnfVirtualLinkResourceInfo2

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **String** | Identifier of this VnfVirtualLinkResourceInfo instance.  | 
**vnfVirtualLinkDescId** | **String** | Identifier of the VNF Virtual Link Descriptor (VLD) in the VNFD.  | 
**networkResource** | [**ResourceHandle**](ResourceHandle.md) | Reference to the VirtualNetwork resource.  | 
**reservationId** | **String** | The reservation identifier applicable to the resource. It shall be present when an applicable reservation exists.  |  [optional]
**vnfLinkPorts** | [**List&lt;VnfLinkPortInfo2&gt;**](VnfLinkPortInfo2.md) | Links ports of this VL. Shall be present when the linkPort is used for external connectivity by the VNF (refer to VnfLinkPortInfo). May be present otherwise.  |  [optional]
**metadata** | [**KeyValuePairs**](KeyValuePairs.md) | Metadata about this resource.  |  [optional]



