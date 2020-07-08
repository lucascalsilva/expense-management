package com.mobnova.expense_mgt.services;

import com.mobnova.expense_mgt.model.ExpenseReport;

public interface ExpenseReportService extends BaseService<ExpenseReport, Long>, SearchService<ExpenseReport, Long> {

    ExpenseReport findByReferenceID(String referenceID);
}
