package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.dto.v1.CountryDto;
import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.repositories.CountryRepository;
import com.mobnova.expense_mgt.services.CountryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("jpa")
public class CountryServiceJPAImpl extends AbstractNameCodeEntityBaseServiceJPA<Country, CountryDto, Long> implements CountryService {

    @Autowired
    public CountryServiceJPAImpl(CountryRepository countryRepository, ModelMapper modelMapper) {
        super(countryRepository, modelMapper, Country.class, CountryDto.class);
    }
}
