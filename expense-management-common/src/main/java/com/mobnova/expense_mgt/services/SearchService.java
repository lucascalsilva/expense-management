package com.mobnova.expense_mgt.services;

import java.util.Set;

public interface SearchService<T, ID>{

    Set<T> search(String search);
}
