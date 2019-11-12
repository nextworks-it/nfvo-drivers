
# ForwardingBehaviourInputParameters

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**algortihmName** | [**AlgortihmNameEnum**](#AlgortihmNameEnum) | May be included if forwarding behaviour is equal to LB. Shall not be included otherwise. Permitted values: * ROUND_ROBIN * LEAST_CONNECTION * LEAST_TRAFFIC * LEAST_RESPONSE_TIME * CHAINED_FAILOVER * SOURCE_IP_HASH * SOURCE_MAC_HASH  |  [optional]
**algorithmWeights** | **List&lt;Integer&gt;** | Percentage of messages sent to a CP instance. May be included if applicable to the algorithm. If applicable to the algorithm but not provided, default values determined by the VIM or NFVI are expected to be used. Weight applies to the CP instances in the order they have been created.  |  [optional]


<a name="AlgortihmNameEnum"></a>
## Enum: AlgortihmNameEnum
Name | Value
---- | -----
ROUND_ROBIN | &quot;ROUND_ROBIN&quot;
LEAST_CONNECTION | &quot;LEAST_CONNECTION&quot;
LEAST_TRAFFIC | &quot;LEAST_TRAFFIC&quot;
LEAST_RESPONSE_TIME | &quot;LEAST_RESPONSE_TIME&quot;
CHAINED_FAILOVER | &quot;CHAINED_FAILOVER&quot;
SOURCE_IP_HASH | &quot;SOURCE_IP_HASH&quot;
SOURCE_MAC_HASH | &quot;SOURCE_MAC_HASH&quot;



