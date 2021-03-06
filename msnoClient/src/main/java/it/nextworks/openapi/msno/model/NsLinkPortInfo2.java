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
import it.nextworks.openapi.msno.model.NsCpHandle2;
import it.nextworks.openapi.msno.model.ResourceHandle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This type represents information about a link port of a VL instance. It shall comply with the provisions defined in Table 6.5.3.55-1. 
 */
@ApiModel(description = "This type represents information about a link port of a VL instance. It shall comply with the provisions defined in Table 6.5.3.55-1. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-11-08T16:52:33.422+01:00")
public class NsLinkPortInfo2 {
  @SerializedName("id")
  private String id = null;

  @SerializedName("resourceHandle")
  private ResourceHandle resourceHandle = null;

  @SerializedName("nsCpHandle")
  private List<NsCpHandle2> nsCpHandle = null;

  public NsLinkPortInfo2 id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Identifier of this link port as provided by the entity that has created the link port. 
   * @return id
  **/
  @ApiModelProperty(required = true, value = "Identifier of this link port as provided by the entity that has created the link port. ")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public NsLinkPortInfo2 resourceHandle(ResourceHandle resourceHandle) {
    this.resourceHandle = resourceHandle;
    return this;
  }

   /**
   * Identifier of the virtualised network resource realizing this link port. 
   * @return resourceHandle
  **/
  @ApiModelProperty(required = true, value = "Identifier of the virtualised network resource realizing this link port. ")
  public ResourceHandle getResourceHandle() {
    return resourceHandle;
  }

  public void setResourceHandle(ResourceHandle resourceHandle) {
    this.resourceHandle = resourceHandle;
  }

  public NsLinkPortInfo2 nsCpHandle(List<NsCpHandle2> nsCpHandle) {
    this.nsCpHandle = nsCpHandle;
    return this;
  }

  public NsLinkPortInfo2 addNsCpHandleItem(NsCpHandle2 nsCpHandleItem) {
    if (this.nsCpHandle == null) {
      this.nsCpHandle = new ArrayList<NsCpHandle2>();
    }
    this.nsCpHandle.add(nsCpHandleItem);
    return this;
  }

   /**
   * Identifier of the CP/SAP instance to be connected to this link port. The value refers to a vnfExtCpInfo item in the VnfInstance, or a pnfExtCpInfo item in the PnfInfo, or a sapInfo item in the NS instance. There shall be at most one link port associated with any connection point instance. 
   * @return nsCpHandle
  **/
  @ApiModelProperty(value = "Identifier of the CP/SAP instance to be connected to this link port. The value refers to a vnfExtCpInfo item in the VnfInstance, or a pnfExtCpInfo item in the PnfInfo, or a sapInfo item in the NS instance. There shall be at most one link port associated with any connection point instance. ")
  public List<NsCpHandle2> getNsCpHandle() {
    return nsCpHandle;
  }

  public void setNsCpHandle(List<NsCpHandle2> nsCpHandle) {
    this.nsCpHandle = nsCpHandle;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NsLinkPortInfo2 nsLinkPortInfo2 = (NsLinkPortInfo2) o;
    return Objects.equals(this.id, nsLinkPortInfo2.id) &&
        Objects.equals(this.resourceHandle, nsLinkPortInfo2.resourceHandle) &&
        Objects.equals(this.nsCpHandle, nsLinkPortInfo2.nsCpHandle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, resourceHandle, nsCpHandle);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NsLinkPortInfo2 {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    resourceHandle: ").append(toIndentedString(resourceHandle)).append("\n");
    sb.append("    nsCpHandle: ").append(toIndentedString(nsCpHandle)).append("\n");
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

