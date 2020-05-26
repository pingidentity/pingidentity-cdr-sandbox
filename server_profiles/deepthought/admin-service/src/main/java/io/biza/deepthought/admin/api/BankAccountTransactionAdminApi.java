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
import io.biza.deepthought.admin.api.delegate.BankAccountTransactionAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankAccountTransaction;
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

@Tag(name = Labels.TAG_BANK_TRANSACTION_NAME, description = Labels.TAG_BANK_TRANSACTION_DESCRIPTION)
@RequestMapping("/v1/brand/{brandId}/branch/{branchId}/bank-account/{accountId}/transaction")
public interface BankAccountTransactionAdminApi {

  default BankAccountTransactionAdminApiDelegate getDelegate() {
    return new BankAccountTransactionAdminApiDelegate() {};
  }

  @Operation(summary = "List Transactions",
      description = "List Transactions belonging to a Single Account",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_TRANSACTION_READ})})
  @ApiResponses(value = {@ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
      description = Labels.RESPONSE_SUCCESSFUL_LIST, content = @Content(
          array = @ArraySchema(schema = @Schema(implementation = DioBankAccountTransaction.class))))})
  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_TRANSACTION_READ)
  default ResponseEntity<List<DioBankAccountTransaction>> listTransactions(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("branchId") UUID branchId,
      @NotNull @Valid @PathVariable("accountId") UUID accountId) {
    return getDelegate().listTransactions(brandId, branchId, accountId);
  }

  @Operation(summary = "Get a single Bank Transaction",
      description = "Returns a single transaction detail",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_TRANSACTION_READ})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
          description = Labels.RESPONSE_SUCCESSFUL_READ,
          content = @Content(schema = @Schema(implementation = DioBankAccountTransaction.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_NOT_FOUND,
          description = Labels.RESPONSE_OBJECT_NOT_FOUND)})
  @GetMapping(value = "/{transactionId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_TRANSACTION_READ)
  default ResponseEntity<DioBankAccountTransaction> getTransaction(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("branchId") UUID branchId,
      @NotNull @Valid @PathVariable("accountId") UUID accountId,
      @NotNull @Valid @PathVariable("transactionId") UUID transactionId) {
    return getDelegate().getTransaction(brandId, branchId, accountId, transactionId);
  }

  @Operation(summary = "Create a Transaction",
      description = "Creates and Returns a new Bank Account Transaction",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_TRANSACTION_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_CREATED,
          description = Labels.RESPONSE_SUCCESSFUL_CREATE,
          content = @Content(schema = @Schema(implementation = DioBankAccountTransaction.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = Labels.RESPONSE_INPUT_VALIDATION_ERROR,
          content = @Content(array = @ArraySchema(
              schema = @Schema(implementation = ValidationListException.class))))})
  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_TRANSACTION_WRITE)
  default ResponseEntity<DioBankAccountTransaction> createTransaction(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("branchId") UUID branchId,
      @NotNull @Valid @PathVariable("accountId") UUID accountId,
      @NotNull @RequestBody DioBankAccountTransaction bankAccountRequest) throws ValidationListException {
    return getDelegate().createTransaction(brandId, branchId, accountId, bankAccountRequest);
  }

  @Operation(summary = "Update a single Transaction",
      description = "Updates and Returns an existing Bank Account Transaction",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_TRANSACTION_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_OK,
          description = Labels.RESPONSE_SUCCESSFUL_UPDATE,
          content = @Content(schema = @Schema(implementation = DioBankAccountTransaction.class))),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = Labels.RESPONSE_INPUT_VALIDATION_ERROR,
          content = @Content(array = @ArraySchema(
              schema = @Schema(implementation = ValidationListException.class))))})
  @PutMapping(path = "/{transactionId}", consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Labels.OAUTH2_SCOPE_TRANSACTION_WRITE)
  default ResponseEntity<DioBankAccountTransaction> updateTransaction(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("branchId") UUID branchId,
      @NotNull @Valid @PathVariable("accountId") UUID accountId,
      @NotNull @Valid @PathVariable("transactionId") UUID transactionId,
      @NotNull @RequestBody DioBankAccountTransaction transactionData) throws ValidationListException {
    return getDelegate().updateTransaction(brandId, branchId, accountId, transactionId, transactionData);
  }

  @Operation(summary = "Delete a single Transaction", description = "Deletes a single Bank Account Transaction",
      security = {@SecurityRequirement(name = Labels.SECURITY_SCHEME_NAME,
          scopes = {Labels.SECURITY_SCOPE_TRANSACTION_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_NO_CONTENT,
          description = Labels.RESPONSE_SUCCESSFUL_DELETE),
      @ApiResponse(responseCode = Labels.RESPONSE_CODE_NOT_FOUND,
          description = Labels.RESPONSE_OBJECT_NOT_FOUND)})
  @DeleteMapping(path = "/{transactionId}")
  @PreAuthorize(Labels.OAUTH2_SCOPE_TRANSACTION_WRITE)
  default ResponseEntity<Void> deleteTransaction(
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("branchId") UUID branchId,
      @NotNull @Valid @PathVariable("accountId") UUID accountId,
      @NotNull @Valid @PathVariable("transactionId") UUID transactionId) {
    return getDelegate().deleteTransaction(brandId, branchId, accountId, transactionId);
  }


}
