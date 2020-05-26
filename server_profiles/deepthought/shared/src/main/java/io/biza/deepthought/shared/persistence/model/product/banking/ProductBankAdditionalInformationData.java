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
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import org.hibernate.annotations.Type;
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
@Table(name = "PRODUCT_BANK_ADDITIONAL_INFORMATION")
public class ProductBankAdditionalInformationData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;

  @Transient
  @Builder.Default
  private DioSchemeType schemeType = DioSchemeType.CDR_BANKING;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PRODUCT_BANK_ID", foreignKey = @ForeignKey(name = "PRODUCT_BANK_ADDITIONAL_INFO_PRODUCT_BANK_ID"))
  @ToString.Exclude
  ProductBankData product;

  @Column(name = "OVERVIEW_URI")
  @Convert(converter = URIDataConverter.class)
  URI overviewUri;

  @Column(name = "TERMS_URI")
  @Convert(converter = URIDataConverter.class)
  URI termsUri;

  @Column(name = "ELIGIBILITY_URI")
  @Convert(converter = URIDataConverter.class)
  URI eligibilityUri;

  @Column(name = "FEES_PRICING_URI")
  @Convert(converter = URIDataConverter.class)
  URI feesPricingUri;

  @Column(name = "BUNDLE_URI")
  @Convert(converter = URIDataConverter.class)
  URI bundleUri;

  @PrePersist
  public void prePersist() {
    if(this.product() != null) {
      this.product.additionalInformation(this);
    }
  }
}
