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
import io.biza.deepthought.admin.api.delegate.ProductConstraintAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.admin.support.DeepThoughtValidator;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioExceptionType;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
import io.biza.deepthought.shared.payloads.dio.product.DioProductConstraint;
import io.biza.deepthought.shared.persistence.model.product.ProductData;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankConstraintData;
import io.biza.deepthought.shared.persistence.repository.ProductBankConstraintRepository;
import io.biza.deepthought.shared.persistence.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;

@Controller
@Validated
@Slf4j
public class ProductConstraintAdminApiDelegateImpl implements ProductConstraintAdminApiDelegate {

  @Autowired
  private DeepThoughtMapper mapper;

  @Autowired
  ProductBankConstraintRepository constraintRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  private Validator validator;
  
  @Override
  public ResponseEntity<List<DioProductConstraint>> listProductConstraints(UUID brandId,
      UUID productId) {

    List<ProductBankConstraintData> constraintList = constraintRepository
        .findAllByProduct_Product_Brand_IdAndProduct_Product_Id(brandId, productId);
    LOG.debug("Listing constraints and have database result of {}", constraintList);
    return ResponseEntity.ok(mapper.mapAsList(constraintList, DioProductConstraint.class));
  }

  @Override
  public ResponseEntity<DioProductConstraint> getProductConstraint(UUID brandId, UUID productId,
      UUID id) {
    Optional<ProductBankConstraintData> data = constraintRepository
        .findByIdAndProduct_Product_Brand_IdAndProduct_Product_Id(id, brandId, productId);

    if (data.isPresent()) {
      LOG.debug("Get Product Constraint for brand {} and product {} returning: {}", brandId,
          productId, data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioProductConstraint.class));
    } else {
      LOG.warn("Get product constraint for brand {} and product {} not found", brandId, productId,
          data.get());
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioProductConstraint> createProductConstraint(UUID brandId, UUID productId,
      DioProductConstraint createData) throws ValidationListException {
    
    DeepThoughtValidator.validate(validator, createData);
    
    Optional<ProductData> product = productRepository.findByIdAndBrandId(productId, brandId);

    if (!product.isPresent()) {
      throw ValidationListException.builder().type(DioExceptionType.INVALID_BRAND_AND_PRODUCT).explanation(Labels.ERROR_INVALID_BRAND_AND_PRODUCT).build();
    }

    ProductBankConstraintData data =
        mapper.map(createData, ProductBankConstraintData.class);

    if (!product.get().schemeType().equals(createData.schemeType())) {
      throw ValidationListException.builder().type(DioExceptionType.UNSUPPORTED_PRODUCT_SCHEME_TYPE).explanation(Labels.ERROR_UNSUPPORTED_PRODUCT_SCHEME_TYPE).build();
    }

    if (product.get().schemeType().equals(DioSchemeType.CDR_BANKING)) {
      data.product(product.get().cdrBanking());
    }

    data = constraintRepository.save(data);
    LOG.debug("Create Product Constraint for brand {} and product {} returning: {}", brandId,
        productId, data);
    return getProductConstraint(brandId, productId, data.id());
  }

  @Override
  public ResponseEntity<Void> deleteProductConstraint(UUID brandId, UUID productId, UUID id) {
    Optional<ProductBankConstraintData> optionalData = constraintRepository
        .findByIdAndProduct_Product_Brand_IdAndProduct_Product_Id(id, brandId, productId);

    if (optionalData.isPresent()) {
      LOG.debug("Deleting product constraint with brand: {} productId: {} id: {}", brandId,
          productId, id);
      constraintRepository.delete(optionalData.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioProductConstraint> updateProductConstraint(UUID brandId, UUID productId,
      UUID id, DioProductConstraint updateData) throws ValidationListException {
    
    DeepThoughtValidator.validate(validator, updateData);
    
    Optional<ProductBankConstraintData> optionalData = constraintRepository
        .findByIdAndProduct_Product_Brand_IdAndProduct_Product_Id(id, brandId, productId);

    if (optionalData.isPresent()) {
      ProductBankConstraintData data = optionalData.get();
      mapper.map(updateData, data);
      constraintRepository.save(data);
      LOG.debug("Updated product constraint for brand: {} productId: {} id: {} with data of {}",
          brandId, productId, id, data);
      return getProductConstraint(brandId, productId, constraintRepository.save(data).id());
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

}
