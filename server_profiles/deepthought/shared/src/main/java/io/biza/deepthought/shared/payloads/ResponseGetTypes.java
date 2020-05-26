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
package io.biza.deepthought.shared.payloads;

import java.util.List;
import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Valid
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ResponseGetTypes",
    description = "Lists of Common Value types for use in Form select fields")
public class ResponseGetTypes {

  @JsonProperty("fieldNames")
  @Builder.Default
  @Schema(description = "Fields included in this payload")
  List<String> fieldNames = new ArrayList<String>();

  @JsonProperty("ADDRESS_PAF_FLAT_UNIT_TYPE")
  @Schema(description = "Type of flat or unit for the address")
  List<FormLabelValue> ADDRESS_PAF_FLAT_UNIT_TYPE;

  @JsonProperty("ADDRESS_PAF_FLOOR_LEVEL_TYPE")
  @Schema(description = "Floor or level type for the address")
  List<FormLabelValue> ADDRESS_PAF_FLOOR_LEVEL_TYPE;

  @JsonProperty("ADDRESS_PAF_POSTAL_DELIVERY_TYPE")
  @Schema(description = "Postal Delivery Type for the Address")
  List<FormLabelValue> ADDRESS_PAF_POSTAL_DELIVERY_TYPE;

  @JsonProperty("ADDRESS_PAF_STATE_TYPE")
  @Schema(description = "Australian State for the Address")
  List<FormLabelValue> ADDRESS_PAF_STATE_TYPE;

  @JsonProperty("ADDRESS_PAF_STREET_SUFFIX")
  @Schema(description = "Street Suffix for the Address")
  List<FormLabelValue> ADDRESS_PAF_STREET_SUFFIX;

  @JsonProperty("ADDRESS_PAF_STREET_TYPE")
  @Schema(description = "Street Type for the Address")
  List<FormLabelValue> ADDRESS_PAF_STREET_TYPE;

  @JsonProperty("ADDRESS_PURPOSE")
  @Schema(description = "Purpose for the Address")
  List<FormLabelValue> ADDRESS_PURPOSE;

  @JsonProperty("BANKING_ACCOUNT_STATUS")
  @Schema(description = "Banking Account Status")
  List<FormLabelValue> BANKING_ACCOUNT_STATUS;

  @JsonProperty("BANKING_ACCOUNT_STATUS_WITH_ALL")
  @Schema(description = "Banking Account Status with All Status")
  List<FormLabelValue> BANKING_ACCOUNT_STATUS_WITH_ALL;

  @JsonProperty("BANKING_LOAN_REPAYMENT_TYPE")
  @Schema(description = "Banking Account Loan Repayment Type")
  List<FormLabelValue> BANKING_LOAN_REPAYMENT_TYPE;

  @JsonProperty("BANKING_PAYEE_TYPE")
  @Schema(description = "Banking Payee Type")
  List<FormLabelValue> BANKING_PAYEE_TYPE;

  @JsonProperty("BANKING_PAYEE_TYPE_WITH_ALL")
  @Schema(description = "Banking Payee Type with All")
  List<FormLabelValue> BANKING_PAYEE_TYPE_WITH_ALL;

  @JsonProperty("BANKING_PAYMENT_NON_BUSINESS_DAY_TREATMENT")
  @Schema(
      description = "Enumerated field giving the treatment where a scheduled payment date is not a business day.")
  List<FormLabelValue> BANKING_PAYMENT_NON_BUSINESS_DAY_TREATMENT;

  @JsonProperty("BANKING_PRODUCT_CATEGORY")
  @Schema(description = "Banking Product Categories")
  List<FormLabelValue> BANKING_PRODUCT_CATEGORY;

  @JsonProperty("BANKING_PRODUCT_CONSTRAINT_TYPE")
  @Schema(description = "Banking Product Constraint Type")
  List<FormLabelValue> BANKING_PRODUCT_CONSTRAINT_TYPE;

  @JsonProperty("BANKING_PRODUCT_DEPOSIT_RATE_TYPE")
  @Schema(description = "Banking Product Deposit Rate Type")
  List<FormLabelValue> BANKING_PRODUCT_DEPOSIT_RATE_TYPE;

