package com.mobnova.expense_mgt.config.mapper;

import com.mobnova.expense_mgt.config.mapper.converter.SegmentValuePairsToMapConverter;
import com.mobnova.expense_mgt.model.ExpenseItem;
import com.mobnova.expense_mgt.model.SegmentValuePair;
import com.mobnova.expense_mgt.rest.v1.dto.ExpenseItemDtoV1;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenseItemModelToDtoV1 extends PropertyMap<ExpenseItem, ExpenseItemDtoV1> {

    @Override
    protected void configure() {
        map().getExpenseCity().setCountryCode(source.getExpenseCity().getStateOrProvince().getCountry().getCode());
        using(new SegmentValuePairsToMapConverter()).map(source.getSegmentValuePairs()).setSegmentValuePairs(null);
    }
}
