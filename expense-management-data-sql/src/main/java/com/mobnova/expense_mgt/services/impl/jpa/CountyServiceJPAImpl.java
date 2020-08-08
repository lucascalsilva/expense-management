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
        County county = modelMapper.map(countyDto, County.class);

        if (county.getStateOrProvince() != null && county.getStateOrProvince().getCode() != null) {
            String stateOrProvinceCode = county.getStateOrProvince().getCode();
            stateOrProvinceRepository.findByCode(stateOrProvinceCode).ifPresentOrElse(county::setStateOrProvince, () -> {
                throw new DataNotFoundException(StateOrProvince.class, Fields.CODE, stateOrProvinceCode);
            });
        }
        County savedCounty = countyRepository.save(county);

        return modelMapper.map(findById(savedCounty.getId()), CountyDto.class);
    }
}
