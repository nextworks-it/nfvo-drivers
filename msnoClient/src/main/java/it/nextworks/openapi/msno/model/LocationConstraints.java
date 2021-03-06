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
import it.nextworks.openapi.msno.model.LocationConstraintsCivicAddressElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This type represents location constraints for a VNF to be instantiated. The location constraints shall be presented as a country code, optionally followed by a civic address based on the format defined by IETF RFC 4776 [13]. 
 */
@ApiModel(description = "This type represents location constraints for a VNF to be instantiated. The location constraints shall be presented as a country code, optionally followed by a civic address based on the format defined by IETF RFC 4776 [13]. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-11-08T16:52:33.422+01:00")
public class LocationConstraints {
  @SerializedName("countryCode")
  private String countryCode = null;

  @SerializedName("civicAddressElement")
  private List<LocationConstraintsCivicAddressElement> civicAddressElement = null;

  public LocationConstraints countryCode(String countryCode) {
    this.countryCode = countryCode;
    return this;
  }

   /**
   * The two-letter ISO 3166 [29] country code in capital letters. 
   * @return countryCode
  **/
  @ApiModelProperty(required = true, value = "The two-letter ISO 3166 [29] country code in capital letters. ")
  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public LocationConstraints civicAddressElement(List<LocationConstraintsCivicAddressElement> civicAddressElement) {
    this.civicAddressElement = civicAddressElement;
    return this;
  }

  public LocationConstraints addCivicAddressElementItem(LocationConstraintsCivicAddressElement civicAddressElementItem) {
    if (this.civicAddressElement == null) {
      this.civicAddressElement = new ArrayList<LocationConstraintsCivicAddressElement>();
    }
    this.civicAddressElement.add(civicAddressElementItem);
    return this;
  }

   /**
   * Zero or more elements comprising the civic address. 
   * @return civicAddressElement
  **/
  @ApiModelProperty(value = "Zero or more elements comprising the civic address. ")
  public List<LocationConstraintsCivicAddressElement> getCivicAddressElement() {
    return civicAddressElement;
  }

  public void setCivicAddressElement(List<LocationConstraintsCivicAddressElement> civicAddressElement) {
    this.civicAddressElement = civicAddressElement;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationConstraints locationConstraints = (LocationConstraints) o;
    return Objects.equals(this.countryCode, locationConstraints.countryCode) &&
        Objects.equals(this.civicAddressElement, locationConstraints.civicAddressElement);
  }

  @Override
  public int hashCode() {
    return Objects.hash(countryCode, civicAddressElement);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocationConstraints {\n");
    
    sb.append("    countryCode: ").append(toIndentedString(countryCode)).append("\n");
    sb.append("    civicAddressElement: ").append(toIndentedString(civicAddressElement)).append("\n");
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

