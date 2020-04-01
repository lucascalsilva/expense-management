package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.StateOrProvinceRepository;
import com.mobnova.expense_mgt.services.StateOrProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class StateOrProvinceServiceJPAImpl implements StateOrProvinceService {

    private final StateOrProvinceRepository stateOrProvinceRepository;

    @Override
    public StateOrProvince save(StateOrProvince stateOrProvince) {
        return stateOrProvinceRepository.save(stateOrProvince);
    }

    @Override
    public Set<StateOrProvince> saveBulk(Set<StateOrProvince> stateOrProvinces) {
        return stateOrProvinces.stream().map(this::save).collect(Collectors.toSet());
    }

    @Override
    public Optional<StateOrProvince> findById(Long id) {
        return stateOrProvinceRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        stateOrProvinceRepository.deleteById(id);
    }
}
