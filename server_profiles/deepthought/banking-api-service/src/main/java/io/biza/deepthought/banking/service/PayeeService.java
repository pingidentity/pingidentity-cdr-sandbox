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
package io.biza.deepthought.banking.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import io.biza.babelfish.cdr.enumerations.BankingPayeeTypeWithAll;
import io.biza.deepthought.banking.requests.RequestListPayees;
import io.biza.deepthought.shared.component.service.GrantService;
import io.biza.deepthought.shared.exception.InvalidSubjectException;
import io.biza.deepthought.shared.exception.NotFoundException;
import io.biza.deepthought.shared.payloads.requests.RequestListAccounts;
import io.biza.deepthought.shared.persistence.model.bank.payments.PayeeData;
import io.biza.deepthought.shared.persistence.model.customer.CustomerData;
import io.biza.deepthought.shared.persistence.model.customer.bank.CustomerAccountData;
import io.biza.deepthought.shared.persistence.model.grant.GrantCustomerAccountData;
import io.biza.deepthought.shared.persistence.repository.PayeeRepository;
import io.biza.deepthought.shared.persistence.specification.PayeeSpecifications;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PayeeService {

  @Autowired
  private GrantService grantService;

  @Autowired
  private PayeeRepository payeeRepository;

  public Page<PayeeData> listPayeesWithFilter(RequestListPayees requestList)
      throws InvalidSubjectException {
    LOG.debug("Retrieving a list of payees with input request of {}", requestList);

    Specification<PayeeData> payeeSpecification = PayeeSpecifications.customerId(
        grantService.listGrantAccounts(RequestListAccounts.builder().isOwned(true).build()).stream()
            .map(GrantCustomerAccountData::customerAccount).collect(Collectors.toList()).stream()
            .map(CustomerAccountData::customer).collect(Collectors.toList()).stream()
            .map(CustomerData::id).collect(Collectors.toList()).toArray(UUID[]::new));

    if (List.of(BankingPayeeTypeWithAll.DOMESTIC, BankingPayeeTypeWithAll.BILLER,
        BankingPayeeTypeWithAll.INTERNATIONAL).contains(requestList.payeeType())) {
      payeeSpecification.and(PayeeSpecifications.payeeType(requestList.payeeType()));
    }

    return payeeRepository.findAll(payeeSpecification,
        PageRequest.of(requestList.page() - 1, requestList.pageSize()));
  }

  public PayeeData getPayee(UUID resourceId) throws NotFoundException, InvalidSubjectException {
    UUID payeeId = grantService.getObjectIdByResourceId(resourceId);
    Optional<PayeeData> payeeData = payeeRepository.findById(payeeId);

    if (payeeData.isPresent()) {
      return payeeData.get();
    } else {
      throw new NotFoundException("Unable to convert mapping to payee data");
    }

  }

}
