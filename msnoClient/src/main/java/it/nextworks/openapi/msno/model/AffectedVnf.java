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
import it.nextworks.openapi.msno.model.AffectedVnfChangedInfo;

import java.io.IOException;

/**
 * This type provides information about added, deleted and modified VNFs. It shall comply with the provisions in Table 6.5.3.2-1. 
 */
@ApiModel(description = "This type provides information about added, deleted and modified VNFs. It shall comply with the provisions in Table 6.5.3.2-1. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-11-08T16:52:33.422+01:00")
public class AffectedVnf {
  @SerializedName("vnfInstanceId")
  private String vnfInstanceId = null;

  @SerializedName("vnfdId")
  private String vnfdId = null;

  @SerializedName("vnfProfileId")
  private String vnfProfileId = null;

  @SerializedName("vnfName")
  private String vnfName = null;

  /**
   * Signals the type of change Permitted values: - ADD - REMOVE - INSTANTIATE - TERMINATE - SCALE - CHANGE_FLAVOUR - HEAL - OPERATE - MODIFY_INFORMATION - CHANGE_EXTERNAL_VNF_CONNECTIVITY 
   */
  @JsonAdapter(ChangeTypeEnum.Adapter.class)
  public enum ChangeTypeEnum {
    ADD("ADD"),
    
    REMOVE("REMOVE"),
    
    INSTANTIATE("INSTANTIATE"),
    
    TERMINATE("TERMINATE"),
    
    SCALE("SCALE"),
    
    CHANGE_FLAVOUR("CHANGE_FLAVOUR"),
    
    HEAL("HEAL"),
    
    OPERATE("OPERATE"),
    
    MODIFY_INFORMATION("MODIFY_INFORMATION"),
    
    CHANGE_EXTERNAL_VNF_CONNECTIVITY("CHANGE_EXTERNAL_VNF_CONNECTIVITY");

    private String value;

    ChangeTypeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static ChangeTypeEnum fromValue(String text) {
      for (ChangeTypeEnum b : ChangeTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<ChangeTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ChangeTypeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public ChangeTypeEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return ChangeTypeEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("changeType")
  private ChangeTypeEnum changeType = null;

  /**
   * Signals the result of change identified by the \&quot;changeType\&quot; attribute. Permitted values: - COMPLETED - ROLLED_BACK - FAILED 
   */
  @JsonAdapter(ChangeResultEnum.Adapter.class)
  public enum ChangeResultEnum {
    COMPLETED("COMPLETED"),
    
    ROLLED_BACK("ROLLED_BACK"),
    
    FAILED("FAILED");

    private String value;

    ChangeResultEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static ChangeResultEnum fromValue(String text) {
      for (ChangeResultEnum b : ChangeResultEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<ChangeResultEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ChangeResultEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public ChangeResultEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return ChangeResultEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("changeResult")
  private ChangeResultEnum changeResult = null;

  @SerializedName("changedInfo")
  private AffectedVnfChangedInfo changedInfo = null;

  public AffectedVnf vnfInstanceId(String vnfInstanceId) {
    this.vnfInstanceId = vnfInstanceId;
    return this;
  }

   /**
   * Identifier of the VNF instance. 
   * @return vnfInstanceId
  **/
  @ApiModelProperty(required = true, value = "Identifier of the VNF instance. ")
  public String getVnfInstanceId() {
    return vnfInstanceId;
  }

  public void setVnfInstanceId(String vnfInstanceId) {
    this.vnfInstanceId = vnfInstanceId;
  }

  public AffectedVnf vnfdId(String vnfdId) {
    this.vnfdId = vnfdId;
    return this;
  }

   /**
   * Identifier of the VNFD of the VNF Instance. 
   * @return vnfdId
  **/
  @ApiModelProperty(required = true, value = "Identifier of the VNFD of the VNF Instance. ")
  public String getVnfdId() {
    return vnfdId;
  }

  public void setVnfdId(String vnfdId) {
    this.vnfdId = vnfdId;
  }

  public AffectedVnf vnfProfileId(String vnfProfileId) {
    this.vnfProfileId = vnfProfileId;
    return this;
  }

   /**
   * Identifier of the VNF profile of the NSD. 
   * @return vnfProfileId
  **/
  @ApiModelProperty(required = true, value = "Identifier of the VNF profile of the NSD. ")
  public String getVnfProfileId() {
    return vnfProfileId;
  }

  public void setVnfProfileId(String vnfProfileId) {
    this.vnfProfileId = vnfProfileId;
  }

  public AffectedVnf vnfName(String vnfName) {
    this.vnfName = vnfName;
    return this;
  }

   /**
   * Name of the VNF Instance. 
   * @return vnfName
  **/
  @ApiModelProperty(value = "Name of the VNF Instance. ")
  public String getVnfName() {
    return vnfName;
  }

  public void setVnfName(String vnfName) {
    this.vnfName = vnfName;
  }

  public AffectedVnf changeType(ChangeTypeEnum changeType) {
    this.changeType = changeType;
    return this;
  }

   /**
   * Signals the type of change Permitted values: - ADD - REMOVE - INSTANTIATE - TERMINATE - SCALE - CHANGE_FLAVOUR - HEAL - OPERATE - MODIFY_INFORMATION - CHANGE_EXTERNAL_VNF_CONNECTIVITY 
   * @return changeType
  **/
  @ApiModelProperty(value = "Signals the type of change Permitted values: - ADD - REMOVE - INSTANTIATE - TERMINATE - SCALE - CHANGE_FLAVOUR - HEAL - OPERATE - MODIFY_INFORMATION - CHANGE_EXTERNAL_VNF_CONNECTIVITY ")
  public ChangeTypeEnum getChangeType() {
    return changeType;
  }

  public void setChangeType(ChangeTypeEnum changeType) {
    this.changeType = changeType;
  }

  public AffectedVnf changeResult(ChangeResultEnum changeResult) {
    this.changeResult = changeResult;
    return this;
  }

   /**
   * Signals the result of change identified by the \&quot;changeType\&quot; attribute. Permitted values: - COMPLETED - ROLLED_BACK - FAILED 
   * @return changeResult
  **/
  @ApiModelProperty(value = "Signals the result of change identified by the \"changeType\" attribute. Permitted values: - COMPLETED - ROLLED_BACK - FAILED ")
  public ChangeResultEnum getChangeResult() {
    return changeResult;
  }

  public void setChangeResult(ChangeResultEnum changeResult) {
    this.changeResult = changeResult;
  }

  public AffectedVnf changedInfo(AffectedVnfChangedInfo changedInfo) {
    this.changedInfo = changedInfo;
    return this;
  }

   /**
   * Get changedInfo
   * @return changedInfo
  **/
  @ApiModelProperty(value = "")
  public AffectedVnfChangedInfo getChangedInfo() {
    return changedInfo;
  }

  public void setChangedInfo(AffectedVnfChangedInfo changedInfo) {
    this.changedInfo = changedInfo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AffectedVnf affectedVnf = (AffectedVnf) o;
    return Objects.equals(this.vnfInstanceId, affectedVnf.vnfInstanceId) &&
        Objects.equals(this.vnfdId, affectedVnf.vnfdId) &&
        Objects.equals(this.vnfProfileId, affectedVnf.vnfProfileId) &&
        Objects.equals(this.vnfName, affectedVnf.vnfName) &&
        Objects.equals(this.changeType, affectedVnf.changeType) &&
        Objects.equals(this.changeResult, affectedVnf.changeResult) &&
        Objects.equals(this.changedInfo, affectedVnf.changedInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vnfInstanceId, vnfdId, vnfProfileId, vnfName, changeType, changeResult, changedInfo);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AffectedVnf {\n");
    
    sb.append("    vnfInstanceId: ").append(toIndentedString(vnfInstanceId)).append("\n");
    sb.append("    vnfdId: ").append(toIndentedString(vnfdId)).append("\n");
    sb.append("    vnfProfileId: ").append(toIndentedString(vnfProfileId)).append("\n");
    sb.append("    vnfName: ").append(toIndentedString(vnfName)).append("\n");
    sb.append("    changeType: ").append(toIndentedString(changeType)).append("\n");
    sb.append("    changeResult: ").append(toIndentedString(changeResult)).append("\n");
    sb.append("    changedInfo: ").append(toIndentedString(changedInfo)).append("\n");
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

