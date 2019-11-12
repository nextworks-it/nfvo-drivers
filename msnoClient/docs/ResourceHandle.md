
# ResourceHandle

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**vimId** | **String** | Identifier of the VIM under whose control this resource is placed. This attribute shall be present if VNF-related resource management in direct mode is applicable. It shall also be present for resources that are part of an NS instance such as virtual link resources.  |  [optional]
**resourceProviderId** | **String** | Identifier of the entity responsible for the management of the resource. This attribute shall only be supported and present when VNF-related resource management in indirect mode is applicable. The identification scheme is outside the scope of the present document.  |  [optional]
**resourceId** | **String** | Identifier of the resource in the scope of the VIM or the resource provider.  | 
**vimLevelResourceType** | **String** | Type of the resource in the scope of the VIM or the resource provider. The value set of the \&quot;vimLevelResourceType\&quot; attribute is within the scope of the VIM or the resource provider and can be used as information that complements the ResourceHandle.  |  [optional]



