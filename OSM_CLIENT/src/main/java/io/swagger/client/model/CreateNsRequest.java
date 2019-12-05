/*
 * OSM NB API featuring ETSI NFV SOL005
 * This is Open Source MANO Northbound API featuring ETSI NFV SOL005. For more information on OSM, you can visit [http://osm.etsi.org](http://osm.etsi.org). You can send us your comments and questions to OSM_TECH@list.etsi.org or join the [OpenSourceMANO Slack Workplace](https://join.slack.com/t/opensourcemano/shared_invite/enQtMzQ3MzYzNTQ0NDIyLWVkNTE4ZjZjNWI0ZTQyN2VhOTI1MjViMzU1NWYwMWM3ODI4NTQyY2VlODA2ZjczMWIyYTFkZWNiZmFkM2M2ZDk) 
 *
 * OpenAPI spec version: 1.0.0
 * Contact: OSM_TECH@list.etsi.org
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import java.util.UUID;
/**
 * CreateNsRequest
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2019-12-04T13:03:17.599+01:00[Europe/Rome]")
public class CreateNsRequest {
  @SerializedName("nsdId")
  private UUID nsdId = null;

  @SerializedName("nsName")
  private String nsName = null;

  @SerializedName("nsDescription")
  private String nsDescription = null;

  @SerializedName("vimAccountId")
  private UUID vimAccountId = null;

  public CreateNsRequest nsdId(UUID nsdId) {
    this.nsdId = nsdId;
    return this;
  }

   /**
   * Identifier of the NSD that defines the NS instance to be created. 
   * @return nsdId
  **/
  @Schema(required = true, description = "Identifier of the NSD that defines the NS instance to be created. ")
  public UUID getNsdId() {
    return nsdId;
  }

  public void setNsdId(UUID nsdId) {
    this.nsdId = nsdId;
  }

  public CreateNsRequest nsName(String nsName) {
    this.nsName = nsName;
    return this;
  }

   /**
   * Human-readable name of the NS instance to be created. 
   * @return nsName
  **/
  @Schema(required = true, description = "Human-readable name of the NS instance to be created. ")
  public String getNsName() {
    return nsName;
  }

  public void setNsName(String nsName) {
    this.nsName = nsName;
  }

  public CreateNsRequest nsDescription(String nsDescription) {
    this.nsDescription = nsDescription;
    return this;
  }

   /**
   * Human-readable description of the NS instance to be created. 
   * @return nsDescription
  **/
  @Schema(description = "Human-readable description of the NS instance to be created. ")
  public String getNsDescription() {
    return nsDescription;
  }

  public void setNsDescription(String nsDescription) {
    this.nsDescription = nsDescription;
  }

  public CreateNsRequest vimAccountId(UUID vimAccountId) {
    this.vimAccountId = vimAccountId;
    return this;
  }

   /**
   * Identifier of the VIM Account where the NS instance shall be created. 
   * @return vimAccountId
  **/
  @Schema(required = true, description = "Identifier of the VIM Account where the NS instance shall be created. ")
  public UUID getVimAccountId() {
    return vimAccountId;
  }

  public void setVimAccountId(UUID vimAccountId) {
    this.vimAccountId = vimAccountId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateNsRequest createNsRequest = (CreateNsRequest) o;
    return Objects.equals(this.nsdId, createNsRequest.nsdId) &&
        Objects.equals(this.nsName, createNsRequest.nsName) &&
        Objects.equals(this.nsDescription, createNsRequest.nsDescription) &&
        Objects.equals(this.vimAccountId, createNsRequest.vimAccountId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nsdId, nsName, nsDescription, vimAccountId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateNsRequest {\n");
    
    sb.append("    nsdId: ").append(toIndentedString(nsdId)).append("\n");
    sb.append("    nsName: ").append(toIndentedString(nsName)).append("\n");
    sb.append("    nsDescription: ").append(toIndentedString(nsDescription)).append("\n");
    sb.append("    vimAccountId: ").append(toIndentedString(vimAccountId)).append("\n");
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
