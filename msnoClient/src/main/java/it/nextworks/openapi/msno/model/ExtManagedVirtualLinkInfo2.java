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
import it.nextworks.openapi.msno.model.ResourceHandle;
import it.nextworks.openapi.msno.model.VnfLinkPortInfo2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ExtManagedVirtualLinkInfo2
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-11-08T16:52:33.422+01:00")
public class ExtManagedVirtualLinkInfo2 {
  @SerializedName("id")
  private String id = null;

  @SerializedName("vnfVirtualLinkDescId")
  private String vnfVirtualLinkDescId = null;

  @SerializedName("networkResource")
  private ResourceHandle networkResource = null;

  @SerializedName("vnfLinkPorts")
  private List<VnfLinkPortInfo2> vnfLinkPorts = null;

  public ExtManagedVirtualLinkInfo2 id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Identifier of the externally-managed internal VL and the related externally-managed VL information instance. The identifier is assigned by the NFV-MANO entity that manages this VL instance. 
   * @return id
  **/
  @ApiModelProperty(required = true, value = "Identifier of the externally-managed internal VL and the related externally-managed VL information instance. The identifier is assigned by the NFV-MANO entity that manages this VL instance. ")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ExtManagedVirtualLinkInfo2 vnfVirtualLinkDescId(String vnfVirtualLinkDescId) {
    this.vnfVirtualLinkDescId = vnfVirtualLinkDescId;
    return this;
  }

   /**
   * Identifier of the VNF Virtual Link Descriptor (VLD) in the VNFD. 
   * @return vnfVirtualLinkDescId
  **/
  @ApiModelProperty(required = true, value = "Identifier of the VNF Virtual Link Descriptor (VLD) in the VNFD. ")
  public String getVnfVirtualLinkDescId() {
    return vnfVirtualLinkDescId;
  }

  public void setVnfVirtualLinkDescId(String vnfVirtualLinkDescId) {
    this.vnfVirtualLinkDescId = vnfVirtualLinkDescId;
  }

  public ExtManagedVirtualLinkInfo2 networkResource(ResourceHandle networkResource) {
    this.networkResource = networkResource;
    return this;
  }

   /**
   * Reference to the VirtualNetwork resource. 
   * @return networkResource
  **/
  @ApiModelProperty(value = "Reference to the VirtualNetwork resource. ")
  public ResourceHandle getNetworkResource() {
    return networkResource;
  }

  public void setNetworkResource(ResourceHandle networkResource) {
    this.networkResource = networkResource;
  }

  public ExtManagedVirtualLinkInfo2 vnfLinkPorts(List<VnfLinkPortInfo2> vnfLinkPorts) {
    this.vnfLinkPorts = vnfLinkPorts;
    return this;
  }

  public ExtManagedVirtualLinkInfo2 addVnfLinkPortsItem(VnfLinkPortInfo2 vnfLinkPortsItem) {
    if (this.vnfLinkPorts == null) {
      this.vnfLinkPorts = new ArrayList<VnfLinkPortInfo2>();
    }
    this.vnfLinkPorts.add(vnfLinkPortsItem);
    return this;
  }

   /**
   * Link ports of this VL. 
   * @return vnfLinkPorts
  **/
  @ApiModelProperty(value = "Link ports of this VL. ")
  public List<VnfLinkPortInfo2> getVnfLinkPorts() {
    return vnfLinkPorts;
  }

  public void setVnfLinkPorts(List<VnfLinkPortInfo2> vnfLinkPorts) {
    this.vnfLinkPorts = vnfLinkPorts;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExtManagedVirtualLinkInfo2 extManagedVirtualLinkInfo2 = (ExtManagedVirtualLinkInfo2) o;
    return Objects.equals(this.id, extManagedVirtualLinkInfo2.id) &&
        Objects.equals(this.vnfVirtualLinkDescId, extManagedVirtualLinkInfo2.vnfVirtualLinkDescId) &&
        Objects.equals(this.networkResource, extManagedVirtualLinkInfo2.networkResource) &&
        Objects.equals(this.vnfLinkPorts, extManagedVirtualLinkInfo2.vnfLinkPorts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, vnfVirtualLinkDescId, networkResource, vnfLinkPorts);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExtManagedVirtualLinkInfo2 {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    vnfVirtualLinkDescId: ").append(toIndentedString(vnfVirtualLinkDescId)).append("\n");
    sb.append("    networkResource: ").append(toIndentedString(networkResource)).append("\n");
    sb.append("    vnfLinkPorts: ").append(toIndentedString(vnfLinkPorts)).append("\n");
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

