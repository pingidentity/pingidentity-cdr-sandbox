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
package io.biza.deepthought.shared.persistence.mapper.product.banking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductLendingRateV1;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductRateTierV1;
import io.biza.deepthought.shared.mapper.OrikaFactoryConfigurerInterface;
import io.biza.deepthought.shared.payloads.dio.product.DioProductRateLending;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankRateLendingData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankRateLendingTierApplicabilityData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankRateLendingTierData;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductRateTierApplicabilityV1;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;

public class ProductBankRateLendingDataMapper implements OrikaFactoryConfigurerInterface {

  @Override
  public void configure(MapperFactory orikaMapperFactory) {

    orikaMapperFactory
        .classMap(ProductBankRateLendingData.class, BankingProductLendingRateV1.class)
        .exclude("tiers").byDefault()
        .customize(new CustomMapper<ProductBankRateLendingData, BankingProductLendingRateV1>() {
          @Override
          public void mapAtoB(ProductBankRateLendingData from, BankingProductLendingRateV1 to,
              MappingContext context) {

            List<BankingProductRateTierV1> tierList =
                new ArrayList<BankingProductRateTierV1>();

            if (from.tiers() != null) {
              for (ProductBankRateLendingTierData tierData : from.tiers()) {
                BankingProductRateTierV1 rateTier = new BankingProductRateTierV1();
                rateTier.maximumValue(tierData.maximumValue());
                rateTier.minimumValue(tierData.minimumValue());
                rateTier.name(tierData.name());
                rateTier.rateApplicationMethod(tierData.rateApplicationMethod());
                rateTier.unitOfMeasure(tierData.unitOfMeasure());
                if (tierData.applicabilityConditions() != null) {
                  rateTier.applicabilityConditions(new BankingProductRateTierApplicabilityV1()
                      .additionalInfo(tierData.applicabilityConditions().additionalInfo())
                      .additionalInfoUri(tierData.applicabilityConditions().additionalInfoUri()));
                }

                tierList.add(rateTier);
              }
            }

            to.tiers(tierList);
          }
        }).register();

    orikaMapperFactory.classMap(ProductBankRateLendingData.class, DioProductRateLending.class)
        .fieldAToB("id", "id").field("schemeType", "schemeType")
        .field("lendingRateType", "cdrBanking.lendingRateType").field("rate", "cdrBanking.rate")
        .field("comparisonRate", "cdrBanking.comparisonRate")
        .field("applicationFrequency", "cdrBanking.applicationFrequency")
        .field("calculationFrequency", "cdrBanking.calculationFrequency")
        .field("interestPaymentDue", "cdrBanking.interestPaymentDue")
        .field("additionalValue", "cdrBanking.additionalValue")
        .field("additionalInfo", "cdrBanking.additionalInfo")
        .field("additionalInfoUri", "cdrBanking.additionalInfoUri")
        .customize(new CustomMapper<ProductBankRateLendingData, DioProductRateLending>() {
          @Override
          public void mapAtoB(ProductBankRateLendingData from, DioProductRateLending to,
              MappingContext context) {

            List<BankingProductRateTierV1> tierList =
                new ArrayList<BankingProductRateTierV1>();

            if (from.tiers() != null) {

              for (ProductBankRateLendingTierData tierData : from.tiers()) {
                BankingProductRateTierV1 rateTier = new BankingProductRateTierV1();
                rateTier.maximumValue(tierData.maximumValue());
                rateTier.minimumValue(tierData.minimumValue());
                rateTier.name(tierData.name());
                rateTier.rateApplicationMethod(tierData.rateApplicationMethod());
                rateTier.unitOfMeasure(tierData.unitOfMeasure());
                if (tierData.applicabilityConditions() != null) {
                  rateTier.applicabilityConditions(new BankingProductRateTierApplicabilityV1()
                      .additionalInfo(tierData.applicabilityConditions().additionalInfo())
                      .additionalInfoUri(tierData.applicabilityConditions().additionalInfoUri()));
                }

                tierList.add(rateTier);
              }
            }

            to.cdrBanking().tiers(tierList);
          }

          @Override
          public void mapBtoA(DioProductRateLending from, ProductBankRateLendingData to,
              MappingContext context) {

            Set<ProductBankRateLendingTierData> tierList =
                new HashSet<ProductBankRateLendingTierData>();

            if (from.cdrBanking().tiers() != null) {
              for (BankingProductRateTierV1 tierData : from
                  .cdrBanking().tiers()) {
                ProductBankRateLendingTierData rateTier =
                    new ProductBankRateLendingTierData();
                rateTier.maximumValue(tierData.maximumValue());
                rateTier.minimumValue(tierData.minimumValue());
                rateTier.name(tierData.name());
                rateTier.rateApplicationMethod(tierData.rateApplicationMethod());
                rateTier.unitOfMeasure(tierData.unitOfMeasure());
                if (tierData.applicabilityConditions() != null) {
                  rateTier.applicabilityConditions(
                      new ProductBankRateLendingTierApplicabilityData()
                          .additionalInfo(tierData.applicabilityConditions().additionalInfo())
                          .additionalInfoUri(
                              tierData.applicabilityConditions().additionalInfoUri()));
                }
                tierList.add(rateTier);
              }
            }
            to.tiers(tierList);
          }
        }).register();
  }

}
