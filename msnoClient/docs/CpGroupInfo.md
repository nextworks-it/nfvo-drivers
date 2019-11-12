
# CpGroupInfo

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**cpPairInfo** | [**List&lt;CpPairInfo&gt;**](CpPairInfo.md) | One or more pair(s) of ingress and egress CPs or SAPs which the NFP passes by. All CP or SAP pairs in a group shall be instantiated from connection point descriptors or service access point descriptors referenced in the corresponding NfpPositionDesc.  |  [optional]
**forwardingBehaviour** | [**ForwardingBehaviourEnum**](#ForwardingBehaviourEnum) | Identifies a rule to apply to forward traffic to the ingress CPs or SAPs of the group. Permitted values: * ALL &#x3D; Traffic flows shall be forwarded simultaneously to all CPs or SAPs of the group. * LB &#x3D; Traffic flows shall be forwarded to one CP or SAP of the group selected based on a loadbalancing algorithm.  |  [optional]
**forwardingBehaviourInputParameters** | [**ForwardingBehaviourInputParameters**](ForwardingBehaviourInputParameters.md) | Provides input parameters to configure the forwarding behaviour (e.g. identifies a load balancing algorithm and criteria).  |  [optional]


<a name="ForwardingBehaviourEnum"></a>
## Enum: ForwardingBehaviourEnum
Name | Value
---- | -----
ALL | &quot;ALL&quot;
LB | &quot;LB&quot;



