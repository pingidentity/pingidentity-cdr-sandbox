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
package io.biza.deepthought.discovery;

public class Labels {
  /**
   * Tag definitions
   */
  public final static String TAG_PRODUCT_NAME = "products";
  public final static String TAG_PRODUCT_DESCRIPTION = "The Product Data API";

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
      "Successful Response containing requested object";
  public final static String RESPONSE_SUCCESSFUL_CREATE =
      "Successfully created new object with content returned";
  public final static String RESPONSE_SUCCESSFUL_DELETE =
      "Successfully deleted object specified in request with no content returned";
  public final static String RESPONSE_SUCCESSFUL_UPDATE =
      "Successfully updated object specified with updated object returned";
  public final static String RESPONSE_OBJECT_NOT_FOUND = "Requested Object could not be found";
  public final static String RESPONSE_INPUT_VALIDATION_ERROR =
      "Provided request details contains validation errors, validation errors are included in the response";

}
