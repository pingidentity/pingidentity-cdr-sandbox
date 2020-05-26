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
import io.biza.deepthought.admin.api.delegate.CustomerAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.admin.support.DeepThoughtValidator;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.payloads.dio.common.DioCustomer;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioExceptionType;
import io.biza.deepthought.shared.persistence.model.BrandData;
import io.biza.deepthought.shared.persistence.model.customer.CustomerData;
import io.biza.deepthought.shared.persistence.repository.BankAccountRepository;
import io.biza.deepthought.shared.persistence.repository.BrandRepository;
import io.biza.deepthought.shared.persistence.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class CustomerAdminApiDelegateImpl implements CustomerAdminApiDelegate {

  @Autowired
  private DeepThoughtMapper mapper;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private BrandRepository brandRepository;
  
  @Autowired
  private BankAccountRepository bankAccountRepository;
  
  @Autowired
  private Validator validator;

  @Override
  public ResponseEntity<List<DioCustomer>> listCustomers(UUID brandId) {
    List<CustomerData> customerData = customerRepository.findAllByBrandId(brandId);
    LOG.debug("Listing all customers for brand id of {} and received {}", brandId, customerData);
    return ResponseEntity.ok(mapper.mapAsList(customerData, DioCustomer.class));
  }
  
  @Override
  public ResponseEntity<DioCustomer> getCustomer(UUID brandId, UUID customerId) {
    Optional<CustomerData> data = customerRepository.findByIdAndBrandId(customerId, brandId);

    if (data.isPresent()) {
      LOG.info("Retrieving a single customer with brand of {} and id of {} and content of {}",
          brandId, customerId, data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioCustomer.class));
    } else {
      LOG.warn(
          "Attempted to retrieve a single customer but could not find with brand of {} and id of {}",
          brandId, customerId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioCustomer> createCustomer(UUID brandId, DioCustomer createData)
      throws ValidationListException {
    
    DeepThoughtValidator.validate(validator, createData);

    Optional<BrandData> brand = brandRepository.findById(brandId);

    if (!brand.isPresent()) {
      LOG.warn("Attempted to create a customer with non existent brand of {}", brandId);
      throw ValidationListException.builder().type(DioExceptionType.INVALID_BRAND).explanation(Labels.ERROR_INVALID_BRAND).build();
    }
        
    CustomerData data = mapper.map(createData, CustomerData.class);
    data.brand(brand.get());
    LOG.debug("Created a new customer for brand {} with content of {}", brandId, data);
    return getCustomer(brandId, customerRepository.save(data).id());
  }

  @Override
  public ResponseEntity<Void> deleteCustomer(UUID brandId, UUID customerId) {
    Optional<CustomerData> optionalData = customerRepository.findByIdAndBrandId(customerId, brandId);

    if (optionalData.isPresent()) {
      LOG.debug("Deleting customer with brand of {} and customer id of {}", brandId, customerId);
      customerRepository.delete(optionalData.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      LOG.warn(
          "Attempted to delete a customer but it couldn't be found with brand {} and customer {}",
          brandId, customerId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioCustomer> updateCustomer(UUID brandId, UUID customerId,
      DioCustomer updateData) throws ValidationListException {

    DeepThoughtValidator.validate(validator, updateData);
    
    Optional<CustomerData> optionalData = customerRepository.findByIdAndBrandId(customerId, brandId);

    if (optionalData.isPresent()) {
      CustomerData data = optionalData.get();
      mapper.map(updateData, data);
      CustomerData updatedData = customerRepository.save(data);
      LOG.debug("Updated customer with brand {} and customer {} containing content of {}", brandId,
          customerId, updatedData);
      return getCustomer(brandId, updatedData.id());
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

}
