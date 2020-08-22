package com.mobnova.expense_mgt.rest.v1.controllers.exception;

import com.mobnova.expense_mgt.dto.v1.error.ApiErrorDto;
import com.mobnova.expense_mgt.dto.v1.error.ApiSubErrorDto;
import com.mobnova.expense_mgt.dto.v1.error.ApiValidationErrorDto;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.mobnova.expense_mgt.ApplicationConstants.VALIDATION_ERRORS_MESSAGE;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.BAD_REQUEST,
                VALIDATION_ERRORS_MESSAGE, ex);

        List<ApiSubErrorDto> apiSubErrorDtos = ex.getBindingResult().getFieldErrors()
                .stream().map(ApiValidationErrorDto::new).sorted(Comparator.comparing(ApiValidationErrorDto::getField)).collect(Collectors.toList());

        apiErrorDto.setSubErrors(apiSubErrorDtos);

        return buildResponseEntity(apiErrorDto);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        return buildResponseEntity(new ApiErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage(), ex));
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
        return buildResponseEntity(new ApiErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiErrorDto apiErrorDto) {
        return ResponseEntity.status(apiErrorDto.getStatus()).contentType(MediaType.APPLICATION_JSON).body(apiErrorDto);
    }

}
