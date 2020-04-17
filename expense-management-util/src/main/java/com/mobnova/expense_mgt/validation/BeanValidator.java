package com.mobnova.expense_mgt.validation;

import java.util.Collection;
import java.util.Iterator;

public interface BeanValidator {

    void validateObject(Object object);
    void validateObjects(Iterator objects);
    void validateObjects(Collection objects);
}
