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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

@Slf4j
public class LocaleCountryConverter extends BidirectionalConverter<Locale, String> {

  private final Map<String, Locale> localeMap;
  
  public LocaleCountryConverter() {
    String[] countries = Locale.getISOCountries();
    localeMap = new HashMap<String, Locale>(countries.length);
    for (String country : countries) {
        Locale locale = new Locale("", country);
        localeMap.put(locale.getISO3Country().toUpperCase(), locale);
    } 
    
    LOG.info("Populated locale map with {} entries", localeMap.size());
  }
  
  
  @Override
  public String convertTo(Locale source, Type<String> destinationType,
      MappingContext mappingContext) {
    return source.getISO3Country();
  }

  @Override
  public Locale convertFrom(String source, Type<Locale> destinationType,
      MappingContext mappingContext) {
    return localeMap.get(source);
  }
  
  

}