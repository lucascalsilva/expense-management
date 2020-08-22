package com.mobnova.expense_mgt.dto.v1.error;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.FieldError;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApiValidationErrorDto extends ApiSubErrorDto {

    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    public ApiValidationErrorDto(FieldError fieldError){
        this.setField(fieldError.getField());
        this.setMessage(fieldError.getDefaultMessage());
        this.setObject(fieldError.getObjectName());
        this.setRejectedValue(fieldError.getRejectedValue());
    }

    public ApiValidationErrorDto(){

    }
}
