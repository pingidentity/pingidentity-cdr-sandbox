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
package io.biza.deepthought.shared.persistence.mapper.grant;

import java.util.ArrayList;
import java.util.List;
import io.biza.babelfish.cdr.enumerations.BankingLoanRepaymentType;
import io.biza.babelfish.cdr.enumerations.BankingTermDepositMaturityInstructions;
import io.biza.babelfish.cdr.enumerations.PayloadTypeBankingAccount;
import io.biza.babelfish.cdr.exceptions.LabelValueEnumValueNotSupportedException;
import io.biza.babelfish.cdr.models.payloads.banking.account.BankingAccountDetailV1;
import io.biza.babelfish.cdr.models.payloads.banking.account.BankingAccountV1;
import io.biza.babelfish.cdr.models.payloads.banking.account.BankingCreditCardAccountV1;
import io.biza.babelfish.cdr.models.payloads.banking.account.BankingLoanAccountV1;
import io.biza.babelfish.cdr.models.payloads.banking.account.BankingTermDepositAccountV1;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductFeatureWithActivatedV1;
import io.biza.deepthought.shared.mapper.OrikaFactoryConfigurerInterface;
import io.biza.deepthought.shared.payloads.dio.grant.DioGrantAccount;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountCreditCardData;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountFeatureData;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountLoanAccountData;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountTermDepositData;
import io.biza.deepthought.shared.persistence.model.grant.GrantCustomerAccountData;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;

@Slf4j
public class GrantCustomerAccountDataMapper implements OrikaFactoryConfigurerInterface {

