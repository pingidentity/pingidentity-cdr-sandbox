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

import io.biza.babelfish.cdr.enumerations.BankingPayeeTypeWithAll;
import io.biza.babelfish.cdr.models.responses.ResponseBankingPayeeByIdV1;
import io.biza.babelfish.cdr.models.responses.ResponseBankingPayeeListV1;
import io.biza.deepthought.banking.api.delegate.BankingPayeeApiDelegate;
import io.biza.deepthought.banking.requests.RequestListPayees;
import io.biza.deepthought.shared.Constants;
import io.biza.deepthought.shared.exception.InvalidSubjectException;
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
    @Tag(name = Constants.TAG_PAYEES_NAME, description = Constants.TAG_PAYEES_DESCRIPTION)})
@RequestMapping("/v1/banking/payees")
public interface BankingPayeeApi {

  default BankingPayeeApiDelegate getDelegate() {
    return new BankingPayeeApiDelegate() {};
  }

  @Operation(summary = "Get Payees", description = "Obtain a list of pre-registered payees",
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
      description = Constants.RESPONSE_SUCCESSFUL_READ,
      headers = {@Header(name = "x-v",
          description = "The version of the API end point that the data holder has responded with.",
          required = true),
          @Header(name = "x-fapi-interaction-id",
              description = "An RFC4122 UUID used as a correlation id.", required = true)},
      content = @Content(schema = @Schema(implementation = ResponseBankingPayeeListV1.class)))})
  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_BANK_BANK_PAYEES_READ)
  default ResponseEntity<ResponseBankingPayeeListV1> listPayees(
      @Valid @RequestParam(name = "type", required = false,
          defaultValue = "ALL") BankingPayeeTypeWithAll payeeType,
      @Valid @RequestParam(name = "page", required = false,
          defaultValue = "1") @Min(1) Integer page,
      @Valid @RequestParam(name = "page-size", required = false,
          defaultValue = "25") Integer pageSize) throws InvalidSubjectException {
    return getDelegate().listPayees(
        RequestListPayees.builder().payeeType(payeeType).page(page).pageSize(pageSize).build());
  }

  @Operation(summary = "Get Payee Detail",
      description = "Obtain detailed information on a single account",
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
      description = Constants.RESPONSE_SUCCESSFUL_READ,
      headers = {@Header(name = "x-v",
          description = "The version of the API end point that the data holder has responded with.",
          required = true),
          @Header(name = "x-fapi-interaction-id",
              description = "An RFC4122 UUID used as a correlation id.", required = true)},
      content = @Content(schema = @Schema(implementation = ResponseBankingPayeeByIdV1.class)))})
  @GetMapping(value = "/{payeeId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_BANK_BANK_PAYEES_READ)
  default ResponseEntity<ResponseBankingPayeeByIdV1> getPayeeDetail(
      @NotNull @Valid @PathVariable("payeeId") UUID payeeId) throws NotFoundException, InvalidSubjectException {
    return getDelegate().getPayeeDetail(payeeId);
  }
}

