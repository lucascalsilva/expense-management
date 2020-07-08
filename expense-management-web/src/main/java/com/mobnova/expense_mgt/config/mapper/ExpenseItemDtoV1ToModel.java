package com.mobnova.expense_mgt.config.mapper;

import com.mobnova.expense_mgt.model.ExpenseItem;
import com.mobnova.expense_mgt.rest.v1.dto.ExpenseItemDtoV1;
import org.modelmapper.PropertyMap;

public class ExpenseItemDtoV1ToModel extends PropertyMap<ExpenseItemDtoV1, ExpenseItem> {

    @Override
    protected void configure() {
        map().getExpenseCity().setCode(source.getExpenseCity().getCityCode());
        skip().getExpenseCity().setName(null);

        map().getExpenseCity().getStateOrProvince().getCountry().setCode(source.getExpenseCity().getCountryCode());
    }
}
