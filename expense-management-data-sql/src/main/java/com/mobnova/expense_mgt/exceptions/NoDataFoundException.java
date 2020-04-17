package com.mobnova.expense_mgt.exceptions;

public class NoDataFoundException extends RuntimeException {

    public NoDataFoundException(){
        super();
    }

    public NoDataFoundException(String message){
        super(message);
    }

    public NoDataFoundException(String field, Long id, Class type){
        super("No " + type.getSimpleName() +  " found with " + field + " with value: " + id);
    }

}
