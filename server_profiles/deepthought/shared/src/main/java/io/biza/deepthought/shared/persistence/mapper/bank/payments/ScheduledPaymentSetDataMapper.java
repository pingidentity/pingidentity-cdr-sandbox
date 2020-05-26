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
package io.biza.deepthought.shared.persistence.mapper.bank.payments;

import io.biza.babelfish.cdr.enumerations.PayloadTypeBankingScheduledPaymentTo;
import io.biza.babelfish.cdr.models.payloads.banking.account.payee.scheduled.BankingScheduledPaymentSetV1;
import io.biza.babelfish.cdr.models.payloads.banking.account.payee.scheduled.BankingScheduledPaymentToV1;
import io.biza.deepthought.shared.mapper.OrikaFactoryConfigurerInterface;
import io.biza.deepthought.shared.persistence.model.bank.payments.ScheduledPaymentSetData;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;

public class ScheduledPaymentSetDataMapper implements OrikaFactoryConfigurerInterface {

  @Override
  public void configure(MapperFactory orikaMapperFactory) {
    orikaMapperFactory
        .classMap(ScheduledPaymentSetData.class, BankingScheduledPaymentSetV1.class)
        .customize(
            new CustomMapper<ScheduledPaymentSetData, BankingScheduledPaymentSetV1>() {
              @Override
              public void mapAtoB(ScheduledPaymentSetData from,
                  BankingScheduledPaymentSetV1 to, MappingContext context) {
                if (from.account() != null) {
                  to.to(BankingScheduledPaymentToV1.builder()
                      .accountId(from.account().id().toString())
                      .type(PayloadTypeBankingScheduledPaymentTo.ACCOUNT_ID).build());
                }
                if (from.payee() != null) {
                  to.to(BankingScheduledPaymentToV1.builder().payeeId(from.payee().id().toString())
                      .type(PayloadTypeBankingScheduledPaymentTo.PAYEE_ID).build());
                }
                // TODO: Support domestic, biller and international payment sets
              }
            })
        .byDefault().register();

  }
}
