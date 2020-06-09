package com.mobnova.expense_mgt.services.dto;

public interface NameCodeService<T, ID> {

    T findByCode(ID code);
}
