package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.repositories.CountryRepository;
import com.mobnova.expense_mgt.services.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class CountryServiceJPAImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    public Country save(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public Set<Country> saveBulk(Set<Country> countries) {
        return countries.stream().map(this::save).collect(Collectors.toSet());
    }

    @Override
    public Optional<Country> findById(Long id) {
        return countryRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        countryRepository.deleteById(id);
    }

    @Override
    public Optional<Country> findByCode(String code) {
        return countryRepository.findByCode(code);
    }
}
