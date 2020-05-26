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
package io.biza.deepthought.shared.persistence.model.bank.transaction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import io.biza.babelfish.cdr.enumerations.BankingTransactionStatus;
import io.biza.babelfish.cdr.enumerations.BankingTransactionType;
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
@Table(name = "BANK_TRANSACTION")
public class BankAccountTransactionData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;

  @Builder.Default
  private DioSchemeType schemeType = DioSchemeType.DIO_BANKING;

  @ManyToOne
  @JoinColumn(name = "ACCOUNT_ID", nullable = false, foreignKey = @ForeignKey(name = "BANK_TRANSACTION_ACCOUNT_ID_FK"))
  @ToString.Exclude
  @NotNull
  private BankAccountData account;

  @Column(name = "TRANSACTION_TYPE")
  @Enumerated(EnumType.STRING)
  @NotNull
  private BankingTransactionType type;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  @NotNull
  private BankingTransactionStatus status;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "ORIGINATED")
  private OffsetDateTime originated;

  @Column(name = "POSTED")
  private OffsetDateTime posted;

  @Column(name = "APPLIED")
  private OffsetDateTime applied;

  @Column(name = "AMOUNT")
  private BigDecimal amount;

  @Column(name = "CURRENCY")
  @Builder.Default
  private Currency currency = Currency.getInstance(Constants.DEFAULT_CURRENCY);

  @Column(name = "REFERENCE")
  @NotNull
  @Builder.Default
  private String reference = "";

  @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL, optional = true)
  private BankAccountTransactionCardData card;

  @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL, optional = true)
  private BankAccountTransactionBPAYData bpay;

  @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL, optional = true)
  private BankAccountTransactionAPCSData apcs;

  @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL, optional = true)
  private BankAccountTransactionNPPData npp;

  @PrePersist
  public void prePersist() {

    if (this.account() != null) {
      Set<BankAccountTransactionData> set = new HashSet<BankAccountTransactionData>();
      if (this.account().transactions() != null) {
        set.addAll(this.account().transactions());
      }
      set.add(this);
      this.account().transactions(set);
    }

    if (this.card() != null) {
      this.card().transaction(this);
    }
    if (this.bpay() != null) {
      this.bpay().transaction(this);
    }
    if (this.apcs() != null) {
      this.apcs().transaction(this);
    }
    if (this.npp() != null) {
      this.npp().transaction(this);
    }
  }
}
