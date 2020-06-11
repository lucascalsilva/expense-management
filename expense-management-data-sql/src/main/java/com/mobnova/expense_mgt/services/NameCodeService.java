package com.mobnova.expense_mgt.services;

import java.util.Optional;

public interface NameCodeService<T, ID> {

    T findByCode(ID code);
}
