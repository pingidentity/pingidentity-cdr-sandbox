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
package io.biza.deepthought.shared.persistence.mapper.bank.account;

import io.biza.babelfish.cdr.models.payloads.banking.account.payee.scheduled.BankingScheduledPaymentFromV1;
import io.biza.deepthought.shared.mapper.OrikaFactoryConfigurerInterface;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankAccount;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountData;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFactory;

@Slf4j
public class BankAccountDataMapper implements OrikaFactoryConfigurerInterface {

  @Override
  public void configure(MapperFactory orikaMapperFactory) {
    
    orikaMapperFactory.classMap(BankAccountData.class, DioBankAccount.class).fieldAToB("id", "id")
    .fieldAToB("accountNumber", "accountNumber")
    .field("termDeposits", "cdrBanking.termDeposits")
    .field("loanAccounts", "cdrBanking.loanAccounts")
    .field("creditCards", "cdrBanking.creditCards")
        .byDefault().register();

    orikaMapperFactory.classMap(BankAccountData.class, BankingScheduledPaymentFromV1.class).field("id", "accountId").register();
  }
}
