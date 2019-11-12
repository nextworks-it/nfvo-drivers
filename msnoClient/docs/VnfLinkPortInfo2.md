
# VnfLinkPortInfo2

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **String** | Identifier of this link port as provided by the entity that has created the link port.  | 
**resourceHandle** | [**ResourceHandle**](ResourceHandle.md) | Reference to the virtualised network resource realizing this link port.  | 
**cpInstanceId** | **String** | When the link port is used for external connectivity by the VNF, this attribute represents the identifier of the external CP of the VNF to be connected to this link port. When the link port is used for internal connectivity in the VNF, this attribute represents the VNFC CP to be connected to this link port. Shall be present when the link port is used for external connectivity by the VNF. may be present if used to reference a VNFC CP instance. There shall be at most one link port associated with any external connection point instance or internal connection point (i.e. VNFC CP) instance. The value refers to an \&quot;extCpInfo\&quot; item in the VnfInstance or a \&quot;vnfcCpInfo\&quot; item of a \&quot;vnfcResouceInfo\&quot; item in the VnfInstance.  |  [optional]
**cpInstanceType** | [**CpInstanceTypeEnum**](#CpInstanceTypeEnum) | Type of the CP instance that is identified by cpInstanceId. Shall be present if \&quot;cpInstanceId\&quot; is present, and shall be absent otherwise. Permitted values: * VNFC_CP: The link port is connected to a VNFC CP * EXT_CP: The link port is associated to an external CP.  |  [optional]


<a name="CpInstanceTypeEnum"></a>
## Enum: CpInstanceTypeEnum
Name | Value
---- | -----
VNFC_CP | &quot;VNFC_CP&quot;
EXT_CP | &quot;EXT_CP&quot;