  @Override
  public void configure(MapperFactory orikaMapperFactory) {
    orikaMapperFactory.classMap(GrantCustomerAccountData.class, DioGrantAccount.class)
        .field("customerAccount.customer", "customer")
        .field("customerAccount.bankAccount", "bankAccount").field("customerAccount.owner", "owner")
        .field("permissions", "permissions").byDefault().register();

    orikaMapperFactory.classMap(GrantCustomerAccountData.class, BankingAccountV1.class)
        .fieldAToB("id", "accountId").fieldAToB("customerAccount.owner", "isOwned")
        .fieldAToB("customerAccount.bankAccount.creationDateTime", "creationDate")
        .fieldAToB("customerAccount.bankAccount.displayName", "displayName")
        .fieldAToB("customerAccount.bankAccount.nickName", "nickname")
        .fieldAToB("customerAccount.bankAccount.status", "openStatus")
        .fieldAToB("customerAccount.bankAccount.product.cdrBanking.productCategory",
            "productCategory")
        .fieldAToB("customerAccount.bankAccount.product.name", "productName")
        .customize(new CustomMapper<GrantCustomerAccountData, BankingAccountV1>() {
          @Override
          public void mapAtoB(GrantCustomerAccountData from, BankingAccountV1 to,
              MappingContext context) {
            /**
             * Account Number masking
             */
            String accountNumber = from.customerAccount().bankAccount().accountNumber().toString();
            Double maskLength = Math.ceil(Double.valueOf(accountNumber.length()) * 0.75);
            to.maskedNumber("x".repeat(maskLength.intValue())
                .concat(accountNumber.substring(maskLength.intValue(), accountNumber.length())));
          }
        }).byDefault().register();

    orikaMapperFactory.classMap(GrantCustomerAccountData.class, BankingAccountDetailV1.class)
        .fieldAToB("id", "accountId").fieldAToB("customerAccount.owner", "isOwned")
        .fieldAToB("customerAccount.bankAccount.creationDateTime", "creationDate")
        .fieldAToB("customerAccount.bankAccount.displayName", "displayName")
        .fieldAToB("customerAccount.bankAccount.nickName", "nickname")
        .fieldAToB("customerAccount.bankAccount.status", "openStatus")
        .fieldAToB("customerAccount.bankAccount.product.cdrBanking.productCategory",
            "productCategory")
        .fieldAToB("customerAccount.bankAccount.product.name", "productName")
        .fieldAToB("customerAccount.bankAccount.branch.bsb", "bsb")
        .fieldAToB("customerAccount.bankAccount.accountNumber", "accountNumber")
        .fieldAToB("customerAccount.bankAccount.bundle.name", "bundleName")
        .customize(new CustomMapper<GrantCustomerAccountData, BankingAccountDetailV1>() {
          @Override
          public void mapAtoB(GrantCustomerAccountData from, BankingAccountDetailV1 to,
              MappingContext context) {
            /**
             * Populate features
             */
            List<BankingProductFeatureWithActivatedV1> features =
                new ArrayList<BankingProductFeatureWithActivatedV1>();
            for (BankAccountFeatureData feature : from.customerAccount().bankAccount().features()) {
              features.add(BankingProductFeatureWithActivatedV1.builder()
                  .additionalInfo(feature.feature().additionalInfo())
                  .additionalInfoUri(feature.feature().additionalInfoUri())
                  .additionalValue(feature.feature().additionalValue())
                  .featureType(feature.feature().featureType()).isActivated(feature.isActivated())
                  .build());
            }

            /**
             * Account Number masking
             */
            String accountNumber = from.customerAccount().bankAccount().accountNumber().toString();
            Double maskLength = Math.ceil(Double.valueOf(accountNumber.length()) * 0.75);
            to.maskedNumber("x".repeat(maskLength.intValue())
                .concat(accountNumber.substring(maskLength.intValue(), accountNumber.length())));

            /**
             * Account Sub type
             */
            if (from.customerAccount().bankAccount().termDeposits() != null && from.customerAccount().bankAccount().termDeposits().size() > 0) {
              LOG.debug("Processing term deposit with content of: {}", from.customerAccount().bankAccount().termDeposits());
              to.specificAccountUType(PayloadTypeBankingAccount.TERM_DEPOSIT);
              List<BankingTermDepositAccountV1> termDepositList =
                  new ArrayList<BankingTermDepositAccountV1>();
              for (BankAccountTermDepositData termDeposit : from.customerAccount().bankAccount()
                  .termDeposits()) {
                BankingTermDepositAccountV1 termDepositResult = BankingTermDepositAccountV1
                    .builder().lodgementDate(termDeposit.lodgement().toLocalDate())
                    .maturityDate(termDeposit.lodgement()
                        .plusDays(termDeposit.termLength().getDays()).toLocalDate())
                    .maturityCurrency(termDeposit.currency()).build();

                try {
                  termDepositResult.maturityInstructions(BankingTermDepositMaturityInstructions
                      .fromValue(termDeposit.maturityInstruction().toString()));
                } catch (LabelValueEnumValueNotSupportedException e) {
                  LOG.warn("Unable to convert maturity instruction to CDR type: {}",
                      termDeposit.maturityInstruction().toString());
                }

                // TODO: Rate calculation for maturity amount
                termDepositList.add(termDepositResult);
              }
            } else if (from.customerAccount().bankAccount().creditCards() != null && from.customerAccount().bankAccount().creditCards().size() > 0) {
              LOG.debug("Processing term deposit with content of: {}", from.customerAccount().bankAccount().creditCards());
              to.specificAccountUType(PayloadTypeBankingAccount.CREDIT_CARD);
              BankAccountCreditCardData creditCard =
                  from.customerAccount().bankAccount().creditCards().iterator().next();
              to.creditCard(
                  BankingCreditCardAccountV1.builder().minPaymentAmount(creditCard.paymentMinimum())
                      .paymentCurrency(creditCard.paymentCurrency())
                      .paymentDueAmount(creditCard.paymentDueAmount())
                      .paymentDueDate(creditCard.paymentDue().toLocalDate()).build());
            } else if (from.customerAccount().bankAccount().loanAccounts() != null && from.customerAccount().bankAccount().loanAccounts().size() > 0) {
              LOG.debug("Processing term deposit with content of: {}", from.customerAccount().bankAccount().loanAccounts());
              to.specificAccountUType(PayloadTypeBankingAccount.LOAN);
              BankAccountLoanAccountData loanAccount =
                  from.customerAccount().bankAccount().loanAccounts().iterator().next();
              BankingLoanAccountV1 loan = BankingLoanAccountV1.builder()
                  .loanEndDate(loanAccount.creationDateTime()
                      .plusDays(loanAccount.creationLength().getDays()).toLocalDate())
                  .maxRedraw(loanAccount.redrawAvailable())
                  .maxRedrawCurrency(loanAccount.currency())
                  .nextInstalmentDate(loanAccount.lastRepayment()
                      .plusDays(loanAccount.repaymentFrequency().getDays()).toLocalDate())
                  .originalLoanAmount(loanAccount.creationAmount())
                  .originalLoanCurrency(loanAccount.currency())
                  .originalStartDate(loanAccount.creationDateTime().toLocalDate())
                  .repaymentFrequency(loanAccount.repaymentFrequency()).build();

              // TODO: Rate calculations
              try {
                loan.repaymentType(
                    BankingLoanRepaymentType.fromValue(loanAccount.repaymentType().toString()));
              } catch (LabelValueEnumValueNotSupportedException e) {
                LOG.warn("Unable to convert maturity instruction to CDR type: {}",
                    loanAccount.repaymentType());
              }

              to.loan(loan);
            }


          }
        }).byDefault().register();

    /**
     * TODO: Calculate active deposit/lending rate from product definition using account balance
     * TODO: Retrieve addresses from customer record or introduce account address mapping
     */


  }
}
