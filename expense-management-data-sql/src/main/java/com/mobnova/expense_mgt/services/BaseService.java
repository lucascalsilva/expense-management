package com.mobnova.expense_mgt.services;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Set;

@Validated
public interface BaseService<T, ID> {

    T save(@Valid T object);
    Set<T> saveBulk(Set<T> objects);
    T findById(ID id);
    void deleteById(ID id);

}
