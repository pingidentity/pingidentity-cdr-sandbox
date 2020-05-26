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
import io.biza.deepthought.banking.requests.RequestDirectDebitsByAccounts;
import io.biza.deepthought.banking.requests.RequestDirectDebitsByBulk;
import io.biza.deepthought.shared.component.service.GrantService;
import io.biza.deepthought.shared.exception.NotFoundException;
import io.biza.deepthought.shared.payloads.requests.RequestListAccounts;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountData;
import io.biza.deepthought.shared.persistence.model.bank.payments.DirectDebitData;
import io.biza.deepthought.shared.persistence.model.customer.bank.CustomerAccountData;
import io.biza.deepthought.shared.persistence.model.grant.GrantCustomerAccountData;
import io.biza.deepthought.shared.persistence.repository.BankAccountDirectDebitRepository;
import io.biza.deepthought.shared.persistence.specification.DirectDebitSpecifications;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DirectDebitService {

  @Autowired
  private GrantService grantService;

  @Autowired
  private BankAccountDirectDebitRepository directDebitRepository;


  public Page<DirectDebitData> listDirectDebitsByAccount(UUID accountId,
      RequestDirectDebitsByAccounts requestListAccounts) throws NotFoundException {

    GrantCustomerAccountData grantAccount = grantService.getGrantAccount(accountId);
    Specification<DirectDebitData> filterSpecifications =
        Specification.where(DirectDebitSpecifications.accountId(grantAccount.customerAccount().bankAccount().id()));

    return directDebitRepository.findAll(filterSpecifications,
        PageRequest.of(requestListAccounts.page() - 1, requestListAccounts.pageSize()));

  }

  public Page<DirectDebitData> listDirectDebitsByAccountList(
      RequestDirectDebitsByAccounts requestList) {
    LOG.debug("Retrieving a list of direct debits with input request of {}", requestList);

    List<GrantCustomerAccountData> accountsByList = grantService.listGrantAccountByIds(requestList
        .accountIds().data().accountIds().stream().map(UUID::fromString).toArray(UUID[]::new));

    Page<DirectDebitData> data = directDebitRepository.findAll(
        DirectDebitSpecifications.accountIds(accountsByList.stream().map(GrantCustomerAccountData::customerAccount)
            .collect(Collectors.toList()).stream().map(CustomerAccountData::bankAccount)
            .collect(Collectors.toList()).stream().map(BankAccountData::id)
            .collect(Collectors.toList()).toArray(UUID[]::new)),
        PageRequest.of(requestList.page() - 1, requestList.pageSize()));
    
    data.get().forEach(debit -> {
      try {
        debit.id(grantService.getOrCreateResourceIdByAccountIdAndObjectId(debit.account().id(), debit.id()));
      } catch (NotFoundException e) {
        LOG.error("Received a NotFoundException for an account that should have been identifiable for account {} and debit {}", debit.account().id(), debit.id());
      }
    });
    
    return data;
  }

  public Page<DirectDebitData> listDirectDebitsWithFilter(
      RequestDirectDebitsByBulk requestList) {
    LOG.debug("Retrieving a list of direct debits with input request of {}", requestList);

    List<GrantCustomerAccountData> accountsByList = grantService
        .listGrantAccounts(RequestListAccounts.builder().accountStatus(requestList.accountStatus())
            .isOwned(requestList.isOwned()).productCategory(requestList.productCategory()).build());

    return directDebitRepository.findAll(
        DirectDebitSpecifications.accountIds(accountsByList.stream().map(GrantCustomerAccountData::customerAccount)
            .collect(Collectors.toList()).stream().map(CustomerAccountData::bankAccount)
            .collect(Collectors.toList()).stream().map(BankAccountData::id)
            .collect(Collectors.toList()).toArray(UUID[]::new)),
        PageRequest.of(requestList.page() - 1, requestList.pageSize()));
  }

}
