package com.mobnova.expense_mgt.config;

import com.mobnova.expense_mgt.config.mapper.ExpenseItemDtoV1ToModel;
import com.mobnova.expense_mgt.config.mapper.ExpenseItemModelToDtoV1;
import com.mobnova.expense_mgt.config.mapper.ExpenseReportDtoV1ToModel;
import com.mobnova.expense_mgt.config.mapper.ExpenseReportModelToDtoV1;
import com.mobnova.expense_mgt.model.*;
import com.mobnova.expense_mgt.rest.v1.dto.CityDtoV1;
import com.mobnova.expense_mgt.rest.v1.dto.ExpenseItemDtoV1;
import com.mobnova.expense_mgt.rest.v1.dto.ExpenseReportDtoV1;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