  @JsonProperty("BANKING_PRODUCT_DISCOUNT_ELIGIBILITY_TYPE")
  @Schema(description = "Banking Product Discount Eligibility Type")
  List<FormLabelValue> BANKING_PRODUCT_DISCOUNT_ELIGIBILITY_TYPE;

  @JsonProperty("BANKING_PRODUCT_DISCOUNT_TYPE")
  @Schema(description = "Banking Product Discount Type")
  List<FormLabelValue> BANKING_PRODUCT_DISCOUNT_TYPE;

  @JsonProperty("BANKING_PRODUCT_EFFECTIVE_WITH_ALL")
  @Schema(description = "Banking Product Effective Filter with All")
  List<FormLabelValue> BANKING_PRODUCT_EFFECTIVE_WITH_ALL;

  @JsonProperty("BANKING_PRODUCT_ELIGIBILITY_TYPE")
  @Schema(description = "Banking Product Eligibility Criteria Type")
  List<FormLabelValue> BANKING_PRODUCT_ELIGIBILITY_TYPE;

  @JsonProperty("BANKING_PRODUCT_FEATURE_TYPE")
  @Schema(description = "Banking Product Feature Type")
  List<FormLabelValue> BANKING_PRODUCT_FEATURE_TYPE;

  @JsonProperty("BANKING_PRODUCT_FEE_TYPE")
  @Schema(description = "Banking Product Fee Type")
  List<FormLabelValue> BANKING_PRODUCT_FEE_TYPE;

  @JsonProperty("BANKING_PRODUCT_LENDING_RATE_INTEREST_PAYMENT_TYPE")
  @Schema(description = "Banking Product Lending Rate Interest Payment Configuration")
  List<FormLabelValue> BANKING_PRODUCT_LENDING_RATE_INTEREST_PAYMENT_TYPE;

  @JsonProperty("BANKING_PRODUCT_LENDING_RATE_TYPE")
  @Schema(description = "Banking Product: Lending Rate Type")
  List<FormLabelValue> BANKING_PRODUCT_LENDING_RATE_TYPE;

  @JsonProperty("BANKING_PRODUCT_RATE_TIER_APPLICATION_METHOD")
  @Schema(description = "Banking Product: Rate Tier Application Method")
  List<FormLabelValue> BANKING_PRODUCT_RATE_TIER_APPLICATION_METHOD;

  @JsonProperty("BANKING_SCHEDULED_PAYMENT_STATUS")
  @Schema(description = "Banking: Scheduled Payment Status")
  List<FormLabelValue> BANKING_SCHEDULED_PAYMENT_STATUS;

  @JsonProperty("BANKING_TERM_DEPOSIT_MATURITY_INSTRUCTIONS")
  @Schema(description = "Banking: Term Deposit Maturity Instructions")
  List<FormLabelValue> BANKING_TERM_DEPOSIT_MATURITY_INSTRUCTIONS;

  @JsonProperty("BANKING_TRANSACTION_SERVICE")
  @Schema(description = "Banking Transaction: NPP Service Overlay")
  List<FormLabelValue> BANKING_TRANSACTION_SERVICE;

  @JsonProperty("BANKING_TRANSACTION_STATUS")
  @Schema(description = "Banking: Transaction Status")
  List<FormLabelValue> BANKING_TRANSACTION_STATUS;

  @JsonProperty("BANKING_TRANSACTION_TYPE")
  @Schema(description = "Banking: Transaction Type")
  List<FormLabelValue> BANKING_TRANSACTION_TYPE;

  @JsonProperty("COMMON_DISCOVERY_STATUS_TYPE")
  @Schema(description = "Common Discovery: Service Status Type")
  List<FormLabelValue> COMMON_DISCOVERY_STATUS_TYPE;

  @JsonProperty("COMMON_EMAIL_ADDRESS_PURPOSE")
  @Schema(description = "Common: Email Address Purpose")
  List<FormLabelValue> COMMON_EMAIL_ADDRESS_PURPOSE;

