
# ScaleNsRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**scaleType** | [**ScaleTypeEnum**](#ScaleTypeEnum) | Indicates the type of scaling to be performed. Possible values: - SCALE_NS - SCALE_VNF  | 
**scaleNsData** | [**ScaleNsData**](ScaleNsData.md) | The necessary information to scale the referenced NS instance. It shall be present when scaleType &#x3D; SCALE_NS.  |  [optional]
**scaleVnfData** | [**List&lt;ScaleVnfData&gt;**](ScaleVnfData.md) | The necessary information to scale the referenced NS instance. It shall be present when scaleType &#x3D; SCALE_VNF.  |  [optional]
**scaleTime** | **String** | Timestamp indicating the scale time of the NS, i.e. the NS will be scaled at this timestamp. Cardinality \&quot;0\&quot; indicates the NS scaling takes place immediately\&quot;.  |  [optional]


<a name="ScaleTypeEnum"></a>
## Enum: ScaleTypeEnum
Name | Value
---- | -----
NS | &quot;SCALE_NS&quot;
VNF | &quot;SCALE_VNF&quot;



