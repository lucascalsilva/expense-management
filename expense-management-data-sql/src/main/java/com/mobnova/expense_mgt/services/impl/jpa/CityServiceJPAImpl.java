package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.InvalidDataException;
import com.mobnova.expense_mgt.model.City;
import com.mobnova.expense_mgt.model.County;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.CityRepository;
import com.mobnova.expense_mgt.repositories.CountyRepository;
import com.mobnova.expense_mgt.repositories.StateOrProvinceRepository;
import com.mobnova.expense_mgt.services.CityService;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.validation.BeanValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class CityServiceJPAImpl implements CityService {

    private final CityRepository cityRepository;
    private final CountyRepository countyRepository;
    private final StateOrProvinceRepository stateOrProvinceRepository;
    private final BeanValidator beanValidator;

    @Override
    public City save(City city) {
        beanValidator.validateObject(city);

        if (city.getCounty() != null && city.getCounty().getCode() != null) {
            String countyCode = city.getCounty().getCode();
            countyRepository.findByCode(countyCode).ifPresentOrElse(county -> {
                city.setCounty(county);
            }, () -> {
                throw new InvalidDataException(County.class, "countyCode", countyCode);
            });
        }

        String stateOrProvinceCode = city.getStateOrProvince().getCode();
        stateOrProvinceRepository.findByCode(stateOrProvinceCode).ifPresentOrElse(stateOrProvince -> {
            city.setStateOrProvince(stateOrProvince);
        }, () -> {
            throw new InvalidDataException(StateOrProvince.class, "stateOrProvinceCode", stateOrProvinceCode);
        });
        return cityRepository.save(city);
    }

    @Override
    public Set<City> saveBulk(Set<City> cities) {
        return cities.stream().map(this::save).collect(Collectors.toSet());
    }

    @Override
    public City findById(Long id) {
        return cityRepository.findById(id).orElseThrow(() -> new DataNotFoundException(City.class, Fields.ID, id));
    }

    @Override
    public void deleteById(Long id) {
        cityRepository.deleteById(id);
    }

    @Override
    public City findByCode(String code) {
        return cityRepository.findByCode(code).orElseThrow(() -> new DataNotFoundException(City.class, Fields.CODE, code));
    }
}