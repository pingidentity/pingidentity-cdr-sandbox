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
package io.biza.deepthought.discovery.api;

import io.biza.babelfish.cdr.models.responses.ResponseCommonDiscoveryOutagesListV1;
import io.biza.babelfish.cdr.models.responses.ResponseCommonDiscoveryStatusV1;
import io.biza.deepthought.discovery.api.delegate.CommonDiscoveryApiDelegate;
import io.biza.deepthought.shared.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tags({
  @Tag(name = Constants.TAG_COMMON_NAME, description = Constants.TAG_COMMON_DESCRIPTION),
  @Tag(name = Constants.TAG_DISCOVERY_NAME, description = Constants.TAG_DISCOVERY_DESCRIPTION)
})
@RequestMapping("/v1/discovery")
public interface CommonDiscoveryApi {

  default CommonDiscoveryApiDelegate getDelegate() {
    return new CommonDiscoveryApiDelegate() {};
  }

  @Operation(summary = "Get Status",
      description = "Obtain a health check status for the implementation")
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = Constants.RESPONSE_SUCCESSFUL_READ,
      content = @Content(schema = @Schema(implementation = ResponseCommonDiscoveryStatusV1.class)))})
  @GetMapping(value = "/status", produces = {MediaType.APPLICATION_JSON_VALUE})
  default ResponseEntity<ResponseCommonDiscoveryStatusV1> getStatus() {
    return getDelegate().getStatus();
  }
  
  @Operation(summary = "Get Outages",
      description = "Obtain a list of scheduled outages for the implementation")
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = Constants.RESPONSE_SUCCESSFUL_READ,
      content = @Content(schema = @Schema(implementation = ResponseCommonDiscoveryOutagesListV1.class)))})
  @GetMapping(value = "/outages", produces = {MediaType.APPLICATION_JSON_VALUE})
  default ResponseEntity<ResponseCommonDiscoveryOutagesListV1> getOutages() {
    return getDelegate().getOutages();
  }

}

