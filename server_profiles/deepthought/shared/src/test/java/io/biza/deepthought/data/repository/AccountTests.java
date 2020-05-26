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
package io.biza.deepthought.data.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.biza.babelfish.cdr.enumerations.BankingProductDiscountType;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductAdditionalInformationV1;
import io.biza.deepthought.data.support.DeepThoughtJpaConfig;
import io.biza.deepthought.data.support.TranslatorInitialisation;
import io.biza.deepthought.data.support.VariableConstants;
import io.biza.deepthought.shared.Constants;
import io.biza.deepthought.shared.payloads.cdr.CdrBankingAccount;
import io.biza.deepthought.shared.payloads.cdr.CdrBankingProduct;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankAccount;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankAccountCreditCard;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankAccountLoanAccount;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankAccountTermDeposit;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioAccountStatus;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioBankAccountType;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
import io.biza.deepthought.shared.payloads.dio.product.DioProduct;
import io.biza.deepthought.shared.payloads.dio.product.DioProductBundle;
import io.biza.deepthought.shared.persistence.model.BrandData;
import io.biza.deepthought.shared.persistence.model.bank.BankBranchData;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountCreditCardData;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountData;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountLoanAccountData;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountTermDepositData;
import io.biza.deepthought.shared.persistence.model.customer.CustomerData;
import io.biza.deepthought.shared.persistence.model.customer.bank.CustomerAccountData;
import io.biza.deepthought.shared.persistence.model.person.PersonAddressData;
import io.biza.deepthought.shared.persistence.model.person.PersonAddressSimpleData;
import io.biza.deepthought.shared.persistence.model.person.PersonData;
import io.biza.deepthought.shared.persistence.model.person.PersonEmailData;
import io.biza.deepthought.shared.persistence.model.person.PersonPhoneData;
import io.biza.deepthought.shared.persistence.model.product.ProductBundleData;
import io.biza.deepthought.shared.persistence.model.product.ProductData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankAdditionalInformationData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankCardArtData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankConstraintData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankEligibilityData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankFeatureData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankFeeData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankFeeDiscountData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankFeeDiscountEligibilityData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankRateDepositData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankRateDepositTierApplicabilityData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankRateDepositTierData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankRateLendingData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankRateLendingTierApplicabilityData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankRateLendingTierData;
import io.biza.deepthought.shared.persistence.repository.BankAccountCreditCardRepository;
import io.biza.deepthought.shared.persistence.repository.BankAccountLoanAccountRepository;
import io.biza.deepthought.shared.persistence.repository.BankAccountRepository;
import io.biza.deepthought.shared.persistence.repository.BankAccountTermDepositRepository;
import io.biza.deepthought.shared.persistence.repository.BankBranchRepository;
import io.biza.deepthought.shared.persistence.repository.BrandRepository;
import io.biza.deepthought.shared.persistence.repository.CustomerRepository;
import io.biza.deepthought.shared.persistence.repository.PersonRepository;
import io.biza.deepthought.shared.persistence.repository.ProductBundleRepository;
import io.biza.deepthought.shared.persistence.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("Account Data Tests")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DeepThoughtJpaConfig.class},
    loader = AnnotationConfigContextLoader.class)
@Transactional
@Slf4j
public class AccountTests extends TranslatorInitialisation {

  @Resource
  private CustomerRepository customerRepository;

  @Resource
  private PersonRepository personRepository;

  @Resource
  private BrandRepository brandRepository;

  @Resource
  private BankBranchRepository branchRepository;

  @Resource
  private BankAccountRepository accountRepository;

  @Resource
  private BankAccountCreditCardRepository accountCreditCardRepository;

  @Resource
  private BankAccountTermDepositRepository accountTermDepositRepository;

  @Resource
  private BankAccountLoanAccountRepository accountLoanAccountRepository;

  @Resource
  private ProductRepository productRepository;

  @Resource
  private ProductBundleRepository productBundleRepository;

