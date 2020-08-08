package com.mobnova.expense_mgt.config.mapper;

import com.mobnova.expense_mgt.config.mapper.converter.SegmentValuePairsEntityToStringConverter;
import com.mobnova.expense_mgt.dto.v1.ExpenseItemDto;
import com.mobnova.expense_mgt.model.ExpenseItem;
import org.modelmapper.PropertyMap;

public class ExpenseItemModelToDto extends PropertyMap<ExpenseItem, ExpenseItemDto> {

    @Override
    protected void configure() {
        map().getExpenseCity().setCountryCode(source.getExpenseCity().getStateOrProvince().getCountry().getCode());
        using(new SegmentValuePairsEntityToStringConverter()).map(source.getSegmentValuePairs()).setSegmentValuePairs(null);
    }
}
