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
package io.biza.deepthought.admin.support;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.util.StringUtils;
import io.biza.babelfish.cdr.support.AssertTrueBabelfish;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.shared.payloads.ValidationError;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioExceptionType;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioValidationErrorType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeepThoughtValidator {
  public static <T> void validate(Validator validator, T input) throws ValidationListException {
    ValidationListException errors = ValidationListException.builder()
        .type(DioExceptionType.VALIDATION_ERROR)
        .explanation("Input has invalid parameters, see validationErrors for explanation").build();

    for (ConstraintViolation<T> error : validator.validate(input)) {
      LOG.debug("Validation experienced and caught: {}", error.getPropertyPath());
      LOG.debug("Validation error class is: {}", error.getConstraintDescriptor());
      if (error.getConstraintDescriptor().getAnnotation() instanceof AssertTrueBabelfish) {
        AssertTrueBabelfish assertion =
            (AssertTrueBabelfish) error.getConstraintDescriptor().getAnnotation();

        errors.validationErrors().add(ValidationError.builder()
            .fields(List.of(assertion.fields()).stream()
                .map(s -> error.getPropertyPath().toString().replaceAll(".[^.]*$", ".") + s)
                .collect(Collectors.toList()))
            .message(StringUtils.capitalize(error.getMessage()))
            .type(DioValidationErrorType.ATTRIBUTE_INVALID).build());

      } else {
        errors.validationErrors()
            .add(ValidationError.builder().fields(List.of(error.getPropertyPath().toString()))
                .message(StringUtils.capitalize(error.getMessage()))
                .type(DioValidationErrorType.ATTRIBUTE_INVALID).build());
      }
    }

    if (errors.validationErrors().size() > 0) {
      throw errors;
    }
  }
}
