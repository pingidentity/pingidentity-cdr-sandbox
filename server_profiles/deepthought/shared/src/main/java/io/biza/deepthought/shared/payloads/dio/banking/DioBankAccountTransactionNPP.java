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
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.babelfish.cdr.enumerations.BankingTransactionService;
import io.biza.babelfish.cdr.enumerations.PayloadTypeTransactionExtension;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Valid
@ToString(callSuper = true)
@EqualsAndHashCode
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Banking Information")
public class DioBankAccountTransactionNPP {
  
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
  @Schema(description = "Scheme Type", defaultValue = "DIO_BANKING")
  @Builder.Default
  public DioSchemeType schemeType = DioSchemeType.DIO_BANKING;

  @Schema(description = "Label of the originating payer. Mandatory for inbound payment")
  @JsonProperty("payer")
  String payer;

  @Schema(
      description = "Label of the target PayID.  Mandatory for an outbound payment. The name assigned to the BSB/Account Number or PayID (by the owner of the PayID)")
  @JsonProperty("payee")
  String payee;

  @Schema(
      description = "Optional extended data provided specific to transaction originated via NPP")
  @JsonProperty("extensionUType")
  PayloadTypeTransactionExtension extensionUType;

  @Schema(
      description = "An extended string description. Only present if specified by the extensionUType field",
      required = true)
  @NotNull
  @JsonProperty("extendedDescription")
  String extendedDescription;

  @Schema(description = "An end to end ID for the payment created at initiation")
  @JsonProperty("endToEndId")
  String endToEndId;

  @Schema(
      description = "Purpose of the payment.  Format is defined by NPP standards for the x2p1.01 overlay service")
  @JsonProperty("purposeCode")
  String purposeCode;

  @Schema(description = "Identifier of the applicable overlay service.", required = true)
  @NotNull
  @JsonProperty("service")
  @Valid
  BankingTransactionService service;
}
