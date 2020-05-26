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

import java.util.Locale;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
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
@Table(name = "ORGANISATION_ADDRESS_SIMPLE")
public class OrganisationAddressSimpleData {

  @Id
  @Type(type = "uuid-char")
  UUID id;
  
  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "ORGANISATION_ADDRESS_ID", foreignKey = @ForeignKey(name = "ORGANISATION_ADDRESS_SIMPLE_ORGANISATION_ADDRESS_FK"))
  @ToString.Exclude
  OrganisationAddressData address;
  
  @Column(name = "MAILING_NAME")
  String mailingName;
  
  @Column(name = "ADDRESS_LINE1")
  @NotNull
  String addressLine1;
  
  @Column(name = "ADDRESS_LINE2")
  String addressLine2;

  @Column(name = "ADDRESS_LINE3")
  String addressLine3;
  
  @Column(name = "POSTCODE")
  String postcode;
  
  @Column(name = "CITY")
  String city;
  
  @Column(name = "STATE")
  String state;
  
  @Column(name = "COUNTRY")
  @Builder.Default
  Locale country = new Locale("en", "AU");

}
