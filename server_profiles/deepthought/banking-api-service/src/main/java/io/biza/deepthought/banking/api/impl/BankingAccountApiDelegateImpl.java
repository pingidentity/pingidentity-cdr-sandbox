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

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.babelfish.cdr.models.payloads.banking.account.BankingAccountDetailV1;
import io.biza.babelfish.cdr.models.payloads.banking.account.BankingAccountV1;
import io.biza.babelfish.cdr.models.responses.ResponseBankingAccountByIdV1;
import io.biza.babelfish.cdr.models.responses.ResponseBankingAccountListV1;
import io.biza.babelfish.cdr.models.responses.container.ResponseBankingAccountListDataV1;
import io.biza.deepthought.banking.api.delegate.BankingAccountApiDelegate;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.component.service.GrantService;
import io.biza.deepthought.shared.exception.NotFoundException;
import io.biza.deepthought.shared.payloads.requests.RequestListAccounts;
import io.biza.deepthought.shared.persistence.model.grant.GrantCustomerAccountData;
import io.biza.deepthought.shared.util.CDRContainerAttributes;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingAccountApiDelegateImpl implements BankingAccountApiDelegate {

  @Autowired
  GrantService bankingService;

  @Autowired
  private DeepThoughtMapper mapper;

  @Override
  public ResponseEntity<ResponseBankingAccountByIdV1> getAccountDetail(UUID accountId)
      throws NotFoundException {
    GrantCustomerAccountData accountResult = bankingService.getGrantAccount(accountId);
    ResponseBankingAccountByIdV1 accountResponse = new ResponseBankingAccountByIdV1();
    accountResponse.meta(CDRContainerAttributes.toMeta());
    accountResponse.links(CDRContainerAttributes.toLinks());
    accountResponse.data(mapper.map(accountResult, BankingAccountDetailV1.class));
    return ResponseEntity.ok(accountResponse);
  }

  @Override
  public ResponseEntity<ResponseBankingAccountListV1> listAccounts(
      RequestListAccounts requestList) {
    Page<GrantCustomerAccountData> accountList = bankingService.listGrantAccountsPaginated(requestList);
    
    LOG.debug("Paginated account list returned: {}", accountList.getContent());

    /**
     * Build response components
     */
    ResponseBankingAccountListV1 listResponse = ResponseBankingAccountListV1.builder()
        .meta(CDRContainerAttributes.toMetaPaginated(accountList))
        .links(CDRContainerAttributes.toLinksPaginated(accountList))
        .data(ResponseBankingAccountListDataV1.builder()
            .accounts(mapper.mapAsList(accountList.getContent(), BankingAccountV1.class)).build())
        .build();
    LOG.debug("List response came back with: {}", listResponse);
    return ResponseEntity.ok(listResponse);
  }
}