  public BankAccountData createAccountWithCard() {

    CustomerData customer = createCustomerPerson();
    BankBranchData branch = createBranch();
    ProductData product = createProductWithTheWorks();

    BankAccountData account = BankAccountData.builder().nickName(VariableConstants.NICK_NAME)
        .status(DioAccountStatus.OPEN).displayName(VariableConstants.DISPLAY_NAME)
        .creationDateTime(VariableConstants.OPEN_DATE_TIME)
        .accountType(DioBankAccountType.TRANS_AND_SAVINGS_ACCOUNTS).build();
    account.branch(branch);
    account.product(product);
    account.bundle(product.bundle().iterator().next());
    CustomerAccountData customerAccount = CustomerAccountData.builder().owner(true).build();
    customerAccount.bankAccount(account);
    customerAccount.customer(customer);
    account.customerAccounts(Set.of(customerAccount));

    accountRepository.save(account);

    return account;
  }

  public BankAccountData createAccountWithCardCredit() {

    BankAccountData account = createAccountWithCard();
    account.accountType(DioBankAccountType.CRED_AND_CHRG_CARDS);

    BankAccountCreditCardData creditCard =
        BankAccountCreditCardData.builder().lastStatement(VariableConstants.OPEN_DATE_TIME)
            .paymentCurrency(Currency.getInstance(Constants.DEFAULT_CURRENCY))
            .paymentDue(VariableConstants.PAYMENT_DUE_DATE_TIME)
            .paymentDueAmount(VariableConstants.PAYMENT_DUE)
            .paymentMinimum(VariableConstants.PAYMENT_MINIMUM).build();
    creditCard.account(account);
    accountCreditCardRepository.save(creditCard);
    return accountRepository.findById(account.id()).get();
  }

  @Test
  public void testAccountCreditCardAndCompare() {

    BankAccountData account = createAccountWithCardCredit();
    DioBankAccount dioAccount = mapper.getMapperFacade().map(account, DioBankAccount.class);

    DioBankAccount dioAccountStatic = getAccountStaticBase(account);

    dioAccountStatic
        .cdrBanking(
            CdrBankingAccount.builder()
                .creditCards(List.of(DioBankAccountCreditCard.builder()
                    .id(account.creditCards().iterator().next().id())
                    .lastStatement(VariableConstants.OPEN_DATE_TIME)
                    .paymentCurrency(Currency.getInstance(Constants.DEFAULT_CURRENCY))
                    .paymentDue(VariableConstants.PAYMENT_DUE_DATE_TIME)
                    .paymentDueAmount(VariableConstants.PAYMENT_DUE)
                    .paymentMinimum(VariableConstants.PAYMENT_MINIMUM).build()))
                .build());

    LOG.warn("Source:      {}", dioAccount.toString());
    LOG.warn("Destination: {}", dioAccountStatic.toString());

    LOG.info("\n\n{}\n\n", createComparisonTable(dioAccount, dioAccountStatic));

    if (!dioAccount.equals(dioAccountStatic)) {
      LOG.error("Customer Data record to string reports: {}", account.toString());
      fail("Payload conversion did not provide equality:\n"
          + createComparisonTable(dioAccount, dioAccountStatic) + "\nDB        : "
          + account.toString() + "\nDB Convert: " + dioAccount.toString() + "\nStatic    : "
          + dioAccountStatic.toString());
    }
  }


