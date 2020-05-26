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
import io.biza.deepthought.admin.api.delegate.ProductFeatureAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.admin.support.DeepThoughtValidator;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioExceptionType;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
import io.biza.deepthought.shared.payloads.dio.product.DioProductFeature;
import io.biza.deepthought.shared.persistence.model.product.ProductData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankFeatureData;
import io.biza.deepthought.shared.persistence.repository.ProductBankFeatureRepository;
import io.biza.deepthought.shared.persistence.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;

@Controller
@Validated
@Slf4j
public class ProductFeatureAdminApiDelegateImpl implements ProductFeatureAdminApiDelegate {

  @Autowired
  private DeepThoughtMapper mapper;

  @Autowired
  ProductBankFeatureRepository featureRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  private Validator validator;
  
  @Override
  public ResponseEntity<List<DioProductFeature>> listProductFeatures(UUID brandId, UUID productId) {

    List<ProductBankFeatureData> featureList = featureRepository
        .findAllByProduct_Product_Brand_IdAndProduct_Product_Id(brandId, productId);
    LOG.debug("Listing features and have database result of {}", featureList);
    return ResponseEntity.ok(mapper.mapAsList(featureList, DioProductFeature.class));
  }

  @Override
  public ResponseEntity<DioProductFeature> getProductFeature(UUID brandId, UUID productId,
      UUID id) {
    Optional<ProductBankFeatureData> data = featureRepository
        .findByIdAndProduct_Product_Brand_IdAndProduct_Product_Id(id, brandId, productId);

    if (data.isPresent()) {
      LOG.debug("Get Product Feature for brand {} and product {} returning: {}", brandId, productId,
          data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioProductFeature.class));
    } else {
      LOG.warn("Get product feature for brand {} and product {} not found", brandId, productId,
          data.get());
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioProductFeature> createProductFeature(UUID brandId, UUID productId,
      DioProductFeature createData) throws ValidationListException {
    
    DeepThoughtValidator.validate(validator, createData);

    Optional<ProductData> product = productRepository.findByIdAndBrandId(productId, brandId);

    if (!product.isPresent()) {
      throw ValidationListException.builder().type(DioExceptionType.INVALID_BRAND_AND_PRODUCT).explanation(Labels.ERROR_INVALID_BRAND_AND_PRODUCT).build();
    }

    ProductBankFeatureData data = mapper.map(createData, ProductBankFeatureData.class);

    LOG.debug("Attempting to save: {}", data);

    if (!product.get().schemeType().equals(createData.schemeType())) {
      throw ValidationListException.builder().type(DioExceptionType.UNSUPPORTED_PRODUCT_SCHEME_TYPE).explanation(Labels.ERROR_UNSUPPORTED_PRODUCT_SCHEME_TYPE).build();
    }

    if (product.get().schemeType().equals(DioSchemeType.CDR_BANKING)) {
      data.product(product.get().cdrBanking());
    }

    data = featureRepository.save(data);
    LOG.debug("Create Product Feature for brand {} and product {} returning: {}", brandId,
        productId, data);
    return getProductFeature(brandId, productId, data.id());
  }

  @Override
  public ResponseEntity<Void> deleteProductFeature(UUID brandId, UUID productId, UUID id) {
    Optional<ProductBankFeatureData> optionalData = featureRepository
        .findByIdAndProduct_Product_Brand_IdAndProduct_Product_Id(id, brandId, productId);

    if (optionalData.isPresent()) {
      LOG.debug("Deleting product feature with brand: {} productId: {} id: {}", brandId, productId,
          id);
      featureRepository.delete(optionalData.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioProductFeature> updateProductFeature(UUID brandId, UUID productId,
      UUID id, DioProductFeature updateData) throws ValidationListException {
    
    DeepThoughtValidator.validate(validator, updateData);
    
    Optional<ProductBankFeatureData> optionalData = featureRepository
        .findByIdAndProduct_Product_Brand_IdAndProduct_Product_Id(id, brandId, productId);

    if (optionalData.isPresent()) {
      ProductBankFeatureData data = optionalData.get();
      mapper.map(updateData, data);
      featureRepository.save(data);
      LOG.debug("Updated product feature for brand: {} productId: {} id: {} with data of {}",
          brandId, productId, id, data);
      return getProductFeature(brandId, productId, featureRepository.save(data).id());
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

}
