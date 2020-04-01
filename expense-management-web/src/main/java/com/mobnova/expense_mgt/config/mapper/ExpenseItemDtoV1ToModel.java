package com.mobnova.expense_mgt.config.mapper;

import com.mobnova.expense_mgt.model.City;
import com.mobnova.expense_mgt.model.ExpenseItem;
import com.mobnova.expense_mgt.model.ExpenseReport;
import com.mobnova.expense_mgt.rest.v1.dto.ExpenseItemDtoV1;
import com.mobnova.expense_mgt.rest.v1.dto.ExpenseReportDtoV1;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;

public class ExpenseItemDtoV1ToModel extends PropertyMap<ExpenseItemDtoV1, ExpenseItem> {

    @Override
    protected void configure() {
        if(source.getExpenseCity() != null && source.getExpenseCity().getCityCode() != null) {
            map().setExpenseCity(new City());
            map().getExpenseCity().setCode(source.getExpenseCity().getCityCode());
        }
        skip().getExpenseCity().setName(null);
    }
}
