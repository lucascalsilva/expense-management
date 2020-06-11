package com.mobnova.expense_mgt.services;

import com.mobnova.expense_mgt.model.ExpenseReport;

import java.util.Optional;

public interface ExpenseReportService extends BaseService<ExpenseReport, Long>, SearchService<ExpenseReport, Long> {

    ExpenseReport findByReferenceID(String referenceID);
}
