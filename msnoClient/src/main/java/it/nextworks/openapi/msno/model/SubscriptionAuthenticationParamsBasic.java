/*
 * SOL005 - NS Lifecycle Management Interface
 * SOL005 - NS Lifecycle Management Interface IMPORTANT: Please note that this file might be not aligned to the current version of the ETSI Group Specification it refers to and has not been approved by the ETSI NFV ISG. In case of discrepancies the published ETSI Group Specification takes precedence. Please report bugs to https://forge.etsi.org/bugzilla/buglist.cgi?component=Nfv-Openapis 
 *
 * OpenAPI spec version: 1.1.0-impl:etsi.org:ETSI_NFV_OpenAPI:1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package it.nextworks.openapi.msno.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * Parameters for authentication/authorization using BASIC. Shall be present if authType is \&quot;BASIC\&quot; and the contained information has not been provisioned out of band. Shall be absent otherwise. 
 */
@ApiModel(description = "Parameters for authentication/authorization using BASIC. Shall be present if authType is \"BASIC\" and the contained information has not been provisioned out of band. Shall be absent otherwise. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-11-08T16:52:33.422+01:00")
public class SubscriptionAuthenticationParamsBasic {
  @SerializedName("userName")
  private String userName = null;

  @SerializedName("password")
  private String password = null;

  public SubscriptionAuthenticationParamsBasic userName(String userName) {
    this.userName = userName;
    return this;
  }

   /**
   * Username to be used in HTTP Basic authentication. Shall be present if it has not been provisioned out of band. 
   * @return userName
  **/
  @ApiModelProperty(value = "Username to be used in HTTP Basic authentication. Shall be present if it has not been provisioned out of band. ")
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public SubscriptionAuthenticationParamsBasic password(String password) {
    this.password = password;
    return this;
  }

   /**
   * Password to be used in HTTP Basic authentication. Shall be present if it has not been provisioned out of band. 
   * @return password
  **/
  @ApiModelProperty(value = "Password to be used in HTTP Basic authentication. Shall be present if it has not been provisioned out of band. ")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubscriptionAuthenticationParamsBasic subscriptionAuthenticationParamsBasic = (SubscriptionAuthenticationParamsBasic) o;
    return Objects.equals(this.userName, subscriptionAuthenticationParamsBasic.userName) &&
        Objects.equals(this.password, subscriptionAuthenticationParamsBasic.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userName, password);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubscriptionAuthenticationParamsBasic {\n");
    
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

