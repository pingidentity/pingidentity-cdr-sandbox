/*******************************************************************************
 * Copyright (C) 2020 Biza Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package io.biza.deepthought.shared.payloads.dio.product;

import java.net.URI;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.biza.babelfish.cdr.converters.UriStringToUriConverter;
import io.biza.babelfish.cdr.converters.UriToUriStringConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Valid
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "A Deep Thought Product Bundle")
public class DioProductBundle {

  @Schema(description = "Bundle Identifier", required = true,
      defaultValue = "00000000-0000-0000-0000-000000000000")
  @JsonProperty("id")
  UUID id;

  @Schema(description = "Name of the bundle", required = true)
  @NonNull
  @NotNull
  @NotBlank
  @Valid
  @JsonProperty("name")
  String name;

  @Schema(description = "Description of the bundle", required = true)
  @NonNull
  @NotNull
  @NotBlank
  @Valid
  @JsonProperty("description")
  String description;

  @Schema(description = "Display text providing more information on the bundle")
  @JsonProperty("additionalInfo")
  String additionalInfo;

  @Schema(
      description = "Link to a web page with more information on the bundle criteria and benefits",
      type = "string", format = "uri")
  @JsonSerialize(converter = UriToUriStringConverter.class)
  @JsonDeserialize(converter = UriStringToUriConverter.class)
  @Valid
  @JsonProperty("additionalInfoUri")
  URI additionalInfoUri;

}
