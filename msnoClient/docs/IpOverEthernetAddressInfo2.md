
# IpOverEthernetAddressInfo2

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**macAddress** | **String** | Assigned MAC address.  | 
**ipAddresses** | [**List&lt;IpOverEthernetAddressInfo2IpAddresses&gt;**](IpOverEthernetAddressInfo2IpAddresses.md) | Addresses assigned to the CP instance. Each entry represents IP addresses assigned by fixed or dynamic IP address assignment per subnet.  | 
**type** | [**TypeEnum**](#TypeEnum) | The type of the IP addresses  |  [optional]
**addresses** | **List&lt;String&gt;** | Fixed addresses assigned (from the subnet defined by \&quot;subnetId\&quot; if provided). See note.  | 
**isDynamic** | **Boolean** | Indicates whether this set of addresses was assigned dynamically (true) or based on address information provided as input from the API consumer (false). Shall be present if \&quot;addresses\&quot; is present and shall be absent otherwise.  |  [optional]
**addressRange** | [**IpOverEthernetAddressInfo2AddressRange1**](IpOverEthernetAddressInfo2AddressRange1.md) |  | 
**minAddress** | **String** | Lowest IP address belonging to the range  |  [optional]
**maxAddress** | **String** | Highest IP address belonging to the range.  |  [optional]
**subnetId** | **String** | Subnet defined by the identifier of the subnet resource in the VIM. In case this attribute is present, IP addresses are bound to that subnet.  | 


<a name="TypeEnum"></a>
## Enum: TypeEnum
Name | Value
---- | -----
PV4 | &quot;PV4&quot;
PV6 | &quot;PV6&quot;



