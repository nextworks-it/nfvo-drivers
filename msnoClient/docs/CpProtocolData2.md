
# CpProtocolData2

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**layerProtocol** | [**LayerProtocolEnum**](#LayerProtocolEnum) | Identifier of layer(s) and protocol(s). Permitted values: IP_OVER_ETHERNET.  | 
**ipOverEthernet** | [**IpOverEthernetAddressData2**](IpOverEthernetAddressData2.md) | Network address data for IP over Ethernet to assign to the extCP instance. Shall be present if layerProtocol is equal to \&quot;IP_OVER_ETHERNET\&quot;, and shall be absent otherwise.  |  [optional]


<a name="LayerProtocolEnum"></a>
## Enum: LayerProtocolEnum
Name | Value
---- | -----
IP_OVER_ETHERNET | &quot;IP_OVER_ETHERNET&quot;



