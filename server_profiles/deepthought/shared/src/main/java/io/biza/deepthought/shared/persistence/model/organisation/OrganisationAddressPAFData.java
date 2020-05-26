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
package io.biza.deepthought.shared.persistence.model.organisation;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import io.biza.babelfish.cdr.enumerations.AddressPAFFlatUnitType;
import io.biza.babelfish.cdr.enumerations.AddressPAFFloorLevelType;
import io.biza.babelfish.cdr.enumerations.AddressPAFPostalDeliveryType;
import io.biza.babelfish.cdr.enumerations.AddressPAFStateType;
import io.biza.babelfish.cdr.enumerations.AddressPAFStreetSuffix;
import io.biza.babelfish.cdr.enumerations.AddressPAFStreetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Valid
@Table(name = "ORGANISATION_ADDRESS_PAF")
public class OrganisationAddressPAFData {

  @Id
  @Type(type = "uuid-char")
  UUID id;
  
  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "ORGANISATION_ADDRESS_ID", foreignKey = @ForeignKey(name = "ORGANISATION_ADDRESS_PAF_ORGANISATION_ADDRESS_ID_FK"))
  @ToString.Exclude
  OrganisationAddressData address;
  
  @Column(name = "DP_ID")
  @Min(30000000)
  @Max(99999999)
  @NotNull
  Integer dpId;
  
  @Column(name = "THOROUGHFARE_NUMBER1")
  Integer thoroughfareNumber1;
  
  @Column(name = "THOROUGHFARE_NUMBER1_SUFFIX")
  String thoroughfareNumber1Suffix;
  
  @Column(name = "THOROUGHFARE_NUMBER2")
  Integer thoroughfareNumber2;
  
  @Column(name = "THOROUGHFARE_NUMBER2_SUFFIX")
  String thoroughfareNumber2Suffix;
  
  @Column(name = "FLAT_UNIT_TYPE")
  @Enumerated(EnumType.STRING)
  AddressPAFFlatUnitType flatUnitType;
    
  @Column(name = "FLAT_UNIT_NUMBER")
  String flatUnitNumber;
  
  @Column(name = "FLOOR_LEVEL_TYPE")
  @Enumerated(EnumType.STRING)
  AddressPAFFloorLevelType floorLevelType;
  
  @Column(name = "FLOOR_LEVEL_NUMBER")
  String floorLevelNumber;

  @Column(name = "LOT_NUMBER")
  String lotNumber;
  
  @Column(name = "BUILDING_NAME1")
  String buildingName1;
  
  @Column(name = "BUILDING_NAME2")
  String buildingName2;

  @Column(name = "STREET_NAME")
  String streetName;
  
  @Column(name = "STREET_TYPE")
  @Enumerated(EnumType.STRING)
  AddressPAFStreetType streetType;

  @Column(name = "STREET_SUFFIX")
  @Enumerated(EnumType.STRING)
  AddressPAFStreetSuffix streetSuffix;

  @Column(name = "POSTAL_DELIVERY_TYPE")
  @Enumerated(EnumType.STRING)
  AddressPAFPostalDeliveryType postalDeliveryType;
  
  @Column(name = "POSTAL_DELIVERY_NUMBER_PREFIX")
  String postalDeliveryNumberPrefix;
  
  @Column(name = "POSTAL_DELIVERY_NUMBER_SUFFIX")
  String postalDeliveryNumberSuffix;
  
  @Column(name = "LOCALITY_NAME")
  String localityName;
  
  @Column(name = "POSTCODE")
  String postCode;

  @Column(name = "STATE")
  @Enumerated(EnumType.STRING)
  AddressPAFStateType state;
  
}
