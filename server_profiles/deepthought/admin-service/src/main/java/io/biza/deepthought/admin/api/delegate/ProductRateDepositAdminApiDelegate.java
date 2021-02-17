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
package io.biza.deepthought.admin.api.delegate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.shared.payloads.dio.product.DioProductRateDeposit;

public interface ProductRateDepositAdminApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<List<DioProductRateDeposit>> listProductRateDeposits(UUID brandId,
      UUID productId) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DioProductRateDeposit> getProductRateDeposit(UUID brandId, UUID productId,
      UUID rateId) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DioProductRateDeposit> createProductRateDeposit(UUID brandId, UUID productId,
      DioProductRateDeposit rate) throws ValidationListException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<Void> deleteProductRateDeposit(UUID brandId, UUID productId, UUID rateId) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DioProductRateDeposit> updateProductRateDeposit(UUID brandId, UUID productId,
      UUID rateId, DioProductRateDeposit rate) throws ValidationListException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}