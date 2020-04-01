package com.mobnova.expense_mgt.services;

import java.util.Optional;

public interface NameCodeService<T, ID> {

    Optional<T> findByCode(ID code);
}
