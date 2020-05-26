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
package io.biza.deepthought.shared.persistence.converter;

import java.util.Locale;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter(autoApply = true)
public class LocaleDataConverter implements AttributeConverter<Locale, String> {

  @Override
  public String convertToDatabaseColumn(Locale entityValue) {
    return (entityValue == null) ? null : entityValue.toLanguageTag();
  }

  @Override
  public Locale convertToEntityAttribute(String databaseValue) {
    return databaseValue != null && databaseValue.length() > 0
        ? Locale.forLanguageTag(databaseValue.trim())
        : null;
  }
}
