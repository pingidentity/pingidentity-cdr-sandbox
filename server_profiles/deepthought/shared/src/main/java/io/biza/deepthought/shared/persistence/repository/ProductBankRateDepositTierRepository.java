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
package io.biza.deepthought.shared.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import io.biza.deepthought.shared.persistence.model.product.banking.ProductBankRateDepositTierData;

@Repository
public interface ProductBankRateDepositTierRepository
    extends JpaRepository<ProductBankRateDepositTierData, UUID> {
  public List<ProductBankRateDepositTierData> findAllByDepositRate_Product_Product_Brand_IdAndDepositRate_Product_Product_IdAndDepositRate_Id(
      UUID brandId, UUID productId, UUID depositRateId);
  public Optional<ProductBankRateDepositTierData> findByIdAndDepositRate_Product_Product_Brand_IdAndDepositRate_Product_Product_IdAndDepositRate_Id(
      UUID id, UUID brandId, UUID productId, UUID depositRateId);
}


