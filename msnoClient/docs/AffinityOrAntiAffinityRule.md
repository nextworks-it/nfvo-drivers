
# AffinityOrAntiAffinityRule

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**vnfdId** | **List&lt;String&gt;** | Reference to a VNFD. When the VNFD which is not used to instantiate VNF, it presents all VNF instances of this type as the subjects of the affinity or anti-affinity rule. The VNF instance which the VNFD presents is not necessary as a part of the NS to be instantiated.  |  [optional]
**vnfProfileId** | **List&lt;String&gt;** | Reference to a vnfProfile defined in the NSD. At least one VnfProfile which is used to instantiate VNF for the NS to be instantiated as the subject of the affinity or anti-affinity rule shall be present. When the VnfProfile which is not used to instantiate VNF, it presents all VNF instances of this type as the subjects of the affinity or anti-affinity rule. The VNF instance which the VnfProfile presents is not necessary as a part of the NS to be instantiated.  |  [optional]
**vnfInstanceId** | **List&lt;String&gt;** | Reference to the existing VNF instance as the subject of the affinity or anti-affinity rule. The existing VNF instance is not necessary as a part of the NS to be instantiated.  |  [optional]
**affinityOrAntiAffiinty** | [**AffinityOrAntiAffiintyEnum**](#AffinityOrAntiAffiintyEnum) | The type of the constraint. Permitted values: AFFINITY ANTI_AFFINITY.  | 
**scope** | [**ScopeEnum**](#ScopeEnum) | Specifies the scope of the rule where the placement constraint applies. Permitted values: NFVI_POP ZONE ZONE_GROUP NFVI_NODE.  | 


<a name="AffinityOrAntiAffiintyEnum"></a>
## Enum: AffinityOrAntiAffiintyEnum
Name | Value
---- | -----
AFFINITY | &quot;AFFINITY&quot;
ANTI_AFFINITY | &quot;ANTI_AFFINITY&quot;


<a name="ScopeEnum"></a>
## Enum: ScopeEnum
Name | Value
---- | -----
NFVI_POP | &quot;NFVI_POP&quot;
ZONE | &quot;ZONE&quot;
ZONE_GROUP | &quot;ZONE_GROUP&quot;
NFVI_NODE | &quot;NFVI_NODE&quot;



