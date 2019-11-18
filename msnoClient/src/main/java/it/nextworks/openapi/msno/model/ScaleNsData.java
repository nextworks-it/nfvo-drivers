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
import it.nextworks.openapi.msno.model.ParamsForVnf;
import it.nextworks.openapi.msno.model.ScaleNsByStepsData;
import it.nextworks.openapi.msno.model.ScaleNsToLevelData;
import it.nextworks.openapi.msno.model.VnfInstanceData;
import it.nextworks.openapi.msno.model.VnfLocationConstraint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This type represents the information to scale a NS. 
 */
@ApiModel(description = "This type represents the information to scale a NS. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-11-08T16:52:33.422+01:00")
public class ScaleNsData {
  @SerializedName("vnfInstanceToBeAdded")
  private List<VnfInstanceData> vnfInstanceToBeAdded = null;

  @SerializedName("vnfInstanceToBeRemoved")
  private List<String> vnfInstanceToBeRemoved = null;

  @SerializedName("scaleNsByStepsData")
  private ScaleNsByStepsData scaleNsByStepsData = null;

  @SerializedName("scaleNsToLevelData")
  private ScaleNsToLevelData scaleNsToLevelData = null;

  @SerializedName("additionalParamsForNs")
  private ParamsForVnf additionalParamsForNs = null;

  @SerializedName("additionalParamsForVnf")
  private List<ParamsForVnf> additionalParamsForVnf = null;

  @SerializedName("locationConstraints")
  private List<VnfLocationConstraint> locationConstraints = null;

  public ScaleNsData vnfInstanceToBeAdded(List<VnfInstanceData> vnfInstanceToBeAdded) {
    this.vnfInstanceToBeAdded = vnfInstanceToBeAdded;
    return this;
  }

  public ScaleNsData addVnfInstanceToBeAddedItem(VnfInstanceData vnfInstanceToBeAddedItem) {
    if (this.vnfInstanceToBeAdded == null) {
      this.vnfInstanceToBeAdded = new ArrayList<VnfInstanceData>();
    }
    this.vnfInstanceToBeAdded.add(vnfInstanceToBeAddedItem);
    return this;
  }

   /**
   * An existing VNF instance to be added to the NS instance as part of the scaling operation. If needed, the VNF Profile to be used for this VNF instance may also be provided. 
   * @return vnfInstanceToBeAdded
  **/
  @ApiModelProperty(value = "An existing VNF instance to be added to the NS instance as part of the scaling operation. If needed, the VNF Profile to be used for this VNF instance may also be provided. ")
  public List<VnfInstanceData> getVnfInstanceToBeAdded() {
    return vnfInstanceToBeAdded;
  }

  public void setVnfInstanceToBeAdded(List<VnfInstanceData> vnfInstanceToBeAdded) {
    this.vnfInstanceToBeAdded = vnfInstanceToBeAdded;
  }

  public ScaleNsData vnfInstanceToBeRemoved(List<String> vnfInstanceToBeRemoved) {
    this.vnfInstanceToBeRemoved = vnfInstanceToBeRemoved;
    return this;
  }

  public ScaleNsData addVnfInstanceToBeRemovedItem(String vnfInstanceToBeRemovedItem) {
    if (this.vnfInstanceToBeRemoved == null) {
      this.vnfInstanceToBeRemoved = new ArrayList<String>();
    }
    this.vnfInstanceToBeRemoved.add(vnfInstanceToBeRemovedItem);
    return this;
  }

   /**
   * The VNF instance to be removed from the NS instance as part of the scaling operation. 
   * @return vnfInstanceToBeRemoved
  **/
  @ApiModelProperty(value = "The VNF instance to be removed from the NS instance as part of the scaling operation. ")
  public List<String> getVnfInstanceToBeRemoved() {
    return vnfInstanceToBeRemoved;
  }

  public void setVnfInstanceToBeRemoved(List<String> vnfInstanceToBeRemoved) {
    this.vnfInstanceToBeRemoved = vnfInstanceToBeRemoved;
  }

  public ScaleNsData scaleNsByStepsData(ScaleNsByStepsData scaleNsByStepsData) {
    this.scaleNsByStepsData = scaleNsByStepsData;
    return this;
  }

   /**
   * The information used to scale an NS instance by one or more scaling steps. 
   * @return scaleNsByStepsData
  **/
  @ApiModelProperty(value = "The information used to scale an NS instance by one or more scaling steps. ")
  public ScaleNsByStepsData getScaleNsByStepsData() {
    return scaleNsByStepsData;
  }

  public void setScaleNsByStepsData(ScaleNsByStepsData scaleNsByStepsData) {
    this.scaleNsByStepsData = scaleNsByStepsData;
  }

  public ScaleNsData scaleNsToLevelData(ScaleNsToLevelData scaleNsToLevelData) {
    this.scaleNsToLevelData = scaleNsToLevelData;
    return this;
  }

   /**
   * The information used to scale an NS instance to a target size. 
   * @return scaleNsToLevelData
  **/
  @ApiModelProperty(value = "The information used to scale an NS instance to a target size. ")
  public ScaleNsToLevelData getScaleNsToLevelData() {
    return scaleNsToLevelData;
  }

  public void setScaleNsToLevelData(ScaleNsToLevelData scaleNsToLevelData) {
    this.scaleNsToLevelData = scaleNsToLevelData;
  }

