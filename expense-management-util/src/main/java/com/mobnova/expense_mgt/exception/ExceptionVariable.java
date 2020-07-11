package com.mobnova.expense_mgt.exception;

import com.mobnova.expense_mgt.exception.constant.Fields;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ExceptionVariable {

    private Fields field;
    private Object value;

}
