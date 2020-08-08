package com.mobnova.expense_mgt.config;

import com.mobnova.expense_mgt.config.mapper.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper globalMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new ExpenseReportDtoToModel());
        modelMapper.addMappings(new ExpenseReportModelToDto());

        modelMapper.addMappings(new ExpenseItemDtoToModel());
        modelMapper.addMappings(new ExpenseItemModelToDto());

        return modelMapper;
    }
}
