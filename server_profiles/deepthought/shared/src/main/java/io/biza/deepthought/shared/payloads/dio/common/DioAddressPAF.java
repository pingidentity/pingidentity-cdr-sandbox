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

import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.babelfish.cdr.enumerations.AddressPAFFlatUnitType;
import io.biza.babelfish.cdr.enumerations.AddressPAFFloorLevelType;
import io.biza.babelfish.cdr.enumerations.AddressPAFPostalDeliveryType;
import io.biza.babelfish.cdr.enumerations.AddressPAFStateType;
import io.biza.babelfish.cdr.enumerations.AddressPAFStreetSuffix;
import io.biza.babelfish.cdr.enumerations.AddressPAFStreetType;
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
@Schema(description = "A Deep Thought AusPost Postal Address Format")
public class DioAddressPAF {   
  
  @JsonProperty("id")
  @NotNull
  @NonNull
  @Schema(description = "Deep Thought PAF Address Identifier",
      defaultValue = "00000000-0000-0000-0000-000000000000")
  @Builder.Default
  UUID id = new UUID(0, 0);

  @JsonProperty("schemeType")
  @NotNull
  @NonNull
  @Schema(description = "Scheme Type", defaultValue = "DIO_COMMON")
  @Builder.Default
  DioSchemeType schemeType = DioSchemeType.DIO_COMMON;
  
  @Schema(
      description = "Unique identifier for an address as defined by Australia Post.  Also known as Delivery Point Identifier")
  @JsonProperty("dpid")
  String dpId;

  @Schema(
      description = "Thoroughfare number for a property (first number in a property ranged address)")
  @JsonProperty("thoroughfareNumber1")
  Integer thoroughfareNumber1;

  @Schema(
      description = "Suffix for the thoroughfare number. Only relevant is thoroughfareNumber1 is populated")
  @JsonProperty("thoroughfareNumber1Suffix")
  String thoroughfareNumber1Suffix;

  @Schema(
      description = "Second thoroughfare number (only used if the property has a ranged address eg 23-25)")
  @JsonProperty("thoroughfareNumber2")
  Integer thoroughfareNumber2;

  @Schema(
      description = "Suffix for the second thoroughfare number. Only relevant is thoroughfareNumber2 is populated")
  @JsonProperty("thoroughfareNumber2Suffix")
  String thoroughfareNumber2Suffix;

  @Schema(description = "Type of flat or unit for the address", type = "string")
  @JsonProperty("flatUnitType")
  AddressPAFFlatUnitType flatUnitType;

  @Schema(description = "Unit number (including suffix, if applicable)")
  @JsonProperty("flatUnitNumber")
  String flatUnitNumber;

  @Schema(description = "Type of floor or level for the address", type = "string")
  @JsonProperty("floorLevelType")
  AddressPAFFloorLevelType floorLevelType;

  @Schema(description = "Floor or level number (including alpha characters)")
  @JsonProperty("floorLevelNumber")
  String floorLevelNumber;

  @Schema(description = "Allotment number for the address")
  @JsonProperty("lotNumber")
  String lotNumber;

  @Schema(description = "Building/Property name 1")
  @JsonProperty("buildingName1")
  String buildingName1;

  @Schema(description = "Building/Property name 2")
  @JsonProperty("buildingName2")
  String buildingName2;

  @Schema(description = "The name of the street")
  @JsonProperty("streetName")
  String streetName;

  @Schema(
      description = "The street type. Valid enumeration defined by Australia Post PAF code file",
      type = "string")
  @JsonProperty("streetType")
  AddressPAFStreetType streetType;

  @Schema(
      description = "The street type suffix. Valid enumeration defined by Australia Post PAF code file",
      type = "string")
  @JsonProperty("streetSuffix")
  AddressPAFStreetSuffix streetSuffix;

  @Schema(
      description = "Postal delivery type. (eg. PO BOX). Valid enumeration defined by Australia Post PAF code file",
      type = "string")
  @JsonProperty("postalDeliveryType")
  AddressPAFPostalDeliveryType postalDeliveryType;

  @Schema(description = "Postal delivery number if the address is a postal delivery type")
  @JsonProperty("postalDeliveryNumber")
  Integer postalDeliveryNumber;

  @Schema(description = "Postal delivery number prefix related to the postal delivery number")
  @JsonProperty("postalDeliveryNumberPrefix")
  String postalDeliveryNumberPrefix;

  @Schema(description = "Postal delivery number suffix related to the postal delivery number")
  @JsonProperty("postalDeliveryNumberSuffix")
  String postalDeliveryNumberSuffix;

  @Schema(description = "Full name of locality", required = true)
  @JsonProperty("localityName")
  @NotEmpty(message = "Locality must be supplied")
  String localityName;

  @Schema(description = "Postcode for the locality", required = true)
  @JsonProperty("postcode")
  @NotEmpty(message = "Postcode must be supplied")
  String postCode;

  @Schema(
      description = "State in which the address belongs. Valid enumeration defined by Australia Post PAF code file",
      required = true, type = "string")
  @JsonProperty("state")
  @NotNull
  AddressPAFStateType state;


}
