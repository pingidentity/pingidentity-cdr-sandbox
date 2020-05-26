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
import io.biza.deepthought.admin.api.delegate.BankAccountAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankAccount;
import io.biza.deepthought.shared.payloads.requests.RequestBankAccount;
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

@Tag(name = Labels.TAG_BANK_ACCOUNT_NAME, description = Labels.TAG_BANK_ACCOUNT_DESCRIPTION)
@RequestMapping("/v1/brand/{brandId}/branch/{branchId}/bank-account")
public interface BankAccountAdminApi {

  default BankAccountAdminApiDelegate getDelegate() {
    return new BankAccountAdminApiDelegate() {};
  }

  @Operation(summary = "List all Bank Accounts", description = "List all Bank Accounts",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_BANK_ACCOUNT_READ})})
  @ApiResponses(value = {@ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
      description = Labels.RESPONSE_SUCCESSFUL_LIST, content = @Content(
          array = @ArraySchema(schema = @Schema(implementation = DioBankAccount.class))))})
  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_BANK_ACCOUNT_READ)
  default ResponseEntity<List<DioBankAccount>> listBankAccounts(
      @NotNull @Valid @PathVariable("brandId") UUID brandId, @NotNull @Valid @PathVariable("branchId") UUID branchId) {
    return getDelegate().listBankAccounts(brandId, branchId);
  }
  
  @Operation(summary = "Get a single Bank Account", description = "Returns a single bank account entry",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_BANK_ACCOUNT_READ})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
          description = Labels.RESPONSE_SUCCESSFUL_READ,
          content = @Content(schema = @Schema(implementation = DioBankAccount.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_NOT_FOUND,
          description = Labels.RESPONSE_OBJECT_NOT_FOUND)})
  @GetMapping(value = "/{bankAccountId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_BANK_ACCOUNT_READ)
  default ResponseEntity<DioBankAccount> getBankAccount(
      @NotNull @Valid @PathVariable("brandId") UUID brandId, @NotNull @Valid @PathVariable("branchId") UUID branchId,
      @NotNull @Valid @PathVariable("bankAccountId") UUID bankAccountId) {
    return getDelegate().getBankAccount(brandId, branchId, bankAccountId);
  }

  @Operation(summary = "Create a Bank Account", description = "Creates and Returns a new Bank Account",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_BANK_ACCOUNT_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_CREATED,
          description = Labels.RESPONSE_SUCCESSFUL_CREATE,
          content = @Content(schema = @Schema(implementation = DioBankAccount.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = Labels.RESPONSE_INPUT_VALIDATION_ERROR, content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = ValidationListException.class))))})
  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_BANK_ACCOUNT_WRITE)
  default ResponseEntity<DioBankAccount> createBankAccount(
      @NotNull @Valid @PathVariable("brandId") UUID brandId, @NotNull @Valid @PathVariable("branchId") UUID branchId,
      @NotNull @RequestBody RequestBankAccount bankAccountRequest) throws ValidationListException {
    return getDelegate().createBankAccount(brandId, branchId, bankAccountRequest);
  }

  @Operation(summary = "Update a single Bank Account",
      description = "Updates and Returns an existing BankAccount",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_BANK_ACCOUNT_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
          description = Labels.RESPONSE_SUCCESSFUL_UPDATE,
          content = @Content(schema = @Schema(implementation = DioBankAccount.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = Labels.RESPONSE_INPUT_VALIDATION_ERROR, content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = ValidationListException.class))))})
  @PutMapping(path = "/{bankAccountId}", consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_BANK_ACCOUNT_WRITE)
  default ResponseEntity<DioBankAccount> updateBankAccount(
      @NotNull @Valid @PathVariable("brandId") UUID brandId, @NotNull @Valid @PathVariable("branchId") UUID branchId,
      @NotNull @Valid @PathVariable("bankAccountId") UUID bankAccountId,
      @NotNull @RequestBody RequestBankAccount bankAccountRequest) throws ValidationListException {
    return getDelegate().updateBankAccount(brandId, branchId, bankAccountId, bankAccountRequest);
  }

  @Operation(summary = "Delete a single Bank Account", description = "Deletes a Bank Account",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_BANK_ACCOUNT_WRITE})})

  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
          description = Labels.RESPONSE_SUCCESSFUL_DELETE,
          content = @Content(schema = @Schema(implementation = DioBankAccount.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_NOT_FOUND,
          description = Labels.RESPONSE_OBJECT_NOT_FOUND)})
  @DeleteMapping(path = "/{bankAccountId}")
  @PreAuthorize(Labels.OAUTH2_SCOPE_BANK_ACCOUNT_WRITE)
  default ResponseEntity<Void> deleteBankAccount(@NotNull @Valid @PathVariable("brandId") UUID brandId, @NotNull @Valid @PathVariable("branchId") UUID branchId,
      @NotNull @Valid @PathVariable("bankAccountId") UUID bankAccountId) {
    return getDelegate().deleteBankAccount(brandId, branchId, bankAccountId);
  }


}
