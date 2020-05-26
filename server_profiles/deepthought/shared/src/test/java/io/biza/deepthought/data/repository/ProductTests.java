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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.biza.babelfish.cdr.enumerations.BankingProductDiscountType;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductAdditionalInformationV1;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductCardArtV1;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductConstraintV1;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductDepositRateV1;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductDiscountEligibilityV1;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductDiscountV1;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductEligibilityV1;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductFeatureV1;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductFeeV1;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductLendingRateV1;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductRateTierV1;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductRateTierApplicabilityV1;
import io.biza.deepthought.data.support.DeepThoughtJpaConfig;
import io.biza.deepthought.data.support.TranslatorInitialisation;
import io.biza.deepthought.data.support.VariableConstants;
import io.biza.deepthought.shared.payloads.cdr.CdrBankingProduct;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
import io.biza.deepthought.shared.payloads.dio.product.DioProduct;
import io.biza.deepthought.shared.payloads.dio.product.DioProductCardArt;
import io.biza.deepthought.shared.payloads.dio.product.DioProductConstraint;
import io.biza.deepthought.shared.payloads.dio.product.DioProductEligibility;
import io.biza.deepthought.shared.payloads.dio.product.DioProductFeature;
import io.biza.deepthought.shared.payloads.dio.product.DioProductFee;
import io.biza.deepthought.shared.payloads.dio.product.DioProductRateDeposit;
import io.biza.deepthought.shared.payloads.dio.product.DioProductRateLending;
import io.biza.deepthought.shared.persistence.model.BrandData;
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
import io.biza.deepthought.shared.persistence.repository.BrandRepository;
import io.biza.deepthought.shared.persistence.repository.ProductBundleRepository;
import io.biza.deepthought.shared.persistence.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("Product Data Tests")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DeepThoughtJpaConfig.class},
    loader = AnnotationConfigContextLoader.class)
@Transactional
@Slf4j
public class ProductTests extends TranslatorInitialisation {

  @Resource
  private ProductRepository productRepository;

  @Resource
  private BrandRepository brandRepository;

