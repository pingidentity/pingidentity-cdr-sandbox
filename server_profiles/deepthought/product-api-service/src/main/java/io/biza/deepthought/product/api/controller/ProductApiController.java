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
package io.biza.deepthought.product.api.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import io.biza.deepthought.product.api.ProductApi;
import io.biza.deepthought.product.api.delegate.ProductApiDelegate;

@Controller
public class ProductApiController implements ProductApi {
  private final ProductApiDelegate delegate;


  public ProductApiController(@Autowired(required = false) ProductApiDelegate delegate) {
    this.delegate = Optional.ofNullable(delegate).orElse(new ProductApiDelegate() {

    });
  }

  @Override
  public ProductApiDelegate getDelegate() {
    return delegate;
  }
}
