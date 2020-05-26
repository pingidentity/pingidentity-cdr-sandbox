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
package io.biza.deepthought.shared.payloads.cdr;

import java.util.List;
import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankAccountCreditCard;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankAccountLoanAccount;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankAccountTermDeposit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Valid
@Data
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "CDR Bank Account Specifics")
public class CdrBankingAccount {

  @Schema(description = "Term Deposit Details")
  @JsonProperty("termDeposits")
  List<DioBankAccountTermDeposit> termDeposits;
  
  @Schema(description = "Credit Card Details")
  @JsonProperty("creditCards")
  List<DioBankAccountCreditCard> creditCards;
  
  @Schema(description = "Loan Account Details")
  @JsonProperty("loanAccounts")
  List<DioBankAccountLoanAccount> loanAccounts;
}
