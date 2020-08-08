package com.mobnova.expense_mgt.services;

import com.mobnova.expense_mgt.dto.v1.ExpenseReportDto;

public interface ExpenseReportService extends BaseService<ExpenseReportDto, Long>, SearchService<ExpenseReportDto, Long> {

    ExpenseReportDto findByReferenceID(String referenceID);
}
