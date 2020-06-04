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
package io.biza.deepthought.shared.persistence.model.customer;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import javax.validation.constraints.AssertTrue;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
import io.biza.deepthought.shared.persistence.model.BrandData;
import io.biza.deepthought.shared.persistence.model.bank.payments.PayeeData;
import io.biza.deepthought.shared.persistence.model.bank.payments.ScheduledPaymentData;
import io.biza.deepthought.shared.persistence.model.customer.bank.CustomerAccountData;
import io.biza.deepthought.shared.persistence.model.organisation.OrganisationData;
import io.biza.deepthought.shared.persistence.model.person.PersonData;
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
@Table(name = "CUSTOMER")
public class CustomerData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;
  
  @Transient
  @Builder.Default
  DioSchemeType schemeType = DioSchemeType.DIO_COMMON;
  
  @Column(name = "CREATION_TIME", updatable = false, insertable = false)
  @CreationTimestamp
  @Builder.Default
  OffsetDateTime creationTime = OffsetDateTime.now();

  @Column(name = "LAST_UPDATED", updatable = false, insertable = false)
  @UpdateTimestamp
  @Builder.Default
  OffsetDateTime lastUpdated = OffsetDateTime.now();
  
  @ManyToOne
  @JoinColumn(name = "BRAND_ID", nullable = false, foreignKey = @ForeignKey(name = "CUSTOMER_BRAND_ID_FK"))
  @ToString.Exclude
  BrandData brand;
  
  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
  @ToString.Exclude
  Set<CustomerAccountData> accounts;
  
  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
  @ToString.Exclude
  Set<ScheduledPaymentData> scheduledPayments;
  
  @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
  @ToString.Exclude
  Set<PayeeData> payees;
    
  @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, optional = true, fetch = FetchType.EAGER)
  PersonData person;
  
  @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, optional = true, fetch = FetchType.EAGER)
  OrganisationData organisation;
  
  @AssertTrue(
      message = "Only one of PersonData or OrganisationData can be populated")
  private boolean isPersonXorOrganisation() {
    return (person != null && organisation == null) || (person == null && organisation != null);
  }
  
  @PrePersist
  public void prePersist() {
    if (this.person != null) {
      this.person.customer(this);
    }
    
    if(this.organisation != null) {
      this.organisation.customer(this);
    }
  }
  
}
