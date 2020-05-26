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
package io.biza.deepthought.data.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Resource;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.biza.babelfish.cdr.enumerations.CommonOrganisationType;
import io.biza.deepthought.data.support.DeepThoughtJpaConfig;
import io.biza.deepthought.data.support.TranslatorInitialisation;
import io.biza.deepthought.data.support.VariableConstants;
import io.biza.deepthought.shared.Constants;
import io.biza.deepthought.shared.payloads.dio.common.DioCustomer;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioCustomerType;
import io.biza.deepthought.shared.persistence.model.BrandData;
import io.biza.deepthought.shared.persistence.model.customer.CustomerData;
import io.biza.deepthought.shared.persistence.model.organisation.OrganisationAddressData;
import io.biza.deepthought.shared.persistence.model.organisation.OrganisationAddressSimpleData;
import io.biza.deepthought.shared.persistence.model.organisation.OrganisationData;
import io.biza.deepthought.shared.persistence.model.person.PersonAddressData;
import io.biza.deepthought.shared.persistence.model.person.PersonAddressSimpleData;
import io.biza.deepthought.shared.persistence.model.person.PersonData;
import io.biza.deepthought.shared.persistence.model.person.PersonEmailData;
import io.biza.deepthought.shared.persistence.model.person.PersonPhoneData;
import io.biza.deepthought.shared.persistence.repository.BrandRepository;
import io.biza.deepthought.shared.persistence.repository.CustomerRepository;
import io.biza.deepthought.shared.persistence.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("Customer Data Tests")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DeepThoughtJpaConfig.class},
    loader = AnnotationConfigContextLoader.class)
@Transactional
@Slf4j
public class CustomerTests extends TranslatorInitialisation {

  @Resource
  private CustomerRepository customerRepository;

  @Resource
  private PersonRepository personRepository;

  @Resource
  private BrandRepository brandRepository;

  public CustomerData createCustomerPerson() {

    BrandData brand = BrandData.builder().name(VariableConstants.BRAND_NAME)
        .displayName(VariableConstants.BRAND_DISPLAY_NAME).build();
    brandRepository.save(brand);

    Optional<BrandData> brandReturn = brandRepository.findById(brand.id());
    assertTrue(brandReturn.isPresent());

    CustomerData customer = CustomerData.builder().creationTime(VariableConstants.CREATION_DATETIME)
        .lastUpdated(VariableConstants.UPDATE_DATETIME).brand(brandReturn.get()).build();

    PersonData person = PersonData.builder().firstName(VariableConstants.PERSON_FIRST_NAME)
        .lastName(VariableConstants.PERSON_LAST_NAME).prefix(VariableConstants.PERSON_PREFIX)
        .middleNames(VariableConstants.PERSON_MIDDLE_NAME).suffix(VariableConstants.PERSON_SUFFIX)
        .occupationCode(VariableConstants.OCCUPATION_CODE).build();
    PersonEmailData email = PersonEmailData.builder().address(VariableConstants.DIO_EMAIL.address())
        .isPreferred(VariableConstants.DIO_EMAIL.isPreferred())
        .type(VariableConstants.DIO_EMAIL.type()).build();
    email.person(person);
    PersonPhoneData phone =
        PersonPhoneData.builder().fullNumber(VariableConstants.DIO_PHONE_NUMBER.fullNumber())
            .isPreferred(VariableConstants.DIO_PHONE_NUMBER.isPreferred())
            .phoneType(VariableConstants.DIO_PHONE_NUMBER.phoneType()).build();
    phone.person(person);
    PersonAddressSimpleData simple = PersonAddressSimpleData.builder()
        .addressLine1(VariableConstants.DIO_ADDRESS.simple().addressLine1())
        .city(VariableConstants.DIO_ADDRESS.simple().city())
        .country(VariableConstants.DIO_ADDRESS.simple().country())
        .mailingName(VariableConstants.DIO_ADDRESS.simple().mailingName())
        .postcode(VariableConstants.DIO_ADDRESS.simple().postcode())
        .state(VariableConstants.DIO_ADDRESS.simple().state()).build();
    PersonAddressData address =
        PersonAddressData.builder().purpose(VariableConstants.DIO_ADDRESS.purpose()).build();
    simple.address(address);
    address.simple(simple);

    person.emailAddress(Set.of(email));
    person.phoneNumber(Set.of(phone));
    person.physicalAddress(Set.of(address));
    person.customer(customer);
    customer.person(person);

    customerRepository.save(customer);

    return customer;
  }