  public BankAccountData createAccountWithTermDeposit() {

    BankAccountData account = createAccountWithCard();
    account.accountType(DioBankAccountType.TERM_DEPOSITS);

    BankAccountTermDepositData termDeposit = BankAccountTermDepositData.builder()
        .amount(VariableConstants.TERM_DEPOSIT_AMOUNT).rate(VariableConstants.TERM_DEPOSIT_RATE)
        .lodgement(VariableConstants.TERM_DEPOSIT_LODGEMENT_DATE)
        .termLength(VariableConstants.TERM_DEPOSIT_LENGTH)
        .calculationFrequency(VariableConstants.TERM_DEPOSIT_CALCULATION_FREQUENCY)
        .lastCalculated(VariableConstants.TERM_DEPOSIT_LAST_CALCULATED)
        .applicationFrequency(VariableConstants.TERM_DEPOSIT_APPLICATION_FREQUENCY)
        .maturityInstruction(VariableConstants.TERM_DEPOSIT_MATURITY_INSTRUCTION)
        .lastApplied(VariableConstants.TERM_DEPOSIT_LAST_APPLIED).build();
    termDeposit.account(account);
    accountTermDepositRepository.save(termDeposit);
    return accountRepository.findById(account.id()).get();
  }

  @Test
  public void testAccountTermDepositAndCompare() {

    BankAccountData account = createAccountWithTermDeposit();
    DioBankAccount dioAccount = mapper.getMapperFacade().map(account, DioBankAccount.class);

    DioBankAccount dioAccountStatic = getAccountStaticBase(account);

    dioAccountStatic.cdrBanking(CdrBankingAccount.builder()
        .termDeposits(List.of(DioBankAccountTermDeposit.builder()
            .id(account.termDeposits().iterator().next().id())
            .amount(VariableConstants.TERM_DEPOSIT_AMOUNT).rate(VariableConstants.TERM_DEPOSIT_RATE)
            .lodgement(VariableConstants.TERM_DEPOSIT_LODGEMENT_DATE)
            .termLength(VariableConstants.TERM_DEPOSIT_LENGTH)
            .calculationFrequency(VariableConstants.TERM_DEPOSIT_CALCULATION_FREQUENCY)
            .lastCalculated(VariableConstants.TERM_DEPOSIT_LAST_CALCULATED)
            .applicationFrequency(VariableConstants.TERM_DEPOSIT_APPLICATION_FREQUENCY)
            .maturityInstruction(VariableConstants.TERM_DEPOSIT_MATURITY_INSTRUCTION)
            .lastApplied(VariableConstants.TERM_DEPOSIT_LAST_APPLIED).build()))
        .build());

    LOG.warn("Source:      {}", dioAccount.toString());
    LOG.warn("Destination: {}", dioAccountStatic.toString());

    LOG.info("\n\n{}\n\n", createComparisonTable(dioAccount, dioAccountStatic));

    if (!dioAccount.equals(dioAccountStatic)) {
      LOG.error("Customer Data record to string reports: {}", account.toString());
      fail("Payload conversion did not provide equality:\n"
          + createComparisonTable(dioAccount, dioAccountStatic) + "\nDB        : "
          + account.toString() + "\nDB Convert: " + dioAccount.toString() + "\nStatic    : "
          + dioAccountStatic.toString());
    }
  }

  public BankAccountData createAccountWithLoanAccount() {

    BankAccountData account = createAccountWithCard();
    account.accountType(DioBankAccountType.RESIDENTIAL_MORTGAGES);

    BankAccountLoanAccountData loanAccount = BankAccountLoanAccountData.builder()
        .creationDateTime(VariableConstants.LOAN_ACCOUNT_CREATION_DATE_TIME)
        .creationAmount(VariableConstants.LOAN_ACCOUNT_CREATION_AMOUNT)
        .creationLength(VariableConstants.LOAN_ACCOUNT_CREATION_LENGTH)
        .repaymentFrequency(VariableConstants.LOAN_ACCOUNT_REPAYMENT_FREQUENCY)
        .lastRepayment(VariableConstants.LOAN_ACCOUNT_NEXT_DATE_TIME)
        .nextRepaymentAmount(VariableConstants.LOAN_ACCOUNT_NEXT_REPAYMENT)
        .redrawAvailable(VariableConstants.LOAN_ACCOUNT_REDRAW_AVAILABLE)
        .repaymentType(VariableConstants.LOAN_ACCOUNT_REPAYMENT_TYPE).build();
    loanAccount.account(account);
    accountLoanAccountRepository.save(loanAccount);
    return accountRepository.findById(account.id()).get();
  }

