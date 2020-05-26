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
import io.biza.deepthought.admin.api.delegate.ProductRateDepositAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.shared.payloads.dio.product.DioProductRateDeposit;
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

@Tag(name = Labels.TAG_PRODUCT_NAME, description = Labels.TAG_PRODUCT_DESCRIPTION)
@RequestMapping("/v1/brand/{brandId}/product/{productId}/deposit-rate")
public interface ProductRateDepositAdminApi {

  default ProductRateDepositAdminApiDelegate getDelegate() {
    return new ProductRateDepositAdminApiDelegate() {};
  }

  @Operation(summary = "List all Product Deposit Rates", description = "List all Product Rates",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_PRODUCT_READ})})
  @ApiResponses(value = {@ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
      description = Labels.RESPONSE_SUCCESSFUL_LIST, content = @Content(
          array = @ArraySchema(schema = @Schema(implementation = DioProductRateDeposit.class))))})
  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_PRODUCT_READ)
  default ResponseEntity<List<DioProductRateDeposit>> listProductRateDeposits(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("productId") UUID productId) {
    return getDelegate().listProductRateDeposits(brandId, productId);
  }

  @Operation(summary = "Get a single Product Deposit Rate",
      description = "Returns a single product rate entry",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_PRODUCT_READ})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
          description = Labels.RESPONSE_SUCCESSFUL_READ,
          content = @Content(schema = @Schema(implementation = DioProductRateDeposit.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_NOT_FOUND,
          description = Labels.RESPONSE_OBJECT_NOT_FOUND)})
  @GetMapping(value = "/{rateId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_PRODUCT_READ)
  default ResponseEntity<DioProductRateDeposit> getProductRateDeposit(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("productId") UUID productId,
      @NotNull @Valid @PathVariable("rateId") UUID rateId) {
    return getDelegate().getProductRateDeposit(brandId, productId, rateId);
  }

  @Operation(summary = "Create a Product Deposit Rate",
      description = "Creates and Returns a new Product Deposit Rate",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_PRODUCT_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
          description = Labels.RESPONSE_SUCCESSFUL_CREATE,
          content = @Content(schema = @Schema(implementation = DioProductRateDeposit.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = Labels.RESPONSE_INPUT_VALIDATION_ERROR, content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = ValidationListException.class))))})
  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_PRODUCT_WRITE)
  default ResponseEntity<DioProductRateDeposit> createProductRateDeposit(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("productId") UUID productId,
      @NotNull @RequestBody DioProductRateDeposit rate) throws ValidationListException {
    return getDelegate().createProductRateDeposit(brandId, productId, rate);
  }

  @Operation(summary = "Update a single Product Deposit Rate",
      description = "Updates and Returns an existing Product Deposit Rate",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_PRODUCT_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
          description = Labels.RESPONSE_SUCCESSFUL_UPDATE,
          content = @Content(schema = @Schema(implementation = DioProductRateDeposit.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = Labels.RESPONSE_INPUT_VALIDATION_ERROR, content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = ValidationListException.class))))})
  @PutMapping(path = "/{rateId}", consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_PRODUCT_WRITE)
  default ResponseEntity<DioProductRateDeposit> updateProductRateDeposit(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("productId") UUID productId,
      @NotNull @Valid @PathVariable("rateId") UUID rateId,
      @NotNull @RequestBody DioProductRateDeposit rate) throws ValidationListException {
    return getDelegate().updateProductRateDeposit(brandId, productId, rateId, rate);
  }

  @Operation(summary = "Delete a single Product Deposit Rate", description = "Deletes a Product Deposit Rate",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_PRODUCT_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_NO_CONTENT,
          description = Labels.RESPONSE_SUCCESSFUL_DELETE),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_NOT_FOUND,
          description = Labels.RESPONSE_OBJECT_NOT_FOUND)})
  @DeleteMapping(path = "/{rateId}")
  @PreAuthorize(Labels.OAUTH2_SCOPE_PRODUCT_WRITE)
  default ResponseEntity<Void> deleteProductRateDeposit(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("productId") UUID productId,
      @NotNull @Valid @PathVariable("rateId") UUID rateId) {
    return getDelegate().deleteProductRateDeposit(brandId, productId, rateId);
  }


}
