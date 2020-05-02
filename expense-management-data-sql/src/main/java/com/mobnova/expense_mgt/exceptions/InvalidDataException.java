package com.mobnova.expense_mgt.exceptions;

import java.util.Map;

public class InvalidDataException extends RuntimeException {

    public InvalidDataException(Class type, Map<String, String> fieldValuePairs){
        super(formatMessage(type, fieldValuePairs));
    }

    public InvalidDataException(Class type, String field, String value){
        super(type.getName()+" with "+ field +": " + "\'" + value + "\'" + " provided was not found.");
    }

    public InvalidDataException(String message){
        super(message);
    }

    private static String formatMessage(Class type, String field, String value){
        return type.getName()+" with "+ field +": " + "\'" + value + "\'" + " provided was not found.";
    }

    private static String formatMessage(Class type, Map<String, String> fieldValuePairs){
        StringBuffer messageBuffer = new StringBuffer();
        messageBuffer.append(type.getName()).append(" with: \n");
        for(Map.Entry<String, String> fieldValuePair : fieldValuePairs.entrySet()){
            messageBuffer.append(fieldValuePair.getKey() +": " + "\'" + fieldValuePair.getValue() + "\'\n");
        }
        messageBuffer.append(" provided was not found.");
        return messageBuffer.toString();
    }
}
