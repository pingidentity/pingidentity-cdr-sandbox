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
package io.biza.deepthought.common.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.biza.deepthought.shared.component.service.GrantService;
import io.biza.deepthought.shared.exception.InvalidSubjectException;
import io.biza.deepthought.shared.payloads.requests.RequestListAccounts;
import io.biza.deepthought.shared.persistence.model.customer.CustomerData;
import io.biza.deepthought.shared.persistence.model.grant.GrantCustomerAccountData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerService {

  @Autowired
  private GrantService grantService;

  public CustomerData getCustomer() throws InvalidSubjectException {
    return getCustomers().get(0);
  }
  
  public List<CustomerData> getCustomers() throws InvalidSubjectException {
    List<GrantCustomerAccountData> grantAccounts = grantService.listGrantAccounts(RequestListAccounts.builder().isOwned(true).build());
    Set<CustomerData> customerList = new HashSet<CustomerData>();
    
    grantAccounts.forEach(grant -> {
      customerList.add(grant.customerAccount().customer());
    });
    
    return customerList.stream().collect(Collectors.toList());
  }

}
