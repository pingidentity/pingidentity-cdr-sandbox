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

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.biza.babelfish.cdr.converters.CurrencyToStringConverter;
import io.biza.babelfish.cdr.converters.StringToCurrencyConverter;
import io.biza.babelfish.cdr.enumerations.BankingTransactionStatus;
import io.biza.babelfish.cdr.enumerations.BankingTransactionType;
import io.biza.deepthought.shared.Constants;
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
public class DioBankAccountTransaction {
  
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

  @Schema(description = "The type of the transaction", required = true)
  @NotNull
  @JsonProperty("type")
  BankingTransactionType type;

  @Schema(
      description = "Status of the transaction whether pending or posted. Note that there is currently no provision in the standards to guarantee the ability to correlate a pending transaction with an associated posted transaction",
      required = true)
  @NotNull
  @JsonProperty("status")
  BankingTransactionStatus status;

  @Schema(description = "The transaction description as applied by the financial institution",
      required = true)
  @NotNull
  @JsonProperty("description")
  String description;
  
  @Schema(
      description = "Origination Date and Time",
      format = "date-time")
  @JsonProperty("originated")
  OffsetDateTime originated;

  @Schema(
      description = "The time the transaction was posted.",
      format = "date-time")
  @JsonProperty("posted")
  OffsetDateTime posted;
  
  @Schema(
      description = "The time the transaction was applied.",
      format = "date-time")
  @JsonProperty("applied")
  OffsetDateTime applied;

  @Schema(
      description = "The value of the transaction. Negative values mean money was outgoing from the account",
      required = true)
  @NotNull
  @JsonProperty("amount")
  BigDecimal amount;

  @Schema(description = "The currency for the transaction amount. AUD assumed if not present",
      type = "string")
  @JsonSerialize(converter = CurrencyToStringConverter.class)
  @JsonDeserialize(converter = StringToCurrencyConverter.class)
  @JsonProperty(value = "currency", defaultValue = "AUD")
  @Builder.Default
  Currency currency = Currency.getInstance(Constants.DEFAULT_CURRENCY);

  @Schema(
      description = "The reference for the transaction provided by the originating institution. Empty string if no data provided",
      required = true)
  @NotNull
  @JsonProperty("reference")
  String reference;

  @Schema(
      description = "Details associated with Card Originated Transactions")
  @JsonProperty("card")
  DioBankAccountTransactionCard card;
  
  @Schema(
      description = "Details associated with BPAY Originated Transactions")
  @JsonProperty("bpay")
  DioBankAccountTransactionBPAY bpay;
  
  @Schema(
      description = "Details associated with APCS Originated Transactions")
  @JsonProperty("apcs")
  DioBankAccountTransactionAPCS apcs;
  
  @Schema(
      description = "Details associated with NPP Originated Transactions")
  @JsonProperty("npp")
  DioBankAccountTransactionNPP npp;

  
}
