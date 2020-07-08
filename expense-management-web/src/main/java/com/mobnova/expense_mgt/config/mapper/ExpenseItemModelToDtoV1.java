package com.mobnova.expense_mgt.config.mapper;

import com.mobnova.expense_mgt.config.mapper.converter.SegmentValuePairsToMapConverter;
import com.mobnova.expense_mgt.model.ExpenseItem;
import com.mobnova.expense_mgt.rest.v1.dto.ExpenseItemDtoV1;
import org.modelmapper.PropertyMap;

public class ExpenseItemModelToDtoV1 extends PropertyMap<ExpenseItem, ExpenseItemDtoV1> {

    @Override
    protected void configure() {
        map().getExpenseCity().setCountryCode(source.getExpenseCity().getStateOrProvince().getCountry().getCode());
        using(new SegmentValuePairsToMapConverter()).map(source.getSegmentValuePairs()).setSegmentValuePairs(null);
    }
}
