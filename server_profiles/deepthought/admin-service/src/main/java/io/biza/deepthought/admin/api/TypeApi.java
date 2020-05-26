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
package io.biza.deepthought.admin.api;

import io.biza.deepthought.admin.Labels;
import io.biza.deepthought.admin.api.delegate.TypeApiDelegate;
import io.biza.deepthought.shared.payloads.ResponseGetTypes;
import io.biza.deepthought.shared.payloads.dio.enumerations.FormFieldType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Tag(name = Labels.TAG_TYPE_NAME, description = Labels.TAG_TYPE_DESCRIPTION)
@RequestMapping("/v1/type")
public interface TypeApi {

  default TypeApiDelegate getDelegate() {
    return new TypeApiDelegate() {};
  }

  @Operation(summary = "Get a value/label set of a specified type",
      description = "Returns a value/label set of a specified type")
  @ApiResponses(value = {@ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
      description = "List of Value/Label for use in Form selects",
      content = @Content(schema = @Schema(implementation = ResponseGetTypes.class)))})
  @RequestMapping(method = RequestMethod.GET, params = {"labelTypes"})
  default ResponseEntity<ResponseGetTypes> getTypes(
      @Parameter(name = "labelTypes", description = "Set of Value Label Type lists to get",
          required = true, in = ParameterIn.QUERY) @RequestParam List<FormFieldType> labelTypes) {
    return getDelegate().getTypes(labelTypes);
  }

}

