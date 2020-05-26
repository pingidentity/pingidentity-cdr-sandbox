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
import io.biza.deepthought.admin.api.delegate.BankAccountTransactionAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.admin.support.DeepThoughtValidator;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankAccountTransaction;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioExceptionType;
import io.biza.deepthought.shared.persistence.model.bank.BankBranchData;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountData;
import io.biza.deepthought.shared.persistence.model.bank.transaction.BankAccountTransactionData;
import io.biza.deepthought.shared.persistence.repository.BankAccountRepository;
import io.biza.deepthought.shared.persistence.repository.BankAccountTransactionRepository;
import io.biza.deepthought.shared.persistence.repository.BankBranchRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankAccountTransactionAdminApiDelegateImpl implements BankAccountTransactionAdminApiDelegate {

  @Autowired
  private DeepThoughtMapper mapper;

  @Autowired
  private BankAccountTransactionRepository bankTransactionRepository;
  
  @Autowired
  private BankAccountRepository bankAccountRepository;
  
  @Autowired
  private BankBranchRepository branchRepository;
  
  @Autowired
  private Validator validator;

  @Override
  public ResponseEntity<List<DioBankAccountTransaction>> listTransactions(UUID brandId, UUID branchId, UUID accountId) {
    List<BankAccountTransactionData> bankAccountData = bankTransactionRepository.findAllByAccountIdAndAccountBranchIdAndAccountBranchBrandId(accountId, branchId, brandId);
    LOG.debug("Listing all bank transactions for brand id of {} branch id of {} account id of {} and received {}", brandId, branchId, accountId, bankAccountData);
    return ResponseEntity.ok(mapper.mapAsList(bankAccountData, DioBankAccountTransaction.class));
  }

  @Override
  public ResponseEntity<DioBankAccountTransaction> getTransaction(UUID brandId, UUID branchId, UUID accountId, UUID transactionId) {
    Optional<BankAccountTransactionData> data = bankTransactionRepository.findByIdAndAccountIdAndAccountBranchIdAndAccountBranchBrandId(transactionId, accountId, branchId, brandId);

    if (data.isPresent()) {
      LOG.info("Retrieving a single bank transaction with branch of {} brand id {} and account id of {} and id of {} and content of {}",
          branchId, brandId, accountId, transactionId, data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioBankAccountTransaction.class));
    } else {
      LOG.warn(
          "Attempted to retrieve a single bank transaction but could not find with branch of {} brand id of {} and account id of {} and id of {}",
          branchId, branchId, accountId, transactionId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioBankAccountTransaction> createTransaction(UUID brandId, UUID branchId, UUID accountId, DioBankAccountTransaction createRequest)
      throws ValidationListException {
    
    DeepThoughtValidator.validate(validator, createRequest);
    
    Optional<BankAccountData> bankAccount = bankAccountRepository.findByIdAndBranchIdAndBranchBrandId(accountId, branchId, brandId);
    
    if (!bankAccount.isPresent()) {
      LOG.warn("Attempted to create a transaction for an account which could not be identified with brand {} branch {} and account id of {}", brandId, branchId, accountId);
      throw ValidationListException.builder().type(DioExceptionType.INVALID_ACCOUNT).explanation(Labels.ERROR_INVALID_ACCOUNT).build();
    }
    
    BankAccountTransactionData requestTransaction = mapper.map(createRequest, BankAccountTransactionData.class);
    requestTransaction.account(bankAccount.get());
    
    if(createRequest.apcs() != null) {
      Optional<BankBranchData> destinationBranch = branchRepository.findByBsb(createRequest.apcs().branch().bsb());
      if(!destinationBranch.isPresent()) {
        LOG.warn("Attempted to create a transaction with APCS details where the Branch BSB {} cannot be identified", createRequest.apcs().branch().bsb());
        throw ValidationListException.builder().type(DioExceptionType.INVALID_BRANCH).explanation(Labels.ERROR_INVALID_BRANCH).build();        
      }
      requestTransaction.apcs().branch(destinationBranch.get());
    }
    
    BankAccountTransactionData transaction = bankTransactionRepository.save(requestTransaction);
    
    LOG.debug("Creating a new bank account for brand {} branch {} account {} with content of {}", brandId, branchId, accountId, transaction);
    return getTransaction(brandId, branchId, accountId, transaction.id());
  }

  @Override
  public ResponseEntity<Void> deleteTransaction(UUID brandId, UUID branchId, UUID accountId, UUID transactionId) {
    Optional<BankAccountTransactionData> optionalData = bankTransactionRepository.findByIdAndAccountIdAndAccountBranchIdAndAccountBranchBrandId(transactionId, accountId, branchId, brandId);

    if (optionalData.isPresent()) {
      LOG.info("Deleting transaction with id of {} brand of {} branch of {} account of {}", transactionId, brandId, branchId, accountId);
      bankTransactionRepository.delete(optionalData.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      LOG.warn(
          "Attempted to delete a bank transaction but it couldn't be found with branch {} branch {} account {} and id of {}",
          branchId, branchId, accountId, transactionId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioBankAccountTransaction> updateTransaction(UUID brandId, UUID branchId, UUID accountId, UUID transactionId,
      DioBankAccountTransaction updateRequest) throws ValidationListException {

    DeepThoughtValidator.validate(validator, updateRequest);
    
    Optional<BankAccountTransactionData> optionalData = bankTransactionRepository.findByIdAndAccountIdAndAccountBranchIdAndAccountBranchBrandId(transactionId, accountId, branchId, brandId);

    if (optionalData.isPresent()) {
      BankAccountTransactionData data = optionalData.get();
      mapper.map(updateRequest, data);
      BankAccountTransactionData responseData = bankTransactionRepository.save(data);
      
      LOG.debug("Updated transaction with brand {} branch {} account id {} and id {} containing content of {}", brandId, branchId,
          accountId, transactionId, responseData);
      return getTransaction(brandId, branchId, accountId, transactionId);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

}
