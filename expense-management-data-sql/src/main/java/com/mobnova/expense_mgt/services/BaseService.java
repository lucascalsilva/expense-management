package com.mobnova.expense_mgt.services;

import java.util.Optional;
import java.util.Set;

public interface BaseService<T, ID> {

    T save(T object);
    Set<T> saveBulk(Set<T> objects);
    T findById(ID id);
    void deleteById(ID id);

}
