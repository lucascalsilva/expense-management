package com.mobnova.expense_mgt.exceptions;

import com.mobnova.expense_mgt.exception.ExceptionVariable;
import com.mobnova.expense_mgt.exception.constant.Fields;

import java.util.List;

import static com.mobnova.expense_mgt.exception.ExceptionUtil.buildExceptionMessage;

public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException(Class class_, Fields field, Object value){
        super(class_.getSimpleName()+ " with '" + field + "' equal to '" + value + "' was not found.");
    }

    public DataNotFoundException(Class class_, List<ExceptionVariable> exceptionVariables){
        super(buildExceptionMessage(class_, exceptionVariables));
    }
}
