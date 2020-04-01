package com.mobnova.expense_mgt.config;

import com.mobnova.expense_mgt.config.mapper.ExpenseItemDtoV1ToModel;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public ModelMapper globalMapper() {
        ModelMapper expenseReportModelMapper = new ModelMapper();
        expenseReportModelMapper.addMappings(new ExpenseItemDtoV1ToModel());
        return expenseReportModelMapper;
    }
}
