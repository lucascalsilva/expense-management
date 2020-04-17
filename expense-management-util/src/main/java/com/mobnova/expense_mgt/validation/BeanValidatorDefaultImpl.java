package com.mobnova.expense_mgt.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class BeanValidatorDefaultImpl implements BeanValidator {

    private final Validator validator;

    public void validateObject(Object object) {
        List<Object> singleObjectList = new ArrayList<>();
        singleObjectList.add(object);
        validateObjects(singleObjectList);
    }

    public void validateObjects(Iterator objects) {
        List<Object> list = new ArrayList<Object>();
        objects.forEachRemaining(list::add);
        validateObjects(list);
    }

    public void validateObjects(Collection objects) {
        for (Object object : objects) {
            Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);

            if (constraintViolations.size() > 0) {
                Set<ConstraintViolation<?>> propagatedViolations =
                        new HashSet<ConstraintViolation<?>>(constraintViolations.size());
                Set<String> classNames = new HashSet<String>();

                for (ConstraintViolation<?> violation : constraintViolations) {
                    log.trace(violation.toString());
                    propagatedViolations.add(violation);
                    classNames.add(violation.getLeafBean().getClass().getName());
                }
                StringBuilder builder = new StringBuilder();
                builder.append("Validation failed for classes ");
                builder.append(classNames);
                builder.append("\nList of constraint violations:[\n");

                for (ConstraintViolation<?> violation : constraintViolations) {
                    builder.append("\t").append(violation.toString()).append("\n");
                }
                builder.append("]");

                throw new ConstraintViolationException(
                        builder.toString(), propagatedViolations
                );
            }
        }
    }
}
