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
package io.biza.deepthought.shared.persistence.model.bank.account;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import io.biza.deepthought.shared.Constants;
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
@Table(name = "BANK_ACCOUNT_CREDIT_CARD")
public class BankAccountCreditCardData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;
  
  @Transient
  @Builder.Default
  DioSchemeType schemeType = DioSchemeType.DIO_BANKING;
  
  @ManyToOne
  @JoinColumn(name = "ACCOUNT_ID", nullable = false, foreignKey = @ForeignKey(name = "BANK_ACCOUNT_CREDIT_CARD_ACCOUNT_ID_FK"))
  @ToString.Exclude
  BankAccountData account;
  
  @Column(name = "LAST_STATEMENT")
  @NotNull
  @CreationTimestamp
  OffsetDateTime lastStatement;
  
  @Column(name = "PAYMENT_CURRENCY")
  @NotNull
  @Builder.Default
  Currency paymentCurrency = Currency.getInstance(Constants.DEFAULT_CURRENCY);
  
  @Column(name = "MIN_PAYMENT_AMOUNT")
  @NotNull
  BigDecimal paymentMinimum;
  
  @Column(name = "PAYMENT_DUE_AMOUNT")
  @NotNull
  BigDecimal paymentDueAmount;
  
  @Column(name = "PAYMENT_DUE_DATE")
  @NotNull
  OffsetDateTime paymentDue;
  
  @PrePersist
  public void prePersist() {
    if (this.account() != null) {
      Set<BankAccountCreditCardData> set = new HashSet<BankAccountCreditCardData>();
      if (this.account().creditCards() != null) {
        set.addAll(this.account().creditCards());
      }
      set.add(this);
      this.account().creditCards(set);
    }
  }

  
}
