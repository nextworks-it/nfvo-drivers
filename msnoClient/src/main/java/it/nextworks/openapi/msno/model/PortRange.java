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
 * The PortRange data type provides the lower and upper bounds of a range of Internet ports. It shall comply with the provisions defined in Table 6.5.3.42-1. 
 */
@ApiModel(description = "The PortRange data type provides the lower and upper bounds of a range of Internet ports. It shall comply with the provisions defined in Table 6.5.3.42-1. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-11-08T16:52:33.422+01:00")
public class PortRange {
  @SerializedName("lowerPort")
  private Integer lowerPort = null;

  @SerializedName("upperPort")
  private Integer upperPort = null;

  public PortRange lowerPort(Integer lowerPort) {
    this.lowerPort = lowerPort;
    return this;
  }

   /**
   * Identifies the lower bound of the port range. upperPort Integer 
   * minimum: 0
   * @return lowerPort
  **/
  @ApiModelProperty(required = true, value = "Identifies the lower bound of the port range. upperPort Integer ")
  public Integer getLowerPort() {
    return lowerPort;
  }

  public void setLowerPort(Integer lowerPort) {
    this.lowerPort = lowerPort;
  }

  public PortRange upperPort(Integer upperPort) {
    this.upperPort = upperPort;
    return this;
  }

   /**
   * Identifies the upper bound of the port range. 
   * minimum: 0
   * @return upperPort
  **/
  @ApiModelProperty(required = true, value = "Identifies the upper bound of the port range. ")
  public Integer getUpperPort() {
    return upperPort;
  }

  public void setUpperPort(Integer upperPort) {
    this.upperPort = upperPort;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PortRange portRange = (PortRange) o;
    return Objects.equals(this.lowerPort, portRange.lowerPort) &&
        Objects.equals(this.upperPort, portRange.upperPort);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lowerPort, upperPort);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PortRange {\n");
    
    sb.append("    lowerPort: ").append(toIndentedString(lowerPort)).append("\n");
    sb.append("    upperPort: ").append(toIndentedString(upperPort)).append("\n");
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
