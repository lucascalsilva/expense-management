package com.mobnova.expense_mgt.services.dto;

import com.mobnova.expense_mgt.dto.ExpenseReportDto;

import java.util.Optional;

public interface ExpenseReportService extends BaseService<ExpenseReportDto, Long>, SearchService<ExpenseReportDto, Long> {

    Optional<ExpenseReportDto> findByReferenceID(String referenceID);
}
