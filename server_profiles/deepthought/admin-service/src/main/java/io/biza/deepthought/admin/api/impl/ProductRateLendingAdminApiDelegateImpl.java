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
package io.biza.deepthought.admin.api.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.deepthought.admin.Labels;
import io.biza.deepthought.admin.api.delegate.ProductRateLendingAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.admin.support.DeepThoughtValidator;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioExceptionType;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
import io.biza.deepthought.shared.payloads.dio.product.DioProductRateLending;
import io.biza.deepthought.shared.persistence.model.product.ProductData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankRateLendingData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankRateLendingTierData;
import io.biza.deepthought.shared.persistence.repository.ProductBankRateLendingRepository;
import io.biza.deepthought.shared.persistence.repository.ProductBankRateLendingTierRepository;
import io.biza.deepthought.shared.persistence.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;

@Controller
@Validated
@Slf4j
public class ProductRateLendingAdminApiDelegateImpl implements ProductRateLendingAdminApiDelegate {

  @Autowired
  private DeepThoughtMapper mapper;

  @Autowired
  ProductBankRateLendingRepository rateRepository;

  @Autowired
  ProductBankRateLendingTierRepository tierRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  private Validator validator;

  @Override
  public ResponseEntity<List<DioProductRateLending>> listProductRateLendings(UUID brandId, UUID productId) {

    List<ProductBankRateLendingData> feeList =
        rateRepository.findAllByProduct_Product_Brand_IdAndProduct_Product_Id(brandId, productId);
    LOG.debug("Listing fees and have database result of {}", feeList);
    return ResponseEntity.ok(mapper.mapAsList(feeList, DioProductRateLending.class));
  }

  @Override
  public ResponseEntity<DioProductRateLending> getProductRateLending(UUID brandId, UUID productId, UUID id) {
    Optional<ProductBankRateLendingData> data = rateRepository
        .findByIdAndProduct_Product_Brand_IdAndProduct_Product_Id(id, brandId, productId);

    if (data.isPresent()) {
      LOG.debug("Get Product Lending Rate for brand {} and product {} returning: {}", brandId, productId,
          data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioProductRateLending.class));
    } else {
      LOG.warn("Get product lending rate for brand {} and product {} not found", brandId, productId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioProductRateLending> createProductRateLending(UUID brandId, UUID productId,
      DioProductRateLending createData) throws ValidationListException {

    DeepThoughtValidator.validate(validator, createData);

    Optional<ProductData> product = productRepository.findByIdAndBrandId(productId, brandId);

    if (!product.isPresent()) {
      throw ValidationListException.builder().type(DioExceptionType.INVALID_BRAND_AND_PRODUCT)
          .explanation(Labels.ERROR_INVALID_BRAND_AND_PRODUCT).build();
    }

    ProductBankRateLendingData data = mapper.map(createData, ProductBankRateLendingData.class);
    
    LOG.debug("Preparing to create data: {}", data);

    if (data.tiers() != null) {
      for(ProductBankRateLendingTierData tier : data.tiers()) {
        if (tier.applicabilityConditions() != null) {
          tier.applicabilityConditions().rateTier(tier);
        }

        tier.lendingRate(data);
      }
    }

    LOG.debug("Attempting to save: {}", data);

    if (!product.get().schemeType().equals(createData.schemeType())) {
      throw ValidationListException.builder().type(DioExceptionType.UNSUPPORTED_PRODUCT_SCHEME_TYPE)
          .explanation(Labels.ERROR_UNSUPPORTED_PRODUCT_SCHEME_TYPE).build();

    }

    if (product.get().schemeType().equals(DioSchemeType.CDR_BANKING)) {
      data.product(product.get().cdrBanking());
    }

    data = rateRepository.save(data);
    LOG.debug("Create Product Lending Rate for brand {} and product {} returning: {}", brandId, productId,
        data);
    return getProductRateLending(brandId, productId, data.id());
  }

  @Override
  public ResponseEntity<Void> deleteProductRateLending(UUID brandId, UUID productId, UUID id) {
    Optional<ProductBankRateLendingData> optionalData = rateRepository
        .findByIdAndProduct_Product_Brand_IdAndProduct_Product_Id(id, brandId, productId);

    if (optionalData.isPresent()) {
      LOG.debug("Deleting product lending rate with brand: {} productId: {} id: {}", brandId, productId, id);
      rateRepository.delete(optionalData.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioProductRateLending> updateProductRateLending(UUID brandId, UUID productId, UUID id,
      DioProductRateLending updateData) throws ValidationListException {

    DeepThoughtValidator.validate(validator, updateData);

    Optional<ProductBankRateLendingData> optionalData = rateRepository
        .findByIdAndProduct_Product_Brand_IdAndProduct_Product_Id(id, brandId, productId);

    if (optionalData.isPresent()) {
      ProductBankRateLendingData data = optionalData.get();
      
      if (data.tiers() != null) {
        for(ProductBankRateLendingTierData tier : data.tiers()) {
          data.tiers().remove(tier);
          tierRepository.deleteById(tier.id());
        }
      }

      data = rateRepository.save(data);

      LOG.debug("LendingRate data is now: {}", data);
      
      mapper.map(updateData, data);
      
      if (data.tiers() != null) {
        for(ProductBankRateLendingTierData tier : data.tiers()) {
          if (tier.applicabilityConditions() != null) {
            tier.applicabilityConditions().rateTier(tier);
          }
          tier.lendingRate(data);
        }
      }
      
      data = rateRepository.save(data);

      LOG.debug("Updated product lending rate for brand: {} productId: {} id: {} with data of {}", brandId,
          productId, id, data);
      return getProductRateLending(brandId, productId, data.id());
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

}
