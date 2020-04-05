package com.mobnova.expense_mgt.validation;

import javax.validation.ValidationException;

public class BeanValidationsException extends ValidationException {

    public BeanValidationsException(String message) {
        super(message);
    }

}
