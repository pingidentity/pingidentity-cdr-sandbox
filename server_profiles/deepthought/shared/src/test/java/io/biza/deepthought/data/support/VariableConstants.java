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
package io.biza.deepthought.data.support;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import io.biza.babelfish.cdr.enumerations.AddressPurpose;
import io.biza.babelfish.cdr.enumerations.BankingProductCategory;
import io.biza.babelfish.cdr.enumerations.BankingProductConstraintType;
import io.biza.babelfish.cdr.enumerations.BankingProductDepositRateType;
import io.biza.babelfish.cdr.enumerations.BankingProductDiscountEligibilityType;
import io.biza.babelfish.cdr.enumerations.BankingProductDiscountType;
import io.biza.babelfish.cdr.enumerations.BankingProductEligibilityType;
import io.biza.babelfish.cdr.enumerations.BankingProductFeatureType;
import io.biza.babelfish.cdr.enumerations.BankingProductFeeType;
import io.biza.babelfish.cdr.enumerations.BankingProductLendingRateInterestPaymentType;
import io.biza.babelfish.cdr.enumerations.BankingProductLendingRateType;
import io.biza.babelfish.cdr.enumerations.BankingProductRateTierApplicationMethod;
import io.biza.babelfish.cdr.enumerations.BankingTransactionStatus;
import io.biza.babelfish.cdr.enumerations.BankingTransactionType;
import io.biza.babelfish.cdr.enumerations.CommonOrganisationType;
import io.biza.babelfish.cdr.enumerations.CommonUnitOfMeasureType;
import io.biza.babelfish.cdr.enumerations.PayloadTypeBankingDomesticPayeePayId;
import io.biza.deepthought.shared.Constants;
import io.biza.deepthought.shared.payloads.cdr.CdrCommonOrganisation;
import io.biza.deepthought.shared.payloads.cdr.CdrCommonPerson;
import io.biza.deepthought.shared.payloads.dio.common.DioAddress;
import io.biza.deepthought.shared.payloads.dio.common.DioAddressSimple;
import io.biza.deepthought.shared.payloads.dio.common.DioEmail;
import io.biza.deepthought.shared.payloads.dio.common.DioOrganisation;
import io.biza.deepthought.shared.payloads.dio.common.DioPerson;
import io.biza.deepthought.shared.payloads.dio.common.DioPhoneNumber;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioEmailType;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioLoanRepaymentType;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioMaturityInstructionType;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioPersonPrefix;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioPersonSuffix;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioPhoneType;

