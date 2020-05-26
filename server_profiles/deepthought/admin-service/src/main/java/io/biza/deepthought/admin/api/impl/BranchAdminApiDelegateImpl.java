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
import io.biza.deepthought.admin.api.delegate.BranchAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.admin.support.DeepThoughtValidator;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankBranch;
import io.biza.deepthought.shared.persistence.model.bank.BankBranchData;
import io.biza.deepthought.shared.persistence.repository.BankBranchRepository;
import io.biza.deepthought.shared.persistence.repository.BrandRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BranchAdminApiDelegateImpl implements BranchAdminApiDelegate {

  @Autowired
  private DeepThoughtMapper mapper;

  @Autowired
  private BankBranchRepository branchRepository;

  @Autowired
  private BrandRepository brandRepository;
  
  @Autowired
  private Validator validator;

  @Override
  public ResponseEntity<List<DioBankBranch>> listBranches() {
    List<BankBranchData> branchData = branchRepository.findAll();
    LOG.debug("Listing all branchs and received {}", branchData);
    return ResponseEntity.ok(mapper.mapAsList(branchData, DioBankBranch.class));
  }

  @Override
  public ResponseEntity<DioBankBranch> getBranch(UUID branchId) {
    Optional<BankBranchData> data = branchRepository.findById(branchId);

    if (data.isPresent()) {
      LOG.info("Retrieving a single branch with id of {} and content of {}",
          branchId, data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioBankBranch.class));
    } else {
      LOG.warn(
          "Attempted to retrieve a single branch but could not find with id of {}",
          branchId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioBankBranch> createBranch(DioBankBranch createData)
      throws ValidationListException {
    
    DeepThoughtValidator.validate(validator, createData);

    BankBranchData data = mapper.map(createData, BankBranchData.class);
    LOG.debug("Created a new branch with content of {}", data);
    return getBranch(branchRepository.save(data).id());
  }

  @Override
  public ResponseEntity<Void> deleteBranch(UUID branchId) {
    Optional<BankBranchData> optionalData = branchRepository.findById(branchId);

    if (optionalData.isPresent()) {
      LOG.debug("Deleting branch with branch id of {}", branchId);
      branchRepository.delete(optionalData.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      LOG.warn(
          "Attempted to delete a branch but it couldn't be found with branch {}",
          branchId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioBankBranch> updateBranch(UUID branchId,
      DioBankBranch updateData) throws ValidationListException {

    DeepThoughtValidator.validate(validator, updateData);
    
    Optional<BankBranchData> optionalData = branchRepository.findById(branchId);

    if (optionalData.isPresent()) {
      BankBranchData data = optionalData.get();
      mapper.map(updateData, data);
      branchRepository.save(data);
      LOG.debug("Updated branch with branch {} containing content of {}",
          branchId, data);
      return getBranch(data.id());
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

}
