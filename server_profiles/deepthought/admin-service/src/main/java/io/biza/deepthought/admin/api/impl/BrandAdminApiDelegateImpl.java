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
import io.biza.deepthought.admin.api.delegate.BrandAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.admin.support.DeepThoughtValidator;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.payloads.dio.DioBrand;
import io.biza.deepthought.shared.persistence.model.BrandData;
import io.biza.deepthought.shared.persistence.repository.BrandRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BrandAdminApiDelegateImpl implements BrandAdminApiDelegate {


  @Autowired
  private DeepThoughtMapper mapper;

  @Autowired
  private BrandRepository brandRepository;
  
  @Autowired
  private Validator validator;

  @Override
  public ResponseEntity<List<DioBrand>> listBrands() {
    List<BrandData> brandData = brandRepository.findAll();
    LOG.debug("Listing brands and have database result of {}", brandData);
    return ResponseEntity.ok(mapper.mapAsList(brandData, DioBrand.class));
  }

  @Override
  public ResponseEntity<DioBrand> getBrand(UUID brandId) {
    Optional<BrandData> optionalData = brandRepository.findById(brandId);

    if (optionalData.isPresent()) {
      LOG.debug("Returning brand {} with content {}", brandId, optionalData.get());
      return ResponseEntity.ok(mapper.map(optionalData.get(), DioBrand.class));
    } else {
      LOG.warn("Unable to find brand {}", brandId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

  }

  @Override
  public ResponseEntity<DioBrand> createBrand(DioBrand createData) throws ValidationListException {
    
    DeepThoughtValidator.validate(validator, createData);
    
    BrandData data = brandRepository.save(mapper.map(createData, BrandData.class));
    LOG.info("Created brand with details of {}", data);
    return ResponseEntity.ok(mapper.map(data, DioBrand.class));
  }

  @Override
  public ResponseEntity<Void> deleteBrand(UUID brandId) {
    Optional<BrandData> optionalData = brandRepository.findById(brandId);

    if (optionalData.isPresent()) {
      LOG.info("Deleting brand with identifier of {}", brandId);
      brandRepository.delete(optionalData.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      LOG.warn("Attempted to delete but could not find brand with id of {}", brandId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioBrand> updateBrand(UUID brandId, DioBrand updateData) throws ValidationListException {
    
    DeepThoughtValidator.validate(validator, updateData);
    
    Optional<BrandData> optionalData = brandRepository.findById(brandId);

    if (optionalData.isPresent()) {
      BrandData data = optionalData.get();
      mapper.map(updateData, data);
      data.id(brandId);
      brandRepository.save(data);
      LOG.debug("Updated brand with id of {} to data of {}", brandId, data);
      return ResponseEntity.ok(mapper.map(data, DioBrand.class));
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
