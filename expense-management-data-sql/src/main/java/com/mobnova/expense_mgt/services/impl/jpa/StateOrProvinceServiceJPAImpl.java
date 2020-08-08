package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.dto.v1.CountryDto;
import com.mobnova.expense_mgt.dto.v1.StateOrProvinceDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.CountryRepository;
import com.mobnova.expense_mgt.repositories.StateOrProvinceRepository;
import com.mobnova.expense_mgt.services.StateOrProvinceService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("jpa")
public class StateOrProvinceServiceJPAImpl extends AbstractNameCodeEntityBaseServiceJPA<StateOrProvince, StateOrProvinceDto, Long> implements StateOrProvinceService {

    private final StateOrProvinceRepository stateOrProvinceRepository;
    private final CountryRepository countryRepository;

    public StateOrProvinceServiceJPAImpl(StateOrProvinceRepository stateOrProvinceRepository, CountryRepository countryRepository, ModelMapper modelMapper) {
        super(stateOrProvinceRepository, modelMapper, StateOrProvince.class, StateOrProvinceDto.class);
        this.stateOrProvinceRepository = stateOrProvinceRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public StateOrProvinceDto save(StateOrProvinceDto stateOrProvinceDto) {
        String countryCode = stateOrProvinceDto.getCountry().getCode();
        countryRepository.findByCode(countryCode)
                .ifPresentOrElse(country -> {
                            CountryDto countryDto = modelMapper.map(country, CountryDto.class);
                            stateOrProvinceDto.setCountry(countryDto);
                        },
                        () -> {
                            throw new DataNotFoundException(Country.class, Fields.CODE, countryCode);
                        });

        StateOrProvince stateOrProvince = modelMapper.map(stateOrProvinceDto, StateOrProvince.class);
        StateOrProvince savedStateOrProvince = stateOrProvinceRepository.save(stateOrProvince);

        return modelMapper.map(findById(savedStateOrProvince.getId()), StateOrProvinceDto.class);
    }
}
