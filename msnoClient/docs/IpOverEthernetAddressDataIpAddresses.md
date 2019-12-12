
# IpOverEthernetAddressDataIpAddresses

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**type** | [**TypeEnum**](#TypeEnum) | The type of the IP addresses. Permitted values: IPV4, IPV6.  | 
**fixedAddresses** | **List&lt;String&gt;** | Fixed addresses to assign (from the subnet defined by \&quot;subnetId\&quot; if provided). Exactly one of \&quot;fixedAddresses\&quot;, \&quot;numDynamicAddresses\&quot; or \&quot;ipAddressRange\&quot; shall be present.  |  [optional]
**numDynamicAddresses** | **Integer** | Number of dynamic addresses to assign (from the subnet defined by \&quot;subnetId\&quot; if provided). Exactly one of \&quot;fixedAddresses\&quot;, \&quot;numDynamicAddresses\&quot; or \&quot;ipAddressRange\&quot; shall be present.  |  [optional]
**addressRange** | [**IpOverEthernetAddressDataAddressRange**](IpOverEthernetAddressDataAddressRange.md) |  |  [optional]
**subnetId** | **String** | Subnet defined by the identifier of the subnet resource in the VIM. In case this attribute is present, IP addresses from that subnet will be assigned; otherwise, IP addresses not bound to a subnet will be assigned.  |  [optional]


<a name="TypeEnum"></a>
## Enum: TypeEnum
Name | Value
---- | -----
IPV4 | &quot;IPV4&quot;
IPV6 | &quot;IPV6&quot;



