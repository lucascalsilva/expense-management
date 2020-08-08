package com.mobnova.expense_mgt.services;

import com.mobnova.expense_mgt.dto.v1.ExpenseCategoryDto;

public interface ExpenseCategoryService extends BaseService<ExpenseCategoryDto, Long>, NameCodeService<ExpenseCategoryDto, String> {
}