public class VariableConstants {
  public static final String PRODUCT_BUNDLE_ADDITIONAL_INFO = "Product Bundle Additional Info";
  public static final URI PRODUCT_BUNDLE_ADDITIONAL_INFO_URI =
      URI.create("http://acmebank.com.au/bundle/additional/info");
  public static final String BRAND_NAME = "Brand Name";
  public static final String BRAND_DISPLAY_NAME = "Brand Display Name";
  public static final String PRODUCT_BUNDLE_DESCRIPTION = "Product Bundle Description";
  public static final String PRODUCT_BUNDLE_NAME = "Product Bundle Name";
  public static final String PRODUCT_DESCRIPTION = "Product Description";
  public static final String PRODUCT_NAME = "Product Name";
  public static final BankingProductCategory PRODUCT_CATEGORY =
      BankingProductCategory.TRANS_AND_SAVINGS_ACCOUNTS;
  public static final Boolean PRODUCT_ISTAILORED = false;
  public static final OffsetDateTime PRODUCT_EFFECTIVE_FROM =
      OffsetDateTime.of(2020, 01, 01, 00, 00, 00, 00, ZoneOffset.UTC);
  public static final OffsetDateTime PRODUCT_EFFECTIVE_TO =
      OffsetDateTime.of(2030, 01, 01, 00, 00, 00, 00, ZoneOffset.UTC);
  public static final OffsetDateTime PRODUCT_LAST_UPDATED =
      OffsetDateTime.of(2020, 01, 14, 00, 00, 00, 00, ZoneOffset.UTC);
  public static final URI PRODUCT_ADDITIONAL_INFO_OVERVIEW_URI =
      URI.create("http://www.acme.com.au/product/additional/overview");
  public static final URI PRODUCT_ADDITIONAL_INFO_TERMS_URI =
      URI.create("http://www.acme.com.au/product/additional/terms");
  public static final URI PRODUCT_ADDITIONAL_INFO_ELIGIBILITY_URI =
      URI.create("http://www.acme.com.au/product/additional/eligibility");
  public static final URI PRODUCT_ADDITIONAL_INFO_FEES_URI =
      URI.create("http://www.acme.com.au/product/additional/fees");
  public static final URI PRODUCT_ADDITIONAL_INFO_BUNDLE_URI =
      URI.create("http://www.acme.com.au/product/additional/bundle");
  public static final String PRODUCT_CARDART_TITLE = "Card Art Title";
  public static final URI PRODUCT_CARDART_URI =
      URI.create("http://www.acme.com.au/product/cardart/image");
  public static final URI PRODUCT_APPLICATION_URI =
      URI.create("http://www.acme.com.au/product/application");
  public static final BankingProductFeatureType PRODUCT_FEATURE_TYPE =
      BankingProductFeatureType.ADDITIONAL_CARDS;
  public static final String PRODUCT_FEATURE_ADDITIONAL_VALUE = "10";
  public static final String PRODUCT_FEATURE_ADDITIONAL_INFO =
      "Additional Information Regarding Additional Cards";
  public static final URI PRODUCT_FEATURE_ADDITIONAL_INFO_URI =
      URI.create("http://www.acme.com.au/product/feature/additional/info");
  public static final BankingProductConstraintType PRODUCT_CONSTRAINT_TYPE =
      BankingProductConstraintType.MAX_BALANCE;
  public static final String PRODUCT_CONSTRAINT_ADDITIONAL_VALUE = "100000.00";
  public static final String PRODUCT_CONSTRAINT_ADDITIONAL_INFO =
      "Additional Information Regarding Maximum Balance Limit";
  public static final URI PRODUCT_CONSTRAINT_ADDITIONAL_INFO_URI =
      URI.create("http://www.acme.com.au/product/constraint/additional/info");
  public static final BankingProductEligibilityType PRODUCT_ELIGIBILITY_TYPE =
      BankingProductEligibilityType.MIN_AGE;
  public static final String PRODUCT_ELIGIBILITY_ADDITIONAL_VALUE = "18";
  public static final String PRODUCT_ELIGIBILITY_ADDITIONAL_INFO = "Must be 18 years or older";
  public static final URI PRODUCT_ELIGIBILITY_ADDITIONAL_INFO_URI =
      URI.create("http://www.acme.com.au/product/eligibility/additional/info");
  public static final String PRODUCT_FEE1_NAME = "Monthly Account Keeping Fee";
  public static final BankingProductFeeType PRODUCT_FEE1_TYPE = BankingProductFeeType.PERIODIC;
  public static final BigDecimal PRODUCT_FEE1_AMOUNT = new BigDecimal("10.00");
  public static final Currency PRODUCT_FEE1_CURRENCY = Currency.getInstance("AUD");
  public static final String PRODUCT_FEE1_ADDITIONAL_VALUE = "P1M";
  public static final String PRODUCT_FEE1_ADDITIONAL_INFO =
      "Additional Info about Monthly Account Keeping Fee";
  public static final URI PRODUCT_FEE1_ADDITIONAL_INFO_URI =
      URI.create("http://www.acme.com.au/product/fee1/additional/info");
  public static final String PRODUCT_FEE1_DISCOUNT_DESCRIPTION =
      "Product Fee Discount for Over 18s";
  public static final BankingProductDiscountType PRODUCT_FEE1_DISCOUNT_TYPE =
      BankingProductDiscountType.ELIGIBILITY_ONLY;
  public static final BigDecimal PRODUCT_FEE1_DISCOUNT_AMOUNT = new BigDecimal("5.00");
  public static final String PRODUCT_FEE1_DISCOUNT_ADDITIONAL_VALUE = "Eligibility Criteria Apply";
  public static final String PRODUCT_FEE1_DISCOUNT_ADDITIONAL_INFO =
      "Additional Information about the Over 18s Discount";
  public static final URI PRODUCT_FEE1_DISCOUNT_ADDITIONAL_URI =
      URI.create("http://www.acme.com.au/product/fee1/discount/additional/info");
  public static final BankingProductDiscountEligibilityType PRODUCT_FEE1_DISCOUNT_ELIGIBILITY_TYPE =
      BankingProductDiscountEligibilityType.MIN_AGE;
  public static final String PRODUCT_FEE1_DISCOUNT_ELIGIBILITY_ADDITIONAL_VALUE = "18";
  public static final String PRODUCT_FEE1_DISCOUNT_ELIGIBILITY_ADDITIONAL_INFO =
      "Because those over 18 are more responsible we save you $5!";
  public static final URI PRODUCT_FEE1_DISCOUNT_ELIGIBILITY_ADDITIONAL_URI =
      URI.create("http://www.acme.com.au/product/fee1/discount/eligibility/additional/info");
  public static final BankingProductDepositRateType PRODUCT_DEPOSIT_RATE_TYPE =
      BankingProductDepositRateType.FIXED;
  public static final BigDecimal PRODUCT_DEPOSIT_RATE_RATE = new BigDecimal("0.05");
  public static final Period PRODUCT_DEPOSIT_RATE_CALCULATION_FREQUENCY = Period.ofDays(1);
  public static final Duration PRODUCT_DEPOSIT_RATE_APPLICATION_FREQUENCY = Duration.ofDays(30);
  public static final String PRODUCT_DEPOSIT_RATE_ADDITIONAL_VALUE = "P6M";
  public static final String PRODUCT_DEPOSIT_RATE_ADDITIONAL_INFO =
      "Additional Information about Deposit Rate";
  public static final URI PRODUCT_DEPOSIT_RATE_ADDITIONAL_URI =
      URI.create("http://www.acme.com.au/product/rate/deposit/additional");
  public static final String PRODUCT_DEPOSIT_RATE_TIER1_NAME = "Tier 1 Name";
  public static final CommonUnitOfMeasureType PRODUCT_DEPOSIT_RATE_TIER1_UNITOFMEASURE =
      CommonUnitOfMeasureType.DOLLAR;
  public static final BigDecimal PRODUCT_DEPOSIT_RATE_TIER1_MINIMUM_VALUE = new BigDecimal("10.00");
  public static final BigDecimal PRODUCT_DEPOSIT_RATE_TIER1_MAXIMUM_VALUE =
      new BigDecimal("100.00");
  public static final BankingProductRateTierApplicationMethod PRODUCT_DEPOSIT_RATE_TIER1_RATE_APPLICATION_METHOD =
      BankingProductRateTierApplicationMethod.WHOLE_BALANCE;
  public static final String PRODUCT_DEPOSIT_RATE_TIER1_APPLICABILITY_INFO =
      "Applicability Information";
  public static final URI PRODUCT_DEPOSIT_RATE_TIER1_APPLICABILITY_URI =
      URI.create("http://www.acme.com.au/product/rate/deposit/applicability/info");
  public static final BankingProductLendingRateType PRODUCT_LENDING_RATE_TYPE =
      BankingProductLendingRateType.FIXED;
  public static final BigDecimal PRODUCT_LENDING_RATE_RATE = new BigDecimal("0.05");
  public static final Period PRODUCT_LENDING_RATE_CALCULATION_FREQUENCY = Period.ofDays(1);
  public static final Duration PRODUCT_LENDING_RATE_APPLICATION_FREQUENCY = Duration.ofDays(30);
  public static final String PRODUCT_LENDING_RATE_ADDITIONAL_VALUE = "P6M";
  public static final String PRODUCT_LENDING_RATE_ADDITIONAL_INFO =
      "Additional Information about Lending Rate";
  public static final URI PRODUCT_LENDING_RATE_ADDITIONAL_URI =
      URI.create("http://www.acme.com.au/product/rate/lending/additional");
  public static final String PRODUCT_LENDING_RATE_TIER1_NAME = "Tier 1 Name";
  public static final CommonUnitOfMeasureType PRODUCT_LENDING_RATE_TIER1_UNITOFMEASURE =
      CommonUnitOfMeasureType.DOLLAR;
  public static final BigDecimal PRODUCT_LENDING_RATE_TIER1_MINIMUM_VALUE = new BigDecimal("10.00");
  public static final BigDecimal PRODUCT_LENDING_RATE_TIER1_MAXIMUM_VALUE =
      new BigDecimal("100.00");
  public static final BankingProductRateTierApplicationMethod PRODUCT_LENDING_RATE_TIER1_RATE_APPLICATION_METHOD =
      BankingProductRateTierApplicationMethod.WHOLE_BALANCE;
  public static final String PRODUCT_LENDING_RATE_TIER1_APPLICABILITY_INFO =
      "Applicability Information";
  public static final URI PRODUCT_LENDING_RATE_TIER1_APPLICABILITY_URI =
      URI.create("http://www.acme.com.au/product/rate/lending/applicability/info");
  public static final BankingProductLendingRateInterestPaymentType PRODUCT_LENDING_INTEREST_PAYMENT_DUE =
      BankingProductLendingRateInterestPaymentType.IN_ARREARS;

