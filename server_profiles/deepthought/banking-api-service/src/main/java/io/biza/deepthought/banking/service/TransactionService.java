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

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import io.biza.deepthought.banking.requests.RequestListTransactions;
import io.biza.deepthought.shared.component.service.GrantService;
import io.biza.deepthought.shared.exception.NotFoundException;
import io.biza.deepthought.shared.persistence.model.bank.transaction.BankAccountTransactionData;
import io.biza.deepthought.shared.persistence.model.grant.GrantCustomerAccountData;
import io.biza.deepthought.shared.persistence.repository.BankAccountTransactionRepository;
import io.biza.deepthought.shared.persistence.specification.TransactionSpecifications;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionService {

  @Autowired
  GrantService accountService;
  
  @Autowired
  BankAccountTransactionRepository transactionRepository;
  
  public BankAccountTransactionData getTransaction(UUID accountId, UUID resourceId) throws NotFoundException {
    UUID transactionId = accountService.getObjectIdByAccountIdAndResourceId(accountId, resourceId);
    Optional<BankAccountTransactionData> transactionData = transactionRepository.findById(transactionId);
    
    if(transactionData.isPresent()) {
      return transactionData.get();
    } else {
      throw new NotFoundException("Unable to convert mapping to transaction data");
    }
    
  }

  public Page<BankAccountTransactionData> listTransactions(UUID accountId, RequestListTransactions requestListTransactions) throws NotFoundException {
    
    GrantCustomerAccountData grantAccount = accountService.getGrantAccount(accountId);
    
    Specification<BankAccountTransactionData> filterSpecifications = Specification.where(TransactionSpecifications.accountId(grantAccount.customerAccount().bankAccount().id()));
    
    /**
     * Oldest/Newest Time
     */
    if(requestListTransactions.oldestDateTime() != null) {
      filterSpecifications = filterSpecifications.and(TransactionSpecifications.oldestTime(requestListTransactions.oldestDateTime()));
    }
    if(requestListTransactions.newestDateTime() != null) {
      filterSpecifications = filterSpecifications.and(TransactionSpecifications.newestTime(requestListTransactions.newestDateTime()));
    }

    
    /**
     * Min/Max Amounts
     */
    if(requestListTransactions.maxAmount() != null) {
      filterSpecifications = filterSpecifications.and(TransactionSpecifications.maxAmount(requestListTransactions.maxAmount()));
    }
    if(requestListTransactions.minAmount() != null) {
      filterSpecifications = filterSpecifications.and(TransactionSpecifications.minAmount(requestListTransactions.minAmount()));
    }
    
    /**
     * Text filter
     */
    if(requestListTransactions.stringFilter() != null && requestListTransactions.stringFilter() != "") {
      filterSpecifications = filterSpecifications.and(TransactionSpecifications.textFilter(requestListTransactions.stringFilter()));
    }
    
    return transactionRepository.findAll(filterSpecifications, PageRequest.of(requestListTransactions.page() - 1, requestListTransactions.pageSize()));
    
  }
}
