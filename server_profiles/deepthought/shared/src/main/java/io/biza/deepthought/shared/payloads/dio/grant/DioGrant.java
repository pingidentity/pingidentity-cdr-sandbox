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
package io.biza.deepthought.shared.payloads.dio.grant;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Schema(description = "A single Grant")
public class DioGrant {

  @Schema(description = "Grant Identifier", required = true)
  @JsonProperty("id")
  public UUID id;

  @Schema(description = "Token Subject", required = true)
  @JsonProperty("subject")
  @NotNull
  public String subject;

  @Schema(description = "Customer Account Associations contained in Grant", required = true)
  @JsonProperty("customerAccounts")
  @ToString.Exclude
  @NotNull
  Set<DioGrantAccount> customerAccounts;
  
  @Schema(description = "Creation time of Grant", accessMode = AccessMode.READ_ONLY, type = "string", format = "date-time")
  OffsetDateTime created;
  
  @Schema(description = "Grant Expiry time", type = "string", format = "date-time")
  @JsonProperty("expiry")
  @NotNull
  OffsetDateTime expiry;

}
