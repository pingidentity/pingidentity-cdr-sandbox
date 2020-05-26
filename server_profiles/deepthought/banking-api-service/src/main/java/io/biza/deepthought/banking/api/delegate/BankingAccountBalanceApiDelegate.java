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
package io.biza.deepthought.banking.api.delegate;

import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import io.biza.babelfish.cdr.models.responses.ResponseBankingAccountsBalanceByIdV1;
import io.biza.babelfish.cdr.models.responses.ResponseBankingAccountsBalanceListV1;
import io.biza.deepthought.banking.requests.RequestBalancesByAccounts;
import io.biza.deepthought.banking.requests.RequestBalancesByCriteria;
import io.biza.deepthought.shared.exception.NotFoundException;

public interface BankingAccountBalanceApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<ResponseBankingAccountsBalanceListV1> listBalancesByCriteria(RequestBalancesByCriteria requestListBulkBalances) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  
  default ResponseEntity<ResponseBankingAccountsBalanceListV1> listBalancesByAccounts(
      RequestBalancesByAccounts build) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<ResponseBankingAccountsBalanceByIdV1> getAccountBalance(UUID accountId) throws NotFoundException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}