  @Test
  public void testAccountLoanAccountAndCompare() {

    BankAccountData account = createAccountWithLoanAccount();
    DioBankAccount dioAccount = mapper.getMapperFacade().map(account, DioBankAccount.class);

    DioBankAccount dioAccountStatic = getAccountStaticBase(account);

    dioAccountStatic.cdrBanking(CdrBankingAccount.builder()
        .loanAccounts(List.of(DioBankAccountLoanAccount.builder()
            .id(account.loanAccounts().iterator().next().id())
            .creationDateTime(VariableConstants.LOAN_ACCOUNT_CREATION_DATE_TIME)
            .creationAmount(VariableConstants.LOAN_ACCOUNT_CREATION_AMOUNT)
            .creationLength(VariableConstants.LOAN_ACCOUNT_CREATION_LENGTH)
            .repaymentFrequency(VariableConstants.LOAN_ACCOUNT_REPAYMENT_FREQUENCY)
            .lastRepayment(VariableConstants.LOAN_ACCOUNT_NEXT_DATE_TIME)
            .nextRepaymentAmount(VariableConstants.LOAN_ACCOUNT_NEXT_REPAYMENT)
            .redrawAvailable(VariableConstants.LOAN_ACCOUNT_REDRAW_AVAILABLE)
            .repaymentType(VariableConstants.LOAN_ACCOUNT_REPAYMENT_TYPE).build()))
        .build());

    LOG.warn("Source:      {}", dioAccount.toString());
    LOG.warn("Destination: {}", dioAccountStatic.toString());

    LOG.info("\n\n{}\n\n", createComparisonTable(dioAccount, dioAccountStatic));

    if (!dioAccount.equals(dioAccountStatic)) {
      LOG.error("Customer Data record to string reports: {}", account.toString());
      fail("Payload conversion did not provide equality:\n"
          + createComparisonTable(dioAccount, dioAccountStatic) + "\nDB        : "
          + account.toString() + "\nDB Convert: " + dioAccount.toString() + "\nStatic    : "
          + dioAccountStatic.toString());
    }
  }


  public DioBankAccount getAccountStaticBase(BankAccountData account) {
    DioProduct dioProductStatic = DioProduct.builder().id(account.product().id())
        .name(VariableConstants.PRODUCT_NAME).description(VariableConstants.PRODUCT_DESCRIPTION)
        .schemeType(DioSchemeType.CDR_BANKING)
        .cdrBanking(
            CdrBankingProduct.builder().effectiveFrom(VariableConstants.PRODUCT_EFFECTIVE_FROM)
                // Product Baseline
                .effectiveTo(VariableConstants.PRODUCT_EFFECTIVE_TO)
                .lastUpdated(VariableConstants.PRODUCT_LAST_UPDATED)
                .productCategory(VariableConstants.PRODUCT_CATEGORY)
                .applicationUri(VariableConstants.PRODUCT_APPLICATION_URI)
                .isTailored(VariableConstants.PRODUCT_ISTAILORED)
                .additionalInformation(new BankingProductAdditionalInformationV1()
                    .overviewUri(VariableConstants.PRODUCT_ADDITIONAL_INFO_OVERVIEW_URI)
                    .termsUri(VariableConstants.PRODUCT_ADDITIONAL_INFO_TERMS_URI)
                    .eligibilityUri(VariableConstants.PRODUCT_ADDITIONAL_INFO_ELIGIBILITY_URI)
                    .feesAndPricingUri(VariableConstants.PRODUCT_ADDITIONAL_INFO_FEES_URI)
                    .bundleUri(VariableConstants.PRODUCT_ADDITIONAL_INFO_BUNDLE_URI))
                .build())
        .build();

    DioProductBundle dioProductBundleStatic =
        mapper.getMapperFacade().map(account.bundle(), DioProductBundle.class);

    DioBankAccount dioAccountStatic =
        DioBankAccount.builder().id(account.id()).creationDateTime(account.creationDateTime())
            .accountNumber(account.accountNumber()).accountType(account.accountType())
            .bundle(dioProductBundleStatic).creationDateTime(VariableConstants.OPEN_DATE_TIME)
            .displayName(VariableConstants.DISPLAY_NAME).nickName(VariableConstants.NICK_NAME)
            .product(dioProductStatic).status(account.status()).build();

    return dioAccountStatic;
  }



