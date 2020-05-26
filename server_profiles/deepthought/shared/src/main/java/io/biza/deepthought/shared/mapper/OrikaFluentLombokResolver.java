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
package io.biza.deepthought.shared.mapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import ma.glasnost.orika.metadata.Property;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.property.IntrospectorPropertyResolver;

public class OrikaFluentLombokResolver extends IntrospectorPropertyResolver {

  @Override
  protected void collectProperties(Class<?> type, Type<?> referenceType,
      Map<String, Property> properties) {
    super.collectProperties(type, referenceType, properties);
    Set<String> fieldNames =
        Arrays.stream(type.getDeclaredFields()).map(Field::getName).collect(Collectors.toSet());
    Arrays.stream(type.getDeclaredMethods()).filter(method -> fieldNames.contains(method.getName()))
        .forEach(method -> {
          if (method.getParameterCount() > 0) {
            Property.Builder builder = new Property.Builder();
            builder.expression(method.getName());
            builder.name(method.getName());
            builder.setter(method.getName() + "(%s)");
            builder.getter(method.getName() + "()");
            Class<?> fieldType = method.getParameterTypes()[0];
            builder.type(this.resolvePropertyType(null, fieldType, type, referenceType));
            Property property = builder.build(this);
            properties.put(method.getName(), property);
          }
        });
  }
}
