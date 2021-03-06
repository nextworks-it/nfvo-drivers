/*
 * Copyright 2018 Nextworks s.r.l.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.nextworks.nfvmano.libs.fivegcatalogueclient.sol005.nsdmanagement.elements;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * This type represents a subscription request related to notifications about
 * NSD management.
 */
@ApiModel(description = "This type represents a subscription request related to notifications about NSD management.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-07-23T16:31:35.952+02:00")

public class NsdmSubscriptionRequest {
    @JsonProperty("filter")
    private NsdmNotificationsFilter filter = null;

    @JsonProperty("callbackUri")
    private String callbackUri = null;

    @JsonProperty("authentication")
    private SubscriptionAuthentication authentication = null;

    public NsdmSubscriptionRequest filter(NsdmNotificationsFilter filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Get filter
     *
     * @return filter
     **/
    @ApiModelProperty(value = "")

    @Valid

    public NsdmNotificationsFilter getFilter() {
        return filter;
    }

    public void setFilter(NsdmNotificationsFilter filter) {
        this.filter = filter;
    }

    public NsdmSubscriptionRequest callbackUri(String callbackUri) {
        this.callbackUri = callbackUri;
        return this;
    }

    /**
     * The URI of the endpoint to send the notification to.
     *
     * @return callbackUri
     **/
    @ApiModelProperty(required = true, value = "The URI of the endpoint to send the notification to.")
    @NotNull

    public String getCallbackUri() {
        return callbackUri;
    }

    public void setCallbackUri(String callbackUri) {
        this.callbackUri = callbackUri;
    }

    public NsdmSubscriptionRequest authentication(SubscriptionAuthentication authentication) {
        this.authentication = authentication;
        return this;
    }

    /**
     * Get authentication
     *
     * @return authentication
     **/
    @ApiModelProperty(value = "")

    @Valid

    public SubscriptionAuthentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(SubscriptionAuthentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NsdmSubscriptionRequest nsdmSubscriptionRequest = (NsdmSubscriptionRequest) o;
        return Objects.equals(this.filter, nsdmSubscriptionRequest.filter)
                && Objects.equals(this.callbackUri, nsdmSubscriptionRequest.callbackUri)
                && Objects.equals(this.authentication, nsdmSubscriptionRequest.authentication);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filter, callbackUri, authentication);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NsdmSubscriptionRequest {\n");

        sb.append("    filter: ").append(toIndentedString(filter)).append("\n");
        sb.append("    callbackUri: ").append(toIndentedString(callbackUri)).append("\n");
        sb.append("    authentication: ").append(toIndentedString(authentication)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
