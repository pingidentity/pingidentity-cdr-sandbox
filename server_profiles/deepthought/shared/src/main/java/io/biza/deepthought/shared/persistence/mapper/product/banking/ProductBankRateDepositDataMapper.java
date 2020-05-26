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
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductDepositRateV1;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductRateTierV1;
import io.biza.deepthought.shared.mapper.OrikaFactoryConfigurerInterface;
import io.biza.deepthought.shared.payloads.dio.product.DioProductRateDeposit;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankRateDepositData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankRateDepositTierApplicabilityData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankRateDepositTierData;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductRateTierApplicabilityV1;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;

public class ProductBankRateDepositDataMapper implements OrikaFactoryConfigurerInterface {

  @Override
  public void configure(MapperFactory orikaMapperFactory) {

    orikaMapperFactory
        .classMap(ProductBankRateDepositData.class, BankingProductDepositRateV1.class)
        .exclude("tiers").byDefault()
        .customize(new CustomMapper<ProductBankRateDepositData, BankingProductDepositRateV1>() {
          @Override
          public void mapAtoB(ProductBankRateDepositData from, BankingProductDepositRateV1 to,
              MappingContext context) {

            List<BankingProductRateTierV1> tierList =
                new ArrayList<BankingProductRateTierV1>();

            if (from.tiers() != null) {
              for (ProductBankRateDepositTierData tierData : from.tiers()) {
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

    orikaMapperFactory.classMap(ProductBankRateDepositData.class, DioProductRateDeposit.class)
        .fieldAToB("id", "id").field("schemeType", "schemeType")
        .field("depositRateType", "cdrBanking.depositRateType").field("rate", "cdrBanking.rate")
        .field("applicationFrequency", "cdrBanking.applicationFrequency")
        .field("calculationFrequency", "cdrBanking.calculationFrequency")
        .field("additionalValue", "cdrBanking.additionalValue")
        .field("additionalInfo", "cdrBanking.additionalInfo")
        .field("additionalInfoUri", "cdrBanking.additionalInfoUri")
        .customize(new CustomMapper<ProductBankRateDepositData, DioProductRateDeposit>() {
          @Override
          public void mapAtoB(ProductBankRateDepositData from, DioProductRateDeposit to,
              MappingContext context) {

            List<BankingProductRateTierV1> tierList =
                new ArrayList<BankingProductRateTierV1>();

            if (from.tiers() != null) {
              for (ProductBankRateDepositTierData tierData : from.tiers()) {
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
          public void mapBtoA(DioProductRateDeposit from, ProductBankRateDepositData to,
              MappingContext context) {

            Set<ProductBankRateDepositTierData> tierList =
                new HashSet<ProductBankRateDepositTierData>();

            if (from.cdrBanking().tiers() != null) {
              for (BankingProductRateTierV1 rateTier : from
                  .cdrBanking().tiers()) {
                ProductBankRateDepositTierData tierData =
                    new ProductBankRateDepositTierData();
                tierData.maximumValue(rateTier.maximumValue());
                tierData.minimumValue(rateTier.minimumValue());
                tierData.name(rateTier.name());
                tierData.rateApplicationMethod(rateTier.rateApplicationMethod());
                tierData.unitOfMeasure(rateTier.unitOfMeasure());
                if (rateTier.applicabilityConditions() != null) {
                  tierData.applicabilityConditions(
                      new ProductBankRateDepositTierApplicabilityData()
                          .additionalInfo(rateTier.applicabilityConditions().additionalInfo())
                          .additionalInfoUri(
                              rateTier.applicabilityConditions().additionalInfoUri()));
                }
                tierList.add(tierData);
              }
            }
            to.tiers(tierList);
          }
        }).register();
  }

}
