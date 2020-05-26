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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.biza.babelfish.cdr.converters.MerchantCategoryCodeToString;
import io.biza.babelfish.cdr.converters.StringToMerchantCategoryCodeConverter;
import io.biza.babelfish.cdr.support.customtypes.MerchantCategoryCodeType;
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
@ToString
@EqualsAndHashCode
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Card Transaction Information")
public class DioBankAccountTransactionCard {
  
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


  @Schema(description = "Name of the merchant for an outgoing payment to a merchant")
  @JsonProperty("merchantName")
  String merchantName;

  @Schema(description = "The merchant category code (or MCC) for an outgoing payment to a merchant",
      type = "string")
  @JsonProperty("merchantCategoryCode")
  @JsonSerialize(converter = MerchantCategoryCodeToString.class)
  @JsonDeserialize(converter = StringToMerchantCategoryCodeConverter.class)
  MerchantCategoryCodeType merchantCategoryCode;
}
