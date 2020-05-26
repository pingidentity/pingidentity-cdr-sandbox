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
package io.biza.deepthought.banking.api;

import io.biza.babelfish.cdr.models.responses.ResponseBankingTransactionByIdV1;
import io.biza.babelfish.cdr.models.responses.ResponseBankingTransactionListV1;
import io.biza.deepthought.banking.api.delegate.BankingAccountTransactionApiDelegate;
import io.biza.deepthought.banking.requests.RequestListTransactions;
import io.biza.deepthought.shared.Constants;
import io.biza.deepthought.shared.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tags({
    @Tag(name = Constants.TAG_BANKING_NAME, description = Constants.TAG_BANKING_DESCRIPTION),
    @Tag(name = Constants.TAG_ACCOUNTS_NAME,
        description = Constants.TAG_ACCOUNTS_DESCRIPTION)})
@RequestMapping("/v1/banking/accounts/{accountId}/transactions")
public interface BankingAccountTransactionApi {

  default BankingAccountTransactionApiDelegate getDelegate() {
    return new BankingAccountTransactionApiDelegate() {};
  }

  @Operation(summary = "Get Transactions For Account",
      description = "Obtain transactions for a specific account.\n\nSome general notes that apply to all end points that retrieve transactions:\n\n- Where multiple transactions are returned, transactions should be ordered according to effective date in descending order\n- As the date and time for a transaction can alter depending on status and transaction type two separate date/times are included in the payload. There are still some scenarios where neither of these time stamps is available. For the purpose of filtering and ordering it is expected that the data holder will use the “effective” date/time which will be defined as:\n\t\t- Posted date/time if available, then\n\t\t- Execution date/time if available, then\n\t\t- A reasonable date/time nominated by the data holder using internal data structures\n- For transaction amounts it should be assumed that a negative value indicates a reduction of the available balance on the account while a positive value indicates an increase in the available balance on the account\n- For aggregated transactions (ie. groups of sub transactions reported as a single entry for the account) only the aggregated information, with as much consistent information accross the subsidiary transactions as possible, is required to be shared",
      parameters = {@Parameter(name = "x-v", in = ParameterIn.HEADER,
          description = "Version of the API end point requested by the client. Must be set to a positive integer.",
          required = true, schema = @Schema(defaultValue = "1", type = "integer")),
          @Parameter(name = "x-min-v", in = ParameterIn.HEADER,
              description = "Minimum version of the API end point requested by the client. Must be set to a positive integer if provided. ",
              required = false, schema = @Schema(type = "integer")),
          @Parameter(name = "x-fapi-interaction-id", in = ParameterIn.HEADER,
              description = "An RFC4122 UUID used as a correlation id. If provided, the data holder must play back this value in the x-fapi-interaction-id response header. If not provided a [RFC4122] UUID value is required to be provided in the response header to track the interaction.",
              required = false, schema = @Schema(type = "string", format = "uuid")),
          @Parameter(name = "x-fapi-auth-date", in = ParameterIn.HEADER,
              description = "The data/time when the customer last logged in to the data recipient.",
              required = true, schema = @Schema(type = "string", format = "date-time")),
          @Parameter(name = "x-fapi-customer-ip-address", in = ParameterIn.HEADER,
              description = "The customer's original IP address if the customer is currently logged in to the data recipient.",
              required = false, schema = @Schema(type = "string")),
          @Parameter(name = "x-cds-client-headers", in = ParameterIn.HEADER,
              description = "The customer's original standard http headers Base64 encoded, including the original User Agent header, if the customer is currently logged in to the data recipient. ",
              required = false, schema = @Schema(type = "byte")),},
      security = {@SecurityRequirement(name = "cdr-auth",
          scopes = Constants.SECURITY_SCOPE_BANK_ACCOUNT_BASIC_READ)})
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      headers = {@Header(name = "x-v",
          description = "The version of the API end point that the data holder has responded with.",
          required = true),
          @Header(name = "x-fapi-interaction-id",
              description = "An RFC4122 UUID used as a correlation id.", required = true)},
      description = Constants.RESPONSE_SUCCESSFUL_READ, content = @Content(
          schema = @Schema(implementation = ResponseBankingTransactionListV1.class)))})
  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_BANK_TRANSACTIONS_READ)
  default ResponseEntity<ResponseBankingTransactionListV1> getTransactions(
      @NotNull @Valid @PathVariable("accountId") UUID accountId,
      @Valid @RequestParam(name = "oldest-time", required = false) OffsetDateTime oldestTime,
      @Valid @RequestParam(name = "newest-time", required = false) OffsetDateTime newestTime,
      @Valid @RequestParam(name = "min-amount", required = false) BigDecimal minAmount,
      @Valid @RequestParam(name = "max-amount", required = false) BigDecimal maxAmount,
      @Valid @RequestParam(name = "text", required = false) String stringFilter,
      @Valid @RequestParam(name = "page", required = false,
          defaultValue = "1") @Min(1) Integer page,
      @Valid @RequestParam(name = "page-size", required = false,
          defaultValue = "25") Integer pageSize) throws NotFoundException {
    return getDelegate().getTransactions(accountId,
        RequestListTransactions.builder().oldestDateTime(oldestTime).newestDateTime(newestTime)
            .minAmount(minAmount).maxAmount(maxAmount).stringFilter(stringFilter).page(page)
            .pageSize(pageSize).build());
  }

  @Operation(summary = "Get Transaction Detail",
      description = "Obtain detailed information on a transaction for a specific account",
      parameters = {@Parameter(name = "x-v", in = ParameterIn.HEADER,
          description = "Version of the API end point requested by the client. Must be set to a positive integer.",
          required = true, schema = @Schema(defaultValue = "1", type = "integer")),
          @Parameter(name = "x-min-v", in = ParameterIn.HEADER,
              description = "Minimum version of the API end point requested by the client. Must be set to a positive integer if provided. ",
              required = false, schema = @Schema(type = "integer")),
          @Parameter(name = "x-fapi-interaction-id", in = ParameterIn.HEADER,
              description = "An RFC4122 UUID used as a correlation id. If provided, the data holder must play back this value in the x-fapi-interaction-id response header. If not provided a [RFC4122] UUID value is required to be provided in the response header to track the interaction.",
              required = false, schema = @Schema(type = "string", format = "uuid")),
          @Parameter(name = "x-fapi-auth-date", in = ParameterIn.HEADER,
              description = "The data/time when the customer last logged in to the data recipient.",
              required = true, schema = @Schema(type = "string", format = "date-time")),
          @Parameter(name = "x-fapi-customer-ip-address", in = ParameterIn.HEADER,
              description = "The customer's original IP address if the customer is currently logged in to the data recipient.",
              required = false, schema = @Schema(type = "string")),
          @Parameter(name = "x-cds-client-headers", in = ParameterIn.HEADER,
              description = "The customer's original standard http headers Base64 encoded, including the original User Agent header, if the customer is currently logged in to the data recipient. ",
              required = false, schema = @Schema(type = "byte")),},
      security = {@SecurityRequirement(name = "cdr-auth",
          scopes = Constants.SECURITY_SCOPE_BANK_ACCOUNT_BASIC_READ)})
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      headers = {@Header(name = "x-v",
          description = "The version of the API end point that the data holder has responded with.",
          required = true),
          @Header(name = "x-fapi-interaction-id",
              description = "An RFC4122 UUID used as a correlation id.", required = true)},
      description = Constants.RESPONSE_SUCCESSFUL_READ,
      content = @Content(schema = @Schema(implementation = ResponseBankingTransactionByIdV1.class)))})
  @GetMapping(value = "/{transactionId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_BANK_TRANSACTIONS_READ)
  default ResponseEntity<ResponseBankingTransactionByIdV1> getTransactionDetail(
      @NotNull @Valid @PathVariable("accountId") UUID accountId,
      @NotNull @Valid @PathVariable("transactionId") UUID transactionId) throws NotFoundException {
    return getDelegate().getTransactionDetail(accountId, transactionId);
  }
}

