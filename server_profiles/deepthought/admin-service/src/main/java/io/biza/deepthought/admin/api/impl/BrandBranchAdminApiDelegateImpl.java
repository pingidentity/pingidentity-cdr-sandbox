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
import io.biza.deepthought.admin.api.delegate.BrandBranchAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.admin.support.DeepThoughtValidator;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankBranch;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioExceptionType;
import io.biza.deepthought.shared.payloads.requests.RequestBranchBrandConnection;
import io.biza.deepthought.shared.persistence.model.BrandData;
import io.biza.deepthought.shared.persistence.model.bank.BankBranchData;
import io.biza.deepthought.shared.persistence.repository.BankBranchRepository;
import io.biza.deepthought.shared.persistence.repository.BrandRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BrandBranchAdminApiDelegateImpl implements BrandBranchAdminApiDelegate {


  @Autowired
  private DeepThoughtMapper mapper;

  @Autowired
  private BankBranchRepository branchRepository;
  
  @Autowired
  private BrandRepository brandRepository;
  
  @Autowired
  private Validator validator;

  @Override
  public ResponseEntity<List<DioBankBranch>> listBrandBranches(UUID brandId) throws ValidationListException {
    if(!brandRepository.existsById(brandId)) {
      throw ValidationListException.builder().explanation(Labels.ERROR_INVALID_BRAND).type(DioExceptionType.INVALID_BRAND).build();
    }
    
    List<BankBranchData> brandData = branchRepository.findAllByBrandId(brandId);
    LOG.debug("Listing bank branches for {} and have database result of {}", brandId, brandData);
    return ResponseEntity.ok(mapper.mapAsList(brandData, DioBankBranch.class));
  }

  @Override
  public ResponseEntity<DioBankBranch> associateBrandBranch(UUID brandId, RequestBranchBrandConnection branchRequest) throws ValidationListException {
    
    DeepThoughtValidator.validate(validator, branchRequest);
    
    Optional<BrandData> brand = brandRepository.findById(brandId);
    
    if(!brand.isPresent()) {
      throw ValidationListException.builder().explanation(Labels.ERROR_INVALID_BRAND).type(DioExceptionType.INVALID_BRAND).build();
    }
    
    Optional<BankBranchData> optionalBranch = branchRepository.findById(branchRequest.branchId());
    
    if(!optionalBranch.isPresent()) {
      throw ValidationListException.builder().explanation(Labels.ERROR_INVALID_BRANCH).type(DioExceptionType.INVALID_BRANCH).build();
    }
    
    BankBranchData branch = optionalBranch.get();
    
    if(branch.brand() != null && !branch.brand().equals(brand.get())) {
      throw ValidationListException.builder().explanation(Labels.ERROR_INVALID_BRANCH_ALREADY_ASSIGNED).type(DioExceptionType.INVALID_BRANCH).build();
    }
    
    branch.brand(brand.get());
    branchRepository.save(branch);
    
    LOG.info("Created association between brand {} to branch of {}", brandId, branch.id());
    return ResponseEntity.ok(mapper.map(branch, DioBankBranch.class));
  }

  @Override
  public ResponseEntity<Void> deleteBrandBranch(UUID brandId, UUID branchId) {
    Optional<BankBranchData> optionalData = branchRepository.findByIdAndBrandId(branchId, brandId);

    if (optionalData.isPresent()) {
      LOG.info("Deleting brand to branch association with identifier of {}", brandId);
      BankBranchData branch = optionalData.get();
      branch.brand(null);
      branchRepository.save(branch);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      LOG.warn("Attempted to delete but could not find brand with id of {}", brandId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
