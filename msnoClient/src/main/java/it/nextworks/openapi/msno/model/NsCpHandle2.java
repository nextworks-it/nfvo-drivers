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
 * This type represents an identifier of the CP or SAP instance. It shall comply with the provisions defined in Table 6.5.3.56-1. 
 */
@ApiModel(description = "This type represents an identifier of the CP or SAP instance. It shall comply with the provisions defined in Table 6.5.3.56-1. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-11-08T16:52:33.422+01:00")
public class NsCpHandle2 {
  @SerializedName("vnfInstanceId")
  private String vnfInstanceId = null;

  @SerializedName("vnfExtCpInstanceId")
  private String vnfExtCpInstanceId = null;

  @SerializedName("pnfInfoId")
  private String pnfInfoId = null;

  @SerializedName("pnfExtCpInstanceId")
  private String pnfExtCpInstanceId = null;

  @SerializedName("nsInstanceId")
  private String nsInstanceId = null;

  @SerializedName("nsSapInstanceId")
  private String nsSapInstanceId = null;

  public NsCpHandle2 vnfInstanceId(String vnfInstanceId) {
    this.vnfInstanceId = vnfInstanceId;
    return this;
  }

   /**
   * Identifier of the VNF instance associated to the CP instance. This attribute shall be present if the CP instance is VNF external CP. 
   * @return vnfInstanceId
  **/
  @ApiModelProperty(value = "Identifier of the VNF instance associated to the CP instance. This attribute shall be present if the CP instance is VNF external CP. ")
  public String getVnfInstanceId() {
    return vnfInstanceId;
  }

  public void setVnfInstanceId(String vnfInstanceId) {
    this.vnfInstanceId = vnfInstanceId;
  }

  public NsCpHandle2 vnfExtCpInstanceId(String vnfExtCpInstanceId) {
    this.vnfExtCpInstanceId = vnfExtCpInstanceId;
    return this;
  }

   /**
   * Identifier of the VNF external CP instance in the scope of the VNF instance. This attribute shall be present if the CP instance is VNF external CP. See notes 1 and 4. 
   * @return vnfExtCpInstanceId
  **/
  @ApiModelProperty(value = "Identifier of the VNF external CP instance in the scope of the VNF instance. This attribute shall be present if the CP instance is VNF external CP. See notes 1 and 4. ")
  public String getVnfExtCpInstanceId() {
    return vnfExtCpInstanceId;
  }

  public void setVnfExtCpInstanceId(String vnfExtCpInstanceId) {
    this.vnfExtCpInstanceId = vnfExtCpInstanceId;
  }

  public NsCpHandle2 pnfInfoId(String pnfInfoId) {
    this.pnfInfoId = pnfInfoId;
    return this;
  }

   /**
   * Identifier of the PNF instance associated to the CP instance. This attribute shall be present if the CP instance is PNF external CP. See notes 2 and 4. 
   * @return pnfInfoId
  **/
  @ApiModelProperty(value = "Identifier of the PNF instance associated to the CP instance. This attribute shall be present if the CP instance is PNF external CP. See notes 2 and 4. ")
  public String getPnfInfoId() {
    return pnfInfoId;
  }

  public void setPnfInfoId(String pnfInfoId) {
    this.pnfInfoId = pnfInfoId;
  }

  public NsCpHandle2 pnfExtCpInstanceId(String pnfExtCpInstanceId) {
    this.pnfExtCpInstanceId = pnfExtCpInstanceId;
    return this;
  }

   /**
   * Identifier of the PNF external CP instance in the scope of the PNF. This attribute shall be present if the CP instance is PNF external CP. See notes 2 and 4. 
   * @return pnfExtCpInstanceId
  **/
  @ApiModelProperty(value = "Identifier of the PNF external CP instance in the scope of the PNF. This attribute shall be present if the CP instance is PNF external CP. See notes 2 and 4. ")
  public String getPnfExtCpInstanceId() {
    return pnfExtCpInstanceId;
  }

  public void setPnfExtCpInstanceId(String pnfExtCpInstanceId) {
    this.pnfExtCpInstanceId = pnfExtCpInstanceId;
  }

  public NsCpHandle2 nsInstanceId(String nsInstanceId) {
    this.nsInstanceId = nsInstanceId;
    return this;
  }

   /**
   * Identifier of the NS instance associated to the SAP instance. This attribute shall be present if the CP instance is NS SAP. See notes 3 and 4. 
   * @return nsInstanceId
  **/
  @ApiModelProperty(value = "Identifier of the NS instance associated to the SAP instance. This attribute shall be present if the CP instance is NS SAP. See notes 3 and 4. ")
  public String getNsInstanceId() {
    return nsInstanceId;
  }

  public void setNsInstanceId(String nsInstanceId) {
    this.nsInstanceId = nsInstanceId;
  }

  public NsCpHandle2 nsSapInstanceId(String nsSapInstanceId) {
    this.nsSapInstanceId = nsSapInstanceId;
    return this;
  }

   /**
   * Identifier of the SAP instance in the scope of the NS instance. This attribute shall be present if the CP instance is NS SAP. See notes 3 and 4. 
   * @return nsSapInstanceId
  **/
  @ApiModelProperty(value = "Identifier of the SAP instance in the scope of the NS instance. This attribute shall be present if the CP instance is NS SAP. See notes 3 and 4. ")
  public String getNsSapInstanceId() {
    return nsSapInstanceId;
  }

  public void setNsSapInstanceId(String nsSapInstanceId) {
    this.nsSapInstanceId = nsSapInstanceId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NsCpHandle2 nsCpHandle2 = (NsCpHandle2) o;
    return Objects.equals(this.vnfInstanceId, nsCpHandle2.vnfInstanceId) &&
        Objects.equals(this.vnfExtCpInstanceId, nsCpHandle2.vnfExtCpInstanceId) &&
        Objects.equals(this.pnfInfoId, nsCpHandle2.pnfInfoId) &&
        Objects.equals(this.pnfExtCpInstanceId, nsCpHandle2.pnfExtCpInstanceId) &&
        Objects.equals(this.nsInstanceId, nsCpHandle2.nsInstanceId) &&
        Objects.equals(this.nsSapInstanceId, nsCpHandle2.nsSapInstanceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vnfInstanceId, vnfExtCpInstanceId, pnfInfoId, pnfExtCpInstanceId, nsInstanceId, nsSapInstanceId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NsCpHandle2 {\n");
    
    sb.append("    vnfInstanceId: ").append(toIndentedString(vnfInstanceId)).append("\n");
    sb.append("    vnfExtCpInstanceId: ").append(toIndentedString(vnfExtCpInstanceId)).append("\n");
    sb.append("    pnfInfoId: ").append(toIndentedString(pnfInfoId)).append("\n");
    sb.append("    pnfExtCpInstanceId: ").append(toIndentedString(pnfExtCpInstanceId)).append("\n");
    sb.append("    nsInstanceId: ").append(toIndentedString(nsInstanceId)).append("\n");
    sb.append("    nsSapInstanceId: ").append(toIndentedString(nsSapInstanceId)).append("\n");
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
