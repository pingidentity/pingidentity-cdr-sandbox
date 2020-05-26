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

import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import io.biza.babelfish.cdr.enumerations.CommonOrganisationType;
import io.biza.deepthought.shared.Constants;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
import io.biza.deepthought.shared.persistence.converter.LocaleDataConverter;
import io.biza.deepthought.shared.persistence.model.customer.CustomerData;
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
@Table(name = "ORGANISATION")
public class OrganisationData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;
  
  @Transient
  @Builder.Default
  private DioSchemeType schemeType = DioSchemeType.CDR_COMMON;
  
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CUSTOMER_ID", foreignKey = @ForeignKey(name = "ORGANISATION_CUSTOMER_ID_FK"))
  @ToString.Exclude
  CustomerData customer;
  
  @OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL)
  @ToString.Exclude
  Set<OrganisationPersonData> persons;
  
  @Column(name = "BUSINESS_NAME")
  @NotNull
  String businessName;
  
  @Column(name = "LEGAL_NAME")
  String legalName;
  
  @Column(name = "SHORT_NAME")
  String shortName;
  
  @Column(name = "ABN")
  String abn;
  
  @Column(name = "ACN")
  String acn;
  
  @Column(name = "ACNC_REGISTERED")
  @Type(type = "true_false")
  Boolean isACNC;
  
  @Column(name = "ANZSIC_CODE")
  String industryCode;
  
  @Column(name = "ORGANISATION_TYPE")
  @Enumerated(EnumType.STRING)
  CommonOrganisationType organisationType;
  
  @Column(name = "REGISTERED_COUNTRY")
  @Convert(converter = LocaleDataConverter.class)
  @Builder.Default
  Locale registeredCountry = new Locale(Constants.DEFAULT_LANGUAGE, Constants.DEFAULT_LOCALE);
  
  @Column(name = "ESTABLISHMENT_DATE")
  LocalDate establishmentDate;
  
  @OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL)
  private Set<OrganisationAddressData> physicalAddress;

}
