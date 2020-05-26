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

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountData_;
import io.biza.deepthought.shared.persistence.model.bank.transaction.BankAccountTransactionData_;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountData;
import io.biza.deepthought.shared.persistence.model.bank.transaction.BankAccountTransactionData;

public class TransactionSpecifications {

  public static Specification<BankAccountTransactionData> accountId(UUID accountId) {
    return (root, query, cb) -> {
      Join<BankAccountTransactionData, BankAccountData> grantJoin = root.join(BankAccountTransactionData_.account);
      return cb.equal(grantJoin.get(BankAccountData_.id), accountId);
    };
  }
  
  public static Specification<BankAccountTransactionData> oldestTime(OffsetDateTime oldestTime) {
    return (root, query, cb) -> {
      return cb.greaterThan(root.get(BankAccountTransactionData_.posted), oldestTime);
    };
  }

  public static Specification<BankAccountTransactionData> newestTime(OffsetDateTime newestTime) {
    return (root, query, cb) -> {
      return cb.lessThan(root.get(BankAccountTransactionData_.posted), newestTime);
    };
  }
  
  public static Specification<BankAccountTransactionData> minAmount(BigDecimal minAmount) {
    return (root, query, cb) -> {
      return cb.greaterThan(root.get(BankAccountTransactionData_.amount), minAmount);
    };
  }

  public static Specification<BankAccountTransactionData> maxAmount(BigDecimal maxAmount) {
    return (root, query, cb) -> {
      return cb.lessThan(root.get(BankAccountTransactionData_.amount), maxAmount);
    };
  }
  
  public static Specification<BankAccountTransactionData> textFilter(String text) {
    return (root, query, cb) -> {
      return cb.like(root.get(BankAccountTransactionData_.reference), "%" + text + "%");
    };
  }

}
