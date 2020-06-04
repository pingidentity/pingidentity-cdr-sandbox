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
package io.biza.deepthought.shared.persistence.mapper.customer;

import io.biza.deepthought.shared.mapper.OrikaFactoryConfigurerInterface;
import io.biza.deepthought.shared.payloads.dio.common.DioCustomer;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioCustomerType;
import io.biza.deepthought.shared.persistence.model.customer.CustomerData;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;

@Slf4j
public class CustomerDataMapper implements OrikaFactoryConfigurerInterface {

  @Override
  public void configure(MapperFactory orikaMapperFactory) {
    orikaMapperFactory.classMap(CustomerData.class, DioCustomer.class).fieldAToB("id", "id")
        .fieldAToB("creationTime", "creationTime").fieldAToB("lastUpdated", "lastUpdated")
        .customize(new CustomMapper<CustomerData, DioCustomer>() {
          @Override
          public void mapAtoB(CustomerData from, DioCustomer to, MappingContext context) {

            if (from.person() != null) {
              to.customerType(DioCustomerType.PERSON);
            } else if (from.organisation() != null) {
              to.customerType(DioCustomerType.ORGANISATION);
            } else {
              LOG.error(
                  "Neither Organisation nor Person are populated in CustomerData so cannot set customer type");
            }
          }
        }).byDefault().register();
  }
}
