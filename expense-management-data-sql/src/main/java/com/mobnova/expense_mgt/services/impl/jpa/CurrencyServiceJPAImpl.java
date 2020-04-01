package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.model.Currency;
import com.mobnova.expense_mgt.repositories.CurrencyRepository;
import com.mobnova.expense_mgt.services.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class CurrencyServiceJPAImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Override
    public Currency save(Currency object) {
        return currencyRepository.save(object);
    }

    @Override
    public Set<Currency> saveBulk(Set<Currency> objects) {
        return objects.stream().map(this::save).collect(Collectors.toSet());
    }

    @Override
    public Optional<Currency> findById(Long id) {
        return currencyRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        currencyRepository.deleteById(id);
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        return currencyRepository.findByCode(code);
    }
}
