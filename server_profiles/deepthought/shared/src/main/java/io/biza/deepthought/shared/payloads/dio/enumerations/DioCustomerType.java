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
package io.biza.deepthought.shared.payloads.dio.enumerations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import io.biza.babelfish.cdr.exceptions.LabelValueEnumValueNotSupportedException;
import io.biza.babelfish.cdr.support.LabelValueEnumInterface;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Customer Type", enumAsRef = true)
public enum DioCustomerType implements LabelValueEnumInterface {
  // @formatter:off
  PERSON("PERSON", "Individual Customer"),
  ORGANISATION("ORGANISATION", "Organisational Customer");
  // @formatter:on

  private String value;

  private String label;

  DioCustomerType(String value, String label) {
    this.value = value;
    this.label = label;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static DioCustomerType fromValue(String text)
      throws LabelValueEnumValueNotSupportedException {
    for (DioCustomerType b : DioCustomerType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    throw new LabelValueEnumValueNotSupportedException(
        "Unable to identify value of DioCustomerType from " + text,
        DioCustomerType.class.getSimpleName(), DioCustomerType.values(),
        text);
  }

  @Override
  @JsonIgnore
  public String label() {
    return label;
  }
}