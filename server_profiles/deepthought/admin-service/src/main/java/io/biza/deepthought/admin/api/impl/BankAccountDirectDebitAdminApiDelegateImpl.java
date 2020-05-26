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
import io.biza.deepthought.admin.api.delegate.BankAccountDirectDebitAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.admin.support.DeepThoughtValidator;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankAccountDirectDebit;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioExceptionType;
import io.biza.deepthought.shared.persistence.model.bank.BankBranchData;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountData;
import io.biza.deepthought.shared.persistence.model.bank.payments.DirectDebitData;
import io.biza.deepthought.shared.persistence.repository.BankAccountDirectDebitRepository;
import io.biza.deepthought.shared.persistence.repository.BankAccountRepository;
import io.biza.deepthought.shared.persistence.repository.BankBranchRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankAccountDirectDebitAdminApiDelegateImpl implements BankAccountDirectDebitAdminApiDelegate {

  @Autowired
  private DeepThoughtMapper mapper;

  @Autowired
  private BankAccountDirectDebitRepository bankDirectDebitRepository;
  
  @Autowired
  private BankAccountRepository bankAccountRepository;
  
  @Autowired
  private BankBranchRepository branchRepository;
  
  @Autowired
  private Validator validator;

  @Override
  public ResponseEntity<List<DioBankAccountDirectDebit>> listDirectDebits(UUID brandId, UUID branchId, UUID accountId) {
    List<DirectDebitData> bankAccountData = bankDirectDebitRepository.findAllByAccountIdAndAccountBranchIdAndAccountBranchBrandId(accountId, branchId, brandId);
    LOG.debug("Listing all bank directDebits for brand id of {} branch id of {} account id of {} and received {}", brandId, branchId, accountId, bankAccountData);
    return ResponseEntity.ok(mapper.mapAsList(bankAccountData, DioBankAccountDirectDebit.class));
  }

  @Override
  public ResponseEntity<DioBankAccountDirectDebit> getDirectDebit(UUID brandId, UUID branchId, UUID accountId, UUID directDebitId) {
    Optional<DirectDebitData> data = bankDirectDebitRepository.findByIdAndAccountIdAndAccountBranchIdAndAccountBranchBrandId(directDebitId, accountId, branchId, brandId);

    if (data.isPresent()) {
      LOG.info("Retrieving a single bank directDebit with branch of {} brand id {} and account id of {} and id of {} and content of {}",
          branchId, brandId, accountId, directDebitId, data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioBankAccountDirectDebit.class));
    } else {
      LOG.warn(
          "Attempted to retrieve a single bank directDebit but could not find with branch of {} brand id of {} and account id of {} and id of {}",
          branchId, branchId, accountId, directDebitId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioBankAccountDirectDebit> createDirectDebit(UUID brandId, UUID branchId, UUID accountId, DioBankAccountDirectDebit createRequest)
      throws ValidationListException {
    
    DeepThoughtValidator.validate(validator, createRequest);
    
    Optional<BankAccountData> bankAccount = bankAccountRepository.findByIdAndBranchIdAndBranchBrandId(accountId, branchId, brandId);
    
    if (!bankAccount.isPresent()) {
      LOG.warn("Attempted to create a direct debit for an account which could not be identified with brand {} branch {} and account id of {}", brandId, branchId, accountId);
      throw ValidationListException.builder().type(DioExceptionType.INVALID_ACCOUNT).explanation(Labels.ERROR_INVALID_ACCOUNT).build();
    }
    
    DirectDebitData requestDirectDebit = mapper.map(createRequest, DirectDebitData.class);
    requestDirectDebit.account(bankAccount.get());
    
    if(createRequest.authorisedEntity() != null) {
      Optional<BankBranchData> destinationBranch = branchRepository.findById(createRequest.authorisedEntity().branch().id());
      if(!destinationBranch.isPresent()) {
        LOG.warn("Attempted to create a direct debit with authorised entity where the specified branch id of {} cannot be identified", createRequest.authorisedEntity().branch().id());
        throw ValidationListException.builder().type(DioExceptionType.INVALID_BRANCH).explanation(Labels.ERROR_INVALID_BRANCH).build();        
      }
      requestDirectDebit.authorisedEntity().branch(destinationBranch.get());
    }
    
    DirectDebitData directDebit = bankDirectDebitRepository.save(requestDirectDebit);
    
    LOG.debug("Creating a new bank account for brand {} branch {} account {} with content of {}", brandId, branchId, accountId, directDebit);
    return getDirectDebit(brandId, branchId, accountId, directDebit.id());
  }

  @Override
  public ResponseEntity<Void> deleteDirectDebit(UUID brandId, UUID branchId, UUID accountId, UUID directDebitId) {
    Optional<DirectDebitData> optionalData = bankDirectDebitRepository.findByIdAndAccountIdAndAccountBranchIdAndAccountBranchBrandId(directDebitId, accountId, branchId, brandId);

    if (optionalData.isPresent()) {
      LOG.info("Deleting directDebit with id of {} brand of {} branch of {} account of {}", directDebitId, brandId, branchId, accountId);
      bankDirectDebitRepository.delete(optionalData.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      LOG.warn(
          "Attempted to delete a bank direct debit but it couldn't be found with brand {} branch {} account {} and id of {}",
          brandId, branchId, accountId, directDebitId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioBankAccountDirectDebit> updateDirectDebit(UUID brandId, UUID branchId, UUID accountId, UUID directDebitId,
      DioBankAccountDirectDebit createRequest) throws ValidationListException {

    DeepThoughtValidator.validate(validator, createRequest);
    
    Optional<DirectDebitData> optionalData = bankDirectDebitRepository.findByIdAndAccountIdAndAccountBranchIdAndAccountBranchBrandId(directDebitId, accountId, branchId, brandId);

    if (optionalData.isPresent()) {
      DirectDebitData data = optionalData.get();
      mapper.map(createRequest, data);
      bankDirectDebitRepository.save(data);
      
      LOG.debug("Updated direct debit with brand {} branch {} account id {} and id {} containing content of {}", branchId, branchId,
          accountId, directDebitId, data);
      return getDirectDebit(brandId, branchId, accountId, directDebitId);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

}
