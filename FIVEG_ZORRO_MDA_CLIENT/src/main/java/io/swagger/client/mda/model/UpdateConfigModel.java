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
import io.swagger.client.mda.model.MetricModel;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.OffsetDateTime;
/**
 * UpdateConfigModel
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-06-03T17:13:21.498+02:00[Europe/Rome]")
public class UpdateConfigModel {
  @SerializedName("timestampEnd")
  private OffsetDateTime timestampEnd = null;

  @SerializedName("metrics")
  private List<MetricModel> metrics = null;

  public UpdateConfigModel timestampEnd(OffsetDateTime timestampEnd) {
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

  public UpdateConfigModel metrics(List<MetricModel> metrics) {
    this.metrics = metrics;
    return this;
  }

  public UpdateConfigModel addMetricsItem(MetricModel metricsItem) {
    if (this.metrics == null) {
      this.metrics = new ArrayList<MetricModel>();
    }
    this.metrics.add(metricsItem);
    return this;
  }

   /**
   * Get metrics
   * @return metrics
  **/
  @Schema(description = "")
  public List<MetricModel> getMetrics() {
    return metrics;
  }

  public void setMetrics(List<MetricModel> metrics) {
    this.metrics = metrics;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateConfigModel updateConfigModel = (UpdateConfigModel) o;
    return Objects.equals(this.timestampEnd, updateConfigModel.timestampEnd) &&
        Objects.equals(this.metrics, updateConfigModel.metrics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestampEnd, metrics);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateConfigModel {\n");
    
    sb.append("    timestampEnd: ").append(toIndentedString(timestampEnd)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
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
