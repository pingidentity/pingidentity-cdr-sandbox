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
import java.util.ArrayList;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioExceptionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Valid
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Validation Error Response")
public class ResponseValidationError {
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
