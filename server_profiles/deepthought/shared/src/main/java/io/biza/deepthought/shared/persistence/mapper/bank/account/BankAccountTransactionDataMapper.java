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

import io.biza.babelfish.cdr.models.payloads.banking.account.transaction.BankingTransactionDetailV1;
import io.biza.babelfish.cdr.models.payloads.banking.account.transaction.BankingTransactionV1;
import io.biza.deepthought.shared.mapper.OrikaFactoryConfigurerInterface;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankAccountTransaction;
import io.biza.deepthought.shared.persistence.model.bank.transaction.BankAccountTransactionData;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFactory;

@Slf4j
public class BankAccountTransactionDataMapper implements OrikaFactoryConfigurerInterface {

  @Override
  public void configure(MapperFactory orikaMapperFactory) {
    orikaMapperFactory.classMap(BankAccountTransactionData.class, DioBankAccountTransaction.class)
        .fieldAToB("id", "id").byDefault().register();
    
    orikaMapperFactory.classMap(BankAccountTransactionData.class, BankingTransactionV1.class)
    .fieldAToB("id", "transactionId")
    .fieldAToB("type", "type")
    .fieldAToB("status", "status")
    .fieldAToB("description", "description")
    .fieldAToB("posted", "postingDateTime")
    .fieldAToB("applied", "valueDateTime")    
    .fieldAToB("originated", "executionDateTime")    
    .fieldAToB("amount", "amount")    
    .fieldAToB("currency", "currency")        
    .fieldAToB("reference", "reference")    
    .fieldAToB("card.merchantCategoryCode", "merchantCategoryCode")    
    .fieldAToB("card.merchantName", "merchantName")    
    .fieldAToB("bpay.billerCode", "billerCode")    
    .fieldAToB("bpay.billerName", "billerName")    
    .fieldAToB("bpay.crn", "crn")   
    .fieldAToB("apcs.branch.bsb", "apcaNumber")
    .byDefault().register();
    
    orikaMapperFactory.classMap(BankAccountTransactionData.class, BankingTransactionDetailV1.class)
    .fieldAToB("id", "transactionId")
    .fieldAToB("type", "type")
    .fieldAToB("status", "status")
    .fieldAToB("description", "description")
    .fieldAToB("posted", "postingDateTime")
    .fieldAToB("applied", "valueDateTime")    
    .fieldAToB("originated", "executionDateTime")    
    .fieldAToB("amount", "amount")    
    .fieldAToB("currency", "currency")        
    .fieldAToB("reference", "reference")    
    .fieldAToB("card.merchantCategoryCode", "merchantCategoryCode")    
    .fieldAToB("card.merchantName", "merchantName")    
    .fieldAToB("bpay.billerCode", "billerCode")    
    .fieldAToB("bpay.billerName", "billerName")    
    .fieldAToB("bpay.crn", "crn")   
    .fieldAToB("apcs.branch.bsb", "apcaNumber")
    .fieldAToB("npp.payer", "extendedData.payer")
    .fieldAToB("npp.payee", "extendedData.payer")
    .fieldAToB("npp.endToEndId", "extendedData.x2p101Payload.endToEndId")
    .fieldAToB("npp.purposeCode", "extendedData.x2p101Payload.purposeCode")
    .fieldAToB("npp.service", "extendedData.service")
    .byDefault().register();
    
    // TODO: Account ID
    // TODO: Transaction ID
    // TODO: isDetailAvailable
  }
}
