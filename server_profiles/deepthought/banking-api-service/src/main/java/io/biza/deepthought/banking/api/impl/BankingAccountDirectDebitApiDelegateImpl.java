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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.babelfish.cdr.models.payloads.banking.account.directdebit.BankingDirectDebitV1;
import io.biza.babelfish.cdr.models.responses.ResponseBankingDirectDebitAuthorisationListV1;
import io.biza.babelfish.cdr.models.responses.container.ResponseBankingDirectDebitAuthorisationListDataV1;
import io.biza.deepthought.banking.api.delegate.BankingAccountDirectDebitApiDelegate;
import io.biza.deepthought.banking.requests.RequestDirectDebitsByAccounts;
import io.biza.deepthought.banking.requests.RequestDirectDebitsByBulk;
import io.biza.deepthought.banking.service.DirectDebitService;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.component.service.GrantService;
import io.biza.deepthought.shared.exception.NotFoundException;
import io.biza.deepthought.shared.payloads.requests.RequestListAccounts;
import io.biza.deepthought.shared.persistence.model.bank.payments.DirectDebitData;
import io.biza.deepthought.shared.util.CDRContainerAttributes;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingAccountDirectDebitApiDelegateImpl
    implements BankingAccountDirectDebitApiDelegate {

  @Autowired
  DirectDebitService directDebitService;
  
  @Autowired
  GrantService grantService;

  @Autowired
  private DeepThoughtMapper mapper;


  @Override
  public ResponseEntity<ResponseBankingDirectDebitAuthorisationListV1> listByAccount(UUID accountId,
      RequestDirectDebitsByAccounts requestDirectDebits) throws NotFoundException {

    Page<DirectDebitData> directDebitsListPage =
        directDebitService.listDirectDebitsByAccount(accountId, requestDirectDebits);
    
    /**
     * Do baseline mapping
     */
    List<BankingDirectDebitV1> directDebitList = mapper.mapAsList(directDebitsListPage.getContent(), BankingDirectDebitV1.class);
    directDebitList.forEach(directDebit -> {
      directDebit.accountId(accountId.toString());
    });


    /**
     * Build response components
     */
    ResponseBankingDirectDebitAuthorisationListV1 listResponse =
        ResponseBankingDirectDebitAuthorisationListV1.builder()
            .meta(CDRContainerAttributes.toMetaPaginated(directDebitsListPage))
            .links(CDRContainerAttributes.toLinksPaginated(directDebitsListPage))
            .data(ResponseBankingDirectDebitAuthorisationListDataV1.builder()
                .directDebitAuthorisations(directDebitList)
                .build())
            .build();
    LOG.debug("List response came back with: {}", listResponse);
    return ResponseEntity.ok(listResponse);
  }

  @Override
  public ResponseEntity<ResponseBankingDirectDebitAuthorisationListV1> listByAccountList(
      RequestDirectDebitsByAccounts requestDirectDebits) {
    Page<DirectDebitData> directDebitsListPage =
        directDebitService.listDirectDebitsByAccountList(requestDirectDebits);
    
    /**
     * Do baseline mapping
     */
    List<BankingDirectDebitV1> directDebitList = new ArrayList<BankingDirectDebitV1>();
    
    directDebitsListPage.getContent().forEach(directDebitData -> {
      BankingDirectDebitV1 directDebit = mapper.map(directDebitData, BankingDirectDebitV1.class);
      grantService.listGrantAccounts(RequestListAccounts.builder().build()).forEach(grantAccount -> {
        if(grantAccount.customerAccount().bankAccount().id().equals(directDebitData.account().id())) {
          directDebit.accountId(grantAccount.id().toString());
        }
      });
      directDebitList.add(directDebit);
    });

    /**
     * Build response components
     */
    ResponseBankingDirectDebitAuthorisationListV1 listResponse =
        ResponseBankingDirectDebitAuthorisationListV1.builder()
            .meta(CDRContainerAttributes.toMetaPaginated(directDebitsListPage))
            .links(CDRContainerAttributes.toLinksPaginated(directDebitsListPage))
            .data(ResponseBankingDirectDebitAuthorisationListDataV1.builder()
                .directDebitAuthorisations(directDebitList)
                .build())
            .build();
    LOG.debug("List response came back with: {}", listResponse);
    return ResponseEntity.ok(listResponse);
  }

  @Override
  public ResponseEntity<ResponseBankingDirectDebitAuthorisationListV1> listAll(
      RequestDirectDebitsByBulk requestDirectDebits) {
    Page<DirectDebitData> directDebitsListPage =
        directDebitService.listDirectDebitsWithFilter(requestDirectDebits);
    
    /**
     * Do baseline mapping
     */
    List<BankingDirectDebitV1> directDebitList = new ArrayList<BankingDirectDebitV1>();
    
    directDebitsListPage.getContent().forEach(directDebitData -> {
      BankingDirectDebitV1 directDebit = mapper.map(directDebitData, BankingDirectDebitV1.class);
      grantService.listGrantAccounts(RequestListAccounts.builder().build()).forEach(grantAccount -> {
        if(grantAccount.customerAccount().bankAccount().id().equals(directDebitData.account().id())) {
          directDebit.accountId(grantAccount.id().toString());
        }
      });
      directDebitList.add(directDebit);
    });

    /**
     * Build response components
     */
    ResponseBankingDirectDebitAuthorisationListV1 listResponse =
        ResponseBankingDirectDebitAuthorisationListV1.builder()
            .meta(CDRContainerAttributes.toMetaPaginated(directDebitsListPage))
            .links(CDRContainerAttributes.toLinksPaginated(directDebitsListPage))
            .data(ResponseBankingDirectDebitAuthorisationListDataV1.builder()
                .directDebitAuthorisations(directDebitList)
                .build())
            .build();
    LOG.debug("List response came back with: {}", listResponse);
    return ResponseEntity.ok(listResponse);
  }
}
