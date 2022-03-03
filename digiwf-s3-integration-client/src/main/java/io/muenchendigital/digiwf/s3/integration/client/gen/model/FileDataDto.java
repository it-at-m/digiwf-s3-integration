/*
 * OpenAPI definition
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.muenchendigital.digiwf.s3.integration.client.gen.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import javax.validation.constraints.*;
import javax.validation.Valid;
/**
 * File data with presigned url
 */
@Schema(description = "File data with presigned url")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-03-03T15:19:32.698926+01:00[Europe/Berlin]")
public class FileDataDto {
  @JsonProperty("refId")
  private String refId = null;

  @JsonProperty("filename")
  private String filename = null;

  @JsonProperty("expiresInMinutes")
  private Integer expiresInMinutes = null;

  @JsonProperty("endOfLife")
  private java.time.LocalDate endOfLife = null;

  public FileDataDto refId(String refId) {
    this.refId = refId;
    return this;
  }

   /**
   * Get refId
   * @return refId
  **/
  @NotNull
 @Size(max=512)  @Schema(required = true, description = "")
  public String getRefId() {
    return refId;
  }

  public void setRefId(String refId) {
    this.refId = refId;
  }

  public FileDataDto filename(String filename) {
    this.filename = filename;
    return this;
  }

   /**
   * Get filename
   * @return filename
  **/
  @NotNull
  @Schema(required = true, description = "")
  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public FileDataDto expiresInMinutes(Integer expiresInMinutes) {
    this.expiresInMinutes = expiresInMinutes;
    return this;
  }

   /**
   * Get expiresInMinutes
   * minimum: 1
   * @return expiresInMinutes
  **/
  @NotNull
 @Min(1)  @Schema(required = true, description = "")
  public Integer getExpiresInMinutes() {
    return expiresInMinutes;
  }

  public void setExpiresInMinutes(Integer expiresInMinutes) {
    this.expiresInMinutes = expiresInMinutes;
  }

  public FileDataDto endOfLife(java.time.LocalDate endOfLife) {
    this.endOfLife = endOfLife;
    return this;
  }

   /**
   * Get endOfLife
   * @return endOfLife
  **/
  @Valid
  @Schema(description = "")
  public java.time.LocalDate getEndOfLife() {
    return endOfLife;
  }

  public void setEndOfLife(java.time.LocalDate endOfLife) {
    this.endOfLife = endOfLife;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FileDataDto fileDataDto = (FileDataDto) o;
    return Objects.equals(this.refId, fileDataDto.refId) &&
        Objects.equals(this.filename, fileDataDto.filename) &&
        Objects.equals(this.expiresInMinutes, fileDataDto.expiresInMinutes) &&
        Objects.equals(this.endOfLife, fileDataDto.endOfLife);
  }

  @Override
  public int hashCode() {
    return Objects.hash(refId, filename, expiresInMinutes, endOfLife);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FileDataDto {\n");
    
    sb.append("    refId: ").append(toIndentedString(refId)).append("\n");
    sb.append("    filename: ").append(toIndentedString(filename)).append("\n");
    sb.append("    expiresInMinutes: ").append(toIndentedString(expiresInMinutes)).append("\n");
    sb.append("    endOfLife: ").append(toIndentedString(endOfLife)).append("\n");
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
