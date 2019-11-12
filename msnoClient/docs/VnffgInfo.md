
# VnffgInfo

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **String** | Identifier of this VNFFG instance.  | 
**vnffgdId** | **String** | Identifier of the VNFFGD in the NSD.  | 
**vnfInstanceId** | **List&lt;String&gt;** | Identifier(s) of the constituent VNF instance(s) of this VNFFG instance.  | 
**pnfdInfoId** | **List&lt;String&gt;** | Identifier(s) of the constituent PNF instance(s) of this VNFFG instance.  |  [optional]
**nsVirtualLinkInfoId** | **List&lt;String&gt;** | Identifier(s) of the constituent VL instance(s) of this VNFFG instance.  |  [optional]
**nsCpHandle** | [**List&lt;NsCpHandle&gt;**](NsCpHandle.md) | Identifiers of the CP instances attached to the constituent VNFs and PNFs or the SAP instances of the VNFFG. See note.  |  [optional]



