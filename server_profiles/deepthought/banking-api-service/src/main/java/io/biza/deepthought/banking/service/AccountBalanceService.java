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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import io.biza.babelfish.cdr.enumerations.BankingTransactionStatus;
import io.biza.deepthought.banking.requests.RequestBalancesByAccounts;
import io.biza.deepthought.shared.component.service.GrantService;
import io.biza.deepthought.shared.exception.NotFoundException;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankAccountBalance;
import io.biza.deepthought.shared.payloads.requests.RequestListAccounts;
import io.biza.deepthought.shared.persistence.model.grant.GrantCustomerAccountData;
import io.biza.deepthought.shared.persistence.repository.BankAccountTransactionRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountBalanceService {

  @Autowired
  private GrantService accountService;

  @Autowired
  private BankAccountTransactionRepository transactionRepository;

  public Page<DioBankAccountBalance> listBalancesByCriteria(
      RequestListAccounts requestListAccounts) {
    List<DioBankAccountBalance> accountBalances = new ArrayList<DioBankAccountBalance>();

    Page<GrantCustomerAccountData> accountsByList =
        accountService.listGrantAccountsPaginated(requestListAccounts);

    accountsByList.forEach(grantAccount -> {
      accountBalances.add(getBalanceFromData(grantAccount));
    });

    return new PageImpl<DioBankAccountBalance>(accountBalances,
        PageRequest.of(requestListAccounts.page()-1, requestListAccounts.pageSize()),
        accountsByList.getTotalElements());

  }

  public Page<DioBankAccountBalance> listBalancesByAccountList(
      RequestBalancesByAccounts requestList) {
    List<DioBankAccountBalance> accountBalances = new ArrayList<DioBankAccountBalance>();

    Page<GrantCustomerAccountData> accountsByList = accountService
        .listPaginatedGrantAccountByIds(requestList.page(), requestList.pageSize(), requestList
            .accountIds().data().accountIds().stream().map(UUID::fromString).toArray(UUID[]::new));

    accountsByList.forEach(grantAccount -> {
      accountBalances.add(getBalanceFromData(grantAccount));
    });

    return new PageImpl<DioBankAccountBalance>(accountBalances,
        PageRequest.of(requestList.page()-1, requestList.pageSize()),
        accountsByList.getTotalElements());

  }

  private DioBankAccountBalance getBalanceFromData(GrantCustomerAccountData grantAccount) {
    LOG.debug("Retrieving balances with grant account input of: {}", grantAccount);
    BigDecimal pendingCredits = Optional
        .ofNullable(transactionRepository.creditsByAccountAndStatus(
            grantAccount.customerAccount().bankAccount(), BankingTransactionStatus.PENDING))
        .orElse(BigDecimal.ZERO);
    BigDecimal pendingDebits = Optional
        .ofNullable(transactionRepository.debitsByAccountAndStatus(
            grantAccount.customerAccount().bankAccount(), BankingTransactionStatus.PENDING))
        .orElse(BigDecimal.ZERO);
    BigDecimal postedCredits = Optional
        .ofNullable(transactionRepository.creditsByAccountAndStatus(
            grantAccount.customerAccount().bankAccount(), BankingTransactionStatus.POSTED))
        .orElse(BigDecimal.ZERO);
    BigDecimal postedDebits = Optional
        .ofNullable(transactionRepository.debitsByAccountAndStatus(
            grantAccount.customerAccount().bankAccount(), BankingTransactionStatus.POSTED))
        .orElse(BigDecimal.ZERO);

    BigDecimal currentBalance =
        pendingCredits.add(postedCredits).subtract(pendingDebits).subtract(postedDebits);
    BigDecimal availableBalance = postedCredits.subtract(pendingDebits).subtract(postedDebits);

    return DioBankAccountBalance.builder().currentBalance(currentBalance)
        .availableBalance(availableBalance).accountId(grantAccount.id().toString()).build();
  }

  public DioBankAccountBalance getBalance(UUID accountId) throws NotFoundException {
    return getBalanceFromData(accountService.getGrantAccount(accountId));
  }

}
