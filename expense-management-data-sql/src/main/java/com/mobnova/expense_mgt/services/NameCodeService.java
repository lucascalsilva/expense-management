package com.mobnova.expense_mgt.services;

public interface NameCodeService<T, ID> {

    T findByCode(ID code);
}
