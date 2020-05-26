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

import io.biza.babelfish.cdr.support.customtypes.MerchantCategoryCodeType;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class MerchantCategoryCodeSelfConverter extends BidirectionalConverter<MerchantCategoryCodeType, MerchantCategoryCodeType> {

  @Override
  public MerchantCategoryCodeType convertTo(MerchantCategoryCodeType source, Type<MerchantCategoryCodeType> destinationType,
      MappingContext mappingContext) {
    return source;
  }

  @Override
  public MerchantCategoryCodeType convertFrom(MerchantCategoryCodeType source, Type<MerchantCategoryCodeType> destinationType,
      MappingContext mappingContext) {
    return source;
  }

}