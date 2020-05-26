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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import io.biza.babelfish.cdr.enumerations.BankingTransactionStatus;
import io.biza.deepthought.shared.persistence.model.bank.account.BankAccountData;
import io.biza.deepthought.shared.persistence.model.bank.transaction.BankAccountTransactionData;

@Repository
public interface BankAccountTransactionRepository extends JpaRepository<BankAccountTransactionData, UUID>, JpaSpecificationExecutor<BankAccountTransactionData> {
  public List<BankAccountTransactionData> findAllByAccountIdAndAccountBranchIdAndAccountBranchBrandId(UUID accountId, UUID branchId, UUID brandId);
  public Optional<BankAccountTransactionData> findByIdAndAccountIdAndAccountBranchIdAndAccountBranchBrandId(UUID id, UUID accountId, UUID branchId, UUID brandId);
  
  @Query("SELECT SUM(t.amount) FROM BankAccountTransactionData t WHERE t.account = :bankAccount AND t.status = :status AND t.amount > 0")
  BigDecimal creditsByAccountAndStatus(@Param("bankAccount") BankAccountData bankAccount, @Param("status") BankingTransactionStatus status);
  
  @Query("SELECT SUM(t.amount) FROM BankAccountTransactionData t WHERE t.account = :bankAccount AND t.status = :status AND t.amount < 0")
  BigDecimal debitsByAccountAndStatus(@Param("bankAccount") BankAccountData bankAccount, @Param("status") BankingTransactionStatus status);
  
}
