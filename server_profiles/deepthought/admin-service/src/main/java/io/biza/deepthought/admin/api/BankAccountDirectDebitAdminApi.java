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
import io.biza.deepthought.admin.api.delegate.BankAccountDirectDebitAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankAccountDirectDebit;
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

@Tag(name = Labels.TAG_BANK_DIRECT_DEBIT_NAME, description = Labels.TAG_BANK_DIRECT_DEBIT_DESCRIPTION)
@RequestMapping("/v1/brand/{brandId}/branch/{branchId}/bank-account/{accountId}/direct-debit")
public interface BankAccountDirectDebitAdminApi {

  default BankAccountDirectDebitAdminApiDelegate getDelegate() {
    return new BankAccountDirectDebitAdminApiDelegate() {};
  }

  @Operation(summary = "List Direct Debits",
      description = "List Direct Debits belonging to a Single Account",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_DIRECT_DEBIT_READ})})
  @ApiResponses(value = {@ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
      description = Labels.RESPONSE_SUCCESSFUL_LIST, content = @Content(
          array = @ArraySchema(schema = @Schema(implementation = DioBankAccountDirectDebit.class))))})
  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_DIRECT_DEBIT_READ)
  default ResponseEntity<List<DioBankAccountDirectDebit>> listDirectDebits(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("branchId") UUID branchId,
      @NotNull @Valid @PathVariable("accountId") UUID accountId) {
    return getDelegate().listDirectDebits(brandId, branchId, accountId);
  }

  @Operation(summary = "Get a single Bank DirectDebit",
      description = "Returns a single direct-debit detail",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_DIRECT_DEBIT_READ})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
          description = Labels.RESPONSE_SUCCESSFUL_READ,
          content = @Content(schema = @Schema(implementation = DioBankAccountDirectDebit.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_NOT_FOUND,
          description = Labels.RESPONSE_OBJECT_NOT_FOUND)})
  @GetMapping(value = "/{directDebitId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_DIRECT_DEBIT_READ)
  default ResponseEntity<DioBankAccountDirectDebit> getDirectDebit(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("branchId") UUID branchId,
      @NotNull @Valid @PathVariable("accountId") UUID accountId,
      @NotNull @Valid @PathVariable("directDebitId") UUID directDebitId) {
    return getDelegate().getDirectDebit(brandId, branchId, accountId, directDebitId);
  }

  @Operation(summary = "Create a DirectDebit",
      description = "Creates and Returns a new Bank Account DirectDebit",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_DIRECT_DEBIT_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_CREATED,
          description = Labels.RESPONSE_SUCCESSFUL_CREATE,
          content = @Content(schema = @Schema(implementation = DioBankAccountDirectDebit.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = Labels.RESPONSE_INPUT_VALIDATION_ERROR,
          content = @Content(array = @ArraySchema(
              schema = @Schema(implementation = ValidationListException.class))))})
  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_DIRECT_DEBIT_WRITE)
  default ResponseEntity<DioBankAccountDirectDebit> createDirectDebit(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("branchId") UUID branchId,
      @NotNull @Valid @PathVariable("accountId") UUID accountId,
      @NotNull @RequestBody DioBankAccountDirectDebit bankAccountRequest) throws ValidationListException {
    return getDelegate().createDirectDebit(brandId, branchId, accountId, bankAccountRequest);
  }

  @Operation(summary = "Update a single DirectDebit",
      description = "Updates and Returns an existing Bank Account DirectDebit",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_DIRECT_DEBIT_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
          description = Labels.RESPONSE_SUCCESSFUL_UPDATE,
          content = @Content(schema = @Schema(implementation = DioBankAccountDirectDebit.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = Labels.RESPONSE_INPUT_VALIDATION_ERROR,
          content = @Content(array = @ArraySchema(
              schema = @Schema(implementation = ValidationListException.class))))})
  @PutMapping(path = "/{directDebitId}", consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_DIRECT_DEBIT_WRITE)
  default ResponseEntity<DioBankAccountDirectDebit> updateDirectDebit(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("branchId") UUID branchId,
      @NotNull @Valid @PathVariable("accountId") UUID accountId,
      @NotNull @Valid @PathVariable("directDebitId") UUID directDebitId,
      @NotNull @RequestBody DioBankAccountDirectDebit directDebitData) throws ValidationListException {
    return getDelegate().updateDirectDebit(brandId, branchId, accountId, directDebitId, directDebitData);
  }

  @Operation(summary = "Delete a single DirectDebit", description = "Deletes a single Bank Account DirectDebit",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_DIRECT_DEBIT_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_NO_CONTENT,
          description = Labels.RESPONSE_SUCCESSFUL_DELETE),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_NOT_FOUND,
          description = Labels.RESPONSE_OBJECT_NOT_FOUND)})
  @DeleteMapping(path = "/{directDebitId}")
  @PreAuthorize(Labels.OAUTH2_SCOPE_DIRECT_DEBIT_WRITE)
  default ResponseEntity<Void> deleteDirectDebit(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("branchId") UUID branchId,
      @NotNull @Valid @PathVariable("accountId") UUID accountId,
      @NotNull @Valid @PathVariable("directDebitId") UUID directDebitId) {
    return getDelegate().deleteDirectDebit(brandId, branchId, accountId, directDebitId);
  }


}
