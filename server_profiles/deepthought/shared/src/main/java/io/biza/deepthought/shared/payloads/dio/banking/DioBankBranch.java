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
package io.biza.deepthought.shared.payloads.dio.banking;

import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
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
@Schema(description = "A Deep Thought Branch Container")
public class DioBankBranch {

  @JsonProperty("id")
  @NotNull
  @NonNull
  @Schema(description = "Deep Thought Bank Account Identifier",
      defaultValue = "00000000-0000-0000-0000-000000000000")
  @Builder.Default
  public UUID id = new UUID(0, 0);

  @JsonProperty("schemeType")
  @NotNull
  @NonNull
  @Schema(description = "Deep Thought Scheme Type", defaultValue = "DIO_BANKING")
  @Builder.Default
  public DioSchemeType schemeType = DioSchemeType.DIO_BANKING;
  
  @Schema(
      description = "Australian Payments Clearing Association Number (aka BSB)",
      required = true, format = "string")
  @JsonProperty("bsb")
  @Min(100000)
  @Max(999999)
  Integer bsb;
  
  @Schema(description = "Bank Name")
  @JsonProperty("bankName")
  String bankName;
  
  @Schema(description = "Branch Name")
  @JsonProperty("branchName")
  String branchName;
  
  @Schema(description = "Branch Address")
  @JsonProperty("branchAddress")
  String branchAddress;
  
  @Schema(description = "Branch City")
  @JsonProperty("branchCity")
  String branchCity;
  
  @Schema(description = "Branch State")
  @JsonProperty("branchState")
  String branchState;
  
  @Schema(description = "Branch Postcode")
  @JsonProperty("branchPostcode")
  String branchPostcode;

}
