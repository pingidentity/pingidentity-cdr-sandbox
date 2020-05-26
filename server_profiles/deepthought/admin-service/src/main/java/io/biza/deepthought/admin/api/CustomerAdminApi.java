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
import io.biza.deepthought.admin.api.delegate.CustomerAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.shared.payloads.dio.common.DioCustomer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Tag(name = Labels.TAG_CUSTOMER_NAME, description = Labels.TAG_CUSTOMER_DESCRIPTION)
@RequestMapping("/v1/brand/{brandId}/customer")
public interface CustomerAdminApi {

  default CustomerAdminApiDelegate getDelegate() {
    return new CustomerAdminApiDelegate() {};
  }

  @Operation(summary = "List all Customers", description = "List all Customers",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_CUSTOMER_READ})})
  @ApiResponses(value = {@ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
      description = Labels.RESPONSE_SUCCESSFUL_LIST, content = @Content(
          array = @ArraySchema(schema = @Schema(implementation = DioCustomer.class))))})
  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_CUSTOMER_READ)
  default ResponseEntity<List<DioCustomer>> listCustomers(
      @NotNull @Valid @PathVariable("brandId") UUID brandId) {
    return getDelegate().listCustomers(brandId);
  }
  
  @Operation(summary = "Get a single Customer", description = "Returns a single customer entry",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_CUSTOMER_READ})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
          description = Labels.RESPONSE_SUCCESSFUL_READ,
          content = @Content(schema = @Schema(implementation = DioCustomer.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_NOT_FOUND,
          description = Labels.RESPONSE_OBJECT_NOT_FOUND)})
  @GetMapping(value = "/{customerId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_CUSTOMER_READ)
  default ResponseEntity<DioCustomer> getCustomer(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("customerId") UUID customerId) {
    return getDelegate().getCustomer(brandId, customerId);
  }

  @Operation(summary = "Create a Customer", description = "Creates and Returns a new Customer",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_CUSTOMER_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_CREATED,
          description = Labels.RESPONSE_SUCCESSFUL_CREATE,
          content = @Content(schema = @Schema(implementation = DioCustomer.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = Labels.RESPONSE_INPUT_VALIDATION_ERROR, content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = ValidationListException.class))))})
  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_CUSTOMER_WRITE)
  default ResponseEntity<DioCustomer> createCustomer(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @RequestBody DioCustomer customer) throws ValidationListException {
    return getDelegate().createCustomer(brandId, customer);
  }

  @Operation(summary = "Update a single Customer",
      description = "Updates and Returns an existing Customer",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_CUSTOMER_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
          description = Labels.RESPONSE_SUCCESSFUL_UPDATE,
          content = @Content(schema = @Schema(implementation = DioCustomer.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = Labels.RESPONSE_INPUT_VALIDATION_ERROR, content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = ValidationListException.class))))})
  @PutMapping(path = "/{customerId}", consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_CUSTOMER_WRITE)
  default ResponseEntity<DioCustomer> updateCustomer(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("customerId") UUID customerId,
      @NotNull @RequestBody DioCustomer customer) throws ValidationListException {
    return getDelegate().updateCustomer(brandId, customerId, customer);
  }

  @Operation(summary = "Delete a single Customer", description = "Deletes a Customer",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_CUSTOMER_WRITE})})

  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
          description = Labels.RESPONSE_SUCCESSFUL_DELETE,
          content = @Content(schema = @Schema(implementation = DioCustomer.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_NOT_FOUND,
          description = Labels.RESPONSE_OBJECT_NOT_FOUND)})
  @DeleteMapping(path = "/{customerId}")
  @PreAuthorize(Labels.OAUTH2_SCOPE_CUSTOMER_WRITE)
  default ResponseEntity<Void> deleteCustomer(@NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("customerId") UUID customerId) {
    return getDelegate().deleteCustomer(brandId, customerId);
  }

}
