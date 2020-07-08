package com.mobnova.expense_mgt.validation;

import lombok.experimental.UtilityClass;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ValidationErrorUtils {

    public List<ValidationError> errorsFromException(MethodArgumentNotValidException methodArgumentNotValidException){
        return methodArgumentNotValidException.getBindingResult().getFieldErrors().stream().map(fieldError -> {
            return new ValidationError(fieldError.getObjectName(), fieldError.getDefaultMessage(), fieldError.getField());
        }).sorted((o1, o2) -> o1.getField().compareToIgnoreCase(o2.getField())).collect(Collectors.toList());
    }
}
