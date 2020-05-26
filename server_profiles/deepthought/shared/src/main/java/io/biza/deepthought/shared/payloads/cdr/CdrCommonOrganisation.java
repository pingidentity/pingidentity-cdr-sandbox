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
package io.biza.deepthought.shared.payloads.cdr;

import java.time.LocalDate;
import java.util.Locale;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.biza.babelfish.cdr.converters.CountryStringToLocaleConverter;
import io.biza.babelfish.cdr.converters.LocaleToCountryStringConverter;
import io.biza.babelfish.cdr.enumerations.CommonOrganisationType;
import io.biza.deepthought.shared.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Valid
@Data
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "CDR Organisation Attributes")
public class CdrCommonOrganisation {

  @Schema(description = "Australian Business Number for the organisation")
  @JsonProperty("abn")
  String abn;

  @Schema(
      description = "Australian Company Number for the organisation. Required only if an ACN is applicable for the organisation type")
  @JsonProperty("acn")
  String acn;

  @Schema(
      description = "True if registered with the ACNC.  False if not. Absent or null if not confirmed.")
  @JsonProperty("isACNCRegistered")
  Boolean isACNCRegistered;

  @Schema(description = "[ANZSIC (2006)](http://www.abs.gov.au/anzsic) code for the organisation.")
  @JsonProperty("industryCode")
  String industryCode;

  @Schema(description = "Legal organisation type", required = true)
  @JsonProperty("organisationType")
  @NotNull
  @Valid
  CommonOrganisationType organisationType;

  @Schema(
      description = "Enumeration with values from [ISO 3166 Alpha-3](https://www.iso.org/iso-3166-country-codes.html) country codes.", type = "string", defaultValue = "AUS")
  @JsonProperty("registeredCountry")
  @JsonSerialize(converter = LocaleToCountryStringConverter.class)
  @JsonDeserialize(converter = CountryStringToLocaleConverter.class)
  @NotNull
  @Builder.Default
  Locale registeredCountry = new Locale(Constants.DEFAULT_LANGUAGE, Constants.DEFAULT_LOCALE);

  @Schema(description = "The date the organisation described was established", type = "string", format = "date")
  @JsonProperty("establishmentDate")
  LocalDate establishmentDate;

}
