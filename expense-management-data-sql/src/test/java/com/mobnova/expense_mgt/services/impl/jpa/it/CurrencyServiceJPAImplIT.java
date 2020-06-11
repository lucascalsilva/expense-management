package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.Currency;
import com.mobnova.expense_mgt.services.impl.jpa.CurrencyServiceJPAImpl;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CurrencyServiceJPAImplIT {

    @Autowired
    private CurrencyServiceJPAImpl currencyServiceJPA;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    private static Boolean dbInitialized = false;

    @BeforeEach
    public void init(){
        if(!dbInitialized) {
            integrationTestHelper.cleanAllData();
        }
    }

    @Test
    void save() {
        Currency currency = Currency.builder().code("BRL").name("Brazilian Real").build();

        Currency savedCurrency  = currencyServiceJPA.save(currency);

        assertThat(savedCurrency.getId()).isNotNull();
        assertThat(savedCurrency.getCreationDate()).isNotNull();
    }

    @Test
    void saveBulk() {
        Currency currency1 = Currency.builder().code("USD").name("American Dollar").build();
        Currency currency2 = Currency.builder().code("EUR").name("Euro").build();

        Set<Currency> currencies = new HashSet<>();
        currencies.add(currency1);
        currencies.add(currency2);

        Set<Currency> savedCurrencies = currencyServiceJPA.saveBulk(currencies);

        for(Currency currency : currencies){
            assertThat(currency.getId()).isNotNull();
            assertThat(currency.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        Currency currency = Currency.builder().code("GBP").name("British Pound").build();

        Currency savedCurrency  = currencyServiceJPA.save(currency);
        Long currencyId = savedCurrency.getId();

        Currency currencyById = currencyServiceJPA.findById(currencyId);

        assertThat(currencyById).isEqualTo(savedCurrency);
    }

    @Test
    void deleteById() {
        Currency currency = Currency.builder().code("GBP").name("British Pound").build();

        Currency savedCurrency  = currencyServiceJPA.save(currency);
        Long currencyId = savedCurrency.getId();

        currencyServiceJPA.deleteById(currencyId);

        assertThrows(DataNotFoundException.class, () -> currencyServiceJPA.findById(currencyId));
    }

    @Test
    void findByCode() {
        Currency currency = Currency.builder().code("GBP").name("British Pound").build();

        Currency savedCurrency  = currencyServiceJPA.save(currency);
        Long currencyId = savedCurrency.getId();

        Currency currencyById = currencyServiceJPA.findById(currencyId);

        assertThat(currencyById).isEqualTo(savedCurrency);
    }
}