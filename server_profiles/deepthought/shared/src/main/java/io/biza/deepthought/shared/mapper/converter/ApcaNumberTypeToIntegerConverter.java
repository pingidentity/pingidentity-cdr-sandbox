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
package io.biza.deepthought.shared.mapper.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.biza.babelfish.cdr.support.customtypes.ApcaNumberType;
import io.biza.deepthought.shared.persistence.repository.BankBranchRepository;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

@Service
public class ApcaNumberTypeToIntegerConverter extends BidirectionalConverter<ApcaNumberType, Integer> {
  
  @Autowired
  BankBranchRepository branchRepository;

  @Override
  public Integer convertTo(ApcaNumberType source, Type<Integer> destinationType,
      MappingContext mappingContext) {
    return Integer.parseInt(source.bsb().replaceAll("-", ""));
  }

  @Override
  public ApcaNumberType convertFrom(Integer source, Type<ApcaNumberType> destinationType,
      MappingContext mappingContext) {
    return ApcaNumberType.builder().bsb(source.toString()).build();
  }

}