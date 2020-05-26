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
import io.biza.deepthought.admin.api.delegate.CustomerScheduledPaymentAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.shared.payloads.dio.banking.DioCustomerScheduledPayment;
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

@Tag(name = Labels.TAG_BANK_SCHEDULED_PAYMENT_NAME, description = Labels.TAG_BANK_SCHEDULED_PAYMENT_DESCRIPTION)
@RequestMapping("/v1/brand/{brandId}/customer/{customerId}/scheduled-payment")
public interface CustomerScheduledPaymentAdminApi {

  default CustomerScheduledPaymentAdminApiDelegate getDelegate() {
    return new CustomerScheduledPaymentAdminApiDelegate() {};
  }

  @Operation(summary = "List all Scheduled Payments", description = "List all Scheduled Payments",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_SCHEDULED_PAYMENT_READ})})
  @ApiResponses(value = {@ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
      description = Labels.RESPONSE_SUCCESSFUL_LIST, content = @Content(
          array = @ArraySchema(schema = @Schema(implementation = DioCustomerScheduledPayment.class))))})
  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_SCHEDULED_PAYMENT_READ)
  default ResponseEntity<List<DioCustomerScheduledPayment>> listScheduledPayments(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("customerId") UUID customerId) {
    return getDelegate().listScheduledPayments(brandId, customerId);
  }

  @Operation(summary = "Get a single Scheduled Payment", description = "Returns a single scheduledPayment entry",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_SCHEDULED_PAYMENT_READ})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
          description = Labels.RESPONSE_SUCCESSFUL_READ,
          content = @Content(schema = @Schema(implementation = DioCustomerScheduledPayment.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_NOT_FOUND,
          description = Labels.RESPONSE_OBJECT_NOT_FOUND)})
  @GetMapping(value = "/{scheduledPaymentId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_SCHEDULED_PAYMENT_READ)
  default ResponseEntity<DioCustomerScheduledPayment> getScheduledPayment(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("customerId") UUID customerId,
      @NotNull @Valid @PathVariable("scheduledPaymentId") UUID scheduledPaymentId) {
    return getDelegate().getScheduledPayment(brandId, customerId, scheduledPaymentId);
  }

  @Operation(summary = "Create a Scheduled Payment", description = "Creates and Returns a new Scheduled Payment",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_SCHEDULED_PAYMENT_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_CREATED,
          description = Labels.RESPONSE_SUCCESSFUL_CREATE,
          content = @Content(schema = @Schema(implementation = DioCustomerScheduledPayment.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = Labels.RESPONSE_INPUT_VALIDATION_ERROR,
          content = @Content(array = @ArraySchema(
              schema = @Schema(implementation = ValidationListException.class))))})
  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_SCHEDULED_PAYMENT_WRITE)
  default ResponseEntity<DioCustomerScheduledPayment> createScheduledPayment(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("customerId") UUID customerId,
      @NotNull @RequestBody DioCustomerScheduledPayment scheduledPayment) throws ValidationListException {
    return getDelegate().createScheduledPayment(brandId, customerId, scheduledPayment);
  }

  @Operation(summary = "Update a single Scheduled Payment",
      description = "Updates and Returns an existing Scheduled Payment",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_SCHEDULED_PAYMENT_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
          description = Labels.RESPONSE_SUCCESSFUL_UPDATE,
          content = @Content(schema = @Schema(implementation = DioCustomerScheduledPayment.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = Labels.RESPONSE_INPUT_VALIDATION_ERROR,
          content = @Content(array = @ArraySchema(
              schema = @Schema(implementation = ValidationListException.class))))})
  @PutMapping(path = "/{scheduledPaymentId}", consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_SCHEDULED_PAYMENT_WRITE)
  default ResponseEntity<DioCustomerScheduledPayment> updateScheduledPayment(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("customerId") UUID customerId,
      @NotNull @Valid @PathVariable("scheduledPaymentId") UUID scheduledPaymentId,
      @NotNull @RequestBody DioCustomerScheduledPayment scheduledPayment) throws ValidationListException {
    return getDelegate().updateScheduledPayment(brandId, customerId, scheduledPaymentId, scheduledPayment);
  }

  @Operation(summary = "Delete a single Scheduled Payment", description = "Deletes a Scheduled Payment",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_SCHEDULED_PAYMENT_WRITE})})

  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
          description = Labels.RESPONSE_SUCCESSFUL_DELETE,
          content = @Content(schema = @Schema(implementation = DioCustomerScheduledPayment.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_NOT_FOUND,
          description = Labels.RESPONSE_OBJECT_NOT_FOUND)})
  @DeleteMapping(path = "/{scheduledPaymentId}")
  @PreAuthorize(Labels.OAUTH2_SCOPE_SCHEDULED_PAYMENT_WRITE)
  default ResponseEntity<Void> deleteScheduledPayment(@NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("customerId") UUID customerId,
      @NotNull @Valid @PathVariable("scheduledPaymentId") UUID scheduledPaymentId) {
    return getDelegate().deleteScheduledPayment(brandId, customerId, scheduledPaymentId);
  }

}
