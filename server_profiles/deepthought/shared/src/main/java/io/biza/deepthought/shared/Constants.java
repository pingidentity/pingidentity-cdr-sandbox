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
package io.biza.deepthought.shared;

import java.time.format.DateTimeFormatter;

public class Constants {

  /**
   * Formats
   */
  public static final String BSB_STRING_FORMAT = "###-###";
  public static final String DEFAULT_LANGUAGE = "en";
  public static final String DEFAULT_LOCALE = "AU";
  public static final String DEFAULT_CURRENCY = "AUD";
  public static final DateTimeFormatter CDR_DATESTRING_FORMATTER = io.biza.babelfish.cdr.Constants.CDR_DATESTRING_FORMATTER;

  
  /**
   * Response codes as strings
   */
  public final static String RESPONSE_CODE_CREATED = "201";
  public final static String RESPONSE_CODE_OK = "200";
  public final static String RESPONSE_CODE_NOT_FOUND = "404";
  public final static String RESPONSE_CODE_NO_CONTENT = "204";
  public final static String RESPONSE_CODE_UNPROCESSABLE_ENTITY = "422";


  /**
   * Generic Response descriptions
   */
  public final static String RESPONSE_SUCCESSFUL_LIST =
      "Successful Response containing list of requested objects";
  public final static String RESPONSE_SUCCESSFUL_READ =
      "Success";
  public final static String RESPONSE_SUCCESSFUL_CREATE =
      "Successfully created new object with content returned";
  public final static String RESPONSE_SUCCESSFUL_DELETE =
      "Successfully deleted object specified in request with no content returned";
  public final static String RESPONSE_SUCCESSFUL_UPDATE =
      "Successfully updated object specified with updated object returned";
  public final static String RESPONSE_OBJECT_NOT_FOUND = "Requested Object could not be found";
  public final static String RESPONSE_INPUT_VALIDATION_ERROR =
      "Provided request details contains validation errors, validation errors are included in the response";
  
  /**
   * Scope Definitions
   */
  public final static String SECURITY_SCOPE_CUSTOMER_BASIC_READ = "common:customer.basic:read";
  public final static String OAUTH2_SCOPE_CUSTOMER_BASIC_READ =
      "hasAuthority('SCOPE_" + SECURITY_SCOPE_CUSTOMER_BASIC_READ + "')";
  
  public final static String SECURITY_SCOPE_CUSTOMER_DETAIL_READ = "common:customer.detail:read";
  public final static String OAUTH2_SCOPE_CUSTOMER_DETAIL_READ =
      "hasAuthority('SCOPE_" + SECURITY_SCOPE_CUSTOMER_DETAIL_READ + "')";
  
  public final static String SECURITY_SCOPE_BANK_ACCOUNT_BASIC_READ = "bank:accounts.basic:read";
  public final static String OAUTH2_SCOPE_BANK_ACCOUNT_BASIC_READ =
      "hasAuthority('SCOPE_" + SECURITY_SCOPE_BANK_ACCOUNT_BASIC_READ + "')";
  
  public final static String SECURITY_SCOPE_BANK_ACCOUNT_DETAIL_READ = "bank:accounts.detail:read";
  public final static String OAUTH2_SCOPE_BANK_ACCOUNT_DETAIL_READ =
      "hasAuthority('SCOPE_" + SECURITY_SCOPE_BANK_ACCOUNT_DETAIL_READ + "')";
  
  public final static String SECURITY_SCOPE_BANK_TRANSACTIONS_READ = "bank:transactions:read";
  public final static String OAUTH2_SCOPE_BANK_TRANSACTIONS_READ =
      "hasAuthority('SCOPE_" + SECURITY_SCOPE_BANK_TRANSACTIONS_READ + "')";
  
  public final static String SECURITY_SCOPE_BANK_PAYEES_READ = "bank:payees:read";
  public final static String OAUTH2_SCOPE_BANK_BANK_PAYEES_READ =
      "hasAuthority('SCOPE_" + SECURITY_SCOPE_BANK_PAYEES_READ + "')";
  
  public final static String SECURITY_SCOPE_BANK_REGULAR_PAYMENTS_READ = "bank:regular_payments:read";
  public final static String OAUTH2_SCOPE_BANK_REGULAR_PAYMENTS_READ =
      "hasAuthority('SCOPE_" + SECURITY_SCOPE_BANK_REGULAR_PAYMENTS_READ + "')";
  
  /**
   * Tag definitions
   */
  public final static String TAG_COMMON_NAME = "common";
  public final static String TAG_COMMON_DESCRIPTION = "Common Data API";
  public final static String TAG_DISCOVERY_NAME = "discovery";
  public final static String TAG_DISCOVERY_DESCRIPTION = "Discovery Data API";
  public final static String TAG_CUSTOMER_NAME = "customer";
  public final static String TAG_CUSTOMER_DESCRIPTION = "Customer Data API";
  public final static String TAG_ACCOUNTS_NAME = "accounts";
  public final static String TAG_ACCOUNTS_DESCRIPTION = "Account Data API";
  public final static String TAG_PAYEES_NAME = "payees";
  public final static String TAG_PAYEES_DESCRIPTION = "Payees Data API";  
  public final static String TAG_DIRECT_DEBITS_NAME = "Direct Debits";
  public final static String TAG_DIRECT_DEBITS_DESCRIPTION = "Direct Debits Data API";
  public final static String TAG_SCHEDULED_PAYMENTS_NAME = "Scheduled Payments";
  public final static String TAG_SCHEDULED_PAYMENTS_DESCRIPTION = "Scheduled Payments Data API";  
  public final static String TAG_BANKING_NAME = "banking";
  public final static String TAG_BANKING_DESCRIPTION = "Banking Data API";  
  public final static String TAG_PRODUCT_NAME = "products";
  public final static String TAG_PRODUCT_DESCRIPTION = "The Product Data API";

}
