
# NsVirtualLinkInfo2

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **String** | Identifier of the VL instance.  | 
**nsVirtualLinkDescId** | **String** | Identifier of the VLD in the NSD.  | 
**nsVirtualLinkProfileId** | **String** | Identifier of the VL profile in the NSD.  | 
**resourceHandle** | [**List&lt;ResourceHandle&gt;**](ResourceHandle.md) | Identifier(s) of the virtualised network resource(s) realizing the VL instance. See note.  |  [optional]
**linkPort** | [**List&lt;NsLinkPortInfo2&gt;**](NsLinkPortInfo2.md) | Link ports of the VL instance. Cardinality of zero indicates that no port has yet been created for the VL instance.  |  [optional]



