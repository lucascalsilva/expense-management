package com.mobnova.expense_mgt.rest.v1.controllers;

import com.mobnova.expense_mgt.validation.ValidationErrorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity handleValidationErrors(MethodArgumentNotValidException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(ValidationErrorUtils.errorsFromException(exception));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity handleGenericError(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(exception);
    }

}
