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

import java.io.IOException;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import com.opencsv.exceptions.CsvValidationException;
import io.biza.babelfish.cdr.support.customtypes.MerchantCategoryCodeType;


@Converter(autoApply = true)
public class MerchantCategoryCodeConverter implements AttributeConverter<MerchantCategoryCodeType, String> {

  @Override
  public String convertToDatabaseColumn(MerchantCategoryCodeType entityValue) {
    return (entityValue == null) ? null : entityValue.toString();
  }

  @Override
  public MerchantCategoryCodeType convertToEntityAttribute(String databaseValue) {
    try {
      return databaseValue != null && databaseValue.length() > 0
          ? MerchantCategoryCodeType.fromValue(databaseValue.trim())
          : null;
    } catch (CsvValidationException | IOException e) {
      return null;
    }
  }
}
