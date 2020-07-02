package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.Currency;
import com.mobnova.expense_mgt.repositories.CurrencyRepository;
import com.mobnova.expense_mgt.services.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class CurrencyServiceJPAImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Override
    public Currency save(Currency currency) {
        return currencyRepository.save(currency);
    }

    @Override
    public Set<Currency> saveBulk(Set<Currency> currencies) {
        return currencies.stream().map(this::save).collect(Collectors.toSet());
    }

    @Override
    public Currency findById(Long id) {
        return currencyRepository.findById(id).orElseThrow(() -> new DataNotFoundException(Currency.class, Fields.ID, id));
    }

    @Override
    public void deleteById(Long id) {
        currencyRepository.deleteById(id);
    }

    @Override
    public Currency findByCode(String code) {
        return currencyRepository.findByCode(code).orElseThrow(() -> new DataNotFoundException(Currency.class, Fields.CODE, code));
    }
}
