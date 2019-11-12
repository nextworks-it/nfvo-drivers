
# VnfcResourceInfo2

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **String** | Identifier of this VnfcResourceInfo instance.  | 
**vduId** | **String** | Reference to the applicable VDU in the VNFD.  | 
**computeResource** | [**ResourceHandle**](ResourceHandle.md) | Reference to the VirtualCompute resource.  | 
**storageResourceIds** | **List&lt;String&gt;** | References to the VirtualStorage resources. The value refers to a VirtualStorageResourceInfo item in the VnfInstance.  |  [optional]
**reservationId** | **String** | The reservation identifier applicable to the resource. It shall be present when an applicable reservation exists.  |  [optional]
**vnfcCpInfo** | [**List&lt;VnfcResourceInfo2VnfcCpInfo&gt;**](VnfcResourceInfo2VnfcCpInfo.md) | CPs of the VNFC instance. Shall be present when that particular CP of the VNFC instance is associated to an external CP of the VNF instance. May be present otherwise.  |  [optional]
**metadata** | [**KeyValuePairs**](KeyValuePairs.md) | Metadata about this resource.  |  [optional]



