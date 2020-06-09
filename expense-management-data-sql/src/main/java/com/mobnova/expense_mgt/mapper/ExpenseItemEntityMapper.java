package com.mobnova.expense_mgt.mapper;

import com.mobnova.expense_mgt.dto.ExpenseItemDto;
import com.mobnova.expense_mgt.model.ExpenseItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface  ExpenseItemEntityMapper extends BaseMapper<ExpenseItem, ExpenseItemDto> {

    ExpenseItemEntityMapper INSTANCE = Mappers.getMapper( ExpenseItemEntityMapper.class );

    public abstract ExpenseItem toEntity(ExpenseItemDto expenseItemDto);

    public abstract ExpenseItemDto toDto(ExpenseItem expenseItem);
}
