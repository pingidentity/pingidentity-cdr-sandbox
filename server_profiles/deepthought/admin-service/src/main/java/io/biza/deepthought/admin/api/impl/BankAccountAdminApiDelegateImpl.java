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
import io.biza.deepthought.admin.api.delegate.BankAccountAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.admin.support.DeepThoughtValidator;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankAccount;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioExceptionType;
import io.biza.deepthought.shared.payloads.requests.RequestBankAccount;
import io.biza.deepthought.shared.persistence.model.bank.BankBranchData;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountData;
import io.biza.deepthought.shared.persistence.model.product.ProductBundleData;
import io.biza.deepthought.shared.persistence.model.product.ProductData;
import io.biza.deepthought.shared.persistence.repository.BankAccountRepository;
import io.biza.deepthought.shared.persistence.repository.BankBranchRepository;
import io.biza.deepthought.shared.persistence.repository.ProductBundleRepository;
import io.biza.deepthought.shared.persistence.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankAccountAdminApiDelegateImpl implements BankAccountAdminApiDelegate {

  @Autowired
  private DeepThoughtMapper mapper;

  @Autowired
  private BankAccountRepository bankAccountRepository;

  @Autowired
  private BankBranchRepository branchRepository;
  
  @Autowired
  private ProductRepository productRepository;
  
  @Autowired
  private ProductBundleRepository bundleRepository;
  
  @Autowired
  private Validator validator;

  @Override
  public ResponseEntity<List<DioBankAccount>> listBankAccounts(UUID brandId, UUID branchId) {
    List<BankAccountData> bankAccountData = bankAccountRepository.findAllByBranchIdAndBranchBrandId(branchId, brandId);
    LOG.debug("Listing all bank accounts for brand id of {} branch id of {} and received {}", brandId, branchId, bankAccountData);
    return ResponseEntity.ok(mapper.mapAsList(bankAccountData, DioBankAccount.class));
  }

  @Override
  public ResponseEntity<DioBankAccount> getBankAccount(UUID brandId, UUID branchId, UUID bankAccountId) {
    Optional<BankAccountData> data = bankAccountRepository.findByIdAndBranchIdAndBranchBrandId(bankAccountId, branchId, brandId);

    if (data.isPresent()) {
      LOG.info("Retrieving a single bank account with branch of {} brand id {} and id of {} and content of {}",
          branchId, brandId, bankAccountId, data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioBankAccount.class));
    } else {
      LOG.warn(
          "Attempted to retrieve a single bank account but could not find with branch of {} brand id of {} and id of {}",
          branchId, branchId, bankAccountId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioBankAccount> createBankAccount(UUID brandId, UUID branchId, RequestBankAccount createRequest)
      throws ValidationListException {
    
    DeepThoughtValidator.validate(validator, createRequest);

    Optional<BankBranchData> branch = branchRepository.findByIdAndBrandId(branchId, brandId);

    if (!branch.isPresent()) {
      LOG.warn("Attempted to create a bank account with non existent brand of {} branch of {}", brandId, branchId);
      throw ValidationListException.builder().type(DioExceptionType.INVALID_BRAND).explanation(Labels.ERROR_INVALID_BRAND).build();
    }
    
    Optional<ProductData> product = productRepository.findById(createRequest.productId());
    if (!product.isPresent()) {
      LOG.warn("Attempted to create a bank account associated with non existent product of {}", createRequest.productId());
      throw ValidationListException.builder().type(DioExceptionType.INVALID_PRODUCT).explanation(Labels.ERROR_INVALID_PRODUCT).build();
    }
    
    BankAccountData data = mapper.map(createRequest, BankAccountData.class);
    data.branch(branch.get());
    data.product(product.get());
    
    // TODO: Product Constraint and Eligibility Checking
    
    if(createRequest.bundleId() != null) {
      Optional<ProductBundleData> bundle = bundleRepository.findById(createRequest.bundleId());
      if (!bundle.isPresent()) {
        LOG.warn("Attempted to create a bank account associated with non existent bundle of {}", createRequest.productId());
        throw ValidationListException.builder().type(DioExceptionType.INVALID_BUNDLE).explanation(Labels.ERROR_INVALID_PRODUCT_BUNDLE).build();
      }
      data.bundle(bundle.get());
    }
    
    BankAccountData account = bankAccountRepository.save(data);
    
    LOG.debug("Creating a new bank account for brand {} branch {} with content of {}", brandId, branchId, data);
    return getBankAccount(brandId, branchId, account.id());
  }

  @Override
  public ResponseEntity<Void> deleteBankAccount(UUID brandId, UUID branchId, UUID bankAccountId) {
    Optional<BankAccountData> optionalData = bankAccountRepository.findByIdAndBranchIdAndBranchBrandId(bankAccountId, branchId, brandId);

    if (optionalData.isPresent()) {
      LOG.info("Deleting account with id of {} brand of {} branch of {}", bankAccountId, brandId, branchId);
      bankAccountRepository.delete(optionalData.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      LOG.warn(
          "Attempted to delete a bank account but it couldn't be found with branch {} branch {} and bankAccount {}",
          branchId, branchId, bankAccountId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioBankAccount> updateBankAccount(UUID brandId, UUID branchId, UUID bankAccountId,
      RequestBankAccount createRequest) throws ValidationListException {

    DeepThoughtValidator.validate(validator, createRequest);
    
    Optional<BankAccountData> optionalData = bankAccountRepository.findByIdAndBranchIdAndBranchBrandId(bankAccountId, branchId, brandId);

    if (optionalData.isPresent()) {
      BankAccountData data = optionalData.get();
      
      Optional<ProductData> product = productRepository.findById(createRequest.productId());
      if (!product.isPresent()) {
        LOG.warn("Attempted to create a bank account associated with non existent product of {}", createRequest.productId());
        throw ValidationListException.builder().type(DioExceptionType.INVALID_PRODUCT).explanation(Labels.ERROR_INVALID_PRODUCT).build();
      }
      
      mapper.map(createRequest, BankAccountData.class);
      data.product(product.get());
      
      // TODO: Product Constraint and Eligibility Checking
      
      if(createRequest.bundleId() != null) {
        Optional<ProductBundleData> bundle = bundleRepository.findById(createRequest.bundleId());
        if (!bundle.isPresent()) {
          LOG.warn("Attempted to create a bank account associated with non existent bundle of {}", createRequest.productId());
          throw ValidationListException.builder().type(DioExceptionType.INVALID_BUNDLE).explanation(Labels.ERROR_INVALID_PRODUCT_BUNDLE).build();
        }
        data.bundle(bundle.get());
      }
      
      BankAccountData account = bankAccountRepository.save(data);
      
      LOG.debug("Updated bank account with branch {} branch {} and bankAccount {} containing content of {}", branchId, branchId,
          bankAccountId, data);
      return getBankAccount(branchId, branchId, account.id());
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

}
