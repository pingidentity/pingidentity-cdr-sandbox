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

import java.time.OffsetDateTime;
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
import io.biza.deepthought.admin.api.delegate.GrantAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.admin.support.DeepThoughtValidator;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioExceptionType;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioGrantAccess;
import io.biza.deepthought.shared.payloads.dio.grant.DioGrant;
import io.biza.deepthought.shared.payloads.requests.RequestGrant;
import io.biza.deepthought.shared.payloads.requests.RequestGrantCustomerAccount;
import io.biza.deepthought.shared.persistence.model.customer.bank.CustomerAccountData;
import io.biza.deepthought.shared.persistence.model.grant.GrantCustomerAccountData;
import io.biza.deepthought.shared.persistence.model.grant.GrantData;
import io.biza.deepthought.shared.persistence.repository.BankAccountRepository;
import io.biza.deepthought.shared.persistence.repository.CustomerAccountRepository;
import io.biza.deepthought.shared.persistence.repository.CustomerRepository;
import io.biza.deepthought.shared.persistence.repository.GrantRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class GrantAdminApiDelegateImpl implements GrantAdminApiDelegate {

  @Autowired
  private DeepThoughtMapper mapper;

  @Autowired
  private GrantRepository grantRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private BankAccountRepository accountRepository;
  
  @Autowired
  private CustomerAccountRepository customerAccountRepository;

  @Autowired
  private Validator validator;

  @Override
  public ResponseEntity<List<DioGrant>> listGrants() {
    List<GrantData> grantData = grantRepository.findAll();
    LOG.debug("Listing all grants and received {}", grantData);
    return ResponseEntity.ok(mapper.mapAsList(grantData, DioGrant.class));
  }

  @Override
  public ResponseEntity<DioGrant> getGrant(UUID grantId) {
    Optional<GrantData> data = grantRepository.findById(grantId);

    if (data.isPresent()) {
      LOG.info("Retrieving a single grant with id of {} and content of {}", grantId, data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioGrant.class));
    } else {
      LOG.warn("Attempted to retrieve a single grant but could not find with id of {}", grantId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioGrant> createGrant(RequestGrant createData)
      throws ValidationListException {

    DeepThoughtValidator.validate(validator, createData);
    
    GrantData data = GrantData.builder().subject(createData.subject())
        .expiry(OffsetDateTime.now().plusSeconds(createData.length())).build();

    Set<GrantCustomerAccountData> accountList = new HashSet<GrantCustomerAccountData>();
    for(RequestGrantCustomerAccount customerAccount : createData.customerAccounts()) {
      Optional<CustomerAccountData> customer = customerAccountRepository.findById(customerAccount.customerAccountId());
      
      if(customer.isPresent()) {
        GrantCustomerAccountData grantData = GrantCustomerAccountData.builder().permissions(new HashSet<DioGrantAccess>(customerAccount.permissions())).build();
      grantData.customerAccount(customer.get());
      grantData.grant(data);
      
      accountList.add(grantData);
      } else {
        throw ValidationListException.builder()
        .explanation("Requested Customer Bank Association identifier of " + customerAccount.customerAccountId() + " not found")
        .type(DioExceptionType.INVALID_ASSOCIATION).build();
      }
    }
    
    data.customerAccounts(accountList);
    
    LOG.debug("Created a new grant with content of {}", data);
    return getGrant(grantRepository.save(data).id());
  }

  @Override
  public ResponseEntity<Void> deleteGrant(UUID grantId) {
    Optional<GrantData> optionalData = grantRepository.findById(grantId);

    if (optionalData.isPresent()) {
      LOG.debug("Deleting grant with grant id of {}", grantId);
      grantRepository.delete(optionalData.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      LOG.warn("Attempted to delete a grant but it couldn't be found with grant {}", grantId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioGrant> updateGrant(UUID grantId, RequestGrant updateData)
      throws ValidationListException {

    DeepThoughtValidator.validate(validator, updateData);

    Optional<GrantData> optionalData = grantRepository.findById(grantId);

    if (optionalData.isPresent()) {
      GrantData data = optionalData.get();
      data.expiry(data.expiry().plusSeconds(updateData.length()));
      
      Set<GrantCustomerAccountData> accountList = new HashSet<GrantCustomerAccountData>();
      for(RequestGrantCustomerAccount customerAccount : updateData.customerAccounts()) {
        Optional<CustomerAccountData> customer = customerAccountRepository.findById(customerAccount.customerAccountId());
        
        if(customer.isPresent()) {
          GrantCustomerAccountData grantData = GrantCustomerAccountData.builder().permissions(new HashSet<DioGrantAccess>(customerAccount.permissions())).build();
        grantData.customerAccount(customer.get());
        grantData.grant(data);
        
        accountList.add(grantData);
        } else {
          throw ValidationListException.builder()
          .explanation("Requested Customer Bank Association identifier of " + customerAccount.customerAccountId() + " not found")
          .type(DioExceptionType.INVALID_ASSOCIATION).build();
        }
      }
      
      GrantData updatedData = grantRepository.save(data);
      LOG.debug("Updated grant with grant {} containing content of {}", grantId, updatedData);
      return getGrant(updatedData.id());
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

}
