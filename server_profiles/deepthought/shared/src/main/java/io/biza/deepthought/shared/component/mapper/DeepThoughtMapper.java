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
package io.biza.deepthought.shared.component.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import io.biza.deepthought.shared.mapper.OrikaFactoryConfigurer;

@Component
public class DeepThoughtMapper extends ConfigurableMapper implements ApplicationContextAware {

  @SuppressWarnings("unused")
  private ApplicationContext applicationContext;
  private OrikaFactoryConfigurer configurer;

  public DeepThoughtMapper() {
    super(false);
    configurer = new OrikaFactoryConfigurer();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
    init();
  }

  @Override
  protected void configureFactoryBuilder(DefaultMapperFactory.Builder factoryBuilder) {
    configurer.configureFactoryBuilder(factoryBuilder);
  }

  @Override
  protected void configure(MapperFactory factory) {
    configurer.configureMapperFactory(factory);
  }

}
