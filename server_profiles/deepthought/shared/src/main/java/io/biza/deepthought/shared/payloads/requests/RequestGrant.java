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
package io.biza.deepthought.shared.payloads.requests;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Valid
@ToString
@EqualsAndHashCode
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request for create or update of Grant")
public class RequestGrant {

  @Schema(description = "Token Subject Identifier", required = true)
  @NotNull
  @JsonProperty(value = "subject")
  String subject;

  @Schema(
      description = "How long should the grant remain (in seconds), if supplied in update will extend by this length, if not supplied will default to 30 days",
      defaultValue = "2592000")
  @JsonProperty(value = "length")
  Integer length;
  
  @Schema(description = "Customer Accounts with Permissions")
  @JsonProperty(value = "customerAccounts")
  @NotNull
  List<RequestGrantCustomerAccount> customerAccounts;

}