  public static final String BANK_NAME = "Arlington Bank";
  public static final String BRANCH_ADDRESS = "29 Arlington Avenue";
  public static final String BRANCH_CITY = "Islington";
  public static final String BRANCH_NAME = "Teacup";
  public static final String BRANCH_POSTCODE = "1000";
  public static final String BRANCH_STATE = "NSW";
  public static final Integer BRANCH_BSB = 999999;
  public static final OffsetDateTime CREATION_DATETIME =
      OffsetDateTime.of(2020, 01, 01, 00, 00, 00, 00, ZoneOffset.UTC);
  public static final OffsetDateTime UPDATE_DATETIME =
      OffsetDateTime.of(2020, 01, 05, 00, 00, 00, 00, ZoneOffset.UTC);
  public static final String EMAIL_ADDRESS = "dev@null.com";
  public static final String PHONE_NUMBER = "+61733001234";
  public static final String PERSON_NAME = "Arthur Dent";
  public static final String PERSON_FIRST_NAME = "Arthur";
  public static final String PERSON_LAST_NAME = "Dent";
  public static final List<String> PERSON_MIDDLE_NAME = List.of("Phillip");
  public static final DioPersonPrefix PERSON_PREFIX = DioPersonPrefix.MR;
  public static final DioPersonSuffix PERSON_SUFFIX = DioPersonSuffix.OAM;
  public static final String OCCUPATION_CODE = "0411";

