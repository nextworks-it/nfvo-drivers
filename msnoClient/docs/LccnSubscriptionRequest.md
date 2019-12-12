
# LccnSubscriptionRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**filter** | [**LifecycleChangeNotificationsFilter**](LifecycleChangeNotificationsFilter.md) | Filter settings for this subscription, to define the subset of all notifications this subscription relates to. A particular notification is sent to the subscriber if the filter matches, or if there is no filter.  |  [optional]
**callbackUri** | **String** | The URI of the endpoint to send the notification to.  | 
**authentication** | [**SubscriptionAuthentication**](SubscriptionAuthentication.md) | Authentication parameters to configure the use of Authorization when sending notifications corresponding to this subscription, as defined in clause 4.5.3.4. This attribute shall only be present if the subscriber requires authorization of notifications.  |  [optional]



