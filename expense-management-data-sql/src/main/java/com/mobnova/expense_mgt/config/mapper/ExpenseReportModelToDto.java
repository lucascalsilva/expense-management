package com.mobnova.expense_mgt.config.mapper;

import com.mobnova.expense_mgt.dto.v1.ExpenseReportDto;
import com.mobnova.expense_mgt.model.ExpenseReport;
import org.modelmapper.PropertyMap;

public class ExpenseReportModelToDto extends PropertyMap<ExpenseReport, ExpenseReportDto> {

    @Override
    protected void configure() {
        map().setCreator(source.getUser().getUsername());
    }
}
