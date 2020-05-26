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
package io.biza.deepthought.shared.persistence.specification;

import java.time.OffsetDateTime;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import io.biza.babelfish.cdr.enumerations.BankingProductCategory;
import io.biza.babelfish.cdr.enumerations.BankingProductEffectiveWithAll;
import io.biza.deepthought.shared.persistence.model.BrandData_;
import io.biza.deepthought.shared.persistence.model.product.ProductData_;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankData_;
import io.biza.deepthought.shared.persistence.model.BrandData;
import io.biza.deepthought.shared.persistence.model.product.ProductData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankData;

public class ProductBankingSpecifications {

  public static Specification<ProductData> updatedSince(OffsetDateTime updatedSince) {
    return (root, query, cb) -> {
      Join<ProductData, ProductBankData> bankingJoin = root.join(ProductData_.cdrBanking);
      return cb.greaterThan(bankingJoin.get(ProductBankData_.lastUpdated), updatedSince);
    };
  }
  
  public static Specification<ProductData> brand(String brandName) {
    return (root, query, cb) -> {
      Join<ProductData, BrandData> brandJoin = root.join(ProductData_.brand);
      return cb.equal(brandJoin.get(BrandData_.name), brandName);
    };
  }
  
  public static Specification<ProductData> productCategory(
      BankingProductCategory productCategory) {
    return (root, query, cb) -> {
      Join<ProductData, ProductBankData> bankingJoin = root.join(ProductData_.cdrBanking);
      return cb.equal(bankingJoin.get(ProductBankData_.productCategory), productCategory);
    };
  } 

  public static Specification<ProductData> effective(
      BankingProductEffectiveWithAll effective) {
    
    if (effective.equals(BankingProductEffectiveWithAll.CURRENT)) {
      return (root, query, cb) -> {
        Join<ProductData, ProductBankData> bankingJoin = root.join(ProductData_.cdrBanking);
        
        Predicate effectiveFromNow =
            cb.lessThanOrEqualTo(bankingJoin.get(ProductBankData_.effectiveFrom), OffsetDateTime.now());
        Predicate effectiveFromNull = cb.isNull(bankingJoin.get(ProductBankData_.effectiveFrom));
        Predicate effectiveToNow =
            cb.greaterThanOrEqualTo(bankingJoin.get(ProductBankData_.effectiveTo), OffsetDateTime.now());
        Predicate effectiveToNull = cb.isNull(bankingJoin.get(ProductBankData_.effectiveTo));
        return cb.and(cb.or(effectiveFromNow, effectiveFromNull),
            cb.or(effectiveToNow, effectiveToNull));
      };
    }

    if (effective.equals(BankingProductEffectiveWithAll.FUTURE)) {
      return (root, query, cb) -> {
        
        Join<ProductData, ProductBankData> bankingJoin = root.join(ProductData_.cdrBanking);

        Predicate effectiveFromNow =
            cb.greaterThanOrEqualTo(bankingJoin.get(ProductBankData_.effectiveFrom), OffsetDateTime.now());
        Predicate effectiveFromNull = cb.isNull(bankingJoin.get(ProductBankData_.effectiveFrom));
        Predicate effectiveToNow =
            cb.greaterThanOrEqualTo(bankingJoin.get(ProductBankData_.effectiveTo), OffsetDateTime.now());
        Predicate effectiveToNull = cb.isNull(bankingJoin.get(ProductBankData_.effectiveTo));
        return cb.and(cb.or(effectiveFromNow, effectiveFromNull),
            cb.or(effectiveToNow, effectiveToNull));
      };
    }

    return (root, query, cb) -> {
      /** Pointless match of anything */
      return null;
    };
  }

}
