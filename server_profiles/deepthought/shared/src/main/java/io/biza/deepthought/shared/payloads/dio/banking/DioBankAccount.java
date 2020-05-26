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

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.biza.babelfish.cdr.converters.DateTimeStringToOffsetDateTimeConverter;
import io.biza.babelfish.cdr.converters.OffsetDateTimeToDateTimeStringConverter;
import io.biza.deepthought.shared.payloads.cdr.CdrBankingAccount;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioAccountStatus;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioBankAccountType;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
import io.biza.deepthought.shared.payloads.dio.product.DioProduct;
import io.biza.deepthought.shared.payloads.dio.product.DioProductBundle;
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
@Schema(description = "A Deep Thought Bank Account Container")
public class DioBankAccount {

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
      description = "Creation Date Time",
      type = "string", format = "date-time", accessMode = AccessMode.READ_ONLY)
  @JsonSerialize(converter = OffsetDateTimeToDateTimeStringConverter.class)
  @JsonDeserialize(converter = DateTimeStringToOffsetDateTimeConverter.class)
  @JsonProperty("creationDateTime")
  OffsetDateTime creationDateTime;
  
  @Schema(
      description = "Open or closed status for the account. If not present then OPEN is assumed")
  @JsonProperty(value = "openStatus", defaultValue = "OPEN")
  @Builder.Default
  DioAccountStatus status = DioAccountStatus.OPEN;
  
  @Schema(description = "The category to which a product or account belongs.", required = true)
  @NotNull
  @JsonProperty("productCategory")
  DioBankAccountType accountType;
    
  @Schema(
      description = "The display name of the account as defined by the bank. This should not incorporate account numbers or PANs. If it does the values should be masked according to the rules of the MaskedAccountString common type.",
      required = true)
  @NotEmpty(message = "Must contain a display name for the account")
  @JsonProperty("displayName")
  String displayName;
  
  @Schema(description = "A customer supplied nick name for the account")
  @JsonProperty("nickName")
  String nickName;
  
  @Schema(description = "Account Number")
  @JsonProperty("accountNumber")
  BigInteger accountNumber;
  
  @Schema(description = "Associated Product Bundle")
  @JsonProperty("bundle")
  DioProductBundle bundle;
  
  @Schema(description = "Associated Product")
  @JsonProperty("product")
  DioProduct product;
  
  @JsonProperty("cdrBanking")
  @Schema(description = "CDR Banking Account")
  @Valid
  @NotNull
  public CdrBankingAccount cdrBanking;

}
