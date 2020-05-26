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
package io.biza.deepthought.banking.requests;

import javax.validation.constraints.Min;
import io.biza.babelfish.cdr.enumerations.BankingAccountStatusWithAll;
import io.biza.babelfish.cdr.enumerations.BankingProductCategory;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RequestBalancesByCriteria {
  BankingProductCategory productCategory;
  @Builder.Default
  BankingAccountStatusWithAll accountStatus = BankingAccountStatusWithAll.ALL;
  Boolean isOwned;
  @Min(1)
  Integer page;
  @Min(0)
  @Builder.Default
  Integer pageSize = 25;
}

