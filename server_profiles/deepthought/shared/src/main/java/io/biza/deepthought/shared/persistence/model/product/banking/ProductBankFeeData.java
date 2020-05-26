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
package io.biza.deepthought.shared.persistence.model.product.banking;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Period;
import java.util.Currency;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.biza.babelfish.cdr.enumerations.BankingProductFeeType;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
import io.biza.deepthought.shared.persistence.converter.CurrencyDataConverter;
import io.biza.deepthought.shared.persistence.converter.PeriodDataConverter;
import io.biza.deepthought.shared.persistence.converter.URIDataConverter;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountFeeData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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
@Table(name = "PRODUCT_BANK_FEE")
public class ProductBankFeeData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;

  @Transient
  @Builder.Default
  DioSchemeType schemeType = DioSchemeType.CDR_BANKING;
  
  @OneToMany(mappedBy = "fee", cascade = CascadeType.ALL)
  @ToString.Exclude
  Set<BankAccountFeeData> accounts;

  @ManyToOne
  @JoinColumn(name = "PRODUCT_BANK_ID", nullable = false, foreignKey = @ForeignKey(name = "PRODUCT_BANK_FEE_PRODUCT_BANK_ID_FK"))
  @JsonIgnore
  @ToString.Exclude
  ProductBankData product;
  
  @Column(name = "NAME", length = 4096)
  @NotNull
  @NonNull
  String name;

  @Column(name = "FEE_TYPE")
  @Enumerated(EnumType.STRING)
  @NotNull
  @NonNull
  BankingProductFeeType feeType;

  @Column(name = "AMOUNT")
  BigDecimal amount;

  @Column(name = "BALANCE_RATE", precision = 17, scale = 16)
  BigDecimal balanceRate;

  @Column(name = "TRANSACTION_RATE", precision = 17, scale = 16)
  BigDecimal transactionRate;

  @Column(name = "ACCRUED_RATE", precision = 17, scale = 16)
  BigDecimal accruedRate;

  @Column(name = "ACCRUAL_FREQUENCY")
  @Convert(converter = PeriodDataConverter.class)
  Period accrualFrequency;

  @Column(name = "CURRENCY")
  @Convert(converter = CurrencyDataConverter.class)
  Currency currency;

  @Column(name = "INFO")
  @Lob
  String additionalInfo;

  @Column(name = "URI")
  @Convert(converter = URIDataConverter.class)
  URI additionalInfoUri;

  @Column(name = "VALUE", length = 4096)
  String additionalValue;

  @OneToMany(mappedBy = "fee", cascade = CascadeType.ALL)
  Set<ProductBankFeeDiscountData> discounts;

}
