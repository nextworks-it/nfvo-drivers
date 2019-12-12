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
import it.nextworks.openapi.msno.model.IpOverEthernetAddressData2AddressRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * IpOverEthernetAddressData2IpAddresses
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-11-08T16:52:33.422+01:00")
public class IpOverEthernetAddressData2IpAddresses {
  /**
   * The type of the IP addresses. Permitted values: IPV4, IPV6. 
   */
  @JsonAdapter(TypeEnum.Adapter.class)
  public enum TypeEnum {
    IPV4("IPV4"),
    
    IPV6("IPV6");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<TypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final TypeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public TypeEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return TypeEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("type")
  private TypeEnum type = null;

  @SerializedName("fixedAddresses")
  private List<String> fixedAddresses = null;

  @SerializedName("numDynamicAddresses")
  private Integer numDynamicAddresses = null;

  @SerializedName("addressRange")
  private IpOverEthernetAddressData2AddressRange addressRange = null;

  @SerializedName("subnetId")
  private String subnetId = null;

  public IpOverEthernetAddressData2IpAddresses type(TypeEnum type) {
    this.type = type;
    return this;
  }

   /**
   * The type of the IP addresses. Permitted values: IPV4, IPV6. 
   * @return type
  **/
  @ApiModelProperty(required = true, value = "The type of the IP addresses. Permitted values: IPV4, IPV6. ")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public IpOverEthernetAddressData2IpAddresses fixedAddresses(List<String> fixedAddresses) {
    this.fixedAddresses = fixedAddresses;
    return this;
  }

  public IpOverEthernetAddressData2IpAddresses addFixedAddressesItem(String fixedAddressesItem) {
    if (this.fixedAddresses == null) {
      this.fixedAddresses = new ArrayList<String>();
    }
    this.fixedAddresses.add(fixedAddressesItem);
    return this;
  }

   /**
   * Fixed addresses to assign (from the subnet defined by \&quot;subnetId\&quot; if provided). Exactly one of \&quot;fixedAddresses\&quot;, \&quot;numDynamicAddresses\&quot; or \&quot;ipAddressRange\&quot; shall be present. 
   * @return fixedAddresses
  **/
  @ApiModelProperty(value = "Fixed addresses to assign (from the subnet defined by \"subnetId\" if provided). Exactly one of \"fixedAddresses\", \"numDynamicAddresses\" or \"ipAddressRange\" shall be present. ")
  public List<String> getFixedAddresses() {
    return fixedAddresses;
  }

  public void setFixedAddresses(List<String> fixedAddresses) {
    this.fixedAddresses = fixedAddresses;
  }

  public IpOverEthernetAddressData2IpAddresses numDynamicAddresses(Integer numDynamicAddresses) {
    this.numDynamicAddresses = numDynamicAddresses;
    return this;
  }

   /**
   * Number of dynamic addresses to assign (from the subnet defined by \&quot;subnetId\&quot; if provided). Exactly one of \&quot;fixedAddresses\&quot;, \&quot;numDynamicAddresses\&quot; or \&quot;ipAddressRange\&quot; shall be present. 
   * @return numDynamicAddresses
  **/
  @ApiModelProperty(value = "Number of dynamic addresses to assign (from the subnet defined by \"subnetId\" if provided). Exactly one of \"fixedAddresses\", \"numDynamicAddresses\" or \"ipAddressRange\" shall be present. ")
  public Integer getNumDynamicAddresses() {
    return numDynamicAddresses;
  }

  public void setNumDynamicAddresses(Integer numDynamicAddresses) {
    this.numDynamicAddresses = numDynamicAddresses;
  }

  public IpOverEthernetAddressData2IpAddresses addressRange(IpOverEthernetAddressData2AddressRange addressRange) {
    this.addressRange = addressRange;
    return this;
  }

   /**
   * Get addressRange
   * @return addressRange
  **/
  @ApiModelProperty(value = "")
  public IpOverEthernetAddressData2AddressRange getAddressRange() {
    return addressRange;
  }

  public void setAddressRange(IpOverEthernetAddressData2AddressRange addressRange) {
    this.addressRange = addressRange;
  }

  public IpOverEthernetAddressData2IpAddresses subnetId(String subnetId) {
    this.subnetId = subnetId;
    return this;
  }

   /**
   * Subnet defined by the identifier of the subnet resource in the VIM. In case this attribute is present, IP addresses from that subnet will be assigned; otherwise, IP addresses not bound to a subnet will be assigned. 
   * @return subnetId
  **/
  @ApiModelProperty(value = "Subnet defined by the identifier of the subnet resource in the VIM. In case this attribute is present, IP addresses from that subnet will be assigned; otherwise, IP addresses not bound to a subnet will be assigned. ")
  public String getSubnetId() {
    return subnetId;
  }

  public void setSubnetId(String subnetId) {
    this.subnetId = subnetId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IpOverEthernetAddressData2IpAddresses ipOverEthernetAddressData2IpAddresses = (IpOverEthernetAddressData2IpAddresses) o;
    return Objects.equals(this.type, ipOverEthernetAddressData2IpAddresses.type) &&
        Objects.equals(this.fixedAddresses, ipOverEthernetAddressData2IpAddresses.fixedAddresses) &&
        Objects.equals(this.numDynamicAddresses, ipOverEthernetAddressData2IpAddresses.numDynamicAddresses) &&
        Objects.equals(this.addressRange, ipOverEthernetAddressData2IpAddresses.addressRange) &&
        Objects.equals(this.subnetId, ipOverEthernetAddressData2IpAddresses.subnetId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, fixedAddresses, numDynamicAddresses, addressRange, subnetId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IpOverEthernetAddressData2IpAddresses {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    fixedAddresses: ").append(toIndentedString(fixedAddresses)).append("\n");
    sb.append("    numDynamicAddresses: ").append(toIndentedString(numDynamicAddresses)).append("\n");
    sb.append("    addressRange: ").append(toIndentedString(addressRange)).append("\n");
    sb.append("    subnetId: ").append(toIndentedString(subnetId)).append("\n");
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

