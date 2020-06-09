package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.dto.CountryDto;
import com.mobnova.expense_mgt.dto.CountyDto;
import com.mobnova.expense_mgt.mapper.CountryEntityMapper;
import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.repositories.CountryRepository;
import com.mobnova.expense_mgt.services.dto.CountryService;
import com.mobnova.expense_mgt.validation.BeanValidator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("jpa")
public class CountryServiceJPAImpl extends BaseAbstractServiceJPA<Country, CountryDto, Long> implements CountryService {

    private CountryRepository countryRepository;

    public CountryServiceJPAImpl(CountryRepository countryRepository, CountryEntityMapper countryEntityMapper, BeanValidator beanValidator) {
        super(countryRepository, countryEntityMapper, beanValidator, CountryDto.class);
        this.countryRepository = countryRepository;
    }

    @Override
    public CountryDto findByCode(String code) {
        Optional<Country> countryByCode = countryRepository.findByCode(code);

        if(countryByCode.isEmpty()){
            throw new RuntimeException("Entity with id " + CountryDto.class.getName() + " not found");
        }
        else{
            return baseMapper.toDto(countryByCode.get());
        }
    }
}
