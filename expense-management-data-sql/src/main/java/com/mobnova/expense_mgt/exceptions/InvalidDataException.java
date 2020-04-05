package com.mobnova.expense_mgt.exceptions;

import java.lang.reflect.Field;

public class InvalidDataException extends RuntimeException {

    public InvalidDataException(Class type, String field, String value){
        super(type.getName()+" with "+ field +": " + "\'" + value + "\'" + " provided was not found.");
    }

    public InvalidDataException(String message){
        super(message);
    }
}
