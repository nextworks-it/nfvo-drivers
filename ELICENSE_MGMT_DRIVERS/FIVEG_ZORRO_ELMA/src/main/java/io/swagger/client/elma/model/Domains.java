/*
 * 5GZorro eLicense Manager Core Swagger
 * Swagger 5GZorro for the centralized eLicense Manager System of the project
 *
 * OpenAPI spec version: 0.1.0
 * Contact: guillermo.gomez.external@atos.net
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.elma.model;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;
/**
 * Domains
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-03-10T18:02:02.820+01:00[Europe/Rome]")
public class Domains {
  @SerializedName("domainDID")
  private String domainDID = null;


  @SerializedName("ns")
  private List<NetworkServiceInstance> nsi = new ArrayList<>();


  public Domains domainDID(String domainDID) {
    this.domainDID = domainDID;
    return this;
  }


  public void addNsi(String nsdId,  String nstId, String nsInstanceId, String tenant ){
    nsi.add(new NetworkServiceInstance(nsInstanceId, nsdId, tenant));
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Domains domains = (Domains) o;
    return Objects.equals(this.domainDID, domains.domainDID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(domainDID);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Domains {\n");
    
    sb.append("    domainDID: ").append(toIndentedString(domainDID)).append("\n");
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
