package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.InvalidDataException;
import com.mobnova.expense_mgt.model.County;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.CountyRepository;
import com.mobnova.expense_mgt.repositories.StateOrProvinceRepository;
import com.mobnova.expense_mgt.services.CountyService;
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
public class CountyServiceJPAImpl implements CountyService {

    private final CountyRepository countyRepository;
    private final StateOrProvinceRepository stateOrProvinceRepository;
    private final BeanValidator beanValidator;

    @Override
    public County save(County county) {
        beanValidator.validateObject(county);

        if (county.getStateOrProvince() != null && county.getStateOrProvince().getCode() != null) {
            String stateOrProvinceCode = county.getStateOrProvince().getCode();
            stateOrProvinceRepository.findByCode(stateOrProvinceCode).ifPresentOrElse(stateOrProvince -> {
                county.setStateOrProvince(stateOrProvince);
            }, () -> {
                throw new InvalidDataException(StateOrProvince.class, "stateOrProvinceCode", stateOrProvinceCode);
            });
        }
        return countyRepository.save(county);
    }

    @Override
    public Set<County> saveBulk(Set<County> counties) {
        return counties.stream().map(this::save).collect(Collectors.toSet());
    }

    @Override
    public County findById(Long id) {
        return countyRepository.findById(id).orElseThrow(() -> new DataNotFoundException(County.class, Fields.ID, id));
    }

    @Override
    public void deleteById(Long id) {
        countyRepository.deleteById(id);
    }

    @Override
    public County findByCode(String code) {
        return countyRepository.findByCode(code).orElseThrow(() -> new DataNotFoundException(County.class, Fields.CODE, code));
    }
}
