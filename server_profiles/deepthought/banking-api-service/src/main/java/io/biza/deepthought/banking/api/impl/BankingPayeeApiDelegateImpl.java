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
import io.biza.babelfish.cdr.models.payloads.banking.account.payee.BankingPayeeDetailV1;
import io.biza.babelfish.cdr.models.payloads.banking.account.payee.BankingPayeeV1;
import io.biza.babelfish.cdr.models.responses.ResponseBankingPayeeByIdV1;
import io.biza.babelfish.cdr.models.responses.ResponseBankingPayeeListV1;
import io.biza.babelfish.cdr.models.responses.container.ResponseBankingPayeeListDataV1;
import io.biza.deepthought.banking.api.delegate.BankingPayeeApiDelegate;
import io.biza.deepthought.banking.requests.RequestListPayees;
import io.biza.deepthought.banking.service.PayeeService;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.component.service.GrantService;
import io.biza.deepthought.shared.exception.InvalidSubjectException;
import io.biza.deepthought.shared.exception.NotFoundException;
import io.biza.deepthought.shared.payloads.requests.RequestListAccounts;
import io.biza.deepthought.shared.persistence.model.bank.payments.PayeeData;
import io.biza.deepthought.shared.persistence.model.grant.GrantCustomerAccountData;
import io.biza.deepthought.shared.util.CDRContainerAttributes;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingPayeeApiDelegateImpl implements BankingPayeeApiDelegate {

  @Autowired
  PayeeService payeeService;

  @Autowired
  GrantService grantService;

  @Autowired
  private DeepThoughtMapper mapper;


  @Override
  public ResponseEntity<ResponseBankingPayeeListV1> listPayees(RequestListPayees requestList)
      throws InvalidSubjectException {
    Page<PayeeData> payeePageData = payeeService.listPayeesWithFilter(requestList);

    List<BankingPayeeV1> payeeList = new ArrayList<BankingPayeeV1>();

    List<GrantCustomerAccountData> grantAccounts =
        grantService.listGrantAccounts(RequestListAccounts.builder().isOwned(true).build());

    for (PayeeData payeeData : payeePageData.getContent()) {
      BankingPayeeV1 payee = mapper.map(payeeData, BankingPayeeV1.class);
      for (GrantCustomerAccountData grantCustomer : grantAccounts) {
        if (payeeData.customer().id().equals(grantCustomer.customerAccount().customer().id())) {
          try {
            payee.payeeId(grantService.getOrCreateResourceIdByGrantIdAndObjectId(
                grantCustomer.grant().id(), payeeData.id()).toString());
            break;
          } catch (NotFoundException e) {
            LOG.error("Unable to locate grant id of {} and payee of {}", grantCustomer.grant().id(),
                payeeData.id());
          }
        }
      }
      payeeList.add(payee);
    }

    /**
     * Build response components
     */
    ResponseBankingPayeeListV1 listResponse = ResponseBankingPayeeListV1.builder()
        .meta(CDRContainerAttributes.toMetaPaginated(payeePageData))
        .links(CDRContainerAttributes.toLinksPaginated(payeePageData))
        .data(ResponseBankingPayeeListDataV1.builder().payees(payeeList).build()).build();
    LOG.debug("List response came back with: {}", listResponse);
    return ResponseEntity.ok(listResponse);
  }

  @Override
  public ResponseEntity<ResponseBankingPayeeByIdV1> getPayeeDetail(UUID payeeId)
      throws NotFoundException, InvalidSubjectException {
    PayeeData payeeData = payeeService.getPayee(payeeId);

    List<GrantCustomerAccountData> grantAccounts =
        grantService.listGrantAccounts(RequestListAccounts.builder().isOwned(true).build());

    BankingPayeeDetailV1 payee = mapper.map(payeeData, BankingPayeeDetailV1.class);
    for (GrantCustomerAccountData grantCustomer : grantAccounts) {
      if (payeeData.customer().id().equals(grantCustomer.customerAccount().customer().id())) {
        try {
          payee.payeeId(grantService
              .getOrCreateResourceIdByGrantIdAndObjectId(grantCustomer.grant().id(), payeeData.id())
              .toString());
          break;
        } catch (NotFoundException e) {
          LOG.error("Unable to locate grant id of {} and payee of {}", grantCustomer.grant().id(),
              payeeData.id());
        }
      }
    }

    ResponseBankingPayeeByIdV1 payeeResponse = new ResponseBankingPayeeByIdV1();
    payeeResponse.meta(CDRContainerAttributes.toMeta());
    payeeResponse.links(CDRContainerAttributes.toLinks());
    payeeResponse.data(payee);
    return ResponseEntity.ok(payeeResponse);
  }
}
