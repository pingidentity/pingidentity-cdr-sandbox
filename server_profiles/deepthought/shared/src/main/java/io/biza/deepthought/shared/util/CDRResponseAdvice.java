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
package io.biza.deepthought.shared.util;

import java.util.List;
import java.util.UUID;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import io.biza.babelfish.cdr.exceptions.PayloadConversionException;
import io.biza.babelfish.cdr.exceptions.UnsupportedPayloadException;
import io.biza.babelfish.cdr.support.BabelfishVersioner;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class CDRResponseAdvice implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(MethodParameter returnType, @SuppressWarnings("rawtypes") Class converterType) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(Object inputBody, MethodParameter returnType,
      MediaType selectedContentType, @SuppressWarnings("rawtypes") Class selectedConverterType, ServerHttpRequest request,
      ServerHttpResponse response) {
        
    /**
     * If not supported do nothing
     */
    if(!BabelfishVersioner.isSupported(inputBody.getClass())) {
      return inputBody;
    }
    
    /**
     * Add Interaction Id
     */
    response.getHeaders().put("x-fapi-interaction-id", request.getHeaders()
        .getOrDefault("x-fapi-interaction-id", List.of(UUID.randomUUID().toString())));

    /**
     * Check if we should be converting
     */
    try {
      if(CDRVersioner.needsConversion(inputBody.getClass())) {
        try {
          inputBody = CDRVersioner.convert(inputBody);
        } catch (PayloadConversionException e) {
          LOG.error("Payload conversion error encountered: {}", e.getMessage());
          response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
          return null;
        }
      }
    } catch (UnsupportedPayloadException ex) {
      LOG.error("Attempted to perform conversion and got exception: {}", ex.getMessage());
      response.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
      return null;
    }
    
    /**
     * Add x-v header
     */
    try {
      response.getHeaders().put("x-v",
          List.of(BabelfishVersioner.getVersion(inputBody.getClass()).toString()));
    } catch (Exception e) {
      LOG.error(
          "Identified destination type but was then unable to obtain version again for type of {}",
          inputBody.getClass().getName());
    }
    
    return inputBody;
  }


}
