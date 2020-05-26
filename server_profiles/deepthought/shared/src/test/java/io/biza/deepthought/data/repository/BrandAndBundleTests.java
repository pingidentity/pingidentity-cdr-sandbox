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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.biza.deepthought.data.support.DeepThoughtJpaConfig;
import io.biza.deepthought.data.support.TranslatorInitialisation;
import io.biza.deepthought.data.support.VariableConstants;
import io.biza.deepthought.shared.payloads.dio.DioBrand;
import io.biza.deepthought.shared.payloads.dio.product.DioProductBundle;
import io.biza.deepthought.shared.persistence.model.BrandData;
import io.biza.deepthought.shared.persistence.model.product.ProductBundleData;
import io.biza.deepthought.shared.persistence.repository.BrandRepository;
import io.biza.deepthought.shared.persistence.repository.ProductBundleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("Brand and Bundle Data Tests")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DeepThoughtJpaConfig.class},
    loader = AnnotationConfigContextLoader.class)
@Transactional
@Slf4j
@Component
public class BrandAndBundleTests extends TranslatorInitialisation {

  @Resource
  private BrandRepository brandRepository;

  @Resource
  private ProductBundleRepository productBundleRepository;


  public BrandData createBrand() {
    BrandData brand = BrandData.builder().name(VariableConstants.BRAND_NAME)
        .displayName(VariableConstants.BRAND_DISPLAY_NAME).build();
    brandRepository.save(brand);

    Optional<BrandData> brandReturn = brandRepository.findById(brand.id());
    assertTrue(brandReturn.isPresent());

    return brandReturn.get();
  }

  @Test
  public void testBrandCreateAndCompare() {
    BrandData brand = createBrand();
    DioBrand dioBrandData = mapper.getMapperFacade().map(brand, DioBrand.class);
    DioBrand dioBrandStatic = DioBrand.builder().displayName(VariableConstants.BRAND_DISPLAY_NAME)
        .name(VariableConstants.BRAND_NAME).id(brand.id()).build();
    BrandData brandData = mapper.getMapperFacade().map(brand, BrandData.class);

    LOG.info("\n\n{}\n\n", createComparisonTable(dioBrandData, dioBrandStatic));
    LOG.info("\n\n{}\n\n", createComparisonTable(brand, brandData));

    if (!dioBrandData.equals(dioBrandStatic)) {
      fail("Payload conversion did not provide equality:\n"
          + createComparisonTable(dioBrandData, dioBrandStatic));
    }
  }

  @Test
  public void testBrandCreateAndDelete() {
    BrandData brand = createBrand();
    brandRepository.deleteById(brand.id());
    assertFalse(brandRepository.findById(brand.id()).isPresent());

    brand = createBrand();
    brandRepository.delete(brand);
    assertFalse(brandRepository.findById(brand.id()).isPresent());
  }

  private ProductBundleData createBundle() {
    BrandData brand = createBrand();

    ProductBundleData productBundle =
        ProductBundleData.builder().additionalInfo(VariableConstants.PRODUCT_BUNDLE_ADDITIONAL_INFO)
            .additionalInfoUri(VariableConstants.PRODUCT_BUNDLE_ADDITIONAL_INFO_URI)
            .description(VariableConstants.PRODUCT_BUNDLE_DESCRIPTION)
            .name(VariableConstants.PRODUCT_BUNDLE_NAME).build();
    productBundle.brand(brand);
    productBundleRepository.save(productBundle);

    return productBundle;
  }

  @Test
  public void testBrandAndBundleCreateAndCompare() {

    ProductBundleData productBundle = createBundle();

    Optional<ProductBundleData> bundleReturn = productBundleRepository.findById(productBundle.id());
    assertTrue(bundleReturn.isPresent());

    DioProductBundle dioProductBundleData =
        mapper.getMapperFacade().map(bundleReturn.get(), DioProductBundle.class);
    DioProductBundle dioProductBundleStatic =
        DioProductBundle.builder().additionalInfo(VariableConstants.PRODUCT_BUNDLE_ADDITIONAL_INFO)
            .additionalInfoUri(VariableConstants.PRODUCT_BUNDLE_ADDITIONAL_INFO_URI)
            .description(VariableConstants.PRODUCT_BUNDLE_DESCRIPTION)
            .name(VariableConstants.PRODUCT_BUNDLE_NAME).build().id(bundleReturn.get().id());

    LOG.info("\n\n{}\n\n", createComparisonTable(dioProductBundleData, dioProductBundleStatic));

    if (!dioProductBundleData.equals(dioProductBundleStatic)) {
      fail("Payload conversion did not provide equality:\n"
          + createComparisonTable(dioProductBundleData, dioProductBundleStatic));
    }

    Optional<BrandData> bundleBrandData = brandRepository.findById(productBundle.brand().id());
    assertTrue(bundleBrandData.isPresent());

    assertTrue(bundleBrandData.get().bundle().contains(bundleReturn.get()),
        "Brand Data doesn't contain new bundle");

  }

  @Test
  public void testBundleCreateAndDelete() {
    ProductBundleData productBundle = createBundle();
    productBundleRepository.deleteById(productBundle.id());
    assertFalse(productBundleRepository.findById(productBundle.id()).isPresent());

    productBundle = createBundle();
    UUID brandId = productBundle.brand().id();
    productBundleRepository.delete(productBundle);
    assertFalse(productBundleRepository.findById(productBundle.id()).isPresent());

    /**
     * Brand should not have been wiped out
     */
    assertTrue(brandRepository.findById(brandId).isPresent());
  }
}