  @JsonProperty("COMMON_ORGANISATION_TYPE")
  @Schema(description = "Common: Organisation Type")
  List<FormLabelValue> COMMON_ORGANISATION_TYPE;

  @JsonProperty("COMMON_PHONE_NUMBER_PURPOSE")
  @Schema(description = "Common: Phone Number Purpose")
  List<FormLabelValue> COMMON_PHONE_NUMBER_PURPOSE;

  @JsonProperty("COMMON_UNIT_OF_MEASURE_TYPE")
  @Schema(description = "Common: Unit of Measure")
  List<FormLabelValue> COMMON_UNIT_OF_MEASURE_TYPE;

  @JsonProperty("COMMON_WEEK_DAY")
  @Schema(description = "Day of Week")
  List<FormLabelValue> COMMON_WEEK_DAY;

  @JsonProperty("PAYLOAD_TYPE_ADDRESS")
  @Schema(description = "Payload Type: Address")
  List<FormLabelValue> PAYLOAD_TYPE_ADDRESS;

  @JsonProperty("PAYLOAD_TYPE_BANKING_ACCOUNT")
  @Schema(description = "Payload Type: Banking Account")
  List<FormLabelValue> PAYLOAD_TYPE_BANKING_ACCOUNT;

  @JsonProperty("PAYLOAD_TYPE_BANKING_DOMESTIC_PAYEE")
  @Schema(description = "Payload Type: Domestic Payee")
  List<FormLabelValue> PAYLOAD_TYPE_BANKING_DOMESTIC_PAYEE;

  @JsonProperty("PAYLOAD_TYPE_BANKING_DOMESTIC_PAYEE_PAY_ID")
  @Schema(description = "Payload Type: Domestic Payee PayID Identifier Type")
  List<FormLabelValue> PAYLOAD_TYPE_BANKING_DOMESTIC_PAYEE_PAY_ID;

  @JsonProperty("PAYLOAD_TYPE_BANKING_PAYEE")
  @Schema(description = "Payload Type: Banking Payee Type")
  List<FormLabelValue> PAYLOAD_TYPE_BANKING_PAYEE;

  @JsonProperty("PAYLOAD_TYPE_BANKING_SCHEDULED_PAYMENT_RECURRENCE")
  @Schema(description = "Payload Type: Banking Scheduled Payment Recurrence")
  List<FormLabelValue> PAYLOAD_TYPE_BANKING_SCHEDULED_PAYMENT_RECURRENCE;

  @JsonProperty("PAYLOAD_TYPE_BANKING_SCHEDULED_PAYMENT_TO")
  @Schema(description = "Payload Type: Banking Scheduled Payment Target")
  List<FormLabelValue> PAYLOAD_TYPE_BANKING_SCHEDULED_PAYMENT_TO;

  @JsonProperty("PAYLOAD_TYPE_CUSTOMER")
  @Schema(description = "Payload Type: Customer Type")
  List<FormLabelValue> PAYLOAD_TYPE_CUSTOMER;

  @JsonProperty("PAYLOAD_TYPE_TRANSACTION_EXTENSION")
  @Schema(description = "Payload Type: Transaction Extension Type")
  List<FormLabelValue> PAYLOAD_TYPE_TRANSACTION_EXTENSION;

  public void setTypeField(String fieldName, List<FormLabelValue> fieldValue) {
    try {
      if (this.getClass().getDeclaredField(fieldName) != null) {
        Field thisField = this.getClass().getDeclaredField(fieldName);
        thisField.set(this, fieldValue);
        populateFieldNames();
      }
    } catch (SecurityException | IllegalArgumentException | IllegalAccessException
        | NoSuchFieldException e) {
      // Silently ignore these
    }
  }

  public void populateFieldNames() {
    for (Field field : this.getClass().getDeclaredFields()) {
      try {
        if (field != null && field.getName() != null && field.get(this) != null
            && !field.getName().equals("fieldNames")
            && !field.isAnnotationPresent(JsonIgnore.class)) {
          if (!this.fieldNames.contains(field.getName())) {
            this.fieldNames.add(field.getName());
          }
        }
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
        // Silently ignore these
      }
    }
  }

}
