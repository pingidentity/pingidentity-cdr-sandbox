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
import io.biza.deepthought.admin.api.BrandBranchAdminApi;
import io.biza.deepthought.admin.api.delegate.BrandBranchAdminApiDelegate;

@Controller
public class BrandBranchAdminApiController implements BrandBranchAdminApi {
  private final BrandBranchAdminApiDelegate delegate;


  public BrandBranchAdminApiController(@Autowired(required = false) BrandBranchAdminApiDelegate delegate) {
    this.delegate = Optional.ofNullable(delegate).orElse(new BrandBranchAdminApiDelegate() {

    });
  }

  @Override
  public BrandBranchAdminApiDelegate getDelegate() {
    return delegate;
  }
}
