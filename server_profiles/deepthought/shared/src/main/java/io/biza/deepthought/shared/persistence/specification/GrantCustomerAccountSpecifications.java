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
import java.util.UUID;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import io.biza.babelfish.cdr.enumerations.BankingAccountStatusWithAll;
import io.biza.babelfish.cdr.enumerations.BankingProductCategory;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountData_;
import io.biza.deepthought.shared.persistence.model.customer.bank.CustomerAccountData_;
import io.biza.deepthought.shared.persistence.model.grant.GrantCustomerAccountData_;
import io.biza.deepthought.shared.persistence.model.grant.GrantData_;
import io.biza.deepthought.shared.persistence.model.product.ProductData_;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankData_;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioAccountStatus;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountData;
import io.biza.deepthought.shared.persistence.model.customer.bank.CustomerAccountData;
import io.biza.deepthought.shared.persistence.model.grant.GrantCustomerAccountData;
import io.biza.deepthought.shared.persistence.model.grant.GrantData;
import io.biza.deepthought.shared.persistence.model.product.ProductData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankData;

public class GrantCustomerAccountSpecifications {

  public static Specification<GrantCustomerAccountData> subject(String subject) {
    return (root, query, cb) -> {
      Join<GrantCustomerAccountData, GrantData> grantJoin = root.join(GrantCustomerAccountData_.grant);
      return cb.equal(grantJoin.get(GrantData_.subject), subject);
    };
  }

  public static Specification<GrantCustomerAccountData> expiryBefore(OffsetDateTime expiry) {
    return (root, query, cb) -> {
      Join<GrantCustomerAccountData, GrantData> grantJoin = root.join(GrantCustomerAccountData_.grant);
      return cb.greaterThan(grantJoin.get(GrantData_.expiry), expiry);
    };
  }
  
  public static Specification<GrantCustomerAccountData> accountIds(UUID... accountIds) {
    return (root, query, cb) -> {
      Predicate[] accountFilter = new Predicate[accountIds.length];
      for(int i = 0; i < accountIds.length; i++) {
        accountFilter[i] = cb.equal(root.get(GrantCustomerAccountData_.id), accountIds[i]);
      }
      return cb.or(accountFilter);
    };
  }

  public static Specification<GrantCustomerAccountData> accountId(UUID accountId) {
    return (root, query, cb) -> {
      return cb.equal(root.get(GrantCustomerAccountData_.id), accountId);
    };
  }
  
  public static Specification<GrantCustomerAccountData> productCategory(BankingProductCategory productCategory) {
    return (root, query, cb) -> {
      Join<GrantCustomerAccountData, CustomerAccountData> grantCustomerAccount = root.join(GrantCustomerAccountData_.customerAccount);
      Join<CustomerAccountData, BankAccountData> bankAccount = grantCustomerAccount.join(CustomerAccountData_.bankAccount);
      Join<BankAccountData, ProductData> bankRootProduct = bankAccount.join(BankAccountData_.product);
      Join<ProductData, ProductBankData> bankProductJoin = bankRootProduct.join(ProductData_.cdrBanking);
      return cb.equal(bankProductJoin.get(ProductBankData_.productCategory), productCategory);
    };
  }
  
  public static Specification<GrantCustomerAccountData> accountStatus(BankingAccountStatusWithAll accountStatus) {
    return (root, query, cb) -> {
      if(accountStatus == BankingAccountStatusWithAll.ALL) {
        return null;
      }
      
      Join<GrantCustomerAccountData, CustomerAccountData> grantCustomerAccount = root.join(GrantCustomerAccountData_.customerAccount);
      Join<CustomerAccountData, BankAccountData> bankAccount = grantCustomerAccount.join(CustomerAccountData_.bankAccount);
      return cb.equal(bankAccount.get(BankAccountData_.status), DioAccountStatus.valueOf(accountStatus.toString()));
    };
  }
  
  public static Specification<GrantCustomerAccountData> ownerStatus(String subject, Boolean ownerStatus) {
    return (root, query, cb) -> {
      Join<GrantCustomerAccountData, CustomerAccountData> customerJoin = root.join(GrantCustomerAccountData_.customerAccount);
      Predicate ownerJoin = cb.equal(customerJoin.get(CustomerAccountData_.owner), ownerStatus);
      
      Join<GrantCustomerAccountData, GrantData> grantJoin = root.join(GrantCustomerAccountData_.grant);
      Predicate subjectJoin = cb.equal(grantJoin.get(GrantData_.subject), subject);
      return cb.and(ownerJoin, subjectJoin);
    };
  }


}
