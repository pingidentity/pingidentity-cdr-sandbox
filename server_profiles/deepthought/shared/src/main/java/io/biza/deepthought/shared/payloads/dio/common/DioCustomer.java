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
package io.biza.deepthought.shared.payloads.dio.common;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioCustomerType;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
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
@Schema(description = "A Deep Thought Customer Container")
public class DioCustomer {

  @JsonProperty("id")
  @NotNull
  @NonNull
  @Schema(description = "Deep Thought Customer Identifier",
      defaultValue = "00000000-0000-0000-0000-000000000000")
  @Builder.Default
  public UUID id = new UUID(0, 0);
  
  @JsonProperty("schemeType")
  @NotNull
  @NonNull
  @Schema(description = "Deep Thought Scheme Type", defaultValue = "DIO_COMMON")
  @Builder.Default
  public DioSchemeType schemeType = DioSchemeType.DIO_COMMON;

  @JsonProperty("customerType")
  @NotNull
  @NonNull
  @Schema(description = "Deep Thought Customer Type")
  public DioCustomerType customerType;
  
  @Schema(
      description = "Creation Date Time",
      type = "string", format = "date-time", accessMode = AccessMode.READ_ONLY)
  @JsonProperty("creationTime")
  OffsetDateTime creationTime;
  
  @Schema(
      description = "Last Update Date Time",
      type = "string", format = "date-time", accessMode = AccessMode.READ_ONLY)
  @JsonProperty("lastUpdated")
  OffsetDateTime lastUpdated;

  @JsonProperty("person")
  @Schema(description = "Deep Thought Person")
  DioPerson person;
  
  @JsonProperty("organisation")
  @Schema(description = "Deep Thought Organisation")
  DioOrganisation organisation;
  
}
