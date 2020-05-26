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

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import org.hibernate.annotations.Type;
import io.biza.deepthought.shared.Constants;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountData;
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
@Table(name = "SCHEDULED_PAYMENT_SET")
public class ScheduledPaymentSetData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;
  
  @Transient
  @Builder.Default
  private DioSchemeType schemeType = DioSchemeType.DIO_BANKING;
  
  @ManyToOne
  @JoinColumn(name = "SCHEDULED_PAYMENT_ID", nullable = false, foreignKey = @ForeignKey(name = "SCHEDULED_PAYMENT_SET_SCHEDULED_PAYMENT_ID_FK"))
  @ToString.Exclude
  ScheduledPaymentData scheduledPayment;
  
  @Column(name = "AMOUNT")
  BigDecimal amount;
  
  @Column(name = "IS_AMOUNT_CALCULATED")
  Boolean isAmountCalculated;
  
  @Column(name = "CURRENCY")
  @Builder.Default
  Currency currency = Currency.getInstance(Constants.DEFAULT_CURRENCY);
  
  @ManyToOne
  @JoinColumn(name = "ACCOUNT_ID", foreignKey = @ForeignKey(name = "SCHEDULED_PAYMENT_SET_ACCOUNT_ID_FK"))
  @ToString.Exclude
  BankAccountData account;
  
  @ManyToOne
  @JoinColumn(name = "PAYEE_ID", foreignKey = @ForeignKey(name = "SCHEDULED_PAYMENT_SET_PAYEE_ID_FK"))
  @ToString.Exclude
  PayeeData payee;

  
}
