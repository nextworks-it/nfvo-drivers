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
import it.nextworks.openapi.msno.model.CpPairInfo;
import it.nextworks.openapi.msno.model.ForwardingBehaviourInputParameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This type represents describes a group of CPs and/or SAPs pairs associated to the same position in an NFP. It shall comply with the provisions defined in Table 6.5.3.71-1. 
 */
@ApiModel(description = "This type represents describes a group of CPs and/or SAPs pairs associated to the same position in an NFP. It shall comply with the provisions defined in Table 6.5.3.71-1. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-11-08T16:52:33.422+01:00")
public class CpGroupInfo {
  @SerializedName("cpPairInfo")
  private List<CpPairInfo> cpPairInfo = null;

  /**
   * Identifies a rule to apply to forward traffic to the ingress CPs or SAPs of the group. Permitted values: * ALL &#x3D; Traffic flows shall be forwarded simultaneously to all CPs or SAPs of the group. * LB &#x3D; Traffic flows shall be forwarded to one CP or SAP of the group selected based on a loadbalancing algorithm. 
   */
  @JsonAdapter(ForwardingBehaviourEnum.Adapter.class)
  public enum ForwardingBehaviourEnum {
    ALL("ALL"),
    
    LB("LB");

    private String value;

    ForwardingBehaviourEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static ForwardingBehaviourEnum fromValue(String text) {
      for (ForwardingBehaviourEnum b : ForwardingBehaviourEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<ForwardingBehaviourEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ForwardingBehaviourEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public ForwardingBehaviourEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return ForwardingBehaviourEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("forwardingBehaviour")
  private ForwardingBehaviourEnum forwardingBehaviour = null;

  @SerializedName("forwardingBehaviourInputParameters")
  private ForwardingBehaviourInputParameters forwardingBehaviourInputParameters = null;

  public CpGroupInfo cpPairInfo(List<CpPairInfo> cpPairInfo) {
    this.cpPairInfo = cpPairInfo;
    return this;
  }

  public CpGroupInfo addCpPairInfoItem(CpPairInfo cpPairInfoItem) {
    if (this.cpPairInfo == null) {
      this.cpPairInfo = new ArrayList<CpPairInfo>();
    }
    this.cpPairInfo.add(cpPairInfoItem);
    return this;
  }

   /**
   * One or more pair(s) of ingress and egress CPs or SAPs which the NFP passes by. All CP or SAP pairs in a group shall be instantiated from connection point descriptors or service access point descriptors referenced in the corresponding NfpPositionDesc. 
   * @return cpPairInfo
  **/
  @ApiModelProperty(value = "One or more pair(s) of ingress and egress CPs or SAPs which the NFP passes by. All CP or SAP pairs in a group shall be instantiated from connection point descriptors or service access point descriptors referenced in the corresponding NfpPositionDesc. ")
  public List<CpPairInfo> getCpPairInfo() {
    return cpPairInfo;
  }

  public void setCpPairInfo(List<CpPairInfo> cpPairInfo) {
    this.cpPairInfo = cpPairInfo;
  }

  public CpGroupInfo forwardingBehaviour(ForwardingBehaviourEnum forwardingBehaviour) {
    this.forwardingBehaviour = forwardingBehaviour;
    return this;
  }

   /**
   * Identifies a rule to apply to forward traffic to the ingress CPs or SAPs of the group. Permitted values: * ALL &#x3D; Traffic flows shall be forwarded simultaneously to all CPs or SAPs of the group. * LB &#x3D; Traffic flows shall be forwarded to one CP or SAP of the group selected based on a loadbalancing algorithm. 
   * @return forwardingBehaviour
  **/
  @ApiModelProperty(value = "Identifies a rule to apply to forward traffic to the ingress CPs or SAPs of the group. Permitted values: * ALL = Traffic flows shall be forwarded simultaneously to all CPs or SAPs of the group. * LB = Traffic flows shall be forwarded to one CP or SAP of the group selected based on a loadbalancing algorithm. ")
  public ForwardingBehaviourEnum getForwardingBehaviour() {
    return forwardingBehaviour;
  }

  public void setForwardingBehaviour(ForwardingBehaviourEnum forwardingBehaviour) {
    this.forwardingBehaviour = forwardingBehaviour;
  }

  public CpGroupInfo forwardingBehaviourInputParameters(ForwardingBehaviourInputParameters forwardingBehaviourInputParameters) {
    this.forwardingBehaviourInputParameters = forwardingBehaviourInputParameters;
    return this;
  }

   /**
   * Provides input parameters to configure the forwarding behaviour (e.g. identifies a load balancing algorithm and criteria). 
   * @return forwardingBehaviourInputParameters
  **/
  @ApiModelProperty(value = "Provides input parameters to configure the forwarding behaviour (e.g. identifies a load balancing algorithm and criteria). ")
  public ForwardingBehaviourInputParameters getForwardingBehaviourInputParameters() {
    return forwardingBehaviourInputParameters;
  }

  public void setForwardingBehaviourInputParameters(ForwardingBehaviourInputParameters forwardingBehaviourInputParameters) {
    this.forwardingBehaviourInputParameters = forwardingBehaviourInputParameters;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CpGroupInfo cpGroupInfo = (CpGroupInfo) o;
    return Objects.equals(this.cpPairInfo, cpGroupInfo.cpPairInfo) &&
        Objects.equals(this.forwardingBehaviour, cpGroupInfo.forwardingBehaviour) &&
        Objects.equals(this.forwardingBehaviourInputParameters, cpGroupInfo.forwardingBehaviourInputParameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cpPairInfo, forwardingBehaviour, forwardingBehaviourInputParameters);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CpGroupInfo {\n");
    
    sb.append("    cpPairInfo: ").append(toIndentedString(cpPairInfo)).append("\n");
    sb.append("    forwardingBehaviour: ").append(toIndentedString(forwardingBehaviour)).append("\n");
    sb.append("    forwardingBehaviourInputParameters: ").append(toIndentedString(forwardingBehaviourInputParameters)).append("\n");
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

