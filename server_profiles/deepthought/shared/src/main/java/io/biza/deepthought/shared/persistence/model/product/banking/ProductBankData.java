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

import java.net.URI;
import java.time.OffsetDateTime;
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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import io.biza.babelfish.cdr.enumerations.BankingProductCategory;
import io.biza.deepthought.shared.persistence.converter.URIDataConverter;
import io.biza.deepthought.shared.persistence.model.product.ProductData;
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
@Table(name = "PRODUCT_BANK")
public class ProductBankData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PRODUCT_ID", foreignKey = @ForeignKey(name = "PRODUCT_BANK_PRODUCT_ID_FK"))
  @ToString.Exclude
  ProductData product;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  Set<ProductBankFeatureData> feature;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  Set<ProductBankConstraintData> constraint;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  Set<ProductBankEligibilityData> eligibility;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  Set<ProductBankFeeData> fee;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  Set<ProductBankRateDepositData> depositRate;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  Set<ProductBankRateLendingData> lendingRate;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  Set<ProductBankCardArtData> cardArt;

  @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, optional = true)
  ProductBankAdditionalInformationData additionalInformation;

  @Column(name = "EFFECTIVE_FROM")
  OffsetDateTime effectiveFrom;

  @Column(name = "EFFECTIVE_TO")
  OffsetDateTime effectiveTo;

  @Column(name = "LAST_UPDATED", nullable = false)
  @UpdateTimestamp
  @Builder.Default
  OffsetDateTime lastUpdated = OffsetDateTime.now();

  @Column(name = "PRODUCT_CATEGORY", nullable = false)
  @Enumerated(EnumType.STRING)
  BankingProductCategory productCategory;

  @Column(name = "APPLICATION_URI")
  @Convert(converter = URIDataConverter.class)
  URI applicationUri;

  @Column(name = "IS_TAILORED", nullable = false)
  @NotNull
  @NonNull
  @Type(type = "true_false")
  @Builder.Default
  Boolean isTailored = false;

  @PrePersist
  public void prePersist() {
    if (this.feature != null) {
      for (ProductBankFeatureData one : this.feature) {
        one.product(this);
      }
    }
    if (this.constraint != null) {
      for (ProductBankConstraintData one : this.constraint) {
        one.product(this);
      }
    }
    if (this.eligibility != null) {
      for (ProductBankEligibilityData one : this.eligibility) {
        one.product(this);
      }
    }
    if (this.fee != null) {
      for (ProductBankFeeData one : this.fee) {
        one.product(this);
      }
    }
    if (this.depositRate != null) {
      for (ProductBankRateDepositData one : this.depositRate) {
        one.product(this);
      }
    }
    if (this.lendingRate != null) {
      for (ProductBankRateLendingData one : this.lendingRate) {
        one.product(this);
      }
    }
    if (this.cardArt != null) {
      for (ProductBankCardArtData one : this.cardArt) {
        one.product(this);
      }
    }
    if (this.additionalInformation() != null) {
      this.additionalInformation().product(this);
    }

    if (this.product != null) {
      this.product.cdrBanking(this);
    }
  }

}
