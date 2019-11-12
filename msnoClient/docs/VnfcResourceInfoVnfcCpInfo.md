
# VnfcResourceInfoVnfcCpInfo

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **String** | Identifier of this VNFC CP instance and the associated array entry.  | 
**cpdId** | **String** | Identifier of the VDU CPD, cpdId, in the VNFD.  | 
**vnfExtCpId** | **String** | When the VNFC CP is exposed as external CP of the VNF, the identifier of this external VNF CP.  |  [optional]
**cpProtocolInfo** | [**List&lt;CpProtocolInfo&gt;**](CpProtocolInfo.md) | Network protocol information for this CP.  |  [optional]
**vnfLinkPortId** | **String** | Identifier of the \&quot;vnfLinkPorts\&quot; structure in the \&quot;VnfVirtualLinkResourceInfo\&quot; structure. Shall be present if the CP is associated to a link port.  |  [optional]
**metadata** | [**KeyValuePairs**](KeyValuePairs.md) | Metadata about this CP.  |  [optional]



