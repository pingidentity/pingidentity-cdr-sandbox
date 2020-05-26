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
package io.biza.deepthought.banking.api.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.babelfish.cdr.models.payloads.banking.account.transaction.BankingTransactionDetailV1;
import io.biza.babelfish.cdr.models.payloads.banking.account.transaction.BankingTransactionV1;
import io.biza.babelfish.cdr.models.responses.ResponseBankingTransactionByIdV1;
import io.biza.babelfish.cdr.models.responses.ResponseBankingTransactionListV1;
import io.biza.babelfish.cdr.models.responses.container.ResponseBankingTransactionListDataV1;
import io.biza.deepthought.banking.api.delegate.BankingAccountTransactionApiDelegate;
import io.biza.deepthought.banking.requests.RequestListTransactions;
import io.biza.deepthought.banking.service.TransactionService;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.component.service.GrantService;
import io.biza.deepthought.shared.exception.NotFoundException;
import io.biza.deepthought.shared.persistence.model.bank.transaction.BankAccountTransactionData;
import io.biza.deepthought.shared.util.CDRContainerAttributes;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingAccountTransactionApiDelegateImpl
    implements BankingAccountTransactionApiDelegate {

  @Autowired
  TransactionService transactionService;
  
  @Autowired
  GrantService grantService;
  
  @Autowired
  private DeepThoughtMapper mapper;


  @Override
  public ResponseEntity<ResponseBankingTransactionListV1> getTransactions(
      UUID accountId, RequestListTransactions requestListTransactions) throws NotFoundException {
    
    Page<BankAccountTransactionData> transactionData = transactionService.listTransactions(accountId,  requestListTransactions);
    
    /**
     * Do baseline mapping
     */
    List<BankingTransactionV1> transactionList = mapper.mapAsList(transactionData.getContent(), BankingTransactionV1.class);
    transactionList.forEach(transaction -> {
      transaction.accountId(accountId.toString());
      try {
        transaction.transactionId(grantService.getOrCreateResourceIdByAccountIdAndObjectId(accountId, UUID.fromString(transaction.transactionId())).toString());
      } catch (NotFoundException e) {
        LOG.error("Received not found error despite having already used this accountId, bueller?!");
      }
    });

    /**
     * Build response components
     */
    ResponseBankingTransactionListV1 listResponse = ResponseBankingTransactionListV1
        .builder().meta(CDRContainerAttributes.toMetaPaginated(transactionData))
        .links(CDRContainerAttributes.toLinksPaginated(transactionData))
        .data(ResponseBankingTransactionListDataV1.builder().transactions(transactionList).build())
        .build();
    LOG.debug("List response came back with: {}", listResponse);
    return ResponseEntity.ok(listResponse);
  }

  @Override
  public ResponseEntity<ResponseBankingTransactionByIdV1> getTransactionDetail(UUID accountId,
      UUID transactionId) throws NotFoundException{
    BankAccountTransactionData transaction = transactionService.getTransaction(accountId, transactionId);
    ResponseBankingTransactionByIdV1 transactionResponse = new ResponseBankingTransactionByIdV1();
    transactionResponse.meta(CDRContainerAttributes.toMeta());
    transactionResponse.links(CDRContainerAttributes.toLinks());
    transactionResponse.data(mapper.map(transaction, BankingTransactionDetailV1.class).accountId(accountId.toString()).transactionId(transactionId.toString()));
    return ResponseEntity.ok(transactionResponse);
  }
}
