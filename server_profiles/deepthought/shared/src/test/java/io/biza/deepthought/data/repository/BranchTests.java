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
package io.biza.deepthought.data.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Optional;
import javax.annotation.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.biza.deepthought.data.support.DeepThoughtJpaConfig;
import io.biza.deepthought.data.support.TranslatorInitialisation;
import io.biza.deepthought.data.support.VariableConstants;
import io.biza.deepthought.shared.payloads.dio.banking.DioBankBranch;
import io.biza.deepthought.shared.persistence.model.BrandData;
import io.biza.deepthought.shared.persistence.model.bank.BankBranchData;
import io.biza.deepthought.shared.persistence.repository.BankBranchRepository;
import io.biza.deepthought.shared.persistence.repository.BrandRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("Person Data Tests")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DeepThoughtJpaConfig.class},
    loader = AnnotationConfigContextLoader.class)
@Transactional
@Slf4j
public class BranchTests extends TranslatorInitialisation {

  @Resource
  private BankBranchRepository branchRepository;

  @Resource
  private BrandRepository brandRepository;

  public BankBranchData createBranch() {

    BrandData brand = BrandData.builder().name(VariableConstants.BRAND_NAME)
        .displayName(VariableConstants.BRAND_DISPLAY_NAME).build();
    brandRepository.save(brand);

    Optional<BrandData> brandReturn = brandRepository.findById(brand.id());
    assertTrue(brandReturn.isPresent());

    BankBranchData branch = BankBranchData.builder().bankName(VariableConstants.BANK_NAME)
        .branchAddress(VariableConstants.BRANCH_ADDRESS).branchCity(VariableConstants.BRANCH_CITY)
        .branchName(VariableConstants.BRANCH_NAME).branchPostcode(VariableConstants.BRANCH_POSTCODE)
        .branchState(VariableConstants.BRANCH_STATE).bsb(VariableConstants.BRANCH_BSB).brand(brand)
        .build();
    branchRepository.save(branch);

    return branch;
  }

  @Test
  public void testBranchAndCompare() {

    BankBranchData branch = createBranch();
    DioBankBranch dioBranch = mapper.getMapperFacade().map(branch, DioBankBranch.class);

    DioBankBranch dioBranchStatic = DioBankBranch.builder().id(branch.id())
        .bankName(VariableConstants.BANK_NAME).branchAddress(VariableConstants.BRANCH_ADDRESS)
        .branchCity(VariableConstants.BRANCH_CITY).branchName(VariableConstants.BRANCH_NAME)
        .branchPostcode(VariableConstants.BRANCH_POSTCODE)
        .branchState(VariableConstants.BRANCH_STATE).bsb(VariableConstants.BRANCH_BSB).build();

    LOG.info("\n\n{}\n\n", createComparisonTable(dioBranch, dioBranchStatic));

    if (!dioBranch.equals(dioBranchStatic)) {
      fail("Payload conversion did not provide equality:\n"
          + createComparisonTable(dioBranch, dioBranchStatic));
    }
  }
}