  @Test
  public void testAccountAndCompare() {

    BankAccountData account = createAccountWithCard();
    DioBankAccount dioAccount = mapper.getMapperFacade().map(account, DioBankAccount.class);

    DioBankAccount dioAccountStatic = getAccountStaticBase(account);

    LOG.warn("Source: {}", dioAccount.toString());
    LOG.warn("Destination: {}", dioAccountStatic.toString());

    LOG.info("\n\n{}\n\n", createComparisonTable(dioAccount, dioAccountStatic));

    if (!dioAccount.equals(dioAccountStatic)) {
      LOG.error("Customer Data record to string reports: {}", account.toString());
      fail("Payload conversion did not provide equality:\n"
          + createComparisonTable(dioAccount, dioAccountStatic) + "\nDB        : "
          + account.toString() + "\nDB Convert: " + dioAccount.toString() + "\nStatic    : "
          + dioAccountStatic.toString());
    }
  }

  public CustomerData createCustomerPerson() {

    BrandData brand = BrandData.builder().name(VariableConstants.BRAND_NAME)
        .displayName(VariableConstants.BRAND_DISPLAY_NAME).build();
    brandRepository.save(brand);

    Optional<BrandData> brandReturn = brandRepository.findById(brand.id());
    assertTrue(brandReturn.isPresent());

    CustomerData customer = CustomerData.builder().creationTime(VariableConstants.CREATION_DATETIME)
        .lastUpdated(VariableConstants.UPDATE_DATETIME).brand(brandReturn.get()).build();

    PersonData person = PersonData.builder().firstName(VariableConstants.PERSON_FIRST_NAME)
        .lastName(VariableConstants.PERSON_LAST_NAME).prefix(VariableConstants.PERSON_PREFIX)
        .middleNames(VariableConstants.PERSON_MIDDLE_NAME).suffix(VariableConstants.PERSON_SUFFIX)
        .occupationCode(VariableConstants.OCCUPATION_CODE).build();
    PersonEmailData email = PersonEmailData.builder().address(VariableConstants.DIO_EMAIL.address())
        .isPreferred(VariableConstants.DIO_EMAIL.isPreferred())
        .type(VariableConstants.DIO_EMAIL.type()).build();
    email.person(person);
    PersonPhoneData phone =
        PersonPhoneData.builder().fullNumber(VariableConstants.DIO_PHONE_NUMBER.fullNumber())
            .isPreferred(VariableConstants.DIO_PHONE_NUMBER.isPreferred())
            .phoneType(VariableConstants.DIO_PHONE_NUMBER.phoneType()).build();
    phone.person(person);
    PersonAddressSimpleData simple = PersonAddressSimpleData.builder()
        .addressLine1(VariableConstants.DIO_ADDRESS.simple().addressLine1())
        .city(VariableConstants.DIO_ADDRESS.simple().city())
        .country(VariableConstants.DIO_ADDRESS.simple().country())
        .mailingName(VariableConstants.DIO_ADDRESS.simple().mailingName())
        .postcode(VariableConstants.DIO_ADDRESS.simple().postcode())
        .state(VariableConstants.DIO_ADDRESS.simple().state()).build();
    PersonAddressData address =
        PersonAddressData.builder().purpose(VariableConstants.DIO_ADDRESS.purpose()).build();
    simple.address(address);
    address.simple(simple);

    person.emailAddress(Set.of(email));
    person.phoneNumber(Set.of(phone));
    person.physicalAddress(Set.of(address));
    person.customer(customer);
    customer.person(person);

    customerRepository.save(customer);

    return customer;
  }