  public static final DioEmail DIO_EMAIL =
      DioEmail.builder().address(EMAIL_ADDRESS).isPreferred(true).type(DioEmailType.HOME).build();
  public static final DioPhoneNumber DIO_PHONE_NUMBER = DioPhoneNumber.builder()
      .fullNumber(PHONE_NUMBER).isPreferred(true).phoneType(DioPhoneType.HOME).build();
  public static final DioAddress DIO_ADDRESS = DioAddress.builder()
      .purpose(AddressPurpose.REGISTERED)
      .simple(DioAddressSimple.builder().addressLine1(BRANCH_ADDRESS).city(BRANCH_CITY)
          .country(new Locale(Constants.DEFAULT_LANGUAGE, Constants.DEFAULT_LOCALE))
          .mailingName(PERSON_NAME).postcode(BRANCH_POSTCODE).state(BRANCH_STATE).build())
      .build();

  public static final DioPerson DIO_PERSON = DioPerson.builder().email(VariableConstants.DIO_EMAIL)
      .phone(VariableConstants.DIO_PHONE_NUMBER).address(VariableConstants.DIO_ADDRESS)
      .firstName(VariableConstants.PERSON_FIRST_NAME).lastName(VariableConstants.PERSON_LAST_NAME)
      .prefix(PERSON_PREFIX).suffix(PERSON_SUFFIX).middleNames(PERSON_MIDDLE_NAME)
      .cdrCommon(CdrCommonPerson.builder().occupationCode(OCCUPATION_CODE).build()).build();

