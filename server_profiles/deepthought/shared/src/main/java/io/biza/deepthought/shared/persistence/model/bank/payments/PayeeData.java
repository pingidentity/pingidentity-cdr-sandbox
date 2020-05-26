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

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
import io.biza.deepthought.shared.persistence.model.customer.CustomerData;
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
@Table(name = "PAYEE")
public class PayeeData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;
  
  @Transient
  @Builder.Default
  private DioSchemeType schemeType = DioSchemeType.CDR_BANKING;

  @ManyToOne
  @JoinColumn(name = "CUSTOMER_ID", nullable = false, foreignKey = @ForeignKey(name = "PAYEE_CUSTOMER_ID_FK"))
  @ToString.Exclude
  CustomerData customer;

  @OneToMany(mappedBy = "payee", cascade = CascadeType.ALL)
  @ToString.Exclude
  Set<ScheduledPaymentSetData> paymentSet;
  
  @Column(name = "NICK_NAME", nullable = false)
  @NotNull
  String nickName;
  
  @Column(name = "DESCRIPTION")
  String description;
  
  @Column(name = "CREATION_DATE_TIME")
  @CreationTimestamp
  OffsetDateTime creationDateTime;
  
  @OneToOne(mappedBy = "payee", cascade = CascadeType.ALL, optional = true)
  PayeeDomesticData domestic;
  
  @OneToOne(mappedBy = "payee", cascade = CascadeType.ALL, optional = true)
  PayeeBPAYData bpay;
  
  @OneToOne(mappedBy = "payee", cascade = CascadeType.ALL, optional = true)
  PayeeInternationalData international;
  
  @PrePersist
  public void prePersist() {
    if (this.domestic() != null) {
      this.domestic().payee(this);
    }
    if (this.bpay() != null) {
      this.bpay().payee(this);
    }
    if (this.international() != null) {
      this.international().payee(this);
    }
  }
}
