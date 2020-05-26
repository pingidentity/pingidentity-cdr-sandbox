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

import java.time.LocalDate;
import io.biza.babelfish.cdr.Constants;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class LocalDateToStringConverter extends BidirectionalConverter<LocalDate, String> {

  @Override
  public String convertTo(LocalDate source, Type<String> destinationType,
      MappingContext mappingContext) {
    return source.format(Constants.CDR_DATESTRING_FORMATTER);
  }

  @Override
  public LocalDate convertFrom(String source, Type<LocalDate> destinationType,
      MappingContext mappingContext) {
    return LocalDate.parse(source, Constants.CDR_DATESTRING_FORMATTER);
  }

}