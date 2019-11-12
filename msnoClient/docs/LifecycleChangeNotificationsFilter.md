
# LifecycleChangeNotificationsFilter

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**nsInstanceSubscriptionFilter** | [**NsInstanceSubscriptionFilter**](NsInstanceSubscriptionFilter.md) | Filter criteria to select NS instances about which to notify.  |  [optional]
**notificationTypes** | [**List&lt;NotificationTypesEnum&gt;**](#List&lt;NotificationTypesEnum&gt;) | Match particular notification types. Permitted values: - NsLcmOperationOccurenceNotification - NsIdentifierCreationNotification - NsIdentifierDeletionNotification - NsChangeNotification  |  [optional]
**operationTypes** | [**List&lt;NsLcmOpType&gt;**](NsLcmOpType.md) | Match particular NS lifecycle operation types for the notification of type NsLcmOperationOccurrenceNotification. May be present if the \&quot;notificationTypes\&quot; attribute contains the value \&quot;NsLcmOperationOccurrenceNotification\&quot;, and shall be absent otherwise.  |  [optional]
**operationStates** | [**List&lt;LcmOperationStateType&gt;**](LcmOperationStateType.md) | Match particular LCM operation state values as reported in notifications of type NsLcmOperationOccurrenceNotification. May be present if the \&quot;notificationTypes\&quot; attribute contains the value \&quot;NsLcmOperationOccurrenceNotification\&quot;, and shall be absent otherwise.  |  [optional]
**nsComponentTypes** | [**List&lt;NsComponentType&gt;**](NsComponentType.md) | Match particular NS component types for the notification of type NsChangeNotification. May be present if the \&quot;notificationTypes\&quot; attribute contains the value \&quot;NsChang.  |  [optional]
**lcmOpNameImpactingNsComponent** | [**List&lt;LcmOpNameForChangeNotificationType&gt;**](LcmOpNameForChangeNotificationType.md) | Match particular LCM operation names for the notification of type NsChangeNotification. May be present if the \&quot;notificationTypes\&quot; attribute contains the value \&quot;NsChangeNotification\&quot;, and shall be absent otherwise.  |  [optional]
**lcmOpOccStatusImpactingNsComponent** | [**List&lt;LcmOpOccStatusForChangeNotificationType&gt;**](LcmOpOccStatusForChangeNotificationType.md) | Match particular LCM operation status values as reported in notifications of type NsChangeNotification. May be present if the \&quot;notificationTypes\&quot; attribute contains the value \&quot;NsChangeNotification\&quot;, and shall be absent otherwise.  |  [optional]


<a name="List<NotificationTypesEnum>"></a>
## Enum: List&lt;NotificationTypesEnum&gt;
Name | Value
---- | -----
NSLCMOPERATIONOCCURENCENOTIFICATION | &quot;NsLcmOperationOccurenceNotification&quot;
NSIDENTIFIERCREATIONNOTIFICATION | &quot;NsIdentifierCreationNotification&quot;
NSIDENTIFIERDELETIONNOTIFICATION | &quot;NsIdentifierDeletionNotification&quot;
NSCHANGENOTIFICATION | &quot;NsChangeNotification&quot;



