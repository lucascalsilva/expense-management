package com.mobnova.expense_mgt.mapper;

import com.mobnova.expense_mgt.model.ExpenseItem;

public interface BaseMapper<E, D> {

    E toEntity(D object);
    D toDto(E object);

}
