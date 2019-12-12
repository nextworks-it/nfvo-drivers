
# NfpRule

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**etherDestinationAddress** | **String** | Indicates a destination Mac address.  |  [optional]
**etherSourceAddress** | **String** | Indicates a source Mac address.  |  [optional]
**etherType** | [**EtherTypeEnum**](#EtherTypeEnum) | Human readable description for the VNFFG.  |  [optional]
**vlanTag** | **List&lt;String&gt;** | Indicates a VLAN identifier in an IEEE 802.1Q-2014 tag [6] Multiple tags can be included for QinQ stacking. See note.  |  [optional]
**protocol** | [**ProtocolEnum**](#ProtocolEnum) | Indicates the L4 protocol, For IPv4 [7] this corresponds to the field called \&quot;Protocol\&quot; to identify the next level protocol. For IPv6 [28] this corresponds to the field is called the \&quot;Next Header\&quot; field. Permitted values: Any keyword defined in the IANA protocol registry [1], e.g.: TCP UDP ICMP  |  [optional]
**dscp** | **String** | For IPv4 [7] a string of \&quot;0\&quot; and \&quot;1\&quot; digits that corresponds to the 6-bit Differentiated Services Code Point (DSCP) field of the IP header. For IPv6 [28] a string of \&quot;0\&quot; and \&quot;1\&quot; digits that corresponds to the 6 differentiated services bits of the traffic class header field  |  [optional]
**sourcePortRange** | [**PortRange**](PortRange.md) | Indicates a range of source ports  |  [optional]
**destinationPortRange** | [**PortRange**](PortRange.md) | Indicates a range of destination ports.  |  [optional]
**sourceIpAddressPrefix** | **String** | Indicates the source IP address range in CIDR format.  |  [optional]
**destinationIpAddressPrefix** | **String** | Indicates the destination IP address range in CIDR format.  |  [optional]
**extendedCriteria** | [**List&lt;Mask&gt;**](Mask.md) | Indicates values of specific bits in a frame.  |  [optional]


<a name="EtherTypeEnum"></a>
## Enum: EtherTypeEnum
Name | Value
---- | -----
IPV4 | &quot;IPV4&quot;
IPV6 | &quot;IPV6&quot;


<a name="ProtocolEnum"></a>
## Enum: ProtocolEnum
Name | Value
---- | -----
TCP | &quot;TCP&quot;
UDP | &quot;UDP&quot;
ICMP | &quot;ICMP&quot;



