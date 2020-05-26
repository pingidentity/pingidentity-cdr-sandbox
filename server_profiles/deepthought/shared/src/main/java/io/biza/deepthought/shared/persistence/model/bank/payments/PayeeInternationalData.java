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
package io.biza.deepthought.shared.persistence.model.bank.payments;

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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
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
@Table(name = "PAYEE_INTERNATIONAL")
public class PayeeInternationalData {

  @Id
  @Type(type = "uuid-char")
  UUID id;
  
  @Transient
  @Builder.Default
  private DioSchemeType schemeType = DioSchemeType.DIO_BANKING;
  
  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "PAYEE_ID", foreignKey = @ForeignKey(name = "PAYEE_INTERNATIONAL_PAYEE_ID_FK"))
  @ToString.Exclude
  PayeeData payee;
  
  @Column(name = "BENEFICIARY_NAME")
  String beneficiaryName;
  
  @Column(name = "BENEFICIARY_COUNTRY")
  @Builder.Default
  Locale beneficiaryCountry = Locale.forLanguageTag("en-AU");
  
  @Column(name = "BENEFICIARY_MESSAGE")
  String beneficiaryMessage;
  
  @Column(name = "BANK_SORTCODE")
  String bankSortCode;
  
  @Column(name = "BANK_CHIP")
  String bankChip;
  
  @Column(name = "BANK_ROUTING")
  String bankRoutingNumber;
  
  @Column(name = "LEI")
  String legalEntityIdentifier;
  
  @Column(name = "BANK_COUNTRY")
  @Builder.Default
  Locale bankCountry = Locale.forLanguageTag("en-AU");
  
  @Column(name = "BANK_ACCOUNT_NUMBER")
  @NotNull
  String bankAccountNumber;
  
  @Column(name = "BANK_ADDRESS_NAME")
  String bankAddressName;
  
  @Column(name = "BANK_ADDRESS_ADDRESS")
  String bankAddressAddress;

  @Column(name = "BANK_BENEFICIARY_BIC")
  String bankBeneficiaryBic;

  @Column(name = "BANK_FEDWIRE")
  String bankFedWire;
  
  @PrePersist
  public void prePersist() {
    if (this.payee() != null) {
      this.payee().international(this);
    }
  }


  
}
