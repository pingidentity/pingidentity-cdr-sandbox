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
import io.biza.deepthought.admin.api.delegate.CustomerAccountAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.payloads.dio.common.DioCustomerAccount;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioExceptionType;
import io.biza.deepthought.shared.payloads.requests.RequestCustomerBankAccountConnection;
import io.biza.deepthought.shared.persistence.model.BrandData;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountData;
import io.biza.deepthought.shared.persistence.model.customer.CustomerData;
import io.biza.deepthought.shared.persistence.model.customer.bank.CustomerAccountData;
import io.biza.deepthought.shared.persistence.repository.BankAccountRepository;
import io.biza.deepthought.shared.persistence.repository.BrandRepository;
import io.biza.deepthought.shared.persistence.repository.CustomerAccountRepository;
import io.biza.deepthought.shared.persistence.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class CustomerAccountAdminApiDelegateImpl
    implements CustomerAccountAdminApiDelegate {

  @Autowired
  private DeepThoughtMapper mapper;

  @Autowired
  private CustomerAccountRepository customerAccountRepository;

  @Autowired
  private BankAccountRepository accountRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private BrandRepository brandRepository;

  @Autowired
  private Validator validator;

  @Override
  public ResponseEntity<List<DioCustomerAccount>> listAssociations(UUID brandId,
      UUID customerId) {
    List<CustomerAccountData> customerData =
        customerAccountRepository.findAllByCustomerIdAndCustomerBrandId(customerId, brandId);
    LOG.debug(
        "Listing all customer bank account associations for brand {} and customer id {} and received {}",
        brandId, customerId, customerData);
    return ResponseEntity.ok(mapper.mapAsList(customerData, DioCustomerAccount.class));
  }

  @Override
  public ResponseEntity<DioCustomerAccount> getAssociation(UUID brandId, UUID customerId,
      UUID associationId) throws ValidationListException {

    Optional<CustomerAccountData> optionalCustomer = customerAccountRepository
        .findByIdAndCustomerIdAndCustomerBrandId(associationId, customerId, brandId);

    if (!optionalCustomer.isPresent()) {
      LOG.warn(
          "Attempted to retrieve an association for brand {} customer {} and association {} that doesn't exist",
          brandId, customerId, associationId);
      throw ValidationListException.builder().type(DioExceptionType.INVALID_ASSOCIATION)
          .explanation(Labels.ERROR_INVALID_ASSOCIATION).build();
    }

    return ResponseEntity.ok(mapper.map(optionalCustomer.get(), DioCustomerAccount.class));

  }

  @Override
  public ResponseEntity<DioCustomerAccount> associateAccount(UUID brandId, UUID customerId,
      RequestCustomerBankAccountConnection accountRequest) throws ValidationListException {

    validator.validate(accountRequest);

    Optional<BrandData> brand = brandRepository.findById(brandId);

    if (!brand.isPresent()) {
      LOG.warn("Attempted to create a customer with non existent brand of {}", brandId);
      throw ValidationListException.builder().type(DioExceptionType.INVALID_BRAND)
          .explanation(Labels.ERROR_INVALID_BRAND).build();
    }

    Optional<CustomerData> customer = customerRepository.findByIdAndBrandId(customerId, brandId);

    if (!customer.isPresent()) {
      LOG.warn("Requested customer {} for brand {} doesn't exist", customerId, brandId);
      throw ValidationListException.builder().type(DioExceptionType.INVALID_CUSTOMER)
          .explanation(Labels.ERROR_INVALID_CUSTOMER).build();
    }

    Optional<BankAccountData> account =
        accountRepository.findByIdAndBranchBrandId(accountRequest.bankAccountId(), brandId);

    if (!account.isPresent()) {
      LOG.warn("Requested customer {} for brand {} doesn't exist", customerId, brandId);
      throw ValidationListException.builder().type(DioExceptionType.INVALID_ACCOUNT)
          .explanation(Labels.ERROR_INVALID_BRAND).build();
    }

    Optional<CustomerAccountData> optionalAssociation = customerAccountRepository
        .findByBankAccountIdAndCustomerIdAndCustomerBrandId(accountRequest.bankAccountId(), customerId, brandId);

    if (!optionalAssociation.isPresent()) {
      LOG.info("Creating a Customer Association for brand {} customer {} and account {}", brandId,
          customerId, accountRequest.bankAccountId());

      CustomerAccountData data =
          customerAccountRepository.save(CustomerAccountData.builder().bankAccount(account.get())
              .customer(customer.get()).owner(accountRequest.makeOwner()).build());

      return ResponseEntity.ok(mapper.map(data, DioCustomerAccount.class));
    } else {
      return ResponseEntity.ok(mapper.map(optionalAssociation.get(), DioCustomerAccount.class));
    }
  }


  @Override
  public ResponseEntity<Void> unassociateAccount(UUID brandId, UUID customerId, UUID associationId)
      throws ValidationListException {
    Optional<CustomerAccountData> optionalCustomer = customerAccountRepository
        .findByIdAndCustomerIdAndCustomerBrandId(associationId, customerId, brandId);

    if (!optionalCustomer.isPresent()) {
      LOG.warn(
          "Deleting Customer Association for brand {} customer {} and association {} that doesn't exist",
          brandId, customerId, associationId);
      throw ValidationListException.builder().type(DioExceptionType.INVALID_ASSOCIATION)
          .explanation(Labels.ERROR_INVALID_ASSOCIATION).build();
    } else {
      LOG.debug("Deleting customer with brand of {} and customer id of {}", brandId, customerId);
      customerAccountRepository.delete(optionalCustomer.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
  }
}
