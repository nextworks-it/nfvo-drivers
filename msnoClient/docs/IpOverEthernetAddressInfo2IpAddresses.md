
# IpOverEthernetAddressInfo2IpAddresses

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**type** | [**TypeEnum**](#TypeEnum) | The type of the IP addresses. Permitted values: IPV4, IPV6.  | 
**addresses** | **List&lt;String&gt;** | Fixed addresses assigned (from the subnet defined by \&quot;subnetId\&quot; if provided).  |  [optional]
**isDynamic** | **Boolean** | Indicates whether this set of addresses was assigned dynamically (true) or based on address information provided as input from the API consumer (false). Shall be present if \&quot;addresses\&quot; is present and shall be absent otherwise.  |  [optional]
**addressRange** | [**IpOverEthernetAddressInfo2AddressRange**](IpOverEthernetAddressInfo2AddressRange.md) |  |  [optional]
**subnetId** | **String** | Subnet defined by the identifier of the subnet resource in the VIM. In case this attribute is present, IP addresses are bound to that subnet.  |  [optional]


<a name="TypeEnum"></a>
## Enum: TypeEnum
Name | Value
---- | -----
IPV4 | &quot;IPV4&quot;
IPV6 | &quot;IPV6&quot;