  public ScaleNsData additionalParamsForNs(ParamsForVnf additionalParamsForNs) {
    this.additionalParamsForNs = additionalParamsForNs;
    return this;
  }

   /**
   * Allows the OSS/BSS to provide additional parameter(s) at the NS level necessary for the NS scaling (as opposed to the VNF level, which is covered in additionalParamForVnf). 
   * @return additionalParamsForNs
  **/
  @ApiModelProperty(value = "Allows the OSS/BSS to provide additional parameter(s) at the NS level necessary for the NS scaling (as opposed to the VNF level, which is covered in additionalParamForVnf). ")
  public ParamsForVnf getAdditionalParamsForNs() {
    return additionalParamsForNs;
  }

  public void setAdditionalParamsForNs(ParamsForVnf additionalParamsForNs) {
    this.additionalParamsForNs = additionalParamsForNs;
  }

  public ScaleNsData additionalParamsForVnf(List<ParamsForVnf> additionalParamsForVnf) {
    this.additionalParamsForVnf = additionalParamsForVnf;
    return this;
  }

  public ScaleNsData addAdditionalParamsForVnfItem(ParamsForVnf additionalParamsForVnfItem) {
    if (this.additionalParamsForVnf == null) {
      this.additionalParamsForVnf = new ArrayList<ParamsForVnf>();
    }
    this.additionalParamsForVnf.add(additionalParamsForVnfItem);
    return this;
  }

   /**
   * Allows the OSS/BSS to provide additional parameter(s) per VNF instance (as opposed to the NS level, which is covered in additionalParamforNs). This is for VNFs that are to be created by the NFVO as part of the NS scaling and not for existing VNF that are covered by the scaleVnfData. 
   * @return additionalParamsForVnf
  **/
  @ApiModelProperty(value = "Allows the OSS/BSS to provide additional parameter(s) per VNF instance (as opposed to the NS level, which is covered in additionalParamforNs). This is for VNFs that are to be created by the NFVO as part of the NS scaling and not for existing VNF that are covered by the scaleVnfData. ")
  public List<ParamsForVnf> getAdditionalParamsForVnf() {
    return additionalParamsForVnf;
  }

  public void setAdditionalParamsForVnf(List<ParamsForVnf> additionalParamsForVnf) {
    this.additionalParamsForVnf = additionalParamsForVnf;
  }

  public ScaleNsData locationConstraints(List<VnfLocationConstraint> locationConstraints) {
    this.locationConstraints = locationConstraints;
    return this;
  }

  public ScaleNsData addLocationConstraintsItem(VnfLocationConstraint locationConstraintsItem) {
    if (this.locationConstraints == null) {
      this.locationConstraints = new ArrayList<VnfLocationConstraint>();
    }
    this.locationConstraints.add(locationConstraintsItem);
    return this;
  }

   /**
   * The location constraints for the VNF to be instantiated as part of the NS scaling. An example can be a constraint for the VNF to be in a specific geographic location. 
   * @return locationConstraints
  **/
  @ApiModelProperty(value = "The location constraints for the VNF to be instantiated as part of the NS scaling. An example can be a constraint for the VNF to be in a specific geographic location. ")
  public List<VnfLocationConstraint> getLocationConstraints() {
    return locationConstraints;
  }

  public void setLocationConstraints(List<VnfLocationConstraint> locationConstraints) {
    this.locationConstraints = locationConstraints;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScaleNsData scaleNsData = (ScaleNsData) o;
    return Objects.equals(this.vnfInstanceToBeAdded, scaleNsData.vnfInstanceToBeAdded) &&
        Objects.equals(this.vnfInstanceToBeRemoved, scaleNsData.vnfInstanceToBeRemoved) &&
        Objects.equals(this.scaleNsByStepsData, scaleNsData.scaleNsByStepsData) &&
        Objects.equals(this.scaleNsToLevelData, scaleNsData.scaleNsToLevelData) &&
        Objects.equals(this.additionalParamsForNs, scaleNsData.additionalParamsForNs) &&
        Objects.equals(this.additionalParamsForVnf, scaleNsData.additionalParamsForVnf) &&
        Objects.equals(this.locationConstraints, scaleNsData.locationConstraints);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vnfInstanceToBeAdded, vnfInstanceToBeRemoved, scaleNsByStepsData, scaleNsToLevelData, additionalParamsForNs, additionalParamsForVnf, locationConstraints);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScaleNsData {\n");
    
    sb.append("    vnfInstanceToBeAdded: ").append(toIndentedString(vnfInstanceToBeAdded)).append("\n");
    sb.append("    vnfInstanceToBeRemoved: ").append(toIndentedString(vnfInstanceToBeRemoved)).append("\n");
    sb.append("    scaleNsByStepsData: ").append(toIndentedString(scaleNsByStepsData)).append("\n");
    sb.append("    scaleNsToLevelData: ").append(toIndentedString(scaleNsToLevelData)).append("\n");
    sb.append("    additionalParamsForNs: ").append(toIndentedString(additionalParamsForNs)).append("\n");
    sb.append("    additionalParamsForVnf: ").append(toIndentedString(additionalParamsForVnf)).append("\n");
    sb.append("    locationConstraints: ").append(toIndentedString(locationConstraints)).append("\n");
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