  public static final String ORGANISATION_ABN = "65085684368";
  public static final String ORGANISATION_ACN = "085684368";
  public static final String ORGANISATION_LEGAL_NAME = "DEEP THOUGHT INDUSTRIES PTY LIMITED";
  public static final String ORGANISATION_NAME = "DEEP THOUGHT INDUSTRIES";
  public static final LocalDate ORGANISATION_ESTABLISHMENT = LocalDate.of(2000, 07, 01);
  public static final String ORGANISATION_INDUSTRY_CODE = "1111";

  public static final DioOrganisation DIO_ORGANISATION = DioOrganisation.builder()
      .cdrCommon(CdrCommonOrganisation.builder().abn(VariableConstants.ORGANISATION_ABN)
          .acn(VariableConstants.ORGANISATION_ACN).organisationType(CommonOrganisationType.COMPANY)
          .industryCode(VariableConstants.ORGANISATION_INDUSTRY_CODE)
          .establishmentDate(VariableConstants.ORGANISATION_ESTABLISHMENT)
          .registeredCountry(
              new Locale(Constants.DEFAULT_LANGUAGE, Constants.DEFAULT_LOCALE))
          .build())
      .businessName(VariableConstants.ORGANISATION_NAME)
      .legalName(VariableConstants.ORGANISATION_LEGAL_NAME)
      .address(DioAddress.builder().purpose(VariableConstants.DIO_ADDRESS.purpose())
          .simple(DioAddressSimple.builder()
              .addressLine1(VariableConstants.DIO_ADDRESS.simple().addressLine1())
              .city(VariableConstants.DIO_ADDRESS.simple().city())
              .country(VariableConstants.DIO_ADDRESS.simple().country())
              .mailingName(VariableConstants.DIO_ADDRESS.simple().mailingName())
              .postcode(VariableConstants.DIO_ADDRESS.simple().postcode())
              .state(VariableConstants.DIO_ADDRESS.simple().state()).build())
          .build())
      .build();
  
  public static final String CARD_NUMBER = "4716-7028-1638-2681";
  public static final String DISPLAY_NAME = "Example Display Name";
  public static final String NICK_NAME = "Arthur's Account";
  public static final OffsetDateTime OPEN_DATE_TIME = OffsetDateTime.of(2020, 01, 05, 00, 00, 00, 00, ZoneOffset.UTC);
  public static final OffsetDateTime PAYMENT_DUE_DATE_TIME = OffsetDateTime.of(2020, 02, 05, 00, 00, 00, 00, ZoneOffset.UTC);
  public static final BigDecimal PAYMENT_DUE = new BigDecimal(100.00);
  public static final BigDecimal PAYMENT_MINIMUM = new BigDecimal(50.00);
  public static final BigDecimal TERM_DEPOSIT_AMOUNT = new BigDecimal(10000.00);
  public static final BigDecimal TERM_DEPOSIT_RATE = new BigDecimal(0.015);
  public static final Period TERM_DEPOSIT_LENGTH = Period.of(1, 0, 0);
  public static final OffsetDateTime TERM_DEPOSIT_LODGEMENT_DATE = OffsetDateTime.of(2020, 01, 05, 00, 00, 00, 00, ZoneOffset.UTC);
  public static final Period TERM_DEPOSIT_APPLICATION_FREQUENCY = Period.of(0, 1, 0);
  public static final Period TERM_DEPOSIT_CALCULATION_FREQUENCY = Period.of(0, 0, 1);
  public static final OffsetDateTime TERM_DEPOSIT_LAST_APPLIED = TERM_DEPOSIT_LODGEMENT_DATE.plusMonths(1);
  public static final OffsetDateTime TERM_DEPOSIT_LAST_CALCULATED = TERM_DEPOSIT_LODGEMENT_DATE.plusDays(1);
  public static final DioMaturityInstructionType TERM_DEPOSIT_MATURITY_INSTRUCTION = DioMaturityInstructionType.ROLLED_OVER;
  
