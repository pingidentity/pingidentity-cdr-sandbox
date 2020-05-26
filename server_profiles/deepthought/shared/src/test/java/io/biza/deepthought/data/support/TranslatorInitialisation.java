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
package io.biza.deepthought.data.support;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.Iterator;
import java.util.LinkedHashSet;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.springframework.shell.table.BorderSpecification;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.CellMatchers;
import org.springframework.shell.table.SimpleHorizontalAligner;
import org.springframework.shell.table.SimpleVerticalAligner;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModelBuilder;
import io.biza.deepthought.shared.mapper.OrikaFactoryConfigurer;
import ma.glasnost.orika.MapperFactory;

@DisplayName("Deep Thought Payload Translation Tests")
public class TranslatorInitialisation {

  public static MapperFactory mapper;

  private <A> LinkedHashSet<String> getAllFieldNames(A input) {
    LinkedHashSet<String> result = new LinkedHashSet<String>();

    if(input == null) { return result; }
    
    Class<?> i = input.getClass();
    while (i != null && i != Object.class) {
      for (Field field : i.getDeclaredFields()) {
        try {
          try {
            input.getClass().getMethod("get" + StringUtils.capitalize(field.getName()));
            result.add(field.getName());
          } catch (NoSuchMethodException e) {
            input.getClass().getMethod(field.getName());
            result.add(field.getName());
          }
        } catch (NoSuchMethodException | SecurityException e) {
          // If there's no accessor method forget it
        }
      }
      i = i.getSuperclass();
    }

    return result;
  }

  protected <A> String createComparisonTable(A objectA, A objectB) {
    TableModelBuilder<Object> tableRows = new TableModelBuilder<>();
    tableRows.addRow().addValue("Attribute Name (Type)")
        .addValue("[A]: " + objectA.getClass().getSimpleName())
        .addValue("[B]: " + objectB.getClass().getSimpleName());
    createComparisonModel(objectA, objectB, null, tableRows);
    TableBuilder builder = new TableBuilder(tableRows.build());
    builder.addOutlineBorder(BorderStyle.fancy_double)
        .paintBorder(BorderStyle.air, BorderSpecification.INNER_VERTICAL).fromTopLeft()
        .toBottomRight().paintBorder(BorderStyle.fancy_light, BorderSpecification.INNER)
        .fromTopLeft().toBottomRight().addHeaderBorder(BorderStyle.fancy_double)
        .on(CellMatchers.row(0)).addAligner(SimpleVerticalAligner.middle)
        .addAligner(SimpleHorizontalAligner.center);
    return builder.build().render(80);
  }

  protected <A> TableModelBuilder<Object> createComparisonModel(A objectA, A objectB, String prefix,
      TableModelBuilder<Object> table) {
    LinkedHashSet<String> fieldNames = getAllFieldNames(objectA);

    for (String fieldName : fieldNames) {
      Object objectAValue = new Object();
      Object objectBValue = new Object();
      Type objectAType = null;

      try {
        Method getterFunction;

        try {
          getterFunction = objectA.getClass().getMethod("get" + StringUtils.capitalize(fieldName));
        } catch (NoSuchMethodException e) {
          getterFunction = objectA.getClass().getMethod(fieldName);
        }

        objectAValue = getterFunction.invoke(objectA);
        objectAType = getterFunction.getGenericReturnType();
        objectBValue = getterFunction.invoke(objectB);

        if (objectAValue == null) {
          objectAValue = "";
        }
        if (objectBValue == null) {
          objectBValue = "";
        }
      } catch (IllegalArgumentException | IllegalAccessException | SecurityException
          | InvocationTargetException | NoSuchMethodException e) {
        // No go, do we care?!?
      }

      String cellTitle = String.format("%s", fieldName, objectAType.getTypeName());

      if (prefix != null) {
        cellTitle = String.format("%s.%s", prefix, fieldName, objectAType.getTypeName());
      }

      if (objectAType.getTypeName().startsWith("io.biza.deepthought.data.payload")
          || objectAType.getTypeName().startsWith("io.biza.babelfish.cdr.model")
          || objectAType.getTypeName().startsWith("io.biza.babelfish.cdr.v1.model")
          || objectAType.getTypeName().startsWith("io.biza.babelfish.cdr.v2.model")) {
        createComparisonModel(objectAValue, objectBValue, cellTitle, table);
      } else if (objectAType.getTypeName().startsWith("java.util.List")) {
        List<?> listAItems = List.of();
        List<?> listBItems = List.of();
        if(objectAValue instanceof String) {
          listAItems = List.of(objectAValue);
        }
        
        if(objectBValue instanceof String) {
          listBItems = List.of(objectBValue);
        }
        
        if(objectAValue instanceof List) {
          listAItems = (List<?>) objectAValue;
        }        

        if(objectBValue instanceof List) {
          listBItems = (List<?>)objectBValue;
        }
        
        if(objectAValue instanceof List && objectBValue instanceof List) {
          for (int i = 0; i < listAItems.size(); i++) {
            createComparisonModel(listAItems.get(i), listBItems.get(i), cellTitle + "[" + i + "]",
                table);
          }
        }
      } else if (objectAType.getTypeName().startsWith("java.util.Set") && objectBValue instanceof Set) {
        Iterator<?> listAItems = ((Set<?>) objectAValue).iterator();
        Iterator<?> listBItems = ((Set<?>) objectBValue).iterator();

        int i = 0;
        while (listAItems.hasNext()) {
          i++;
          if (!listBItems.hasNext()) {
            break;
          }
          createComparisonModel(listAItems.next(), listBItems.next(), cellTitle + "[" + i + "]",
              table);
        }
      } else {
        // LOG.info("Object A: {} Type: {}", objectAValue, objectAType);
        if ((objectAValue == null && objectBValue != null) || !objectAValue.equals(objectBValue)) {
          if(objectAValue == null) { objectAValue = "*UNDEFINED*"; }
          cellTitle = "** " + cellTitle;
        }
        table.addRow().addValue(cellTitle).addValue(objectAValue.toString())
            .addValue(objectBValue.toString());
      }
    }

    return table;
  }


  @BeforeAll
  public static void init() throws FileNotFoundException, SQLException {
    OrikaFactoryConfigurer configurer = new OrikaFactoryConfigurer();
    mapper = configurer.getFactory();
  }
}
