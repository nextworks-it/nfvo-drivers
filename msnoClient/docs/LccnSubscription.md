
# LccnSubscription

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **String** | Identifier of this subscription resource.  | 
**filter** | [**LifecycleChangeNotificationsFilter**](LifecycleChangeNotificationsFilter.md) | Filter settings for this subscription, to define the subset of all notifications this subscription relates to. A particular notification is sent to the subscriber if the filter matches, or if there is no filter.  |  [optional]
**callbackUri** | **String** | The URI of the endpoint to send the notification to.  | 
**links** | [**LccnSubscriptionLinks**](LccnSubscriptionLinks.md) |  | 



