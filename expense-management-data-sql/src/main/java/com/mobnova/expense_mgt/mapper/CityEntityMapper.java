package com.mobnova.expense_mgt.mapper;

import com.mobnova.expense_mgt.dto.CityDto;
import com.mobnova.expense_mgt.model.City;
import org.mapstruct.factory.Mappers;

public interface CityEntityMapper extends BaseMapper<City, CityDto> {

    CityEntityMapper INSTANCE = Mappers.getMapper(CityEntityMapper.class);

    public abstract City toEntity(CityDto cityDto);

    public abstract CityDto toDto(City city);
}
