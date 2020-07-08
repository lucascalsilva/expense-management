package com.mobnova.expense_mgt.config;

import com.mobnova.expense_mgt.config.mapper.ExpenseItemDtoV1ToModel;
import com.mobnova.expense_mgt.config.mapper.ExpenseItemModelToDtoV1;
import com.mobnova.expense_mgt.config.mapper.ExpenseReportDtoV1ToModel;
import com.mobnova.expense_mgt.config.mapper.ExpenseReportModelToDtoV1;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper globalMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new ExpenseItemDtoV1ToModel());
        modelMapper.addMappings(new ExpenseReportDtoV1ToModel());

        modelMapper.addMappings(new ExpenseReportModelToDtoV1());
        modelMapper.addMappings(new ExpenseItemModelToDtoV1());

        return modelMapper;
    }
}
