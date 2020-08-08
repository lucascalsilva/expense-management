package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.dto.v1.CityDto;
import com.mobnova.expense_mgt.dto.v1.CountyDto;
import com.mobnova.expense_mgt.dto.v1.StateOrProvinceDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.City;
import com.mobnova.expense_mgt.model.County;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.CityRepository;
import com.mobnova.expense_mgt.repositories.CountyRepository;
import com.mobnova.expense_mgt.repositories.StateOrProvinceRepository;
import com.mobnova.expense_mgt.services.CityService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("jpa")
public class CityServiceJPAImpl extends AbstractNameCodeEntityBaseServiceJPA<City, CityDto, Long> implements CityService {

    private final CityRepository cityRepository;
    private final CountyRepository countyRepository;
    private final StateOrProvinceRepository stateOrProvinceRepository;

    public CityServiceJPAImpl(CityRepository cityRepository, CountyRepository countyRepository, StateOrProvinceRepository stateOrProvinceRepository, ModelMapper modelMapper) {
        super(cityRepository, modelMapper, City.class, CityDto.class);
        this.cityRepository = cityRepository;
        this.countyRepository = countyRepository;
        this.stateOrProvinceRepository = stateOrProvinceRepository;
    }

    @Override
    public CityDto save(CityDto cityDto) {
        City city = modelMapper.map(cityDto, City.class);

        if (city.getCounty() != null && city.getCounty().getCode() != null) {
            String countyCode = city.getCounty().getCode();
            countyRepository.findByCode(countyCode).ifPresentOrElse(city::setCounty, () -> {
                throw new DataNotFoundException(County.class, Fields.CODE, countyCode);
            });
        }

        String stateOrProvinceCode = city.getStateOrProvince().getCode();
        stateOrProvinceRepository.findByCode(stateOrProvinceCode).ifPresentOrElse(city::setStateOrProvince, () -> {
            throw new DataNotFoundException(StateOrProvince.class, Fields.CODE, stateOrProvinceCode);
        });

        City savedCity = cityRepository.save(city);

        return modelMapper.map(findById(city.getId()), CityDto.class);
    }
}