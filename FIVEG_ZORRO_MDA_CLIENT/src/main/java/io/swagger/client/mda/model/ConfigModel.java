/*
 * FastAPI
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 0.1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.mda.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.client.mda.model.ContextModel;
import io.swagger.client.mda.model.MetricModel;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.OffsetDateTime;
/**
 * ConfigModel
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-06-03T17:13:21.498+02:00[Europe/Rome]")
public class ConfigModel {
  @SerializedName("transaction_id")
  private String transactionId = null;

  @SerializedName("topic")
  private String topic = null;

  @SerializedName("monitoring_endpoint")
  private String monitoringEndpoint = null;

  @SerializedName("network_slice_id")
  private String networkSliceId = null;

  @SerializedName("instance_id")
  private String instanceId = null;

  @SerializedName("tenant_id")
  private String tenantId = null;

  @SerializedName("product_id")
  private String productId = null;

  @SerializedName("metrics")
  private List<MetricModel> metrics = new ArrayList<MetricModel>();

  @SerializedName("context_ids")
  private List<ContextModel> contextIds = null;

  @SerializedName("timestamp_start")
  private OffsetDateTime timestampStart = null;

  @SerializedName("timestamp_end")
  private OffsetDateTime timestampEnd = null;


  @SerializedName("data_source_type")
  private DataSourceType dataSourceType = null;

  public ConfigModel transactionId(String transactionId) {
    this.transactionId = transactionId;
    return this;
  }

   /**
   * Get transactionId
   * @return transactionId
  **/
  @Schema(required = true, description = "")
  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public ConfigModel topic(String topic) {
    this.topic = topic;
    return this;
  }


  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  /**
   * Get topic
   * @return topic
  **/
  @Schema(required = true, description = "")
  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public ConfigModel monitoringEndpoint(String monitoringEndpoint) {
    this.monitoringEndpoint = monitoringEndpoint;
    return this;
  }

   /**
   * Get monitoringEndpoint
   * @return monitoringEndpoint
  **/
  @Schema(description = "")
  public String getMonitoringEndpoint() {
    return monitoringEndpoint;
  }

  public void setMonitoringEndpoint(String monitoringEndpoint) {
    this.monitoringEndpoint = monitoringEndpoint;
  }

  public ConfigModel networkSliceId(String networkSliceId) {
    this.networkSliceId = networkSliceId;
    return this;
  }

   /**
   * Get networkSliceId
   * @return networkSliceId
  **/
  @Schema(description = "")
  public String getNetworkSliceId() {
    return networkSliceId;
  }

  public void setNetworkSliceId(String networkSliceId) {
    this.networkSliceId = networkSliceId;
  }

  public ConfigModel tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Get tenantId
   * @return tenantId
  **/
  @Schema(description = "")
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  /**
   * Get dataSourceType
   * @return dataSourceType
   **/
  @Schema(description = "")
  public DataSourceType getDataSourceType() {
    return dataSourceType;
  }

  public void setDataSourceType(DataSourceType dataSourceType) {
    this.dataSourceType = dataSourceType;
  }

  public ConfigModel productId(String productId) {
    this.productId = productId;
    return this;
  }

   /**
   * Get productId
   * @return productId
  **/
  @Schema(description = "")
  public String getProductId() {
    return productId ;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public ConfigModel metrics(List<MetricModel> metrics) {
    this.metrics = metrics;
    return this;
  }

  public ConfigModel addMetricsItem(MetricModel metricsItem) {
    this.metrics.add(metricsItem);
    return this;
  }

   /**
   * Get metrics
   * @return metrics
  **/
  @Schema(required = true, description = "")
  public List<MetricModel> getMetrics() {
    return metrics;
  }

  public void setMetrics(List<MetricModel> metrics) {
    this.metrics = metrics;
  }

  public ConfigModel contextIds(List<ContextModel> contextIds) {
    this.contextIds = contextIds;
    return this;
  }

  public ConfigModel addContextIdsItem(ContextModel contextIdsItem) {
    if (this.contextIds == null) {
      this.contextIds = new ArrayList<ContextModel>();
    }
    this.contextIds.add(contextIdsItem);
    return this;
  }

   /**
   * Get contextIds
   * @return contextIds
  **/
  @Schema(description = "")
  public List<ContextModel> getContextIds() {
    return contextIds;
  }

  public void setContextIds(List<ContextModel> contextIds) {
    this.contextIds = contextIds;
  }

  public ConfigModel timestampStart(OffsetDateTime timestampStart) {
    this.timestampStart = timestampStart;
    return this;
  }

   /**
   * Get timestampStart
   * @return timestampStart
  **/
  @Schema(description = "")
  public OffsetDateTime getTimestampStart() {
    return timestampStart;
  }

  public void setTimestampStart(OffsetDateTime timestampStart) {
    this.timestampStart = timestampStart;
  }

  public ConfigModel timestampEnd(OffsetDateTime timestampEnd) {
    this.timestampEnd = timestampEnd;
    return this;
  }

   /**
   * Get timestampEnd
   * @return timestampEnd
  **/
  @Schema(description = "")
  public OffsetDateTime getTimestampEnd() {
    return timestampEnd;
  }

  public void setTimestampEnd(OffsetDateTime timestampEnd) {
    this.timestampEnd = timestampEnd;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConfigModel configModel = (ConfigModel) o;
    return Objects.equals(this.transactionId, configModel.transactionId) &&
        Objects.equals(this.topic, configModel.topic) &&
        Objects.equals(this.monitoringEndpoint, configModel.monitoringEndpoint) &&
        Objects.equals(this.networkSliceId, configModel.networkSliceId) &&
        Objects.equals(this.tenantId, configModel.tenantId) &&
        Objects.equals(this.metrics, configModel.metrics) &&
        Objects.equals(this.contextIds, configModel.contextIds) &&
        Objects.equals(this.timestampStart, configModel.timestampStart) &&
        Objects.equals(this.timestampEnd, configModel.timestampEnd);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactionId, topic, monitoringEndpoint, networkSliceId, tenantId, productId , metrics, contextIds, timestampStart, timestampEnd);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConfigModel {\n");
    
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    topic: ").append(toIndentedString(topic)).append("\n");
    sb.append("    monitoringEndpoint: ").append(toIndentedString(monitoringEndpoint)).append("\n");
    sb.append("    networkSliceId: ").append(toIndentedString(networkSliceId)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    productId: ").append(toIndentedString(productId)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
    sb.append("    contextIds: ").append(toIndentedString(contextIds)).append("\n");
    sb.append("    timestampStart: ").append(toIndentedString(timestampStart)).append("\n");
    sb.append("    timestampEnd: ").append(toIndentedString(timestampEnd)).append("\n");
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
