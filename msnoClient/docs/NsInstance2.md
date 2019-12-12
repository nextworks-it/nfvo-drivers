
# NsInstance2

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **String** | Identifier of the NS instance.  | 
**nsInstanceName** | **String** | Human readable name of the NS instance.  | 
**nsInstanceDescription** | **String** | Human readable description of the NS instance.  | 
**nsdId** | **String** | Identifier of the NSD on which the NS instance is based.  | 
**nsdInfoId** | **String** | Identifier of the NSD information object on which the NS instance is based. This identifier was allocated by the NFVO.  | 
**flavourId** | **String** | Identifier of the NS deployment flavor applied to the NS instance. This attribute shall be present if the nsState attribute value is INSTANTIATED.  |  [optional]
**vnfInstance** | [**List&lt;VnfInstance2&gt;**](VnfInstance2.md) | Information on constituent VNF(s) of the NS instance.  |  [optional]
**pnfInfo** | [**List&lt;PnfInfo2&gt;**](PnfInfo2.md) | Information on the PNF(s) that are part of the NS instance.  |  [optional]
**virtualLinkInfo** | [**List&lt;NsVirtualLinkInfo2&gt;**](NsVirtualLinkInfo2.md) | Information on the VL(s) of the NS instance. This attribute shall be present if the nsState attribute value is INSTANTIATED and if the NS instance has specified connectivity.  |  [optional]
**vnffgInfo** | [**List&lt;VnffgInfo2&gt;**](VnffgInfo2.md) | Information on the VNFFG(s) of the NS instance.  |  [optional]
**sapInfo** | [**List&lt;SapInfo2&gt;**](SapInfo2.md) | Information on the SAP(s) of the NS instance.  |  [optional]
**nestedNsInstanceId** | **List&lt;String&gt;** | Identifier of the nested NS(s) of the NS instance.  |  [optional]
**nsState** | [**NsStateEnum**](#NsStateEnum) | The state of the NS instance. Permitted values: NOT_INSTANTIATED: The NS instance is terminated or not instantiated. INSTANTIATED: The NS instance is instantiated.  | 
**monitoringParameter** | [**List&lt;NsMonitoringParameter2&gt;**](NsMonitoringParameter2.md) | Performance metrics tracked by the NFVO (e.g. for auto-scaling purposes) as identified by the NS designer in the NSD.  |  [optional]
**nsScaleStatus** | [**List&lt;NsScaleInfo2&gt;**](NsScaleInfo2.md) | Status of each NS scaling aspect declared in the applicable DF, how \&quot;big\&quot; the NS instance has been scaled w.r.t. that aspect. This attribute shall be present if the nsState attribute value is INSTANTIATED.  |  [optional]
**additionalAffinityOrAntiAffinityRule** | [**List&lt;AffinityOrAntiAffinityRule2&gt;**](AffinityOrAntiAffinityRule2.md) | Information on the additional affinity or anti-affinity rule from NS instantiation operation. Shall not conflict with rules already specified in the NSD.  |  [optional]
**links** | [**NsInstanceLinks**](NsInstanceLinks.md) |  |  [optional]


<a name="NsStateEnum"></a>
## Enum: NsStateEnum
Name | Value
---- | -----
NOT_INSTANTIATED | &quot;NOT_INSTANTIATED&quot;
INSTANTIATED | &quot;INSTANTIATED&quot;



