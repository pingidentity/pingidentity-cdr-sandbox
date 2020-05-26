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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.deepthought.admin.Labels;
import io.biza.deepthought.admin.api.delegate.ProductAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.admin.support.DeepThoughtValidator;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioExceptionType;
import io.biza.deepthought.shared.payloads.dio.product.DioProduct;
import io.biza.deepthought.shared.payloads.dio.product.DioProductBundle;
import io.biza.deepthought.shared.persistence.model.BrandData;
import io.biza.deepthought.shared.persistence.model.product.ProductBundleData;
import io.biza.deepthought.shared.persistence.model.product.ProductData;
import io.biza.deepthought.shared.persistence.repository.BrandRepository;
import io.biza.deepthought.shared.persistence.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class ProductAdminApiDelegateImpl implements ProductAdminApiDelegate {

  @Autowired
  private DeepThoughtMapper mapper;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private BrandRepository brandRepository;
  
  @Autowired
  private Validator validator;

  @Override
  public ResponseEntity<List<DioProduct>> listProducts(UUID brandId) {
    List<ProductData> productData = productRepository.findAllByBrandId(brandId);
    LOG.debug("Listing all products for brand id of {} and received {}", brandId, productData);
    return ResponseEntity.ok(mapper.mapAsList(productData, DioProduct.class));
  }

  @Override
  public ResponseEntity<DioProduct> getProduct(UUID brandId, UUID productId) {
    Optional<ProductData> data = productRepository.findByIdAndBrandId(productId, brandId);

    if (data.isPresent()) {
      LOG.info("Retrieving a single product with brand of {} and id of {} and content of {}",
          brandId, productId, data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioProduct.class));
    } else {
      LOG.warn(
          "Attempted to retrieve a single product but could not find with brand of {} and id of {}",
          brandId, productId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioProduct> createProduct(UUID brandId, DioProduct createData)
      throws ValidationListException {
    
    DeepThoughtValidator.validate(validator, createData);

    Optional<BrandData> brand = brandRepository.findById(brandId);

    if (!brand.isPresent()) {
      LOG.warn("Attempted to create a product with non existent brand of {}", brandId);
      throw ValidationListException.builder().type(DioExceptionType.INVALID_BRAND).explanation(Labels.ERROR_INVALID_BRAND).build();
    }
        
    ProductData data = mapper.map(createData, ProductData.class);
    data.brand(brand.get());
    data.cdrBanking().product(data);
    LOG.debug("Created a new product for brand {} with content of {}", brandId, data);
    return getProduct(brandId, productRepository.save(data).id());
  }

  @Override
  public ResponseEntity<Void> deleteProduct(UUID brandId, UUID productId) {
    Optional<ProductData> optionalData = productRepository.findByIdAndBrandId(productId, brandId);

    if (optionalData.isPresent()) {
      LOG.debug("Deleting product with brand of {} and product id of {}", brandId, productId);
      productRepository.delete(optionalData.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      LOG.warn(
          "Attempted to delete a product but it couldn't be found with brand {} and product {}",
          brandId, productId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioProduct> updateProduct(UUID brandId, UUID productId,
      DioProduct updateData) throws ValidationListException {

    DeepThoughtValidator.validate(validator, updateData);
    
    Optional<ProductData> optionalData = productRepository.findByIdAndBrandId(productId, brandId);

    if (optionalData.isPresent()) {
      ProductData data = optionalData.get();
      mapper.map(updateData, data);
      productRepository.save(data);
      LOG.debug("Updated product with brand {} and product {} containing content of {}", brandId,
          productId, data);
      return getProduct(brandId, data.id());
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
  
  @Override
  public ResponseEntity<List<DioProductBundle>> listBundlesForProduct(UUID brandId,
      UUID productId) {
    Optional<ProductData> data = productRepository.findByIdAndBrandId(productId, brandId);

    if (data.isPresent()) {
      LOG.info("Retrieving a single products bundles with brand of {} and id of {}",
          brandId, productId, data.get());
      
      Set<ProductBundleData> bundles = new HashSet<ProductBundleData>();
      if(data.get().bundle() != null) {
        bundles.addAll(data.get().bundle());
      }
      
      return ResponseEntity.ok(mapper.mapAsList(bundles, DioProductBundle.class));
    } else {
      LOG.warn(
          "Attempted to retrieve a single product to return bundles but could not find with brand of {} and id of {}",
          brandId, productId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

}
