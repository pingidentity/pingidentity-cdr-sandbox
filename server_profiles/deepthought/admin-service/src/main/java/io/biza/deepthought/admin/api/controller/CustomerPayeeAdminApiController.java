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
import io.biza.deepthought.admin.api.CustomerPayeeAdminApi;
import io.biza.deepthought.admin.api.delegate.CustomerPayeeAdminApiDelegate;

@Controller
public class CustomerPayeeAdminApiController implements CustomerPayeeAdminApi {
  private final CustomerPayeeAdminApiDelegate delegate;


  public CustomerPayeeAdminApiController(@Autowired(required = false) CustomerPayeeAdminApiDelegate delegate) {
    this.delegate = Optional.ofNullable(delegate).orElse(new CustomerPayeeAdminApiDelegate() {

    });
  }

  @Override
  public CustomerPayeeAdminApiDelegate getDelegate() {
    return delegate;
  }
}
