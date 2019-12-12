
# CpProtocolInfo2

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**layerProtocol** | [**LayerProtocolEnum**](#LayerProtocolEnum) | The identifier of layer(s) and protocol(s) associated to the network address information. Permitted values: IP_OVER_ETHERNET See note.  | 
**ipOverEthernet** | [**IpOverEthernetAddressInfo2**](IpOverEthernetAddressInfo2.md) | IP addresses over Ethernet to assign to the CP or SAP instance. Shall be present if layerProtocol is equal to \&quot; IP_OVER_ETHERNET\&quot;, and shall be absent otherwise.  | 


<a name="LayerProtocolEnum"></a>
## Enum: LayerProtocolEnum
Name | Value
---- | -----
IP_OVER_ETHERNET | &quot;IP_OVER_ETHERNET&quot;



