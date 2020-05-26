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
package io.biza.deepthought.shared.persistence.mapper.product;

import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductDetailV2;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductV2;
import io.biza.deepthought.shared.mapper.OrikaFactoryConfigurerInterface;
import io.biza.deepthought.shared.payloads.dio.product.DioProduct;
import io.biza.deepthought.shared.persistence.model.product.ProductData;
import ma.glasnost.orika.MapperFactory;

public class ProductDataMapper implements OrikaFactoryConfigurerInterface {

  @Override
  public void configure(MapperFactory orikaMapperFactory) {
    orikaMapperFactory.classMap(ProductData.class, DioProduct.class).fieldAToB("id", "id")
        .byDefault().register();

    orikaMapperFactory.classMap(ProductData.class, BankingProductV2.class)
        .fieldAToB("id", "productId").fieldAToB("name", "name")
        .fieldAToB("description", "description")
        .fieldAToB("cdrBanking.effectiveFrom", "effectiveFrom")
        .fieldAToB("cdrBanking.effectiveTo", "effectiveTo")
        .fieldAToB("cdrBanking.lastUpdated", "lastUpdated")
        .fieldAToB("cdrBanking.productCategory", "productCategory").fieldAToB("brand.name", "brand")
        .fieldAToB("brand.displayName", "brandName")
        .fieldAToB("cdrBanking.applicationUri", "applicationUri")
        .fieldAToB("cdrBanking.isTailored", "isTailored")
        .fieldAToB("cdrBanking.additionalInformation", "additionalInformation")
        .fieldAToB("cdrBanking.cardArt", "cardArt").byDefault()
        .register();

    orikaMapperFactory.classMap(ProductData.class, BankingProductDetailV2.class)
        .fieldAToB("id", "productId").fieldAToB("name", "name")
        .fieldAToB("description", "description")
        .fieldAToB("cdrBanking.effectiveFrom", "effectiveFrom")
        .fieldAToB("cdrBanking.effectiveTo", "effectiveTo")
        .fieldAToB("cdrBanking.lastUpdated", "lastUpdated")
        .fieldAToB("cdrBanking.productCategory", "productCategory").fieldAToB("brand.name", "brand")
        .fieldAToB("brand.displayName", "brandName")
        .fieldAToB("cdrBanking.applicationUri", "applicationUri")
        .fieldAToB("cdrBanking.isTailored", "isTailored")
        .fieldAToB("cdrBanking.additionalInformation", "additionalInformation")
        .fieldAToB("cdrBanking.cardArt", "cardArt")
        .fieldAToB("cdrBanking.feature", "features")
        .fieldAToB("cdrBanking.constraint", "constraints").fieldAToB("cdrBanking.fee", "fees")
        .fieldAToB("cdrBanking.depositRate", "depositRates")
        .fieldAToB("cdrBanking.lendingRate", "lendingRates")
        .fieldAToB("bundle", "bundles").byDefault().register();
    
  }
}
