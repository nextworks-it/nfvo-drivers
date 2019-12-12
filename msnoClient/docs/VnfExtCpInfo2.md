
# VnfExtCpInfo2

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **String** | Identifier of the external CP instance and the related information instance.  | 
**cpdId** | **String** | Identifier of the external CPD, VnfExtCpd, in the VNFD.  | 
**cpProtocolInfo** | [**List&lt;CpProtocolInfo2&gt;**](CpProtocolInfo2.md) | Network protocol information for this CP.  |  [optional]
**extLinkPortId** | [**CpProtocolInfo2**](CpProtocolInfo2.md) | Identifier of the \&quot;extLinkPortInfo\&quot; structure inside the \&quot;extVirtualLinkInfo\&quot; structure. Shall be present if the CP is associated to a link port.  |  [optional]
**metadata** | [**KeyValuePairs**](KeyValuePairs.md) | Metadata about this external CP.  |  [optional]
**associatedVnfcCpId** | **String** | Identifier of the \&quot;vnfcCpInfo\&quot; structure in \&quot;VnfcResourceInfo\&quot; structure that represents the VNFC CP which is exposed by this external CP instance. Shall be present in case this CP instance maps to a VNFC CP(s). The attributes \&quot;associatedVnfcCpId\&quot; and \&quot;associatedVnfVirtualLinkId\&quot; are mutually exclusive. One and only one shall be present.  |  [optional]
**associatedVnfVirtualLinkId** | **String** | Identifier of the \&quot;VnfVirtualLinkResourceInfo\&quot; structure that represents the internal VL, which is exposed by this external CP instance. Shall be present in case this CP instance maps to an internal VL. The attributes \&quot;associatedVnfcCpId\&quot; and \&quot;associatedVnfVirtualLinkId\&quot; are mutually exclusive. One and only one shall be present.  |  [optional]



