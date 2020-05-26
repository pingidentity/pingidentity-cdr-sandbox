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
import io.biza.deepthought.admin.api.delegate.CustomerPayeeAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.admin.support.DeepThoughtValidator;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.payloads.dio.banking.DioCustomerPayee;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioExceptionType;
import io.biza.deepthought.shared.persistence.model.bank.payments.PayeeData;
import io.biza.deepthought.shared.persistence.model.customer.CustomerData;
import io.biza.deepthought.shared.persistence.repository.PayeeRepository;
import io.biza.deepthought.shared.persistence.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class CustomerPayeeAdminApiDelegateImpl implements CustomerPayeeAdminApiDelegate {

  @Autowired
  private DeepThoughtMapper mapper;

  @Autowired
  private PayeeRepository bankPayeeRepository;
  
  @Autowired
  private CustomerRepository customerRepository;
  
  @Autowired
  private Validator validator;

  @Override
  public ResponseEntity<List<DioCustomerPayee>> listPayees(UUID brandId, UUID customerId) {
    List<PayeeData> bankAccountData = bankPayeeRepository.findAllByCustomerIdAndCustomerBrandId(customerId, brandId);
    LOG.debug("Listing all bank payees for brand id of {} customer id of {} and received {}", brandId, customerId, bankAccountData);
    return ResponseEntity.ok(mapper.mapAsList(bankAccountData, DioCustomerPayee.class));
  }

  @Override
  public ResponseEntity<DioCustomerPayee> getPayee(UUID brandId, UUID customerId, UUID payeeId) {
    Optional<PayeeData> data = bankPayeeRepository.findByIdAndCustomerIdAndCustomerBrandId(payeeId, customerId, brandId);

    if (data.isPresent()) {
      LOG.info("Retrieving a single bank payee with brand id {} and customer id of {} and id of {} and content of {}",
          brandId, customerId, payeeId, data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioCustomerPayee.class));
    } else {
      LOG.warn(
          "Attempted to retrieve a single bank payee but could not find with brand id of {} and customer id of {} and id of {}",
          brandId, customerId, payeeId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioCustomerPayee> createPayee(UUID brandId, UUID customerId, DioCustomerPayee createRequest)
      throws ValidationListException {
    
    DeepThoughtValidator.validate(validator, createRequest);
    
    Optional<CustomerData> customer = customerRepository.findByIdAndBrandId(customerId, brandId);
    
    if (!customer.isPresent()) {
      LOG.warn("Attempted to create a payee for a customer which could not be identified with brand {} and customer id of {}", brandId, customerId);
      throw ValidationListException.builder().type(DioExceptionType.INVALID_CUSTOMER).explanation(Labels.ERROR_INVALID_CUSTOMER).build();
    }
    
    PayeeData requestPayee = mapper.map(createRequest, PayeeData.class);
    requestPayee.customer(customer.get());
    LOG.debug("Creating a new bank payee for brand {} customer {} with content of {}", brandId, customerId, requestPayee);    
    PayeeData payee = bankPayeeRepository.save(requestPayee);
    return getPayee(brandId, customerId, payee.id());
  }

  @Override
  public ResponseEntity<Void> deletePayee(UUID brandId, UUID customerId, UUID payeeId) {
    Optional<PayeeData> optionalData = bankPayeeRepository.findByIdAndCustomerIdAndCustomerBrandId(payeeId, customerId, brandId);

    if (optionalData.isPresent()) {
      LOG.info("Deleting payee with id of {} brand of {} customer of {}", payeeId, brandId, customerId);
      bankPayeeRepository.delete(optionalData.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      LOG.warn(
          "Attempted to delete payee but it couldn't be found with brand {} customer {} and id of {}",
          brandId, customerId, payeeId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioCustomerPayee> updatePayee(UUID brandId, UUID customerId, UUID payeeId,
      DioCustomerPayee createRequest) throws ValidationListException {

    DeepThoughtValidator.validate(validator, createRequest);
    
    Optional<PayeeData> optionalData = bankPayeeRepository.findByIdAndCustomerIdAndCustomerBrandId(payeeId, customerId, brandId);

    if (optionalData.isPresent()) {
      PayeeData data = optionalData.get();
      mapper.map(createRequest, data);
      bankPayeeRepository.save(data);
      
      LOG.debug("Updated payee with brand {} customer id {} and id {} containing content of {}", brandId,
          customerId, payeeId, data);
      return getPayee(brandId, customerId, payeeId);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

}