  public static final OffsetDateTime LOAN_ACCOUNT_CREATION_DATE_TIME = OffsetDateTime.of(2020, 02, 06, 00, 00, 00, 00, ZoneOffset.UTC);
  public static final BigDecimal LOAN_ACCOUNT_CREATION_AMOUNT = new BigDecimal(150000.00);
  public static final Period LOAN_ACCOUNT_CREATION_LENGTH = Period.of(25, 0, 0);
  public static final Period LOAN_ACCOUNT_REPAYMENT_FREQUENCY = Period.of(0, 1, 0);
  public static final BigDecimal LOAN_ACCOUNT_NEXT_REPAYMENT = new BigDecimal(1000.00);
  public static final BigDecimal LOAN_ACCOUNT_REDRAW_AVAILABLE = new BigDecimal(25000.00);
  public static final DioLoanRepaymentType LOAN_ACCOUNT_REPAYMENT_TYPE = DioLoanRepaymentType.IN_ARREARS;
  public static final OffsetDateTime LOAN_ACCOUNT_NEXT_DATE_TIME = LOAN_ACCOUNT_CREATION_DATE_TIME.plusMonths(1);
  
  public static final BigDecimal DEBIT_LAST_AMOUNT = new BigDecimal(100.00);
  public static final OffsetDateTime DEBIT_DATE_TIME = OffsetDateTime.of(2020, 03, 06, 00, 00, 00, 00, ZoneOffset.UTC);
  public static final String PAYEE_DESCRIPTION = "Slartibartfast Planet Building Account";
  public static final String PAYEE_NICKNAME = "Slartibartfast";
  public static final String PAYEE_BSB = "248-067";
  public static final String PAYEE_ACCOUNT_NUMBER = "12341234";
  public static final String PAYEE_ACCOUNT_NAME = "Slartibartfast Ruler of the Universe";
  public static final String PAYEE_CARD_NUMBER = "4716-7028-1638-2681";
  public static final String PAYEE_PAYID_NAME = "Deep Thought";
  public static final String PAYEE_PAYID_IDENTIFIER = "42@universe.com";
  public static final PayloadTypeBankingDomesticPayeePayId PAYEE_PAYID_TYPE = PayloadTypeBankingDomesticPayeePayId.EMAIL;
  public static final String PAYEE_BILLER_CODE = "12345678";
  public static final String PAYEE_CRN = "1234123412341234";
  public static final String PAYEE_NAME = "Sirius Cybernetics Corporation";
  public static final String BENEFICIARY_DESCRIPTION = "Payment for a new Earth";
  public static final Locale PAYEE_COUNTRY = new Locale(Constants.DEFAULT_LANGUAGE, Constants.DEFAULT_LOCALE);
  
  public static final BigDecimal TRANSACTION_VALUE = new BigDecimal(10.00);
  public static final String TRANSACTION_DESCRIPTION = "Very Expensive Coffee";
  public static final BankingTransactionType TRANSACTION_TYPE = BankingTransactionType.PAYMENT;
  public static final String TRANSACTION_REFERENCE = "Jerry's Hot Jo";
  public static final String TRANSACTION_MERCHANT_NAME = "JERRY JO PTY LTD";
  public static final String TRANSACTION_MERCHANT_CODE = "5814";
  public static final String TRANSACTION_BILLER_CODE = PAYEE_BILLER_CODE;
  public static final String TRANSACTION_BILLER_NAME = PAYEE_NAME;
  public static final String TRANSACTION_CRN = PAYEE_CRN;
  public static final String TRANSACTION_BSB = PAYEE_BSB;
  public static final BankingTransactionStatus TRANSACTION_STATUS = BankingTransactionStatus.POSTED;
  public static final OffsetDateTime TRANSACTION_ORIGINATED_DATETIME = OffsetDateTime.of(2020, 03, 06, 07, 15, 00, 00, ZoneOffset.UTC);
  public static final OffsetDateTime TRANSACTION_POSTED_DATETIME =     OffsetDateTime.of(2020, 03, 06, 07, 33, 00, 00, ZoneOffset.UTC);  
  public static final OffsetDateTime TRANSACTION_APPLIED_DATETIME =    OffsetDateTime.of(2020, 03, 06, 11, 11, 00, 00, ZoneOffset.UTC);
  public static final UUID TRANSACTION_NPP_END_TO_END = UUID.randomUUID();
  public static final String TRANSACTION_NPP_PAYER = PAYEE_NICKNAME;
  public static final String TRANSACTION_NPP_PAYEE = NICK_NAME;
  public static final String TRANSACTION_NPP_PURPOSE = "PURPOSE";

}
