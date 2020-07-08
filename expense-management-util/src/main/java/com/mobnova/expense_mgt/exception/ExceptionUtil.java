package com.mobnova.expense_mgt.exception;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ExceptionUtil {

    public static String buildExceptionMessage(Class class_, List<ExceptionVariable> exceptionVariables) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Object of type ").append(class_.getName()).append(" with ");
        int i = 0;
        for (ExceptionVariable exceptionVariable  : exceptionVariables) {
            stringBuilder.append("field ").append(exceptionVariable.getField()).append(" equal to ").append(exceptionVariable.getValue());
            if(i + 1 == exceptionVariables.size()) {
                stringBuilder.append(", ");
            }
            else{
                stringBuilder.append(" and ");
            }
            i++;

        }

        stringBuilder.append("was not found");

        return stringBuilder.toString();
    }

}
