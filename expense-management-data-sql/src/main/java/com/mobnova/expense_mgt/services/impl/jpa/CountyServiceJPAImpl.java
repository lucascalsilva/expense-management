package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.dto.v1.CountyDto;
import com.mobnova.expense_mgt.dto.v1.StateOrProvinceDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.County;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.CountyRepository;
import com.mobnova.expense_mgt.repositories.StateOrProvinceRepository;
import com.mobnova.expense_mgt.services.CountyService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("jpa")
public class CountyServiceJPAImpl extends AbstractNameCodeEntityBaseServiceJPA<County, CountyDto, Long> implements CountyService {

    private final CountyRepository countyRepository;
    private final StateOrProvinceRepository stateOrProvinceRepository;

    public CountyServiceJPAImpl(CountyRepository countyRepository, StateOrProvinceRepository stateOrProvinceRepository, ModelMapper modelMapper) {
        super(countyRepository, modelMapper, County.class, CountyDto.class);
        this.countyRepository = countyRepository;
        this.stateOrProvinceRepository = stateOrProvinceRepository;
    }

    @Override
    public CountyDto save(CountyDto countyDto) {
        if (countyDto.getStateOrProvince() != null && countyDto.getStateOrProvince().getCode() != null) {
            String stateOrProvinceCode = countyDto.getStateOrProvince().getCode();
            stateOrProvinceRepository.findByCode(stateOrProvinceCode).ifPresentOrElse(stateOrProvince -> {
                StateOrProvinceDto stateOrProvinceDto = modelMapper.map(stateOrProvince, StateOrProvinceDto.class);
                countyDto.setStateOrProvince(stateOrProvinceDto);
            }, () -> {
                throw new DataNotFoundException(StateOrProvince.class, Fields.CODE, stateOrProvinceCode);
            });
        }
        County county = modelMapper.map(countyDto, County.class);
        County savedCounty = countyRepository.save(county);

        return modelMapper.map(findById(savedCounty.getId()), CountyDto.class);
    }
}
