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
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import io.biza.deepthought.banking.requests.RequestScheduledPaymentsByAccounts;
import io.biza.deepthought.banking.requests.RequestScheduledPaymentsByBulk;
import io.biza.deepthought.shared.component.service.GrantService;
import io.biza.deepthought.shared.exception.NotFoundException;
import io.biza.deepthought.shared.payloads.requests.RequestListAccounts;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountData;
import io.biza.deepthought.shared.persistence.model.bank.payments.ScheduledPaymentData;
import io.biza.deepthought.shared.persistence.model.customer.bank.CustomerAccountData;
import io.biza.deepthought.shared.persistence.model.grant.GrantCustomerAccountData;
import io.biza.deepthought.shared.persistence.repository.ScheduledPaymentRepository;
import io.biza.deepthought.shared.persistence.specification.ScheduledPaymentSpecifications;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ScheduledPaymentService {

  @Autowired
  private GrantService grantService;

  @Autowired
  private ScheduledPaymentRepository scheduledPaymentRepository;


  public Page<ScheduledPaymentData> listScheduledPaymentsByAccount(UUID accountId,
      RequestScheduledPaymentsByAccounts requestScheduledPayments) throws NotFoundException {

    GrantCustomerAccountData grantAccount = grantService.getGrantAccount(accountId);
    LOG.debug("Received grant response for account lookup of {}", grantAccount);
    Specification<ScheduledPaymentData> filterSpecifications =
        Specification.where(ScheduledPaymentSpecifications.accountId(grantAccount.customerAccount().bankAccount().id()));

    return scheduledPaymentRepository.findAll(filterSpecifications,
        PageRequest.of(requestScheduledPayments.page() - 1, requestScheduledPayments.pageSize()));

  }

  public Page<ScheduledPaymentData> listScheduledPaymentsByAccountList(
      RequestScheduledPaymentsByAccounts requestList) {
    LOG.debug("Retrieving a list of direct debits with input request of {}", requestList);

    List<GrantCustomerAccountData> accountsByList = grantService.listGrantAccountByIds(requestList
        .accountIds().data().accountIds().stream().map(UUID::fromString).toArray(UUID[]::new));
    
    return scheduledPaymentRepository.findAll(
        ScheduledPaymentSpecifications.accountIds(accountsByList.stream().map(GrantCustomerAccountData::customerAccount)
            .collect(Collectors.toList()).stream().map(CustomerAccountData::bankAccount)
            .collect(Collectors.toList()).stream().map(BankAccountData::id)
            .collect(Collectors.toList()).toArray(UUID[]::new)),
        PageRequest.of(requestList.page() - 1, requestList.pageSize()));
  }

  public Page<ScheduledPaymentData> listScheduledPaymentsWithFilter(
      RequestScheduledPaymentsByBulk requestList) {
    LOG.debug("Retrieving a list of direct debits with input request of {}", requestList);

    List<GrantCustomerAccountData> accountsByList = grantService
        .listGrantAccounts(RequestListAccounts.builder().accountStatus(requestList.accountStatus())
            .isOwned(requestList.isOwned()).productCategory(requestList.productCategory()).build());

    return scheduledPaymentRepository.findAll(
        ScheduledPaymentSpecifications.accountIds(accountsByList.stream().map(GrantCustomerAccountData::customerAccount)
            .collect(Collectors.toList()).stream().map(CustomerAccountData::bankAccount)
            .collect(Collectors.toList()).stream().map(BankAccountData::id)
            .collect(Collectors.toList()).toArray(UUID[]::new)),
        PageRequest.of(requestList.page() - 1, requestList.pageSize()));
  }

}
