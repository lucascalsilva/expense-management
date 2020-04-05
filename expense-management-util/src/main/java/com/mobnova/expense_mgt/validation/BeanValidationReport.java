package com.mobnova.expense_mgt.validation;

import lombok.Getter;

import javax.validation.ConstraintViolation;
import java.util.*;

@Getter
public class BeanValidationReport {

    private Map<String, List<String>> validationErrors = new HashMap<String, List<String>>();

    public void addValidationErrors(String class_, Set<ConstraintViolation<Object>> constraintViolations){
        List<String> validationMessages = validationErrors.computeIfAbsent(class_, k -> new ArrayList<String>());
        validationMessages.addAll(formatMessage(constraintViolations));
    }

    private List<String> formatMessage(Set<ConstraintViolation<Object>> constraintViolations){
        List<String> messages = new ArrayList<String>();

        constraintViolations.forEach(violation -> {
            String message = "Property "+ violation.getPropertyPath() +" in "+ violation.getRootBeanClass() + " " + violation.getMessage();
            messages.add(message);
        });

        return messages;
    }
}
