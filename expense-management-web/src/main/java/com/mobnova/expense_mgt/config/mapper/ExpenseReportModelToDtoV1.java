package com.mobnova.expense_mgt.config.mapper;

import com.mobnova.expense_mgt.model.ExpenseReport;
import com.mobnova.expense_mgt.rest.v1.dto.ExpenseReportDtoV1;
import org.modelmapper.PropertyMap;

public class ExpenseReportModelToDtoV1 extends PropertyMap<ExpenseReport, ExpenseReportDtoV1> {

    @Override
    protected void configure() {
        map().setCreator(source.getUser().getUsername());
    }
}
