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
import io.biza.babelfish.cdr.models.payloads.banking.account.payee.scheduled.BankingScheduledPaymentFromV1;
import io.biza.babelfish.cdr.models.payloads.banking.account.payee.scheduled.BankingScheduledPaymentV1;
import io.biza.babelfish.cdr.models.responses.ResponseBankingScheduledPaymentsListV1;
import io.biza.babelfish.cdr.models.responses.container.ResponseBankingScheduledPaymentsListDataV1;
import io.biza.deepthought.banking.api.delegate.BankingAccountScheduledPaymentApiDelegate;
import io.biza.deepthought.banking.requests.RequestScheduledPaymentsByAccounts;
import io.biza.deepthought.banking.requests.RequestScheduledPaymentsByBulk;
import io.biza.deepthought.banking.service.ScheduledPaymentService;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.component.service.GrantService;
import io.biza.deepthought.shared.exception.NotFoundException;
import io.biza.deepthought.shared.payloads.requests.RequestListAccounts;
import io.biza.deepthought.shared.persistence.model.bank.payments.ScheduledPaymentData;
import io.biza.deepthought.shared.persistence.model.grant.GrantCustomerAccountData;
import io.biza.deepthought.shared.util.CDRContainerAttributes;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingAccountScheduledPaymentApiDelegateImpl
    implements BankingAccountScheduledPaymentApiDelegate {

  @Autowired
  ScheduledPaymentService scheduledPaymentService;

  @Autowired
  GrantService grantService;

  @Autowired
  private DeepThoughtMapper mapper;


  @Override
  public ResponseEntity<ResponseBankingScheduledPaymentsListV1> listByAccount(UUID accountId,
      RequestScheduledPaymentsByAccounts requestScheduledPayments) throws NotFoundException {

    Page<ScheduledPaymentData> scheduledPaymentsPage =
        scheduledPaymentService.listScheduledPaymentsByAccount(accountId, requestScheduledPayments);

    /**
     * Do baseline mapping
     */
    List<BankingScheduledPaymentV1> scheduledPayments =
        mapper.mapAsList(scheduledPaymentsPage.getContent(), BankingScheduledPaymentV1.class);
    scheduledPayments.forEach(payment -> {
      payment.from(BankingScheduledPaymentFromV1.builder().accountId(accountId.toString()).build());
    });

    /**
     * Build response components
     */
    ResponseBankingScheduledPaymentsListV1 listResponse = ResponseBankingScheduledPaymentsListV1
        .builder().meta(CDRContainerAttributes.toMetaPaginated(scheduledPaymentsPage))
        .links(CDRContainerAttributes.toLinksPaginated(scheduledPaymentsPage))
        .data(ResponseBankingScheduledPaymentsListDataV1.builder()
            .scheduledPayments(scheduledPayments).build())
        .build();
    LOG.debug("List response came back with: {}", listResponse);
    return ResponseEntity.ok(listResponse);
  }

  @Override
  public ResponseEntity<ResponseBankingScheduledPaymentsListV1> listByAccountList(
      RequestScheduledPaymentsByAccounts requestScheduledPayments) {

    Page<ScheduledPaymentData> scheduledPaymentsPage =
        scheduledPaymentService.listScheduledPaymentsByAccountList(requestScheduledPayments);

    List<GrantCustomerAccountData> grantAccounts =
        grantService.listGrantAccounts(RequestListAccounts.builder().build());

    /**
     * Do baseline mapping
     */
    List<BankingScheduledPaymentV1> scheduledPayments = new ArrayList<BankingScheduledPaymentV1>();
    scheduledPaymentsPage.forEach(paymentData -> {
      BankingScheduledPaymentV1 payment = mapper.map(paymentData, BankingScheduledPaymentV1.class);
      for (GrantCustomerAccountData grantCustomer : grantAccounts) {
        if (paymentData.from().id().equals(grantCustomer.customerAccount().bankAccount().id())) {
          payment.from(BankingScheduledPaymentFromV1.builder()
              .accountId(grantCustomer.id().toString()).build());
          break;
        }
      }

      scheduledPayments.add(payment);
    });


    /**
     * Build response components
     */
    ResponseBankingScheduledPaymentsListV1 listResponse = ResponseBankingScheduledPaymentsListV1
        .builder().meta(CDRContainerAttributes.toMetaPaginated(scheduledPaymentsPage))
        .links(CDRContainerAttributes.toLinksPaginated(scheduledPaymentsPage))
        .data(ResponseBankingScheduledPaymentsListDataV1.builder()
            .scheduledPayments(scheduledPayments).build())
        .build();
    LOG.debug("List response came back with: {}", listResponse);
    return ResponseEntity.ok(listResponse);
  }

  @Override
  public ResponseEntity<ResponseBankingScheduledPaymentsListV1> listAll(
      RequestScheduledPaymentsByBulk requestScheduledPayments) {

    Page<ScheduledPaymentData> scheduledPaymentsPage =
        scheduledPaymentService.listScheduledPaymentsWithFilter(requestScheduledPayments);

    List<GrantCustomerAccountData> grantAccounts =
        grantService.listGrantAccounts(RequestListAccounts.builder().isOwned(
            requestScheduledPayments.isOwned() != null ? requestScheduledPayments.isOwned() : null)
            .build());

    /**
     * Do baseline mapping
     */
    List<BankingScheduledPaymentV1> scheduledPayments = new ArrayList<BankingScheduledPaymentV1>();
    scheduledPaymentsPage.forEach(paymentData -> {
      BankingScheduledPaymentV1 payment = mapper.map(paymentData, BankingScheduledPaymentV1.class);
      for (GrantCustomerAccountData grantCustomer : grantAccounts) {
        if (paymentData.from().id().equals(grantCustomer.customerAccount().bankAccount().id())) {
          payment.from(BankingScheduledPaymentFromV1.builder()
              .accountId(grantCustomer.id().toString()).build());
          break;
        }
      }

      scheduledPayments.add(payment);
    });

    /**
     * Build response components
     */
    ResponseBankingScheduledPaymentsListV1 listResponse = ResponseBankingScheduledPaymentsListV1
        .builder().meta(CDRContainerAttributes.toMetaPaginated(scheduledPaymentsPage))
        .links(CDRContainerAttributes.toLinksPaginated(scheduledPaymentsPage))
        .data(ResponseBankingScheduledPaymentsListDataV1.builder()
            .scheduledPayments(scheduledPayments).build())
        .build();
    LOG.debug("List response came back with: {}", listResponse);
    return ResponseEntity.ok(listResponse);
  }
}