  @Resource
  private ProductBundleRepository productBundleRepository;

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
            .cardArt(Set.of(ProductBankCardArtData.builder()
                .title(VariableConstants.PRODUCT_CARDART_TITLE)
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

  @Test
  public void testBaseProductCreateAndCompare() {

    ProductData product = createProductWithTheWorks();
    DioProduct dioProduct = mapper.getMapperFacade().map(product, DioProduct.class);

    DioProduct dioProductStatic = DioProduct.builder().id(product.id())
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

    LOG.info("\n\n{}\n\n", createComparisonTable(dioProduct, dioProductStatic));

    if (!dioProduct.equals(dioProductStatic)) {
      fail("Payload conversion did not provide equality:\n"
          + createComparisonTable(dioProduct, dioProductStatic));
    }
  }

  @Test
  public void testProductConstraintCreateAndCompare() {
    ProductData product = createProductWithTheWorks();

    for (ProductBankConstraintData constraint : product.cdrBanking().constraint()) {
      DioProductConstraint dioProductConstraint =
          mapper.getMapperFacade().map(constraint, DioProductConstraint.class);

      DioProductConstraint dioConstraintStatic =
          DioProductConstraint.builder().id(constraint.id()).schemeType(DioSchemeType.CDR_BANKING)
              .cdrBanking(new BankingProductConstraintV1()
                  .constraintType(VariableConstants.PRODUCT_CONSTRAINT_TYPE)
                  .additionalInfo(VariableConstants.PRODUCT_CONSTRAINT_ADDITIONAL_INFO)
                  .additionalInfoUri(VariableConstants.PRODUCT_CONSTRAINT_ADDITIONAL_INFO_URI)
                  .additionalValue(VariableConstants.PRODUCT_CONSTRAINT_ADDITIONAL_VALUE))
              .build();

      LOG.info("\n\n{}\n\n", createComparisonTable(dioProductConstraint, dioConstraintStatic));

      if (!dioProductConstraint.equals(dioConstraintStatic)) {
        fail("Payload conversion did not provide equality:\n"
            + createComparisonTable(dioProductConstraint, dioConstraintStatic));
      }
    }
  }

  @Test
  public void testProductFeatureCreateAndCompare() {
    ProductData product = createProductWithTheWorks();

    for (ProductBankFeatureData feature : product.cdrBanking().feature()) {
      DioProductFeature dioProductFeature =
          mapper.getMapperFacade().map(feature, DioProductFeature.class);

      DioProductFeature dioFeatureStatic =
          DioProductFeature.builder().id(feature.id()).schemeType(DioSchemeType.CDR_BANKING)
              .cdrBanking(
                  new BankingProductFeatureV1().featureType(VariableConstants.PRODUCT_FEATURE_TYPE)
                      .additionalInfo(VariableConstants.PRODUCT_FEATURE_ADDITIONAL_INFO)
                      .additionalInfoUri(VariableConstants.PRODUCT_FEATURE_ADDITIONAL_INFO_URI)
                      .additionalValue(VariableConstants.PRODUCT_FEATURE_ADDITIONAL_VALUE))
              .build();

      LOG.info("\n\n{}\n\n", createComparisonTable(dioProductFeature, dioFeatureStatic));

      if (!dioProductFeature.equals(dioFeatureStatic)) {
        fail("Payload conversion did not provide equality:\n"
            + createComparisonTable(dioProductFeature, dioFeatureStatic));
      }
    }
  }

  @Test
  public void testProductEligibilityCreateAndCompare() {
    ProductData product = createProductWithTheWorks();

    for (ProductBankEligibilityData eligibility : product.cdrBanking().eligibility()) {
      DioProductEligibility dioProductEligibility =
          mapper.getMapperFacade().map(eligibility, DioProductEligibility.class);

      DioProductEligibility dioEligibilityStatic =
          DioProductEligibility.builder().id(eligibility.id()).schemeType(DioSchemeType.CDR_BANKING)
              .cdrBanking(new BankingProductEligibilityV1()
                  .eligibilityType(VariableConstants.PRODUCT_ELIGIBILITY_TYPE)
                  .additionalInfo(VariableConstants.PRODUCT_ELIGIBILITY_ADDITIONAL_INFO)
                  .additionalInfoUri(VariableConstants.PRODUCT_ELIGIBILITY_ADDITIONAL_INFO_URI)
                  .additionalValue(VariableConstants.PRODUCT_ELIGIBILITY_ADDITIONAL_VALUE))
              .build();

      LOG.info("\n\n{}\n\n", createComparisonTable(dioProductEligibility, dioEligibilityStatic));

      if (!dioProductEligibility.equals(dioEligibilityStatic)) {
        fail("Payload conversion did not provide equality:\n"
            + createComparisonTable(dioProductEligibility, dioEligibilityStatic));
      }
    }
  }

  @Test
  public void testProductCardArtCreateAndCompare() {
    ProductData product = createProductWithTheWorks();

    for (ProductBankCardArtData cardArt : product.cdrBanking().cardArt()) {
      DioProductCardArt dioProductCardArt =
          mapper.getMapperFacade().map(cardArt, DioProductCardArt.class);

      DioProductCardArt dioCardArtStatic =
          DioProductCardArt.builder().id(cardArt.id()).schemeType(DioSchemeType.CDR_BANKING)
              .cdrBanking(new BankingProductCardArtV1().title(VariableConstants.PRODUCT_CARDART_TITLE)
                  .imageUri(VariableConstants.PRODUCT_CARDART_URI))
              .build();

      LOG.info("\n\n{}\n\n", createComparisonTable(dioProductCardArt, dioCardArtStatic));

      if (!dioProductCardArt.equals(dioCardArtStatic)) {
        fail("Payload conversion did not provide equality:\n"
            + createComparisonTable(dioProductCardArt, dioCardArtStatic));
      }
    }
  }

  @Test
  public void testProductFeeCreateAndCompare() {
    ProductData product = createProductWithTheWorks();

    for (ProductBankFeeData fee : product.cdrBanking().fee()) {
      DioProductFee dioProductFee = mapper.getMapperFacade().map(fee, DioProductFee.class);

      DioProductFee dioFeeStatic = DioProductFee.builder().id(fee.id())
          .schemeType(DioSchemeType.CDR_BANKING)
          .cdrBanking(new BankingProductFeeV1().name(VariableConstants.PRODUCT_FEE1_NAME)
              .feeType(VariableConstants.PRODUCT_FEE1_TYPE)
              .amount(VariableConstants.PRODUCT_FEE1_AMOUNT)
              .currency(VariableConstants.PRODUCT_FEE1_CURRENCY)
              .additionalValue(VariableConstants.PRODUCT_FEE1_ADDITIONAL_VALUE)
              .additionalInfo(VariableConstants.PRODUCT_FEE1_ADDITIONAL_INFO)
              .additionalInfoUri(VariableConstants.PRODUCT_FEE1_ADDITIONAL_INFO_URI)
              .discounts(List.of(new BankingProductDiscountV1()
                  .discountType(BankingProductDiscountType.ELIGIBILITY_ONLY)
                  .description(VariableConstants.PRODUCT_FEE1_DISCOUNT_DESCRIPTION)
                  .amount(VariableConstants.PRODUCT_FEE1_DISCOUNT_AMOUNT)
                  .additionalInfo(VariableConstants.PRODUCT_FEE1_DISCOUNT_ADDITIONAL_INFO)
                  .additionalValue(VariableConstants.PRODUCT_FEE1_DISCOUNT_ADDITIONAL_VALUE)
                  .additionalInfoUri(VariableConstants.PRODUCT_FEE1_DISCOUNT_ADDITIONAL_URI)
                  .eligibility(List.of(BankingProductDiscountEligibilityV1.builder()
                      .discountEligibilityType(
                          VariableConstants.PRODUCT_FEE1_DISCOUNT_ELIGIBILITY_TYPE)
                      .additionalValue(
                          VariableConstants.PRODUCT_FEE1_DISCOUNT_ELIGIBILITY_ADDITIONAL_VALUE)
                      .additionalInfo(
                          VariableConstants.PRODUCT_FEE1_DISCOUNT_ELIGIBILITY_ADDITIONAL_INFO)
                      .additionalInfoUri(
                          VariableConstants.PRODUCT_FEE1_DISCOUNT_ELIGIBILITY_ADDITIONAL_URI).build())))))
          .build();

      LOG.info("\n\n{}\n\n", createComparisonTable(dioProductFee, dioFeeStatic));

      if (!dioProductFee.equals(dioFeeStatic)) {
        fail("Payload conversion did not provide equality:\n"
            + createComparisonTable(dioProductFee, dioFeeStatic));
      }
    }
  }

  @Test
  public void testProductDepositRateCreateAndCompare() {
    ProductData product = createProductWithTheWorks();

    for (ProductBankRateDepositData depositRate : product.cdrBanking().depositRate()) {
      DioProductRateDeposit dioProductDepositRate =
          mapper.getMapperFacade().map(depositRate, DioProductRateDeposit.class);

      DioProductRateDeposit dioDepositRateStatic = DioProductRateDeposit.builder()
          .id(depositRate.id()).schemeType(DioSchemeType.CDR_BANKING)
          .cdrBanking(new BankingProductDepositRateV1()
              .depositRateType(VariableConstants.PRODUCT_DEPOSIT_RATE_TYPE)
              .rate(VariableConstants.PRODUCT_DEPOSIT_RATE_RATE)
              .calculationFrequency(VariableConstants.PRODUCT_DEPOSIT_RATE_CALCULATION_FREQUENCY)
              .applicationFrequency(VariableConstants.PRODUCT_DEPOSIT_RATE_CALCULATION_FREQUENCY)
              .additionalInfo(VariableConstants.PRODUCT_DEPOSIT_RATE_ADDITIONAL_INFO)
              .additionalValue(VariableConstants.PRODUCT_DEPOSIT_RATE_ADDITIONAL_VALUE)
              .additionalInfoUri(VariableConstants.PRODUCT_DEPOSIT_RATE_ADDITIONAL_URI)
              .tiers(List.of(new BankingProductRateTierV1()
                  .name(VariableConstants.PRODUCT_DEPOSIT_RATE_TIER1_NAME)
                  .unitOfMeasure(VariableConstants.PRODUCT_DEPOSIT_RATE_TIER1_UNITOFMEASURE)
                  .minimumValue(VariableConstants.PRODUCT_DEPOSIT_RATE_TIER1_MINIMUM_VALUE)
                  .maximumValue(VariableConstants.PRODUCT_DEPOSIT_RATE_TIER1_MAXIMUM_VALUE)
                  .rateApplicationMethod(
                      VariableConstants.PRODUCT_DEPOSIT_RATE_TIER1_RATE_APPLICATION_METHOD)
                  .applicabilityConditions(new BankingProductRateTierApplicabilityV1()
                      .additionalInfo(
                          VariableConstants.PRODUCT_DEPOSIT_RATE_TIER1_APPLICABILITY_INFO)
                      .additionalInfoUri(
                          VariableConstants.PRODUCT_DEPOSIT_RATE_TIER1_APPLICABILITY_URI)))))
          .build();

      LOG.info("\n\n{}\n\n", createComparisonTable(dioProductDepositRate, dioDepositRateStatic));

      if (!dioProductDepositRate.equals(dioDepositRateStatic)) {
        fail("Payload conversion did not provide equality:\n"
            + createComparisonTable(dioProductDepositRate, dioDepositRateStatic));
      }
    }
  }

  @Test
  public void testProductLendingRateCreateAndCompare() {
    ProductData product = createProductWithTheWorks();

    for (ProductBankRateLendingData lendingRate : product.cdrBanking().lendingRate()) {
      DioProductRateLending dioProductLendingRate =
          mapper.getMapperFacade().map(lendingRate, DioProductRateLending.class);

      DioProductRateLending dioLendingRateStatic = DioProductRateLending.builder()
          .id(lendingRate.id()).schemeType(DioSchemeType.CDR_BANKING)
          .cdrBanking(new BankingProductLendingRateV1()
              .lendingRateType(VariableConstants.PRODUCT_LENDING_RATE_TYPE)
              .interestPaymentDue(VariableConstants.PRODUCT_LENDING_INTEREST_PAYMENT_DUE)
              .rate(VariableConstants.PRODUCT_LENDING_RATE_RATE)
              .calculationFrequency(VariableConstants.PRODUCT_LENDING_RATE_CALCULATION_FREQUENCY)
              .applicationFrequency(VariableConstants.PRODUCT_LENDING_RATE_CALCULATION_FREQUENCY)
              .additionalInfo(VariableConstants.PRODUCT_LENDING_RATE_ADDITIONAL_INFO)
              .additionalValue(VariableConstants.PRODUCT_LENDING_RATE_ADDITIONAL_VALUE)
              .additionalInfoUri(VariableConstants.PRODUCT_LENDING_RATE_ADDITIONAL_URI)
              .tiers(List.of(new BankingProductRateTierV1()
                  .name(VariableConstants.PRODUCT_LENDING_RATE_TIER1_NAME)
                  .unitOfMeasure(VariableConstants.PRODUCT_LENDING_RATE_TIER1_UNITOFMEASURE)
                  .minimumValue(VariableConstants.PRODUCT_LENDING_RATE_TIER1_MINIMUM_VALUE)
                  .maximumValue(VariableConstants.PRODUCT_LENDING_RATE_TIER1_MAXIMUM_VALUE)
                  .rateApplicationMethod(
                      VariableConstants.PRODUCT_LENDING_RATE_TIER1_RATE_APPLICATION_METHOD)
                  .applicabilityConditions(new BankingProductRateTierApplicabilityV1()
                      .additionalInfo(
                          VariableConstants.PRODUCT_LENDING_RATE_TIER1_APPLICABILITY_INFO)
                      .additionalInfoUri(
                          VariableConstants.PRODUCT_LENDING_RATE_TIER1_APPLICABILITY_URI)))))
          .build();

      LOG.info("\n\n{}\n\n", createComparisonTable(dioProductLendingRate, dioLendingRateStatic));

      if (!dioProductLendingRate.equals(dioLendingRateStatic)) {
        fail("Payload conversion did not provide equality:\n"
            + createComparisonTable(dioProductLendingRate, dioLendingRateStatic));
      }
    }
  }

  @Test
  public void testProductLendingRateDataModelMismatch() {

    DioProductRateLending dioLendingRateStatic = DioProductRateLending.builder()
        .id(UUID.randomUUID()).schemeType(DioSchemeType.CDR_BANKING)
        .cdrBanking(new BankingProductLendingRateV1()
            .lendingRateType(VariableConstants.PRODUCT_LENDING_RATE_TYPE)
            .interestPaymentDue(VariableConstants.PRODUCT_LENDING_INTEREST_PAYMENT_DUE)
            .rate(VariableConstants.PRODUCT_LENDING_RATE_RATE)
            .calculationFrequency(VariableConstants.PRODUCT_LENDING_RATE_CALCULATION_FREQUENCY)
            .applicationFrequency(VariableConstants.PRODUCT_LENDING_RATE_CALCULATION_FREQUENCY)
            .additionalInfo(VariableConstants.PRODUCT_LENDING_RATE_ADDITIONAL_INFO)
            .additionalValue(VariableConstants.PRODUCT_LENDING_RATE_ADDITIONAL_VALUE)
            .additionalInfoUri(VariableConstants.PRODUCT_LENDING_RATE_ADDITIONAL_URI)
            .tiers(List.of(new BankingProductRateTierV1()
                .name(VariableConstants.PRODUCT_LENDING_RATE_TIER1_NAME)
                .unitOfMeasure(VariableConstants.PRODUCT_LENDING_RATE_TIER1_UNITOFMEASURE)
                .minimumValue(VariableConstants.PRODUCT_LENDING_RATE_TIER1_MINIMUM_VALUE)
                .maximumValue(VariableConstants.PRODUCT_LENDING_RATE_TIER1_MAXIMUM_VALUE)
                .rateApplicationMethod(
                    VariableConstants.PRODUCT_LENDING_RATE_TIER1_RATE_APPLICATION_METHOD)
                .applicabilityConditions(new BankingProductRateTierApplicabilityV1()
                    .additionalInfo(VariableConstants.PRODUCT_LENDING_RATE_TIER1_APPLICABILITY_INFO)
                    .additionalInfoUri(
                        VariableConstants.PRODUCT_LENDING_RATE_TIER1_APPLICABILITY_URI)))))
        .build();

    ProductBankRateLendingData lendingData =
        mapper.getMapperFacade().map(dioLendingRateStatic, ProductBankRateLendingData.class);
    
    LOG.info(lendingData.toString());
    
    try {
      lendingData.tiers().forEach(tier -> {
        // no-op, we are just seeing if it blows up
      });
    } catch (ClassCastException e) {
      fail("Tier Data class is mismatched", e.getCause());
    }
  }
}
