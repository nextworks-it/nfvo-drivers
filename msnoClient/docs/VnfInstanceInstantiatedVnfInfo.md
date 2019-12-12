
# VnfInstanceInstantiatedVnfInfo

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**flavourId** | **String** | Identifier of the VNF deployment flavor applied to this VNF instance.  | 
**vnfState** | [**VnfOperationalStateType**](VnfOperationalStateType.md) | The state of the VNF instance.  | 
**scaleStatus** | [**List&lt;VnfScaleInfo&gt;**](VnfScaleInfo.md) | Scale status of the VNF, one entry per aspect. Represents for every scaling aspect how \&quot;big\&quot; the VNF has been scaled w.r.t. that aspect.  |  [optional]
**extCpInfo** | [**List&lt;VnfExtCpInfo&gt;**](VnfExtCpInfo.md) | Information about the external CPs exposed by the VNF instance.  |  [optional]
**extVirtualLinkInfo** | [**List&lt;ExtVirtualLinkInfo&gt;**](ExtVirtualLinkInfo.md) | Information about the external VLs the VNF instance is connected to.  |  [optional]
**extManagedVirtualLinkInfo** | [**List&lt;ExtManagedVirtualLinkInfo&gt;**](ExtManagedVirtualLinkInfo.md) | External virtual links the VNF instance is connected to.  |  [optional]
**monitoringParameters** | [**List&lt;VnfMonitoringParameter&gt;**](VnfMonitoringParameter.md) | Performance metrics tracked by the VNFM (e.g. for auto-scaling purposes) as identified by the VNF provider in the VNFD.  |  [optional]
**localizationLanguage** | **String** | Information about localization language of the VNF (includes e.g. strings in the VNFD). The localization languages supported by a VNF can be declared in the VNFD, and localization language selection can take place at instantiation time. The value shall comply with the format defined in IETF RFC 5646.  |  [optional]
**vnfcResourceInfo** | [**List&lt;VnfcResourceInfo&gt;**](VnfcResourceInfo.md) | Information about the virtualised compute and storage resources used by the VNFCs of the VNF instance.  |  [optional]
**virtualLinkResourceInfo** | [**List&lt;VnfVirtualLinkResourceInfo&gt;**](VnfVirtualLinkResourceInfo.md) | Information about the virtualised network resources used by the VLs of the VNF instance.  |  [optional]
**virtualStorageResourceInfo** | [**List&lt;VirtualStorageResourceInfo&gt;**](VirtualStorageResourceInfo.md) | Information on the virtualised storage resource(s) used as storage for the VNF instance.  |  [optional]



