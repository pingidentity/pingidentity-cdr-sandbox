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

import io.biza.babelfish.cdr.enumerations.BankingPayeeType;
import io.biza.babelfish.cdr.enumerations.PayloadTypeBankingPayee;
import io.biza.babelfish.cdr.models.payloads.banking.account.payee.BankingPayeeDetailV1;
import io.biza.babelfish.cdr.models.payloads.banking.account.payee.BankingPayeeV1;
import io.biza.deepthought.shared.mapper.OrikaFactoryConfigurerInterface;
import io.biza.deepthought.shared.payloads.dio.banking.DioCustomerPayee;
import io.biza.deepthought.shared.persistence.model.bank.payments.PayeeData;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;

public class PayeeDataMapper implements OrikaFactoryConfigurerInterface {

  @Override
  public void configure(MapperFactory orikaMapperFactory) {
    orikaMapperFactory.classMap(PayeeData.class, DioCustomerPayee.class).fieldAToB("id", "id")
        .fieldAToB("id", "cdrBanking.payeeId").field("description", "cdrBanking.description")
        .field("nickName", "cdrBanking.nickname")
        .field("creationDateTime", "cdrBanking.creationDate")
        .field("domestic", "cdrBanking.domestic").field("bpay", "cdrBanking.biller")
        .field("international", "cdrBanking.international")
        .customize(new CustomMapper<PayeeData, DioCustomerPayee>() {
          @Override
          public void mapAtoB(PayeeData from, DioCustomerPayee to, MappingContext context) {
            if (from.domestic() != null) {
              to.cdrBanking.type(PayloadTypeBankingPayee.DOMESTIC);
            }
            if (from.bpay() != null) {
              to.cdrBanking.type(PayloadTypeBankingPayee.BILLER);
            }

            if (from.international() != null) {
              to.cdrBanking.type(PayloadTypeBankingPayee.INTERNATIONAL);
            }
          }
        }).byDefault().register();

    orikaMapperFactory.classMap(PayeeData.class, BankingPayeeV1.class)
        .fieldAToB("description", "description").fieldAToB("nickName", "nickname")
        .field("creationDateTime", "creationDate")
        .customize(new CustomMapper<PayeeData, BankingPayeeV1>() {
          @Override
          public void mapAtoB(PayeeData from, BankingPayeeV1 to, MappingContext context) {
            if (from.domestic() != null) {
              to.payeeType(BankingPayeeType.DOMESTIC);
            }
            if (from.bpay() != null) {
              to.payeeType(BankingPayeeType.BILLER);
            }

            if (from.international() != null) {
              to.payeeType(BankingPayeeType.INTERNATIONAL);
            }
          }
        }).byDefault().register();

    orikaMapperFactory.classMap(PayeeData.class, BankingPayeeDetailV1.class)
        .fieldAToB("description", "description").fieldAToB("nickName", "nickname")
        .field("creationDateTime", "creationDate").fieldAToB("domestic", "domestic")
        .fieldAToB("bpay", "biller").fieldAToB("international", "international")
        .customize(new CustomMapper<PayeeData, BankingPayeeDetailV1>() {
          @Override
          public void mapAtoB(PayeeData from, BankingPayeeDetailV1 to, MappingContext context) {
            if (from.domestic() != null) {
              to.payeeType(BankingPayeeType.DOMESTIC);
            }
            if (from.bpay() != null) {
              to.payeeType(BankingPayeeType.BILLER);
            }

            if (from.international() != null) {
              to.payeeType(BankingPayeeType.INTERNATIONAL);
            }
          }
        }).byDefault().register();

    // TODO : id mapping
    // TODO: detail mapping
  }
}
