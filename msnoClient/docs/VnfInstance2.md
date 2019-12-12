
# VnfInstance2

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **String** | Identifier of the VNF instance.  | 
**vnfInstanceName** | **String** | Name of the VNF instance. This attribute can be modified with the PATCH method.  |  [optional]
**vnfInstanceDescription** | **String** | Human-readable description of the VNF instance. This attribute can be modified with the PATCH method.  |  [optional]
**vnfdId** | **String** | Identifier of the VNFD on which the VNF instance is based.  | 
**vnfProvider** | **String** | Provider of the VNF and the VNFD. The value is copied from the VNFD.  | 
**vnfProductName** | **String** | Name to identify the VNF Product. The value is copied from the VNFD.  | 
**vnfSoftwareVersion** | **String** | Software version of the VNF. The value is copied from the VNFD.  | 
**vnfdVersion** | **String** | Identifies the version of the VNFD. The value is copied from the VNFD.  | 
**vnfPkgId** | **String** | Identifier of information held by the NFVO about the specific VNF package on which the VNF is based. This identifier was allocated by the NFVO. This attribute can be modified with the PATCH method.  | 
**vnfConfigurableProperties** | [**KeyValuePairs**](KeyValuePairs.md) | Current values of the configurable properties of the VNF instance. Configurable properties referred in this attribute are declared in the VNFD. ETSI GS NFV-SOL 001 specifies the structure and format of the VNFD based on TOSCA specifications. VNF configurable properties are sometimes also referred to as configuration parameters applicable to a VNF. Some of these are set prior to instantiation and cannot be modified if the VNF is instantiated, some are set prior to instantiation (are part of initial configuration) and can be modified later, and others can be set only after instantiation. The applicability of certain configuration may depend on the VNF and the required operation of the VNF at a certain point in time. These configurable properties include the following standard attributes, which are declared in the VNFD if auto-scaling and/or auto-healing are supported by the VNF: * isAutoscaleEnabled: If present, the VNF supports auto-scaling. If set to true, auto-scaling is currently enabled. If set to false, auto-scaling is currently disabled. * isAutohealEnabled: If present, the VNF supports auto-healing. If set to true, auto-healing is currently enabled. If set to false, auto-healing is currently disabled. This attribute can be modified with the PATCH method.  |  [optional]
**vimId** | **String** | Identifier of a VIM that manages resources for the VNF instance.  |  [optional]
**instantiationState** | [**InstantiationStateEnum**](#InstantiationStateEnum) | The instantiation state of the VNF.  | 
**instantiatedVnfInfo** | [**VnfInstance2InstantiatedVnfInfo**](VnfInstance2InstantiatedVnfInfo.md) |  |  [optional]
**metadata** | [**KeyValuePairs**](KeyValuePairs.md) | Additional VNF-specific metadata describing the VNF instance. Metadata that are writeable are declared in the VNFD. This attribute can be modified with the PATCH method.  |  [optional]
**extensions** | [**KeyValuePairs**](KeyValuePairs.md) | VNF-specific attributes that affect the lifecycle management of this VNF instance by the VNFM, or the lifecycle management scripts. Extensions that are writeable are declared in the VNFD. This attribute can be modified with the PATCH method.  |  [optional]


<a name="InstantiationStateEnum"></a>
## Enum: InstantiationStateEnum
Name | Value
---- | -----
NOT_INSTANTIATED | &quot;NOT_INSTANTIATED&quot;
INSTANTIATED | &quot;INSTANTIATED&quot;



