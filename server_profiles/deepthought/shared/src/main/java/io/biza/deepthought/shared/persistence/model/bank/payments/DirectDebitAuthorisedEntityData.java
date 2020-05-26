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
package io.biza.deepthought.shared.persistence.model.bank.payments;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import org.hibernate.annotations.Type;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
import io.biza.deepthought.shared.persistence.model.bank.BankBranchData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@Table(name = "DIRECT_DEBIT_AUTHORISED_ENTITY")
@EqualsAndHashCode
public class DirectDebitAuthorisedEntityData {

  @Id
  @Type(type = "uuid-char")
  UUID id;
  
  @Transient
  @Builder.Default
  DioSchemeType schemeType = DioSchemeType.DIO_BANKING;
  
  @ManyToOne
  @JoinColumn(name = "BRANCH_ID", foreignKey = @ForeignKey(name = "DIRECT_DEBIT_AUTHORISED_ENTITY_BRANCH_ID_FK"))
  @ToString.Exclude
  BankBranchData branch;
  
  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "DIRECT_DEBIT_ID", foreignKey = @ForeignKey(name = "DIRECT_DEBIT_AUTHORISED_ENTITY_DIRECT_DEBIT_ID_FK"))
  @ToString.Exclude
  DirectDebitData directDebit;
  
  @Column(name = "DESCRIPTION")
  String description;
  
  @Column(name = "ABN")
  String abn;
  
  @Column(name = "ACN")
  String acn;
  
  @Column(name = "ARBN")
  String arbn;
  
  @PrePersist
  public void prePersist() {
    if (this.directDebit() != null) {
      this.directDebit().authorisedEntity(this);
    }
  }

}
