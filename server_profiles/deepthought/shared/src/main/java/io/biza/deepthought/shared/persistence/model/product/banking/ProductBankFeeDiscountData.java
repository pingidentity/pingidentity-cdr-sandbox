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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import org.hibernate.annotations.Type;
import io.biza.babelfish.cdr.enumerations.BankingProductDiscountType;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
import io.biza.deepthought.shared.persistence.converter.URIDataConverter;
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
@Table(name = "PRODUCT_BANK_FEE_DISCOUNT")
public class ProductBankFeeDiscountData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;

  @Transient
  @Builder.Default
  DioSchemeType schemeType = DioSchemeType.CDR_BANKING;

  @ManyToOne
  @JoinColumn(name = "FEE_ID", nullable = false, foreignKey = @ForeignKey(name = "PRODUCT_BANK_FEE_DISCOUNT_FEE_ID_FK"))
  @ToString.Exclude
  ProductBankFeeData fee;

  @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL)
  Set<ProductBankFeeDiscountEligibilityData> eligibility;

  @Column(name = "DESCRIPTION", length = 2048)
  String description;

  @Column(name = "DISCOUNT_TYPE")
  @Enumerated(EnumType.STRING)
  BankingProductDiscountType discountType;

  @Column(name = "AMOUNT", precision = 24, scale = 8)
  BigDecimal amount;

  @Column(name = "BALANCE_RATE", precision = 17, scale = 16)
  BigDecimal balanceRate;

  @Column(name = "TRANSACTION_RATE", precision = 17, scale = 16)
  BigDecimal transactionRate;

  @Column(name = "ACCRUED_RATE", precision = 17, scale = 16)
  BigDecimal accruedRate;

  @Column(name = "FEE_RATE", precision = 17, scale = 16)
  BigDecimal feeRate;

  @Column(name = "INFO", length = 4096)
  String additionalInfo;

  @Column(name = "URI")
  @Convert(converter = URIDataConverter.class)
  URI additionalInfoUri;

  @Column(name = "VALUE", length = 4096)
  String additionalValue;

}
