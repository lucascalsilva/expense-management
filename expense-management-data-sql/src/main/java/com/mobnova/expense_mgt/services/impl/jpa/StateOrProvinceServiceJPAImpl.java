package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.exceptions.InvalidDataException;
import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.CountryRepository;
import com.mobnova.expense_mgt.repositories.StateOrProvinceRepository;
import com.mobnova.expense_mgt.services.StateOrProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class StateOrProvinceServiceJPAImpl implements StateOrProvinceService {

    private final StateOrProvinceRepository stateOrProvinceRepository;
    private final CountryRepository countryRepository;

    @Override
    public StateOrProvince save(StateOrProvince stateOrProvince) {
        String countryCode = stateOrProvince.getCountry().getCode();
        countryRepository.findByCode(countryCode)
                .ifPresentOrElse(country -> {
                            stateOrProvince.setCountry(country);
                        },
                        () -> {
                            throw new InvalidDataException(Country.class, "countryCode", countryCode);
                        });

        return stateOrProvinceRepository.save(stateOrProvince);
    }

    @Override
    public Set<StateOrProvince> saveBulk(Set<StateOrProvince> stateOrProvinces) {
        return stateOrProvinces.stream().map(this::save).collect(Collectors.toSet());
    }

    @Override
    public StateOrProvince findById(Long id) {
        return stateOrProvinceRepository.findById(id).orElseThrow(() -> new DataNotFoundException(StateOrProvince.class, Fields.ID, id));
    }

    @Override
    public void deleteById(Long id) {
        stateOrProvinceRepository.deleteById(id);
    }

    @Override
    public StateOrProvince findByCode(String code) {
        return stateOrProvinceRepository.findByCode(code).orElseThrow(() -> new DataNotFoundException(StateOrProvince.class, Fields.CODE, code));
    }
}
