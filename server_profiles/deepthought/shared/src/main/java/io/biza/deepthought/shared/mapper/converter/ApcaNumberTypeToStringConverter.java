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

import io.biza.babelfish.cdr.support.customtypes.ApcaNumberType;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class ApcaNumberTypeToStringConverter extends BidirectionalConverter<ApcaNumberType, String> {

  @Override
  public String convertTo(ApcaNumberType source, Type<String> destinationType,
      MappingContext mappingContext) {
    return source.bsb().replaceAll("-", "");
  }

  @Override
  public ApcaNumberType convertFrom(String source, Type<ApcaNumberType> destinationType,
      MappingContext mappingContext) {
    return ApcaNumberType.builder().bsb(source).build();
  }

}