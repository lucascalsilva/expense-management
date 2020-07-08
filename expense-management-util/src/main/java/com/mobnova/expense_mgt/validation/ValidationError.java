package com.mobnova.expense_mgt.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class ValidationError {

    private final String object;
    private final String message;
    private final String field;
}