  public BankBranchData createBranch() {

    BrandData brand = BrandData.builder().name(VariableConstants.BRAND_NAME)
        .displayName(VariableConstants.BRAND_DISPLAY_NAME).build();
    brandRepository.save(brand);

    Optional<BrandData> brandReturn = brandRepository.findById(brand.id());
    assertTrue(brandReturn.isPresent());

    BankBranchData branch = BankBranchData.builder().bankName(VariableConstants.BANK_NAME)
        .branchAddress(VariableConstants.BRANCH_ADDRESS).branchCity(VariableConstants.BRANCH_CITY)
        .branchName(VariableConstants.BRANCH_NAME).branchPostcode(VariableConstants.BRANCH_POSTCODE)
        .branchState(VariableConstants.BRANCH_STATE).bsb(VariableConstants.BRANCH_BSB).brand(brand)
        .build();
    branchRepository.save(branch);

    return branch;
  }

  public BrandData createBrandWithBundle() {
    BrandData brand = BrandData.builder().name(VariableConstants.BRAND_NAME)
        .displayName(VariableConstants.BRAND_DISPLAY_NAME).build();
    brandRepository.save(brand);

    Optional<BrandData> brandReturn = brandRepository.findById(brand.id());
    assertTrue(brandReturn.isPresent());

    ProductBundleData productBundle =
        ProductBundleData.builder().additionalInfo(VariableConstants.PRODUCT_BUNDLE_ADDITIONAL_INFO)
            .additionalInfoUri(VariableConstants.PRODUCT_BUNDLE_ADDITIONAL_INFO_URI)
            .description(VariableConstants.PRODUCT_BUNDLE_DESCRIPTION)
            .name(VariableConstants.PRODUCT_BUNDLE_NAME).build();
    productBundle.brand(brand);
    productBundleRepository.save(productBundle);

    return brandReturn.get();
  }


