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

import java.lang.reflect.InvocationTargetException;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@Slf4j
public class OrikaFactoryConfigurer {

  protected static MapperFactory localMapper;

  /**
   * Produce a local mapper factory
   * 
   * @return MapperFactory with configuration
   */
  public MapperFactory getFactory() {
    if (localMapper == null) {
      buildFactory();
    }

    return localMapper;
  }

  /**
   * Initialise the mapper, configure it and setup localMapper
   */
  public void buildFactory() {
    /**
     * Initialise the orika mapper
     */
    DefaultMapperFactory.Builder builder = new DefaultMapperFactory.Builder();
    configureFactoryBuilder(builder);
    localMapper = builder.build();
    configureMapperFactory(localMapper);

  }

  /**
   * Setup the lombok resolver
   * 
   * @param builder with lombok compatible resolver
   */
  public void configureFactoryBuilder(DefaultMapperFactory.Builder builder) {
    builder.propertyResolverStrategy(new OrikaFluentLombokResolver());
  }

  /**
   * Reflect the converts and mappers and set them up
   * 
   * @param mapper with deepthought mapper/converters setup
   */
  public void configureMapperFactory(MapperFactory mapper) {

    ConverterFactory converterFactory = mapper.getConverterFactory();

    /**
     * Configure bidirectional converters
     */
    try (ScanResult converterResult = new ClassGraph().enableAllInfo()
        .whitelistPackages("io.biza.deepthought.shared.mapper.converter").scan()) {
      ClassInfoList converterClasses =
          converterResult.getSubclasses("ma.glasnost.orika.converter.BidirectionalConverter");

      for (Class<?> clazz : converterClasses.loadClasses()) {
        try {
          BidirectionalConverter<?, ?> converter =
              (BidirectionalConverter<?, ?>) clazz.getConstructor().newInstance();
          converterFactory.registerConverter(converter);
          LOG.info("Registered converter for {} <-> {}", converter.getAType().getName(), converter.getBType().getName());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
            | NoSuchMethodException | SecurityException e) {
          LOG.error(
              "Unable to find a declared constructor for class name of {} with exception of {}",
              clazz.getName(), e.toString());
        } catch (IllegalArgumentException e) {
          LOG.error(
              "Encountered issues when configuring downstream mapper for class name of {} with exception of {}",
              clazz.getName(), e.toString());
        }
      }

      /**
       * Configure bidirectional configurers
       */
      try (ScanResult mapperResult = new ClassGraph().enableAllInfo()
          .whitelistPackages("io.biza.deepthought.shared.persistence.mapper", "io.biza.deepthought.shared.payloads.mapper").scan()) {
        ClassInfoList configurerClasses =
            mapperResult.getClassesImplementing("io.biza.deepthought.shared.mapper.OrikaFactoryConfigurerInterface");

        for (Class<?> clazz : configurerClasses.loadClasses()) {
          try {
            OrikaFactoryConfigurerInterface configurer =
                (OrikaFactoryConfigurerInterface) clazz.getConstructor().newInstance();
            configurer.configure(mapper);
            LOG.info("Registered mapper named {}", configurer.getClass().getName());
          } catch (InstantiationException | IllegalAccessException | InvocationTargetException
              | NoSuchMethodException | SecurityException e) {
            LOG.error(
                "Unable to find a declared constructor for class name of {} with exception of {}",
                clazz.getName(), e.toString());
          } catch (IllegalArgumentException e) {
            LOG.error(
                "Encountered issues when configuring downstream mapper for class name of {} with exception of {}",
                clazz.getName(), e.toString());
          }
        }
      }
    }
  }

}
