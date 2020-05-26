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
import org.springframework.data.jpa.domain.Specification;
import io.biza.deepthought.shared.persistence.model.grant.GrantData_;
import io.biza.deepthought.shared.persistence.model.grant.GrantCustomerAccountData;
import io.biza.deepthought.shared.persistence.model.grant.GrantCustomerAccountData_;
import io.biza.deepthought.shared.persistence.model.grant.GrantData;

public class GrantSpecifications {

  public static Specification<GrantData> subject(String subject) {
    return (root, query, cb) -> {
      return cb.equal(root.get(GrantData_.subject), subject);
    };
  }

  public static Specification<GrantData> expiryBefore(OffsetDateTime expiry) {
    return (root, query, cb) -> {
      return cb.greaterThan(root.get(GrantData_.expiry), expiry);
    };
  }

  public static Specification<GrantData> accountId(UUID accountId) {
    return (root, query, cb) -> {
      Join<GrantData, GrantCustomerAccountData> grantJoin = root.join(GrantData_.customerAccounts);
      return cb.equal(grantJoin.get(GrantCustomerAccountData_.id), accountId);
    };
  }
}
