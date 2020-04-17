package com.mobnova.expense_mgt.criteria;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class SearchCriteria {

    private final String key;
    private final String operation;
    private final Object value;
    private boolean orPredicate;
}