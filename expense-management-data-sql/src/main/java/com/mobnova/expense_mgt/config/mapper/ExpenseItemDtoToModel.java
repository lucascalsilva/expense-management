package com.mobnova.expense_mgt.config.mapper;

import com.mobnova.expense_mgt.config.mapper.converter.SegmentValuePairsStringToEntityConverter;
import com.mobnova.expense_mgt.dto.v1.ExpenseItemDto;
import com.mobnova.expense_mgt.model.ExpenseItem;
import org.modelmapper.PropertyMap;

public class ExpenseItemDtoToModel extends PropertyMap<ExpenseItemDto, ExpenseItem> {

    @Override
    protected void configure() {
        map().getExpenseCity().setCode(source.getExpenseCity().getCityCode());
        skip().getExpenseCity().setName(null);

        map().getExpenseCity().getStateOrProvince().getCountry().setCode(source.getExpenseCity().getCountryCode());
        using(new SegmentValuePairsStringToEntityConverter()).map(source.getSegmentValuePairs()).setSegmentValuePairs(null);
    }
}
