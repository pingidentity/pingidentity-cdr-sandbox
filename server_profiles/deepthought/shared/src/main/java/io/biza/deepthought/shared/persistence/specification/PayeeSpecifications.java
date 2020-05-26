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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import io.biza.babelfish.cdr.enumerations.BankingPayeeTypeWithAll;
import io.biza.deepthought.shared.persistence.model.bank.payments.PayeeData_;
import io.biza.deepthought.shared.persistence.model.customer.CustomerData_;
import io.biza.deepthought.shared.persistence.model.bank.payments.PayeeData;
import io.biza.deepthought.shared.persistence.model.customer.CustomerData;

public class PayeeSpecifications {

  public static Specification<PayeeData> customerId(UUID... customerId) {
    return (root, query, cb) -> {
      Join<PayeeData, CustomerData> customerJoin = root.join(PayeeData_.customer);
      Set<UUID> customerIds = new LinkedHashSet<UUID>(Arrays.asList(customerId));
      List<Predicate> customerFilter = new ArrayList<Predicate>();
      customerIds.stream().forEach(id -> {
        customerFilter.add(cb.equal(customerJoin.get(CustomerData_.id), id));
      });      
      return cb.or(customerFilter.toArray(new Predicate[customerFilter.size()]));
    };
  }
  
  public static Specification<PayeeData> payeeType(BankingPayeeTypeWithAll payeeType) {
    return (root, query, cb) -> {
      
      if(payeeType.equals(BankingPayeeTypeWithAll.BILLER)) {
        return cb.isNotNull(root.get(PayeeData_.bpay));
      }
      
      if(payeeType.equals(BankingPayeeTypeWithAll.INTERNATIONAL)) {
        return cb.isNotNull(root.get(PayeeData_.international));
      }
      
      if(payeeType.equals(BankingPayeeTypeWithAll.DOMESTIC)) {
        return cb.isNotNull(root.get(PayeeData_.domestic));
      }
      
      return null;
    };
  }  

}
