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

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.deepthought.shared.payloads.cdr.CdrCommonPerson;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioPersonPrefix;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioPersonSuffix;
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
@Schema(description = "A Deep Thought Person Container")
public class DioPerson {

  @JsonProperty("id")
  @NotNull
  @NonNull
  @Schema(description = "Deep Thought Person Identifier",
      defaultValue = "00000000-0000-0000-0000-000000000000")
  @Builder.Default
  public UUID id = new UUID(0, 0);

  @JsonProperty("schemeType")
  @NotNull
  @NonNull
  @Schema(description = "Scheme Type", defaultValue = "CDR_COMMON")
  @Builder.Default
  public DioSchemeType schemeType = DioSchemeType.CDR_COMMON;
  
  @Schema(
      description = "Also known as title or salutation.  The prefix to the name (e.g. Mr, Mrs, Ms, Miss, Sir, etc)")
  @JsonProperty("prefix")
  DioPersonPrefix prefix;
  
  @Schema(
      description = "For people with single names this field need not be present.  The single name should be in the lastName field")
  @JsonProperty("firstName")
  String firstName;

  @Schema(description = "For people with single names the single name should be in this field",
      required = true)
  @JsonProperty("lastName")
  @NotEmpty(
      message = "Should be populated with last name or, for single names, this field should be used")
  String lastName;

  @Schema(description = "Field is mandatory but array may be empty", required = true)
  @JsonProperty("middleNames")
  @NotNull
  @Builder.Default
  List<String> middleNames = List.of();

  @Schema(description = "Used for a trailing suffix to the name (e.g. Jr)")
  @JsonProperty("suffix")
  DioPersonSuffix suffix;
  
  @Schema(description = "Preferred Phone Number", accessMode = AccessMode.READ_ONLY)
  @JsonProperty("phone")
  DioPhoneNumber phone;
  
  @Schema(description = "Preferred Email", accessMode = AccessMode.READ_ONLY)
  @JsonProperty("email")
  DioEmail email;
  
  @Schema(description = "Preferred Address", accessMode = AccessMode.READ_ONLY)
  @JsonProperty("address")
  DioAddress address;
  
  @JsonProperty("cdrCommon")
  @Schema(description = "CDR Banking Person")
  @Valid
  @NotNull
  @NonNull
  CdrCommonPerson cdrCommon;

}
