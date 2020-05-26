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
import io.biza.babelfish.cdr.models.responses.ResponseBankingPayeeByIdV1;
import io.biza.babelfish.cdr.models.responses.ResponseBankingPayeeListV1;
import io.biza.deepthought.banking.requests.RequestListPayees;
import io.biza.deepthought.shared.exception.InvalidSubjectException;
import io.biza.deepthought.shared.exception.NotFoundException;

public interface BankingPayeeApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<ResponseBankingPayeeListV1> listPayees(RequestListPayees build) throws InvalidSubjectException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<ResponseBankingPayeeByIdV1> getPayeeDetail(UUID payeeId) throws NotFoundException, InvalidSubjectException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }


}
