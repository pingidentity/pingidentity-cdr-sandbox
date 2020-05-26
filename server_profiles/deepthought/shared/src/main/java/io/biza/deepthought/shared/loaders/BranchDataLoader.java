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
package io.biza.deepthought.shared.loaders;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import io.biza.babelfish.cdr.support.customtypes.ApcaNumberType;
import io.biza.deepthought.shared.persistence.model.bank.BankBranchData;
import io.biza.deepthought.shared.persistence.repository.BankBranchRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BranchDataLoader {
  private BankBranchRepository branchRepository;

  @Autowired
  public BranchDataLoader(BankBranchRepository branchRepository) {
    this.branchRepository = branchRepository;
    loadBankDirectory();
  }
  
  private void loadBankDirectory() {
    try {
      InputStream bsbDirectory = ApcaNumberType.class.getResourceAsStream("/bsb-directory.csv");

      CSVReader csvReader = new CSVReader(new InputStreamReader(bsbDirectory));
      String[] line;
      while ((line = csvReader.readNext()) != null) {
        Integer bsb = Integer.parseInt(line[0].replaceAll("-", ""));
        if (!branchRepository.findByBsb(bsb).isPresent()) {
          branchRepository.save(BankBranchData.builder().bsb(bsb).bankName(line[1]).branchName(line[2])
              .branchAddress(line[3]).branchCity(line[4]).branchState(line[5])
              .branchPostcode(line[6]).build());
        }
      }

      csvReader.close();

    } catch (CsvValidationException | IOException e) {
      LOG.error("Received unexpected exception while converting BSB");
    }

  }
}
