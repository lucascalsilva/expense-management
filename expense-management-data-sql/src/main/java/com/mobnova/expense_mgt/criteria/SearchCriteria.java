package com.mobnova.expense_mgt.criteria;

import lombok.*;

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