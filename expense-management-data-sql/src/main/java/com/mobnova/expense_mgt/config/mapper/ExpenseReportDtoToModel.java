package com.mobnova.expense_mgt.config.mapper;

import com.mobnova.expense_mgt.dto.v1.ExpenseReportDto;
import com.mobnova.expense_mgt.model.ExpenseReport;
import org.modelmapper.PropertyMap;

public class ExpenseReportDtoToModel extends PropertyMap<ExpenseReportDto, ExpenseReport> {

    @Override
    protected void configure() {

        map(source.getCreator()).getUser().setUsername(null);

    }
}
