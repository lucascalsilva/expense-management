package com.mobnova.expense_mgt.config;

import com.mobnova.expense_mgt.config.mapper.ExpenseItemDtoV1ToModel;
import com.mobnova.expense_mgt.config.mapper.ExpenseReportDtoV1ToModel;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebAppConfiguration {

    @Bean
    public ModelMapper globalMapper() {
        ModelMapper expenseReportModelMapper = new ModelMapper();
        expenseReportModelMapper.addMappings(new ExpenseItemDtoV1ToModel());
        expenseReportModelMapper.addMappings(new ExpenseReportDtoV1ToModel());
        return expenseReportModelMapper;
    }
}
