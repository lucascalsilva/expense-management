package com.mobnova.expense_mgt.mapper;

import com.mobnova.expense_mgt.dto.CountryDto;
import com.mobnova.expense_mgt.model.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper(
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface CountryEntityMapper extends BaseMapper<Country, CountryDto> {

    @Override
    Country toEntity(CountryDto object);

    @Override
    CountryDto toDto(Country object);
}