  public ProductData createProductWithTheWorks() {
    BrandData brand = createBrandWithBundle();
    ProductData product = ProductData.builder().name(VariableConstants.PRODUCT_NAME)
        .description(VariableConstants.PRODUCT_DESCRIPTION).schemeType(DioSchemeType.CDR_BANKING)
        .build();
    ProductBankData cdrBanking =
        ProductBankData.builder().effectiveFrom(VariableConstants.PRODUCT_EFFECTIVE_FROM)
            // Product Baseline
            .effectiveTo(VariableConstants.PRODUCT_EFFECTIVE_TO)
            .lastUpdated(VariableConstants.PRODUCT_LAST_UPDATED)
            .productCategory(VariableConstants.PRODUCT_CATEGORY)
            .applicationUri(VariableConstants.PRODUCT_APPLICATION_URI)
            .isTailored(VariableConstants.PRODUCT_ISTAILORED)
            .additionalInformation(ProductBankAdditionalInformationData.builder()
                .overviewUri(VariableConstants.PRODUCT_ADDITIONAL_INFO_OVERVIEW_URI)
                .termsUri(VariableConstants.PRODUCT_ADDITIONAL_INFO_TERMS_URI)
                .eligibilityUri(VariableConstants.PRODUCT_ADDITIONAL_INFO_ELIGIBILITY_URI)
                .feesPricingUri(VariableConstants.PRODUCT_ADDITIONAL_INFO_FEES_URI)
                .bundleUri(VariableConstants.PRODUCT_ADDITIONAL_INFO_BUNDLE_URI).build())
            .cardArt(Set.of(
                ProductBankCardArtData.builder().title(VariableConstants.PRODUCT_CARDART_TITLE)
                    .imageUri(VariableConstants.PRODUCT_CARDART_URI).build()))
            // Product Detail with one of basically everything
            .feature(Set.of(ProductBankFeatureData.builder()
                .featureType(VariableConstants.PRODUCT_FEATURE_TYPE)
                .additionalInfo(VariableConstants.PRODUCT_FEATURE_ADDITIONAL_INFO)
                .additionalInfoUri(VariableConstants.PRODUCT_FEATURE_ADDITIONAL_INFO_URI)
                .additionalValue(VariableConstants.PRODUCT_FEATURE_ADDITIONAL_VALUE).build()))
            .constraint(Set.of(ProductBankConstraintData.builder()
                .constraintType(VariableConstants.PRODUCT_CONSTRAINT_TYPE)
                .additionalInfo(VariableConstants.PRODUCT_CONSTRAINT_ADDITIONAL_INFO)
                .additionalInfoUri(VariableConstants.PRODUCT_CONSTRAINT_ADDITIONAL_INFO_URI)
                .additionalValue(VariableConstants.PRODUCT_CONSTRAINT_ADDITIONAL_VALUE).build()))
            .eligibility(Set.of(ProductBankEligibilityData.builder()
                .eligibilityType(VariableConstants.PRODUCT_ELIGIBILITY_TYPE)
                .additionalInfo(VariableConstants.PRODUCT_ELIGIBILITY_ADDITIONAL_INFO)
                .additionalInfoUri(VariableConstants.PRODUCT_ELIGIBILITY_ADDITIONAL_INFO_URI)
                .additionalValue(VariableConstants.PRODUCT_ELIGIBILITY_ADDITIONAL_VALUE).build()))
            .fee(Set.of(ProductBankFeeData.builder().name(VariableConstants.PRODUCT_FEE1_NAME)
                .feeType(VariableConstants.PRODUCT_FEE1_TYPE)
                .amount(VariableConstants.PRODUCT_FEE1_AMOUNT)
                .currency(VariableConstants.PRODUCT_FEE1_CURRENCY)
                .additionalValue(VariableConstants.PRODUCT_FEE1_ADDITIONAL_VALUE)
                .additionalInfo(VariableConstants.PRODUCT_FEE1_ADDITIONAL_INFO)
                .additionalInfoUri(VariableConstants.PRODUCT_FEE1_ADDITIONAL_INFO_URI)
                .discounts(Set.of(ProductBankFeeDiscountData.builder()
                    .discountType(BankingProductDiscountType.ELIGIBILITY_ONLY)
                    .description(VariableConstants.PRODUCT_FEE1_DISCOUNT_DESCRIPTION)
                    .amount(VariableConstants.PRODUCT_FEE1_DISCOUNT_AMOUNT)
                    .additionalInfo(VariableConstants.PRODUCT_FEE1_DISCOUNT_ADDITIONAL_INFO)
                    .additionalValue(VariableConstants.PRODUCT_FEE1_DISCOUNT_ADDITIONAL_VALUE)
                    .additionalInfoUri(VariableConstants.PRODUCT_FEE1_DISCOUNT_ADDITIONAL_URI)
                    .eligibility(Set.of(ProductBankFeeDiscountEligibilityData.builder()
                        .discountEligibilityType(
                            VariableConstants.PRODUCT_FEE1_DISCOUNT_ELIGIBILITY_TYPE)
                        .additionalValue(
                            VariableConstants.PRODUCT_FEE1_DISCOUNT_ELIGIBILITY_ADDITIONAL_VALUE)
                        .additionalInfo(
                            VariableConstants.PRODUCT_FEE1_DISCOUNT_ELIGIBILITY_ADDITIONAL_INFO)
                        .additionalInfoUri(
                            VariableConstants.PRODUCT_FEE1_DISCOUNT_ELIGIBILITY_ADDITIONAL_URI)
                        .build()))
                    .build()))
                .build()))
            .depositRate(Set.of(ProductBankRateDepositData.builder()
                .depositRateType(VariableConstants.PRODUCT_DEPOSIT_RATE_TYPE)
                .rate(VariableConstants.PRODUCT_DEPOSIT_RATE_RATE)
                .calculationFrequency(VariableConstants.PRODUCT_DEPOSIT_RATE_CALCULATION_FREQUENCY)
                .applicationFrequency(VariableConstants.PRODUCT_DEPOSIT_RATE_CALCULATION_FREQUENCY)
                .additionalInfo(VariableConstants.PRODUCT_DEPOSIT_RATE_ADDITIONAL_INFO)
                .additionalValue(VariableConstants.PRODUCT_DEPOSIT_RATE_ADDITIONAL_VALUE)
                .additionalInfoUri(VariableConstants.PRODUCT_DEPOSIT_RATE_ADDITIONAL_URI)
                .tiers(Set.of(ProductBankRateDepositTierData.builder()
                    .name(VariableConstants.PRODUCT_DEPOSIT_RATE_TIER1_NAME)
                    .unitOfMeasure(VariableConstants.PRODUCT_DEPOSIT_RATE_TIER1_UNITOFMEASURE)
                    .minimumValue(VariableConstants.PRODUCT_DEPOSIT_RATE_TIER1_MINIMUM_VALUE)
                    .maximumValue(VariableConstants.PRODUCT_DEPOSIT_RATE_TIER1_MAXIMUM_VALUE)
                    .rateApplicationMethod(
                        VariableConstants.PRODUCT_DEPOSIT_RATE_TIER1_RATE_APPLICATION_METHOD)
                    .applicabilityConditions(ProductBankRateDepositTierApplicabilityData
                        .builder()
                        .additionalInfo(
                            VariableConstants.PRODUCT_DEPOSIT_RATE_TIER1_APPLICABILITY_INFO)
                        .additionalInfoUri(
                            VariableConstants.PRODUCT_DEPOSIT_RATE_TIER1_APPLICABILITY_URI)
                        .build())
                    .build()))
                .build()))
            .lendingRate(Set.of(ProductBankRateLendingData.builder()
                .lendingRateType(VariableConstants.PRODUCT_LENDING_RATE_TYPE)
                .rate(VariableConstants.PRODUCT_LENDING_RATE_RATE)
                .calculationFrequency(VariableConstants.PRODUCT_LENDING_RATE_CALCULATION_FREQUENCY)
                .applicationFrequency(VariableConstants.PRODUCT_LENDING_RATE_CALCULATION_FREQUENCY)
                .interestPaymentDue(VariableConstants.PRODUCT_LENDING_INTEREST_PAYMENT_DUE)
                .additionalInfo(VariableConstants.PRODUCT_LENDING_RATE_ADDITIONAL_INFO)
                .additionalValue(VariableConstants.PRODUCT_LENDING_RATE_ADDITIONAL_VALUE)
                .additionalInfoUri(VariableConstants.PRODUCT_LENDING_RATE_ADDITIONAL_URI)
                .tiers(Set.of(ProductBankRateLendingTierData.builder()
                    .name(VariableConstants.PRODUCT_LENDING_RATE_TIER1_NAME)
                    .unitOfMeasure(VariableConstants.PRODUCT_LENDING_RATE_TIER1_UNITOFMEASURE)
                    .minimumValue(VariableConstants.PRODUCT_LENDING_RATE_TIER1_MINIMUM_VALUE)
                    .maximumValue(VariableConstants.PRODUCT_LENDING_RATE_TIER1_MAXIMUM_VALUE)
                    .rateApplicationMethod(
                        VariableConstants.PRODUCT_LENDING_RATE_TIER1_RATE_APPLICATION_METHOD)
                    .applicabilityConditions(ProductBankRateLendingTierApplicabilityData
                        .builder()
                        .additionalInfo(
                            VariableConstants.PRODUCT_LENDING_RATE_TIER1_APPLICABILITY_INFO)
                        .additionalInfoUri(
                            VariableConstants.PRODUCT_LENDING_RATE_TIER1_APPLICABILITY_URI)
                        .build())
                    .build()))
                .build()))
            .build();

    product.brand(brand);
    product.cdrBanking(cdrBanking);
    product.bundle(brand.bundle());
    productRepository.save(product);

    // LOG.info("Raw Product is set to: {}", product.toString());

    return product;
  }
}
