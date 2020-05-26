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
package io.biza.deepthought.shared.persistence.model.bank.transaction;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import io.biza.babelfish.cdr.enumerations.BankingTransactionService;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Valid
@Table(name = "BANK_TRANSACTION_NPP")
public class BankAccountTransactionNPPData {

  @Id
  @Type(type = "uuid-char")
  UUID id;
  
  @Builder.Default
  private DioSchemeType schemeType = DioSchemeType.DIO_BANKING;
  
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TRANSACTION_ID", foreignKey = @ForeignKey(name = "BANK_TRANSACTION_NPP_TRANSACTION_ID_FK"))
  @MapsId
  @ToString.Exclude
  BankAccountTransactionData transaction;

  @Column(name = "PAYER")
  String payer;

  @Column(name = "PAYEE")
  String payee;
  
  @Column(name = "END_TO_END_ID")
  String endToEndId;
  
  @Column(name = "PURPOSE")
  String purposeCode;
  
  @Column(name = "SERVICE")
  @Enumerated(EnumType.STRING)
  @NotNull
  @Builder.Default
  BankingTransactionService service = BankingTransactionService.X2P101;
  
}
