package com.mobnova.expense_mgt.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class BeanValidator {

    private final Validator validator;

    public void validateObject(Object object) {
        List<Object> singleObjectList = new ArrayList<>();
        singleObjectList.add(object);
            validateObjects(singleObjectList);
    }

    public void validateObjects(Iterator objects){
        List<Object> list = new ArrayList<Object>();
        objects.forEachRemaining(list::add);
        validateObjects(list);
    }

    public void validateObjects(Collection objects) {
        BeanValidationReport beanValidationReport = new BeanValidationReport();
        Boolean doThrowException = false;
        for (Object object : objects) {
            Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);
            if(constraintViolations.size() > 0){
                beanValidationReport.addValidationErrors(object.getClass().getName(), constraintViolations);
                if(!doThrowException) {
                    doThrowException = true;
                }
            }
        }

        if(doThrowException){
            beanValidationReport.getValidationErrors().forEach((class_, messages) -> {
                log.error("Validation errors in {}", class_);
                messages.forEach(log::error);
            });
            throw new BeanValidationsException("Validated beans contain errors");
        }
    }
}
