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
package io.biza.deepthought.admin.exceptions;

import java.util.List;
import java.util.ArrayList;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.deepthought.shared.payloads.ValidationError;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioExceptionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ValidationListException extends Exception {
  private static final long serialVersionUID = 1L;
  
  @JsonProperty("type")
  @NotNull
  @NonNull
  @Schema(description = "Deep Thought Exception Type")
  DioExceptionType type;
  
  @JsonProperty("explanation")
  @NotNull
  @NonNull
  @Schema(description = "Validation Exception Explanation")
  String explanation;
  
  @JsonProperty("validationErrors")
  @Schema(description = "A List of Validation Errors Encountered")
  @Builder.Default
  List<ValidationError> validationErrors = new ArrayList<ValidationError>();

}
