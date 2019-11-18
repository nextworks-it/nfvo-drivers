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
import it.nextworks.openapi.msno.model.CpProtocolData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This type represents an externally provided link port or network address information per instance of an external connection point. In case a link port is provided, the VNFM shall use that link port when connecting the external CP to the external VL. In a link port is not provided, the VNFM shall create a link port on the external VL, and use that link port to connect the external CP to the external VL. 
 */
@ApiModel(description = "This type represents an externally provided link port or network address information per instance of an external connection point. In case a link port is provided, the VNFM shall use that link port when connecting the external CP to the external VL. In a link port is not provided, the VNFM shall create a link port on the external VL, and use that link port to connect the external CP to the external VL. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-11-08T16:52:33.422+01:00")
public class VnfExtCpConfig {
  @SerializedName("cpInstanceId")
  private String cpInstanceId = null;

  @SerializedName("linkPortId")
  private String linkPortId = null;

  @SerializedName("cpProtocolData")
  private List<CpProtocolData> cpProtocolData = null;

  public VnfExtCpConfig cpInstanceId(String cpInstanceId) {
    this.cpInstanceId = cpInstanceId;
    return this;
  }

   /**
   * Identifier of the external CP instance to which this set of configuration parameters is requested to be applied. Shall be present if this instance has already been created. 
   * @return cpInstanceId
  **/
  @ApiModelProperty(value = "Identifier of the external CP instance to which this set of configuration parameters is requested to be applied. Shall be present if this instance has already been created. ")
  public String getCpInstanceId() {
    return cpInstanceId;
  }

  public void setCpInstanceId(String cpInstanceId) {
    this.cpInstanceId = cpInstanceId;
  }

  public VnfExtCpConfig linkPortId(String linkPortId) {
    this.linkPortId = linkPortId;
    return this;
  }

   /**
   * Identifier of a pre-configured link port to which the external CP will be associated. The following conditions apply to the attributes \&quot;linkPortId\&quot; and \&quot;cpProtocolData\&quot;: * The \&quot;linkPortId\&quot; and \&quot;cpProtocolData\&quot; attributes shall both be   absent for the deletion of an existing external CP instance   addressed by cpInstanceId. * At least one of these attributes shall be present for a   to-be-created external CP instance or an existing external   CP instance. * If the \&quot;linkPortId\&quot; attribute is absent, the VNFM shall create a   link port. * If the \&quot;cpProtocolData\&quot; attribute is absent, the \&quot;linkPortId\&quot;   attribute shall be provided referencing a pre-created link port,   and the VNFM can use means outside the scope of the present   document to obtain the pre-configured address information for the   connection point from the resource representing the link port. * If both \&quot;cpProtocolData\&quot; and \&quot;linkportId\&quot; are provided, the API   consumer shall ensure that the cpProtocolData can be used with the   pre-created link port referenced by \&quot;linkPortId\&quot;. 
   * @return linkPortId
  **/
  @ApiModelProperty(value = "Identifier of a pre-configured link port to which the external CP will be associated. The following conditions apply to the attributes \"linkPortId\" and \"cpProtocolData\": * The \"linkPortId\" and \"cpProtocolData\" attributes shall both be   absent for the deletion of an existing external CP instance   addressed by cpInstanceId. * At least one of these attributes shall be present for a   to-be-created external CP instance or an existing external   CP instance. * If the \"linkPortId\" attribute is absent, the VNFM shall create a   link port. * If the \"cpProtocolData\" attribute is absent, the \"linkPortId\"   attribute shall be provided referencing a pre-created link port,   and the VNFM can use means outside the scope of the present   document to obtain the pre-configured address information for the   connection point from the resource representing the link port. * If both \"cpProtocolData\" and \"linkportId\" are provided, the API   consumer shall ensure that the cpProtocolData can be used with the   pre-created link port referenced by \"linkPortId\". ")
  public String getLinkPortId() {
    return linkPortId;
  }

  public void setLinkPortId(String linkPortId) {
    this.linkPortId = linkPortId;
  }

  public VnfExtCpConfig cpProtocolData(List<CpProtocolData> cpProtocolData) {
    this.cpProtocolData = cpProtocolData;
    return this;
  }

  public VnfExtCpConfig addCpProtocolDataItem(CpProtocolData cpProtocolDataItem) {
    if (this.cpProtocolData == null) {
      this.cpProtocolData = new ArrayList<CpProtocolData>();
    }
    this.cpProtocolData.add(cpProtocolDataItem);
    return this;
  }

   /**
   * Parameters for configuring the network protocols on the link port that connects the CP to a VL. The following conditions apply to the attributes \&quot;linkPortId\&quot; and \&quot;cpProtocolData\&quot;: * The \&quot;linkPortId\&quot; and \&quot;cpProtocolData\&quot; attributes shall both be   absent for the deletion of an existing external CP instance   addressed by cpInstanceId. * At least one of these attributes shall be present for a   to-be-created external CP instance or an existing external   CP instance. * If the \&quot;linkPortId\&quot; attribute is absent, the VNFM shall create a   link port. * If the \&quot;cpProtocolData\&quot; attribute is absent, the \&quot;linkPortId\&quot;   attribute shall be provided referencing a pre-created link port,   and the VNFM can use means outside the scope of the present   document to obtain the pre-configured address information for the   connection point from the resource representing the link port. * If both \&quot;cpProtocolData\&quot; and \&quot;linkportId\&quot; are provided, the API   consumer shall ensure that the cpProtocolData can be used with the   pre-created link port referenced by \&quot;linkPortId\&quot;. 
   * @return cpProtocolData
  **/
  @ApiModelProperty(value = "Parameters for configuring the network protocols on the link port that connects the CP to a VL. The following conditions apply to the attributes \"linkPortId\" and \"cpProtocolData\": * The \"linkPortId\" and \"cpProtocolData\" attributes shall both be   absent for the deletion of an existing external CP instance   addressed by cpInstanceId. * At least one of these attributes shall be present for a   to-be-created external CP instance or an existing external   CP instance. * If the \"linkPortId\" attribute is absent, the VNFM shall create a   link port. * If the \"cpProtocolData\" attribute is absent, the \"linkPortId\"   attribute shall be provided referencing a pre-created link port,   and the VNFM can use means outside the scope of the present   document to obtain the pre-configured address information for the   connection point from the resource representing the link port. * If both \"cpProtocolData\" and \"linkportId\" are provided, the API   consumer shall ensure that the cpProtocolData can be used with the   pre-created link port referenced by \"linkPortId\". ")
  public List<CpProtocolData> getCpProtocolData() {
    return cpProtocolData;
  }

  public void setCpProtocolData(List<CpProtocolData> cpProtocolData) {
    this.cpProtocolData = cpProtocolData;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VnfExtCpConfig vnfExtCpConfig = (VnfExtCpConfig) o;
    return Objects.equals(this.cpInstanceId, vnfExtCpConfig.cpInstanceId) &&
        Objects.equals(this.linkPortId, vnfExtCpConfig.linkPortId) &&
        Objects.equals(this.cpProtocolData, vnfExtCpConfig.cpProtocolData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cpInstanceId, linkPortId, cpProtocolData);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VnfExtCpConfig {\n");
    
    sb.append("    cpInstanceId: ").append(toIndentedString(cpInstanceId)).append("\n");
    sb.append("    linkPortId: ").append(toIndentedString(linkPortId)).append("\n");
    sb.append("    cpProtocolData: ").append(toIndentedString(cpProtocolData)).append("\n");
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
