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
import it.nextworks.openapi.msno.model.IpOverEthernetAddressData2;

import java.io.IOException;

/**
 * This type represents network protocol data. 
 */
@ApiModel(description = "This type represents network protocol data. ")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-11-08T16:52:33.422+01:00")
public class CpProtocolData2 {
  /**
   * Identifier of layer(s) and protocol(s). Permitted values: IP_OVER_ETHERNET. 
   */
  @JsonAdapter(LayerProtocolEnum.Adapter.class)
  public enum LayerProtocolEnum {
    IP_OVER_ETHERNET("IP_OVER_ETHERNET");

    private String value;

    LayerProtocolEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static LayerProtocolEnum fromValue(String text) {
      for (LayerProtocolEnum b : LayerProtocolEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<LayerProtocolEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final LayerProtocolEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public LayerProtocolEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return LayerProtocolEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("layerProtocol")
  private LayerProtocolEnum layerProtocol = null;

  @SerializedName("ipOverEthernet")
  private IpOverEthernetAddressData2 ipOverEthernet = null;

  public CpProtocolData2 layerProtocol(LayerProtocolEnum layerProtocol) {
    this.layerProtocol = layerProtocol;
    return this;
  }

   /**
   * Identifier of layer(s) and protocol(s). Permitted values: IP_OVER_ETHERNET. 
   * @return layerProtocol
  **/
  @ApiModelProperty(required = true, value = "Identifier of layer(s) and protocol(s). Permitted values: IP_OVER_ETHERNET. ")
  public LayerProtocolEnum getLayerProtocol() {
    return layerProtocol;
  }

  public void setLayerProtocol(LayerProtocolEnum layerProtocol) {
    this.layerProtocol = layerProtocol;
  }

  public CpProtocolData2 ipOverEthernet(IpOverEthernetAddressData2 ipOverEthernet) {
    this.ipOverEthernet = ipOverEthernet;
    return this;
  }

   /**
   * Network address data for IP over Ethernet to assign to the extCP instance. Shall be present if layerProtocol is equal to \&quot;IP_OVER_ETHERNET\&quot;, and shall be absent otherwise. 
   * @return ipOverEthernet
  **/
  @ApiModelProperty(value = "Network address data for IP over Ethernet to assign to the extCP instance. Shall be present if layerProtocol is equal to \"IP_OVER_ETHERNET\", and shall be absent otherwise. ")
  public IpOverEthernetAddressData2 getIpOverEthernet() {
    return ipOverEthernet;
  }

  public void setIpOverEthernet(IpOverEthernetAddressData2 ipOverEthernet) {
    this.ipOverEthernet = ipOverEthernet;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CpProtocolData2 cpProtocolData2 = (CpProtocolData2) o;
    return Objects.equals(this.layerProtocol, cpProtocolData2.layerProtocol) &&
        Objects.equals(this.ipOverEthernet, cpProtocolData2.ipOverEthernet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(layerProtocol, ipOverEthernet);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CpProtocolData2 {\n");
    
    sb.append("    layerProtocol: ").append(toIndentedString(layerProtocol)).append("\n");
    sb.append("    ipOverEthernet: ").append(toIndentedString(ipOverEthernet)).append("\n");
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

