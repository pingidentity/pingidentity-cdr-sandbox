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

import java.util.UUID;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class UUIDConverter extends BidirectionalConverter<UUID, String> {

  @Override
  public String convertTo(UUID source, Type<String> destinationType,
      MappingContext mappingContext) {
    return source.toString();
  }

  @Override
  public UUID convertFrom(String source, Type<UUID> destinationType,
      MappingContext mappingContext) {
    return UUID.fromString(source);
  }

}
