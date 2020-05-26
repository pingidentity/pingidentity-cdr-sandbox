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
package io.biza.deepthought.admin.api.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import io.biza.babelfish.cdr.exceptions.LabelValueEnumValueNotSupportedException;
import io.biza.babelfish.cdr.support.LabelValueEnumInterface;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.shared.payloads.ResponseValidationError;
import io.biza.deepthought.shared.payloads.ValidationError;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioExceptionType;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioValidationErrorType;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
public class ExceptionController {

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<Object> handleIllegalStateException(HttpServletRequest req,
      IllegalStateException ex) {

    return ResponseEntity.unprocessableEntity()
        .body(ResponseValidationError.builder().type(DioExceptionType.VALIDATION_ERROR)
            .explanation("Received an illegal state exception").build());
  }

  @ExceptionHandler(RollbackException.class)
  public ResponseEntity<Object> handleRollbackException(HttpServletRequest req,
      RollbackException ex) {

    ResponseValidationError errors = ResponseValidationError.builder()
        .type(DioExceptionType.VALIDATION_ERROR)
        .explanation("Input has invalid parameters, see validationErrors for explanation").build();

    Throwable exThrowable = ex.getCause();

    if (exThrowable != null && exThrowable instanceof ConstraintViolationException) {
      ConstraintViolationException constraintException = (ConstraintViolationException) exThrowable;


      constraintException.getConstraintViolations().forEach(violation -> {
        try {
          errors.validationErrors()
              .add(ValidationError.builder().fields(List.of(violation.getPropertyPath().toString()))
                  .message(StringUtils.capitalize(violation.getMessage()))
                  .type(DioValidationErrorType.ATTRIBUTE_INVALID).build());
        } catch (IllegalArgumentException e) {
          LOG.error("Attempted to unwrap an error which is not supported: {}", violation);
          errors.validationErrors()
              .add(ValidationError.builder()
                  .fields(List.of(violation.getConstraintDescriptor().toString()))
                  .message(StringUtils.capitalize(violation.getMessage()))
                  .type(DioValidationErrorType.ATTRIBUTE_INVALID).build());
        }
      });
    } else {
      LOG.error("Received RollbackException of unknown view with content of {}", ex.toString());
    }

    return ResponseEntity.unprocessableEntity().body(errors);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleHttpMessageNotReadable(HttpServletRequest req,
      HttpMessageNotReadableException ex) {

    if (ex.getCause() != null && ex.getCause() instanceof ValueInstantiationException) {
      ValueInstantiationException e = (ValueInstantiationException) ex.getCause();

      List<String> fieldNames = new ArrayList<String>();

      for (Reference reference : e.getPath()) {
        fieldNames.add(reference.getFieldName());
      }


      if (e.getCause() instanceof LabelValueEnumValueNotSupportedException) {
        LabelValueEnumValueNotSupportedException labelEx =
            (LabelValueEnumValueNotSupportedException) e.getCause();

        List<String> enumValuesList = new ArrayList<String>();
        for (LabelValueEnumInterface enumValue : labelEx.getSourceEnum()) {
          enumValuesList.add(enumValue.name());
        }

        String message = String.format(
            "The value (%s) supplied for %s is not one of the valid options: [ %s ]",
            labelEx.getSuppliedValue(), labelEx.getClassName(), String.join(", ", enumValuesList));

        return ResponseEntity.unprocessableEntity()
            .body(
                ResponseValidationError.builder().type(DioExceptionType.INVALID_JSON)
                    .explanation("The Supplied JSON Payload was unable to be parsed")
                    .validationErrors(List.of(ValidationError.builder()
                        .fields(List.of(String.join(".", fieldNames))).message(message)
                        .type(DioValidationErrorType.ATTRIBUTE_INVALID).build()))
                    .build());
      } else {

        return ResponseEntity.unprocessableEntity()
            .body(ResponseValidationError.builder().type(DioExceptionType.INVALID_JSON)
                .explanation("The Supplied JSON Payload was unable to be parsed")
                .validationErrors(
                    List.of(ValidationError.builder().type(DioValidationErrorType.INVALID_JSON)
                        .fields(List.of("HTTP Body")).message(ex.getMessage()).build()))
                .build());
      }
    } else if (ex.getCause() != null && ex.getCause() instanceof InvalidFormatException) {
      InvalidFormatException e = (InvalidFormatException) ex.getCause();

      ResponseValidationError errors =
          ResponseValidationError.builder().type(DioExceptionType.VALIDATION_ERROR)
              .explanation("Input has invalid parameters, see validationErrors for explanation")
              .build();

      /**
       * Build path
       */
      List<String> pathList = new ArrayList<String>();
      for (Reference reference : e.getPath()) {
        pathList.add(reference.getFieldName());
      }

      errors.validationErrors()
          .add(ValidationError.builder().fields(List.of(String.join(".", pathList)))
              .message(StringUtils.capitalize(e.getCause().getMessage()))
              .type(DioValidationErrorType.ATTRIBUTE_INVALID).build());

      return ResponseEntity.unprocessableEntity().body(errors);
    } else if (ex.getCause() != null && ex.getCause() instanceof JsonMappingException) {
      JsonMappingException e = (JsonMappingException) ex.getCause();

      ResponseValidationError errors =
          ResponseValidationError.builder().type(DioExceptionType.VALIDATION_ERROR)
              .explanation("Input has invalid parameters, see validationErrors for explanation")
              .build();

      /**
       * Build path
       */
      List<String> pathList = new ArrayList<String>();
      for (Reference reference : e.getPath()) {
        pathList.add(reference.getFieldName());
      }

      errors.validationErrors()
          .add(ValidationError.builder().fields(List.of(String.join(".", pathList)))
              .message(StringUtils.capitalize(e.getCause().getMessage()))
              .type(DioValidationErrorType.ATTRIBUTE_INVALID).build());

      return ResponseEntity.unprocessableEntity().body(errors);

    } else {
      return ResponseEntity.unprocessableEntity()
          .body(ResponseValidationError.builder().type(DioExceptionType.INVALID_JSON)
              .explanation("The Supplied JSON Payload was unable to be parsed")
              .validationErrors(
                  List.of(ValidationError.builder().type(DioValidationErrorType.INVALID_JSON)
                      .fields(List.of("HTTP Body")).message(ex.getMessage()).build()))
              .build());
    }
  }


  @ExceptionHandler(ValidationListException.class)
  public ResponseEntity<Object> handleValidationException(HttpServletRequest req,
      ValidationListException ex) {

    return ResponseEntity.unprocessableEntity()
        .body(ResponseValidationError.builder().type(ex.type()).explanation(ex.explanation())
            .validationErrors(ex.validationErrors()).build());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleInvalidArguments(HttpServletRequest req,
      MethodArgumentNotValidException ex) {

    ResponseValidationError errors = ResponseValidationError.builder()
        .type(DioExceptionType.VALIDATION_ERROR)
        .explanation("Input has invalid parameters, see validationErrors for explanation").build();

    ex.getBindingResult().getAllErrors().forEach(error -> {
      ConstraintViolation<?> violation = error.unwrap(ConstraintViolation.class);
      try {
        errors.validationErrors()
            .add(ValidationError.builder().fields(List.of(violation.getPropertyPath().toString()))
                .message(StringUtils.capitalize(violation.getMessage()))
                .type(DioValidationErrorType.ATTRIBUTE_INVALID).build());
      } catch (IllegalArgumentException e) {
        LOG.error("Attempted to unwrap an error which is not supported: {}", error);
        errors.validationErrors()
            .add(ValidationError.builder().fields(List.of(error.getCode()))
                .message(StringUtils.capitalize(error.getDefaultMessage()))
                .type(DioValidationErrorType.ATTRIBUTE_INVALID).build());
      }
    });

    return ResponseEntity.unprocessableEntity().body(errors);
  }

  @ExceptionHandler(JsonParseException.class)
  public ResponseEntity<Object> handleInvalidJsonParseException(HttpServletRequest req,
      JsonParseException ex) {

    ResponseValidationError errors =
        ResponseValidationError.builder().type(DioExceptionType.INVALID_JSON)
            .explanation("The Supplied JSON Payload was unable to be parsed")
            .validationErrors(
                List.of(ValidationError.builder().type(DioValidationErrorType.INVALID_JSON)
                    .fields(List.of("HTTP Body")).message(ex.getMessage()).build()))
            .build();

    return ResponseEntity.unprocessableEntity().body(errors);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Object> handleJdbcIntegrityViolation(HttpServletRequest req,
      DataIntegrityViolationException ex) {

    ResponseValidationError errors = ResponseValidationError.builder()
        .type(DioExceptionType.DATABASE_ERROR)
        .explanation(
            "While attempting to write data to database the server encountered a data format violation error")
        .validationErrors(List.of(ValidationError.builder()
            .type(DioValidationErrorType.DATABASE_ERROR).fields(List.of("Database Exception"))
            .message(ex.getMostSpecificCause().getMessage()).build()))
        .build();

    return ResponseEntity.unprocessableEntity().body(errors);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Object> handleInvalidFieldTypeException(HttpServletRequest req,
      MethodArgumentTypeMismatchException ex) {

    ResponseValidationError errors =
        ResponseValidationError.builder().type(DioExceptionType.INVALID_PARAMETER_FORMAT)
            .explanation("A path parameter of invalid format was supplied")
            .validationErrors(List.of(ValidationError.builder()
                .type(DioValidationErrorType.INVALID_FORMAT)
                .fields(List.of(ex.getParameter().getParameterName()))
                .message("Parameter should be a " + ex.getRequiredType().getSimpleName()).build()))
            .build();

    return ResponseEntity.unprocessableEntity().body(errors);
  }
}
