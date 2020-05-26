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
package io.biza.deepthought.shared.persistence.mapper.organisation;

import io.biza.babelfish.cdr.enumerations.AddressPurpose;
import io.biza.deepthought.shared.mapper.OrikaFactoryConfigurerInterface;
import io.biza.deepthought.shared.payloads.dio.common.DioAddress;
import io.biza.deepthought.shared.payloads.dio.common.DioOrganisation;
import io.biza.deepthought.shared.persistence.model.organisation.OrganisationAddressData;
import io.biza.deepthought.shared.persistence.model.organisation.OrganisationData;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;

@Slf4j
public class OrganisationDataMapper implements OrikaFactoryConfigurerInterface {

  @Override
  public void configure(MapperFactory orikaMapperFactory) {
    orikaMapperFactory.classMap(OrganisationData.class, DioOrganisation.class).fieldAToB("id", "id")
        .field("", "cdrCommon")
        .customize(new CustomMapper<OrganisationData, DioOrganisation>() {
          @Override
          public void mapAtoB(OrganisationData from, DioOrganisation to, MappingContext context) {
            
            if(from.physicalAddress() != null) {
              for(OrganisationAddressData address : from.physicalAddress()) {
                if(address.purpose().equals(AddressPurpose.MAIL)) {
                  to.address(orikaMapperFactory.getMapperFacade().map(address, DioAddress.class));
                  break;
                }
                if(address.purpose().equals(AddressPurpose.REGISTERED)) {
                  to.address(orikaMapperFactory.getMapperFacade().map(address, DioAddress.class));
                }
              }
            }
          }
        }).byDefault().register();
  }
}
