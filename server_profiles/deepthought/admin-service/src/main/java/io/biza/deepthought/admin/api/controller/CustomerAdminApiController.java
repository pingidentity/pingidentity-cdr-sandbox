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
package io.biza.deepthought.admin.api.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import io.biza.deepthought.admin.api.CustomerAdminApi;
import io.biza.deepthought.admin.api.delegate.CustomerAdminApiDelegate;

@Controller
public class CustomerAdminApiController implements CustomerAdminApi {
  private final CustomerAdminApiDelegate delegate;


  public CustomerAdminApiController(@Autowired(required = false) CustomerAdminApiDelegate delegate) {
    this.delegate = Optional.ofNullable(delegate).orElse(new CustomerAdminApiDelegate() {

    });
  }

  @Override
  public CustomerAdminApiDelegate getDelegate() {
    return delegate;
  }
}
