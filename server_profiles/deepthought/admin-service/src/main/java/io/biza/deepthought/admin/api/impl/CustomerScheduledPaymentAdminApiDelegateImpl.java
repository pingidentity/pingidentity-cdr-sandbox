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
import io.biza.deepthought.admin.api.delegate.CustomerScheduledPaymentAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.admin.support.DeepThoughtValidator;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.payloads.dio.banking.DioCustomerScheduledPayment;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioExceptionType;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountData;
import io.biza.deepthought.shared.persistence.model.bank.payments.ScheduledPaymentData;
import io.biza.deepthought.shared.persistence.model.customer.CustomerData;
import io.biza.deepthought.shared.persistence.repository.BankAccountRepository;
import io.biza.deepthought.shared.persistence.repository.CustomerRepository;
import io.biza.deepthought.shared.persistence.repository.ScheduledPaymentRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class CustomerScheduledPaymentAdminApiDelegateImpl implements CustomerScheduledPaymentAdminApiDelegate {

  @Autowired
  private DeepThoughtMapper mapper;

  @Autowired
  private ScheduledPaymentRepository bankScheduledPaymentRepository;
  
  @Autowired
  private CustomerRepository customerRepository;
  
  @Autowired
  private BankAccountRepository accountRepository;
  
  @Autowired
  private Validator validator;

  @Override
  public ResponseEntity<List<DioCustomerScheduledPayment>> listScheduledPayments(UUID brandId, UUID customerId) {
    List<ScheduledPaymentData> bankAccountData = bankScheduledPaymentRepository.findAllByCustomerIdAndCustomerBrandId(customerId, brandId);
    LOG.debug("Listing all bank scheduledPayments for brand id of {} customer id of {} and received {}", brandId, customerId, bankAccountData);
    return ResponseEntity.ok(mapper.mapAsList(bankAccountData, DioCustomerScheduledPayment.class));
  }

  @Override
  public ResponseEntity<DioCustomerScheduledPayment> getScheduledPayment(UUID brandId, UUID customerId, UUID scheduledPaymentId) {
    Optional<ScheduledPaymentData> data = bankScheduledPaymentRepository.findByIdAndCustomerIdAndCustomerBrandId(scheduledPaymentId, customerId, brandId);

    if (data.isPresent()) {
      LOG.info("Retrieving a single bank scheduled payment with brand id {} and customer id of {} and id of {} and content of {}",
          brandId, customerId, scheduledPaymentId, data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioCustomerScheduledPayment.class));
    } else {
      LOG.warn(
          "Attempted to retrieve a single bank scheduled payment but could not find with brand id of {} and customer id of {} and id of {}",
          brandId, customerId, scheduledPaymentId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioCustomerScheduledPayment> createScheduledPayment(UUID brandId, UUID customerId, DioCustomerScheduledPayment createRequest)
      throws ValidationListException {
    
    DeepThoughtValidator.validate(validator, createRequest);
    
    Optional<CustomerData> customer = customerRepository.findByIdAndBrandId(customerId, brandId);
    
    if (!customer.isPresent()) {
      LOG.warn("Attempted to create a scheduled payment for a customer which could not be identified with brand {} and customer id of {}", brandId, customerId);
      throw ValidationListException.builder().type(DioExceptionType.INVALID_CUSTOMER).explanation(Labels.ERROR_INVALID_CUSTOMER).build();
    }
    
    Optional<BankAccountData> bankAccount = accountRepository.findByIdAndCustomerAccountsCustomerId(UUID.fromString(createRequest.cdrBanking().from().accountId()), customer.get().id());
    if (!bankAccount.isPresent()) {
      LOG.warn("Attempted to create a scheduled payment for a bank account which could not be identified with account id {} and customer id of {}",  UUID.fromString(createRequest.cdrBanking().from().accountId()), customer.get().id());
      throw ValidationListException.builder().type(DioExceptionType.INVALID_ACCOUNT).explanation(Labels.ERROR_INVALID_ACCOUNT).build();
    }
    
    ScheduledPaymentData requestScheduledPayment = mapper.map(createRequest, ScheduledPaymentData.class);
    requestScheduledPayment.customer(customer.get());
    requestScheduledPayment.from(bankAccount.get());

    LOG.debug("Creating a new bank scheduled payment for brand {} customer {} with content of {}", brandId, customerId, requestScheduledPayment);
    ScheduledPaymentData scheduledPayment = bankScheduledPaymentRepository.save(requestScheduledPayment);
    return getScheduledPayment(brandId, customerId, scheduledPayment.id());
  }

  @Override
  public ResponseEntity<Void> deleteScheduledPayment(UUID brandId, UUID customerId, UUID scheduledPaymentId) {
    Optional<ScheduledPaymentData> optionalData = bankScheduledPaymentRepository.findByIdAndCustomerIdAndCustomerBrandId(scheduledPaymentId, customerId, brandId);

    if (optionalData.isPresent()) {
      LOG.info("Deleting scheduled payment with id of {} brand of {} customer of {}", scheduledPaymentId, brandId, customerId);
      bankScheduledPaymentRepository.delete(optionalData.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      LOG.warn(
          "Attempted to delete scheduled payment but it couldn't be found with brand {} customer {} and id of {}",
          brandId, customerId, scheduledPaymentId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioCustomerScheduledPayment> updateScheduledPayment(UUID brandId, UUID customerId, UUID scheduledPaymentId,
      DioCustomerScheduledPayment createRequest) throws ValidationListException {

    DeepThoughtValidator.validate(validator, createRequest);
    
    Optional<ScheduledPaymentData> optionalData = bankScheduledPaymentRepository.findByIdAndCustomerIdAndCustomerBrandId(scheduledPaymentId, customerId, brandId);

    if (optionalData.isPresent()) {
      ScheduledPaymentData data = optionalData.get();
      mapper.map(createRequest, data);
      bankScheduledPaymentRepository.save(data);
      
      LOG.debug("Updated scheduled payment with brand {} customer id {} and id {} containing content of {}", brandId,
          customerId, scheduledPaymentId, data);
      return getScheduledPayment(brandId, customerId, scheduledPaymentId);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

}
