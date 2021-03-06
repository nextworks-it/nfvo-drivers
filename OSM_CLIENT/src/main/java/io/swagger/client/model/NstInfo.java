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
 * NetSlice Template Information Only generic fields (_id, id, name) are described For a full specification of the NetSlice Template see: http://osm-download.etsi.org/ftp/osm-doc/nst.html 
 */
@Schema(description = "NetSlice Template Information Only generic fields (_id, id, name) are described For a full specification of the NetSlice Template see: http://osm-download.etsi.org/ftp/osm-doc/nst.html ")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2019-12-04T13:03:17.599+01:00[Europe/Rome]")
public class NstInfo {
  @SerializedName("_id")
  private UUID _id = null;

  @SerializedName("id")
  private String id = null;

  @SerializedName("name")
  private String name = null;

  public NstInfo _id(UUID _id) {
    this._id = _id;
    return this;
  }

   /**
   * NetSlice Template Identifier
   * @return _id
  **/
  @Schema(description = "NetSlice Template Identifier")
  public UUID getIdentifier() {
    return _id;
  }

  public void setIdentifier(UUID _id) {
    this._id = _id;
  }

  public NstInfo id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Human readable NetSlice Template Identifier
   * @return id
  **/
  @Schema(description = "Human readable NetSlice Template Identifier")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public NstInfo name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Human readable name of the NetSlice Template
   * @return name
  **/
  @Schema(description = "Human readable name of the NetSlice Template")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NstInfo nstInfo = (NstInfo) o;
    return Objects.equals(this._id, nstInfo._id) &&
        Objects.equals(this.id, nstInfo.id) &&
        Objects.equals(this.name, nstInfo.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_id, id, name);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NstInfo {\n");
    
    sb.append("    _id: ").append(toIndentedString(_id)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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
