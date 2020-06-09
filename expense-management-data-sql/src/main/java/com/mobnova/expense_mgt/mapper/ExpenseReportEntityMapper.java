package com.mobnova.expense_mgt.mapper;

import com.mobnova.expense_mgt.dto.ExpenseReportDto;
import com.mobnova.expense_mgt.model.ExpenseReport;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExpenseReportEntityMapper extends BaseMapper<ExpenseReport, ExpenseReportDto> {

    ExpenseReportEntityMapper INSTANCE = Mappers.getMapper( ExpenseReportEntityMapper.class );

    public abstract ExpenseReport toEntity(ExpenseReportDto expenseReportDto);

    public abstract ExpenseReportDto toDto(ExpenseReport expenseReport);
}
