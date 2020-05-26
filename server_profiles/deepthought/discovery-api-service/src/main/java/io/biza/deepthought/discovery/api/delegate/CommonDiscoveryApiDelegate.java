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
package io.biza.deepthought.discovery.api.delegate;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import io.biza.babelfish.cdr.models.responses.ResponseCommonDiscoveryOutagesListV1;
import io.biza.babelfish.cdr.models.responses.ResponseCommonDiscoveryStatusV1;

public interface CommonDiscoveryApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }
  
  default ResponseEntity<ResponseCommonDiscoveryStatusV1> getStatus() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<ResponseCommonDiscoveryOutagesListV1> getOutages() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
