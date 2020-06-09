package com.mobnova.expense_mgt.services.dto;

import java.util.Set;

public interface SearchService<T, ID>{

    Set<T> search(String search);
}
