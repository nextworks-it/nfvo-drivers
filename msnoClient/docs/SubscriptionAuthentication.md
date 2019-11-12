
# SubscriptionAuthentication

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**authType** | [**List&lt;AuthTypeEnum&gt;**](#List&lt;AuthTypeEnum&gt;) | Defines the types of Authentication / Authorization which the API consumer is willing to accept when receiving a notification. Permitted values: - BASIC: In every HTTP request to the notification endpoint, use   HTTP Basic authentication with the client credentials. - OAUTH2_CLIENT_CREDENTIALS: In every HTTP request to the   notification endpoint, use an OAuth 2.0 Bearer token, obtained   using the client credentials grant type. - TLS_CERT: Every HTTP request to the notification endpoint is sent   over a mutually authenticated TLS session, i.e. not only the   server is authenticated, but also the client is authenticated   during the TLS tunnel setup.  | 
**paramsBasic** | [**SubscriptionAuthenticationParamsBasic**](SubscriptionAuthenticationParamsBasic.md) |  |  [optional]
**paramsOauth2ClientCredentials** | [**SubscriptionAuthenticationParamsOauth2ClientCredentials**](SubscriptionAuthenticationParamsOauth2ClientCredentials.md) |  |  [optional]


<a name="List<AuthTypeEnum>"></a>
## Enum: List&lt;AuthTypeEnum&gt;
Name | Value
---- | -----
BASIC | &quot;BASIC&quot;
OAUTH2_CLIENT_CREDENTIALS | &quot;OAUTH2_CLIENT_CREDENTIALS&quot;
TLS_CERT | &quot;TLS_CERT&quot;



