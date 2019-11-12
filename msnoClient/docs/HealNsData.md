
# HealNsData

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**degreeHealing** | [**DegreeHealingEnum**](#DegreeHealingEnum) | Indicates the degree of healing. Possible values include: - HEAL_RESTORE: Complete the healing of the NS restoring the state of the NS before the failure occurred - HEAL_QOS: Complete the healing of the NS based on the newest QoS values - HEAL_RESET: Complete the healing of the NS resetting to the original instantiation state of the NS - PARTIAL_HEALING  | 
**actionsHealing** | **List&lt;String&gt;** | Used to specify dedicated healing actions in a particular order (e.g. as a script). The actionsHealing attribute can be used to provide a specific script whose content and actions might only be possible to be derived during runtime.  |  [optional]
**healScript** | **String** | Reference to a script from the NSD that shall be used to execute dedicated healing actions in a particular order. The healScript, since it refers to a script in the NSD, can be used to execute healing actions which are defined during NS design time.  |  [optional]
**additionalParamsforNs** | [**KeyValuePairs**](KeyValuePairs.md) | Allows the OSS/BSS to provide additional parameter(s) to the healing process at the NS level.  |  [optional]


<a name="DegreeHealingEnum"></a>
## Enum: DegreeHealingEnum
Name | Value
---- | -----
HEAL_RESTORE | &quot;HEAL_RESTORE&quot;
HEAL_QOS | &quot;HEAL_QOS&quot;
HEAL_RESET | &quot;HEAL_RESET&quot;
PARTIAL_HEALING | &quot;PARTIAL_HEALING&quot;



