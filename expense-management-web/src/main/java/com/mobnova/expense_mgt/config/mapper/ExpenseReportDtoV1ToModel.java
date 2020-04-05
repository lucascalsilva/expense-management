package com.mobnova.expense_mgt.config.mapper;

import com.mobnova.expense_mgt.model.City;
import com.mobnova.expense_mgt.model.ExpenseReport;
import com.mobnova.expense_mgt.model.User;
import com.mobnova.expense_mgt.rest.v1.dto.ExpenseReportDtoV1;
import org.modelmapper.PropertyMap;

public class ExpenseReportDtoV1ToModel  extends PropertyMap<ExpenseReportDtoV1, ExpenseReport> {

    @Override
    protected void configure() {
        map().setUser(new User());
        map().getUser().setUsername(source.getCreator());
    }
}
