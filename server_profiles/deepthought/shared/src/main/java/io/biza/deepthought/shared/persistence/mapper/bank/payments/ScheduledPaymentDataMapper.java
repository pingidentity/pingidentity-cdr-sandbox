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

import io.biza.babelfish.cdr.models.payloads.banking.account.payee.scheduled.BankingScheduledPaymentV1;
import io.biza.deepthought.shared.mapper.OrikaFactoryConfigurerInterface;
import io.biza.deepthought.shared.payloads.dio.banking.DioCustomerScheduledPayment;
import io.biza.deepthought.shared.persistence.model.bank.payments.ScheduledPaymentData;
import ma.glasnost.orika.MapperFactory;

public class ScheduledPaymentDataMapper implements OrikaFactoryConfigurerInterface {

  @Override
  public void configure(MapperFactory orikaMapperFactory) {
    orikaMapperFactory.classMap(ScheduledPaymentData.class, DioCustomerScheduledPayment.class)
        .fieldAToB("id", "cdrBanking.scheduledPaymentId").fieldAToB("id", "id")
        .fieldAToB("from.id", "cdrBanking.from.accountId").field("nickName", "cdrBanking.nickname")
        .field("payerReference", "cdrBanking.payerReference")
        .field("payeeReference", "cdrBanking.payeeReference").field("status", "cdrBanking.status")
        .field("nextPaymentDate", "cdrBanking.recurrence.nextPaymentDate")
        .field("scheduleType", "cdrBanking.recurrence.type")
        .field("nextPaymentDate", "cdrBanking.recurrence.onceOff.paymentDate")
        .field("finalPaymentDate", "cdrBanking.recurrence.intervalSchedule.finalPaymentDate")
        .field("paymentsRemaining", "cdrBanking.recurrence.intervalSchedule.paymentsRemaining")
        .field("nonBusinessDayTreatment",
            "cdrBanking.recurrence.intervalSchedule.nonBusinessDayTreatment")
        .field("finalPaymentDate", "cdrBanking.recurrence.lastWeekDay.finalPaymentDate")
        .field("paymentsRemaining", "cdrBanking.recurrence.lastWeekDay.paymentsRemaining")
        .field("paymentFrequency", "cdrBanking.recurrence.lastWeekDay.interval")
        .field("dayOfWeek", "cdrBanking.recurrence.lastWeekDay.lastWeekDay")
        .field("nonBusinessDayTreatment",
            "cdrBanking.recurrence.lastWeekDay.nonBusinessDayTreatment")
        .field("scheduleDescription", "cdrBanking.recurrence.eventBased.description").register();

    orikaMapperFactory.classMap(ScheduledPaymentData.class, BankingScheduledPaymentV1.class)
        .fieldAToB("nickName", "nickname").fieldAToB("payerReference", "payerReference")
        .fieldAToB("payeeReference", "payeeReference").fieldAToB("status", "status")
        .fieldAToB("nextPaymentDate", "recurrence.nextPaymentDate")
        .fieldAToB("scheduleType", "recurrence.type")
        .field("nextPaymentDate", "recurrence.onceOff.paymentDate")
        .fieldAToB("finalPaymentDate", "recurrence.intervalSchedule.finalPaymentDate")
        .fieldAToB("paymentsRemaining", "recurrence.intervalSchedule.paymentsRemaining")
        .fieldAToB("nonBusinessDayTreatment", "recurrence.intervalSchedule.nonBusinessDayTreatment")
        .fieldAToB("finalPaymentDate", "recurrence.lastWeekDay.finalPaymentDate")
        .fieldAToB("paymentsRemaining", "recurrence.lastWeekDay.paymentsRemaining")
        .fieldAToB("paymentFrequency", "recurrence.lastWeekDay.interval")
        .fieldAToB("dayOfWeek", "recurrence.lastWeekDay.lastWeekDay")
        .fieldAToB("nonBusinessDayTreatment", "recurrence.lastWeekDay.nonBusinessDayTreatment")
        .fieldAToB("scheduleDescription", "recurrence.eventBased.description").register();

    // TODO: from mapping
    // TODO: id mapping

  }
}
