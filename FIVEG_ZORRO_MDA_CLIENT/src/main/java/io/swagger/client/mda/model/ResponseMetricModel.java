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
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import org.threeten.bp.OffsetDateTime;
/**
 * ResponseMetricModel
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-06-03T17:13:21.498+02:00[Europe/Rome]")
public class ResponseMetricModel {
  @SerializedName("metricName")
  private String metricName = null;

  @SerializedName("metricType")
  private String metricType = null;

  @SerializedName("step")
  private String step = null;

  @SerializedName("aggregationMethod")
  private String aggregationMethod = null;

  @SerializedName("step_aggregation")
  private String stepAggregation = null;

  @SerializedName("next_run_at")
  private OffsetDateTime nextRunAt = null;

  @SerializedName("next_aggregation")
  private OffsetDateTime nextAggregation = null;

  public ResponseMetricModel metricName(String metricName) {
    this.metricName = metricName;
    return this;
  }

   /**
   * Get metricName
   * @return metricName
  **/
  @Schema(required = true, description = "")
  public String getMetricName() {
    return metricName;
  }

  public void setMetricName(String metricName) {
    this.metricName = metricName;
  }

  public ResponseMetricModel metricType(String metricType) {
    this.metricType = metricType;
    return this;
  }

   /**
   * Get metricType
   * @return metricType
  **/
  @Schema(required = true, description = "")
  public String getMetricType() {
    return metricType;
  }

  public void setMetricType(String metricType) {
    this.metricType = metricType;
  }

  public ResponseMetricModel step(String step) {
    this.step = step;
    return this;
  }

   /**
   * Get step
   * @return step
  **/
  @Schema(required = true, description = "")
  public String getStep() {
    return step;
  }

  public void setStep(String step) {
    this.step = step;
  }

  public ResponseMetricModel aggregationMethod(String aggregationMethod) {
    this.aggregationMethod = aggregationMethod;
    return this;
  }

   /**
   * Get aggregationMethod
   * @return aggregationMethod
  **/
  @Schema(description = "")
  public String getAggregationMethod() {
    return aggregationMethod;
  }

  public void setAggregationMethod(String aggregationMethod) {
    this.aggregationMethod = aggregationMethod;
  }

  public ResponseMetricModel stepAggregation(String stepAggregation) {
    this.stepAggregation = stepAggregation;
    return this;
  }

   /**
   * Get stepAggregation
   * @return stepAggregation
  **/
  @Schema(description = "")
  public String getStepAggregation() {
    return stepAggregation;
  }

  public void setStepAggregation(String stepAggregation) {
    this.stepAggregation = stepAggregation;
  }

  public ResponseMetricModel nextRunAt(OffsetDateTime nextRunAt) {
    this.nextRunAt = nextRunAt;
    return this;
  }

   /**
   * Get nextRunAt
   * @return nextRunAt
  **/
  @Schema(required = true, description = "")
  public OffsetDateTime getNextRunAt() {
    return nextRunAt;
  }

  public void setNextRunAt(OffsetDateTime nextRunAt) {
    this.nextRunAt = nextRunAt;
  }

  public ResponseMetricModel nextAggregation(OffsetDateTime nextAggregation) {
    this.nextAggregation = nextAggregation;
    return this;
  }

   /**
   * Get nextAggregation
   * @return nextAggregation
  **/
  @Schema(description = "")
  public OffsetDateTime getNextAggregation() {
    return nextAggregation;
  }

  public void setNextAggregation(OffsetDateTime nextAggregation) {
    this.nextAggregation = nextAggregation;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseMetricModel responseMetricModel = (ResponseMetricModel) o;
    return Objects.equals(this.metricName, responseMetricModel.metricName) &&
        Objects.equals(this.metricType, responseMetricModel.metricType) &&
        Objects.equals(this.step, responseMetricModel.step) &&
        Objects.equals(this.aggregationMethod, responseMetricModel.aggregationMethod) &&
        Objects.equals(this.stepAggregation, responseMetricModel.stepAggregation) &&
        Objects.equals(this.nextRunAt, responseMetricModel.nextRunAt) &&
        Objects.equals(this.nextAggregation, responseMetricModel.nextAggregation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(metricName, metricType, step, aggregationMethod, stepAggregation, nextRunAt, nextAggregation);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResponseMetricModel {\n");
    
    sb.append("    metricName: ").append(toIndentedString(metricName)).append("\n");
    sb.append("    metricType: ").append(toIndentedString(metricType)).append("\n");
    sb.append("    step: ").append(toIndentedString(step)).append("\n");
    sb.append("    aggregationMethod: ").append(toIndentedString(aggregationMethod)).append("\n");
    sb.append("    stepAggregation: ").append(toIndentedString(stepAggregation)).append("\n");
    sb.append("    nextRunAt: ").append(toIndentedString(nextRunAt)).append("\n");
    sb.append("    nextAggregation: ").append(toIndentedString(nextAggregation)).append("\n");
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