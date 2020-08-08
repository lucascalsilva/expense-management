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
        if (cityDto.getCounty() != null && cityDto.getCounty().getCode() != null) {
            String countyCode = cityDto.getCounty().getCode();
            countyRepository.findByCode(countyCode).ifPresentOrElse(county -> {
                CountyDto countyDto = modelMapper.map(county, CountyDto.class);
                cityDto.setCounty(countyDto);
            }, () -> {
                throw new DataNotFoundException(County.class, Fields.CODE, countyCode);
            });
        }

        String stateOrProvinceCode = cityDto.getStateOrProvince().getCode();
        stateOrProvinceRepository.findByCode(stateOrProvinceCode).ifPresentOrElse(stateOrProvince -> {
            StateOrProvinceDto stateOrProvinceDto = modelMapper.map(stateOrProvince, StateOrProvinceDto.class);
            cityDto.setStateOrProvince(stateOrProvinceDto);
        }, () -> {
            throw new DataNotFoundException(StateOrProvince.class, Fields.CODE, stateOrProvinceCode);
        });

        City city = modelMapper.map(cityDto, City.class);
        City savedCity = cityRepository.save(city);

        return modelMapper.map(findById(city.getId()), CityDto.class);
    }
}