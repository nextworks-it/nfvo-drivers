
# NsLinkPortInfo2

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **String** | Identifier of this link port as provided by the entity that has created the link port.  | 
**resourceHandle** | [**ResourceHandle**](ResourceHandle.md) | Identifier of the virtualised network resource realizing this link port.  | 
**nsCpHandle** | [**List&lt;NsCpHandle2&gt;**](NsCpHandle2.md) | Identifier of the CP/SAP instance to be connected to this link port. The value refers to a vnfExtCpInfo item in the VnfInstance, or a pnfExtCpInfo item in the PnfInfo, or a sapInfo item in the NS instance. There shall be at most one link port associated with any connection point instance.  |  [optional]



