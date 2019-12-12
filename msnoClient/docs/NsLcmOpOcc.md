
# NsLcmOpOcc

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **String** | Identifier of this NS lifecycle operation occurrence.  | 
**operationState** | [**NsLcmOperationStateType**](NsLcmOperationStateType.md) | The state of the NS LCM operation.  | 
**stateEnteredTime** | **String** | Date-time when the current state was entered.  | 
**nsInstanceId** | **String** | Identifier of the NS instance to which the operation applies.  | 
**lcmOperationType** | [**NsLcmOpType**](NsLcmOpType.md) | Type of the actual LCM operation represented by this lcm operation occurrence.  | 
**startTime** | **String** | Date-time of the start of the operation.  | 
**isAutomaticInvocation** | **Boolean** | Set to true if this NS LCM operation occurrence has been automatically triggered by the NFVO. This occurs in the case of auto-scaling, auto-healing and when a nested NS is modified as a result of an operation on its composite NS. Set to false otherwise.  | 
**operationParams** | [**OperationParamsEnum**](#OperationParamsEnum) | Input parameters of the LCM operation. This attribute shall be formatted according to the request data type of the related LCM operation. The following mapping between lcmOperationType and the data type of this attribute shall apply: - INSTANTIATE: InstantiateNsRequest - SCALE: ScaleNsRequest - UPDATE: UpdateNsRequest - HEAL: HealNsRequest - TERMINATE: TerminateNsRequest This attribute shall be present if this data type is returned in a response to reading an individual resource, and may be present according to the chosen attribute selector parameter if this data type is returned in a response to a query of a container resource.  |  [optional]
**isCancelPending** | **Boolean** | If the LCM operation occurrence is in \&quot;PROCESSING\&quot; or \&quot;ROLLING_BACK\&quot; state and the operation is being cancelled, this attribute shall be set to true. Otherwise, it shall be set to false.  | 
**cancelMode** | [**CancelModeType**](CancelModeType.md) | The mode of an ongoing cancellation. Shall be present when isCancelPending&#x3D;true, and shall be absent otherwise.  |  [optional]
**error** | [**ProblemDetails**](ProblemDetails.md) | If \&quot;operationState\&quot; is \&quot;FAILED_TEMP\&quot; or \&quot;FAILED\&quot; or \&quot;operationState\&quot; is \&quot;PROCESSING\&quot; or \&quot;ROLLING_BACK\&quot; and previous value of \&quot;operationState\&quot; was \&quot;FAILED_TEMP\&quot;, this attribute shall be present and contain error information, unless it has been requested to be excluded via an attribute selector.  |  [optional]
**resourceChanges** | [**NsLcmOpOccResourceChanges**](NsLcmOpOccResourceChanges.md) |  |  [optional]
**links** | [**NsLcmOpOccLinks**](NsLcmOpOccLinks.md) |  | 


<a name="OperationParamsEnum"></a>
## Enum: OperationParamsEnum
Name | Value
---- | -----
INSTANTIATE | &quot;INSTANTIATE&quot;
SCALE | &quot;SCALE&quot;
UPDATE | &quot;UPDATE&quot;
HEAL | &quot;HEAL&quot;
TERMINATE | &quot;TERMINATE&quot;