  public CustomerData createCustomerOrganisation() {

    BrandData brand = BrandData.builder().name(VariableConstants.BRAND_NAME)
        .displayName(VariableConstants.BRAND_DISPLAY_NAME).build();
    brandRepository.save(brand);

    Optional<BrandData> brandReturn = brandRepository.findById(brand.id());
    assertTrue(brandReturn.isPresent());

    CustomerData customer = CustomerData.builder().creationTime(VariableConstants.CREATION_DATETIME)
        .lastUpdated(VariableConstants.UPDATE_DATETIME).brand(brandReturn.get()).build();

    OrganisationData organisation = OrganisationData.builder()
        .abn(VariableConstants.ORGANISATION_ABN).acn(VariableConstants.ORGANISATION_ACN)
        .businessName(VariableConstants.ORGANISATION_NAME)
        .legalName(VariableConstants.ORGANISATION_LEGAL_NAME)
        .establishmentDate(VariableConstants.ORGANISATION_ESTABLISHMENT)
        .industryCode(VariableConstants.ORGANISATION_INDUSTRY_CODE)
        .organisationType(CommonOrganisationType.COMPANY).registeredCountry(new Locale(Constants.DEFAULT_LANGUAGE, Constants.DEFAULT_LOCALE)).build();
    OrganisationAddressSimpleData simple = OrganisationAddressSimpleData.builder()
        .addressLine1(VariableConstants.DIO_ADDRESS.simple().addressLine1())
        .city(VariableConstants.DIO_ADDRESS.simple().city())
        .country(VariableConstants.DIO_ADDRESS.simple().country())
        .mailingName(VariableConstants.DIO_ADDRESS.simple().mailingName())
        .postcode(VariableConstants.DIO_ADDRESS.simple().postcode())
        .state(VariableConstants.DIO_ADDRESS.simple().state()).build();
    OrganisationAddressData address =
        OrganisationAddressData.builder().purpose(VariableConstants.DIO_ADDRESS.purpose()).build();
    simple.address(address);
    address.simple(simple);

    organisation.physicalAddress(Set.of(address));
    organisation.customer(customer);
    customer.organisation(organisation);

    customerRepository.save(customer);

    return customer;
  }

  @Test
  public void testMissingPersonOrOrganisation() {
    BrandData brand = BrandData.builder().name(VariableConstants.BRAND_NAME)
        .displayName(VariableConstants.BRAND_DISPLAY_NAME).build();
    brandRepository.save(brand);

    Optional<BrandData> brandReturn = brandRepository.findById(brand.id());
    assertTrue(brandReturn.isPresent());

    CustomerData customer = CustomerData.builder().creationTime(VariableConstants.CREATION_DATETIME)
        .lastUpdated(VariableConstants.UPDATE_DATETIME).brand(brandReturn.get()).build();

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    assertFalse(validator.validate(customer).isEmpty());

  }

  @Test
  public void testCustomerPersonAndCompare() {

    CustomerData customer = createCustomerPerson();
    DioCustomer dioCustomer = mapper.getMapperFacade().map(customer, DioCustomer.class);

    DioCustomer dioCustomerStatic =
        DioCustomer.builder().id(dioCustomer.id()).creationTime(VariableConstants.CREATION_DATETIME)
            .lastUpdated(VariableConstants.UPDATE_DATETIME).customerType(DioCustomerType.PERSON)
            .person(VariableConstants.DIO_PERSON).build();
    dioCustomerStatic.person().id(customer.person().id());
    dioCustomerStatic.person().phone().id(customer.person().phoneNumber().iterator().next().id());
    dioCustomerStatic.person().email().id(customer.person().emailAddress().iterator().next().id());
    dioCustomerStatic.person().address()
        .id(customer.person().physicalAddress().iterator().next().id());
    dioCustomerStatic.person().address().simple()
        .id(customer.person().physicalAddress().iterator().next().id());

    LOG.info("\n\n{}\n\n", createComparisonTable(dioCustomer, dioCustomerStatic));

    if (!dioCustomer.equals(dioCustomerStatic)) {
      LOG.error("Customer Data record to string reports: {}", customer.toString());
      fail("Payload conversion did not provide equality:\n"
          + createComparisonTable(dioCustomer, dioCustomerStatic) + "\nDB:     "
          + dioCustomer.toString() + "\nStatic: " + dioCustomerStatic.toString());
    }
  }

  @Test
  public void testCustomerOrganisationAndCompare() {

    CustomerData customer = createCustomerOrganisation();
    DioCustomer dioCustomer = mapper.getMapperFacade().map(customer, DioCustomer.class);

    LOG.error("We have: {}", customer.toString());

    DioCustomer dioCustomerStatic = DioCustomer.builder().id(dioCustomer.id())
        .creationTime(VariableConstants.CREATION_DATETIME)
        .lastUpdated(VariableConstants.UPDATE_DATETIME).customerType(DioCustomerType.ORGANISATION)
        .organisation(VariableConstants.DIO_ORGANISATION).build();

    dioCustomerStatic.organisation().id(customer.organisation().id());
    dioCustomerStatic.organisation().address()
        .id(customer.organisation().physicalAddress().iterator().next().id());
    dioCustomerStatic.organisation().address().simple()
        .id(customer.organisation().physicalAddress().iterator().next().id());

    LOG.info("\n\n{}\n\n", createComparisonTable(dioCustomer, dioCustomerStatic));

    if (!dioCustomer.equals(dioCustomerStatic)) {
      LOG.error("Customer Data record to string reports: {}", customer.toString());
      fail("Payload conversion did not provide equality:\n"
          + createComparisonTable(dioCustomer, dioCustomerStatic) + "\nDB:     "
          + dioCustomer.toString() + "\nStatic: " + dioCustomerStatic.toString());
    }
  }
}
