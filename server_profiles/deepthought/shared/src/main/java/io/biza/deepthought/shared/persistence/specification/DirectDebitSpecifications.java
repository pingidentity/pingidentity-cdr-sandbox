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

import java.util.Arrays;
import java.util.UUID;
import javax.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountData_;
import io.biza.deepthought.shared.persistence.model.bank.payments.DirectDebitData_;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountData;
import io.biza.deepthought.shared.persistence.model.bank.payments.DirectDebitData;

public class DirectDebitSpecifications {

  public static Specification<DirectDebitData> accountId(UUID accountId) {
    return (root, query, cb) -> {
      Join<DirectDebitData, BankAccountData> grantJoin = root.join(DirectDebitData_.account);
      return cb.equal(grantJoin.get(BankAccountData_.id), accountId);
    };
  }
  
  public static Specification<DirectDebitData> accountIds(UUID... accountIds) {
    return (root, query, cb) -> {
      Join<DirectDebitData, BankAccountData> grantJoin = root.join(DirectDebitData_.account);
      return grantJoin.get(BankAccountData_.id).in(Arrays.asList(accountIds));
    };
  }
  
}
