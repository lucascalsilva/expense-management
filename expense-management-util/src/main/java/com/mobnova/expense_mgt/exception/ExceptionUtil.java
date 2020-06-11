package com.mobnova.expense_mgt.exception;

import lombok.experimental.UtilityClass;
import org.mockito.internal.util.reflection.Fields;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ExceptionUtil {

    public static String buildExceptionMessage(Class class_, List<ExceptionVariable> exceptionVariables) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Object of type ").append(class_.getName()).append(" with\n");
        for (ExceptionVariable exceptionVariable  : exceptionVariables) {
            stringBuilder.append("field ").append(exceptionVariable.getField()).append(" equal to ").append(exceptionVariable.getValue()).append("\n");
        }

        stringBuilder.append("was not found");

        return stringBuilder.toString();
    }

}
