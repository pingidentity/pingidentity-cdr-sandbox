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
package io.biza.deepthought.shared.persistence.model.grant;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioGrantAccess;
import io.biza.deepthought.shared.persistence.model.customer.bank.CustomerAccountData;
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
@Table(name = "GRANT_BANK_ACCOUNT",
uniqueConstraints = {@UniqueConstraint(columnNames = {"GRANT_ID", "CUSTOMER_BANK_ACCOUNT_ID"})})
public class GrantCustomerAccountData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;
  
  @ManyToOne
  @JoinColumn(name = "GRANT_ID", nullable = false, foreignKey = @ForeignKey(name = "GRANT_ACCOUNT_GRANT_ID_FK"))
  @NotNull
  GrantData grant;
  
  @ManyToOne
  @JoinColumn(name = "CUSTOMER_BANK_ACCOUNT_ID", nullable = false, foreignKey = @ForeignKey(name = "GRANT_CUSTOMER_BANK_ACCOUNT_ID_FK"))
  @NotNull
  CustomerAccountData customerAccount;
  
  @ElementCollection(targetClass = DioGrantAccess.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "GRANT_BANK_ACCOUNT_PERMISSIONS", joinColumns=@JoinColumn(name = "GRANT_BANK_ACCOUNT_ID"))
  @Column(name="PERMISSIONS", nullable=false)
  @Enumerated(EnumType.STRING)
  Set<DioGrantAccess> permissions;
  
  @PrePersist
  public void prePersist() {
    if (this.grant() != null) {
      Set<GrantCustomerAccountData> set = new HashSet<GrantCustomerAccountData>();
      if (this.customerAccount().grants() != null) {
        set.addAll(this.customerAccount().grants());
      }
      set.add(this);
      this.customerAccount().grants(set);
    }
  }
}
