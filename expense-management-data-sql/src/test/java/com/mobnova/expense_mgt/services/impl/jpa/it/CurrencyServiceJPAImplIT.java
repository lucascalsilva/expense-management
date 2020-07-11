package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.Currency;
import com.mobnova.expense_mgt.services.impl.jpa.CurrencyServiceJPAImpl;
import com.mobnova.expense_mgt.util.AssertionsUtil;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CurrencyServiceJPAImplIT {

    @Autowired
    private CurrencyServiceJPAImpl currencyServiceJPA;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    private static final Boolean dbInitialized = false;

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
    void saveValidationError() {
        Currency currency = Currency.builder().code(null).name("Brazilian Real").build();

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> currencyServiceJPA.save(currency));
        Assertions.assertThat(constraintViolationException.getMessage()).contains("save.arg0.code: must not be blank");
    }

    @Test
    void update() {
        Currency currency = Currency.builder().code("CHF").name("Swiss Frank").build();

        currency = currencyServiceJPA.save(currency);

        assertThat(currency).isNotNull();
        assertThat(currency.getId()).isNotNull();

        Currency updatedCurrency = currencyServiceJPA.findById(currency.getId());

        updatedCurrency.setName("Condado de Torres_1");

        updatedCurrency = currencyServiceJPA.save(updatedCurrency);

        AssertionsUtil.entityUpdateAssertions(currency, updatedCurrency);
        assertThat(currency.getName()).isNotEqualTo(updatedCurrency.getName());
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

    @Test
    void findByCodeNotFound() {
        String code = "1000";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> currencyServiceJPA.findByCode(code));
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Currency.class.getName());
        assertThat(dataNotFoundException.getMessage().compareToIgnoreCase(Fields.CODE.toString()));
        assertThat(dataNotFoundException.getMessage().compareToIgnoreCase(code));
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> currencyServiceJPA.findById(id));
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Currency.class.getName());
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}