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
package io.biza.deepthought.shared.payloads.dio.enumerations;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Deep Thought Exception Type", enumAsRef = true)
public enum DioExceptionType {
  INVALID_BRAND, INVALID_BRANCH, INVALID_JSON, INVALID_PARAMETER_FORMAT, VALIDATION_ERROR, INVALID_BRAND_AND_PRODUCT, UNSUPPORTED_PRODUCT_SCHEME_TYPE, DATABASE_ERROR, INVALID_PRODUCT, INVALID_BUNDLE, INVALID_CUSTOMER, INVALID_ACCOUNT, INVALID_ASSOCIATION
}
