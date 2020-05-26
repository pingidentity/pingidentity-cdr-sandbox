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
import io.biza.babelfish.cdr.enumerations.BankingProductLendingRateInterestPaymentType;
import io.biza.babelfish.cdr.enumerations.BankingProductLendingRateType;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
import io.biza.deepthought.shared.persistence.converter.PeriodDataConverter;
import io.biza.deepthought.shared.persistence.converter.URIDataConverter;
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
@Table(name = "PRODUCT_BANK_RATE_LENDING")
public class ProductBankRateLendingData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;

  @Transient
  @Builder.Default
  private DioSchemeType schemeType = DioSchemeType.CDR_BANKING;

  @ManyToOne
  @JoinColumn(name = "PRODUCT_BANK_ID", nullable = false, foreignKey = @ForeignKey(name = "PRODUCT_BANK_RATE_LENDING_PRODUCT_ID_FK"))
  @JsonIgnore
  @ToString.Exclude
  private ProductBankData product;

  @NonNull
  @NotNull
  @Column(name = "RATE_TYPE")
  @Enumerated(EnumType.STRING)
  BankingProductLendingRateType lendingRateType;

  @NonNull
  @NotNull
  @Column(name = "RATE", precision = 17, scale = 16)
  BigDecimal rate;

  @Column(name = "COMPARISON_RATE", precision = 17, scale = 16)
  BigDecimal comparisonRate;

  @Column(name = "CALCULATION_FREQUENCY")
  @Convert(converter = PeriodDataConverter.class)
  Period calculationFrequency;

  @Column(name = "APPLICATION_FREQUENCY")
  @Convert(converter = PeriodDataConverter.class)
  Period applicationFrequency;

  @Column(name = "INTEREST_PAYMENT_DUE")
  @Enumerated(EnumType.STRING)
  BankingProductLendingRateInterestPaymentType interestPaymentDue;

  @Column(name = "ADDITIONAL_VALUE", length = 4096)
  String additionalValue;

  @OneToMany(mappedBy = "lendingRate", cascade = CascadeType.ALL)
  Set<ProductBankRateLendingTierData> tiers;

  @Column(name = "ADDITIONAL_INFO")
  @Lob
  String additionalInfo;

  @Column(name = "ADDITIONAL_INFO_URL")
  @Convert(converter = URIDataConverter.class)
  URI additionalInfoUri;

}
