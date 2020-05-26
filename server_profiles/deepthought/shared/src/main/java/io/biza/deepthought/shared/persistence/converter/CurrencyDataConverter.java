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

import java.util.Currency;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class CurrencyDataConverter implements AttributeConverter<Currency, String> {

  @Override
  public String convertToDatabaseColumn(Currency entityValue) {
    return (entityValue == null) ? null : entityValue.getCurrencyCode();
  }

  @Override
  public Currency convertToEntityAttribute(String databaseValue) {
    return databaseValue != null && databaseValue.length() > 0 ? Currency.getInstance(databaseValue)
        : null;
  }
}
