package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.Currency;
import com.mobnova.expense_mgt.repositories.CurrencyRepository;
import com.mobnova.expense_mgt.services.impl.jpa.CurrencyServiceJPAImpl;
import com.mobnova.expense_mgt.validation.BeanValidator;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceJPAImplTest {

    @InjectMocks
    @Spy
    private CurrencyServiceJPAImpl currencyServiceJPA;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private BeanValidator beanValidator;

    @Test
    void save() {
        Currency currency = Currency.builder().code("BRL").name("Brazilian Real").build();

        doAnswer(returnsFirstArg()).when(currencyRepository).save(currency);

        currencyServiceJPA.save(currency);

        verify(currencyRepository).save(currency);
    }

    @Test
    void saveBulk() {
        Currency currency1 = Currency.builder().code("BRL").name("Brazilian Real").build();
        Currency currency2 = Currency.builder().code("USD").name("American Dollar").build();

        Set<Currency> currencies = new HashSet<>();

        currencies.add(currency1);
        currencies.add(currency2);

        doAnswer(returnsFirstArg()).when(currencyRepository).save(any(Currency.class));

        Set<Currency> savedCurrencies = currencyServiceJPA.saveBulk(currencies);

        for(Currency currency : savedCurrencies){
            verify(currencyRepository, times(1)).save(currency);
            verify(currencyServiceJPA, times(1)).save(currency);
        }
    }

    @Test
    void findById() {
        Currency currency = Currency.builder().id(1L).code("BRL").name("Brazilian Real").build();

        when(currencyRepository.findById(currency.getId())).thenReturn(Optional.of(currency));

        Currency currencyById = currencyServiceJPA.findById(1L);

        assertThat(currencyById).isEqualTo(currency);
    }

    @Test
    void deleteById() {
        currencyServiceJPA.deleteById(1L);

        verify(currencyRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByCode() {
        Currency currency = Currency.builder().code("BRL").name("Brazilian Real").build();

        when(currencyRepository.findByCode(currency.getCode())).thenReturn(Optional.of(currency));

        Currency currencyByCode = currencyServiceJPA.findByCode("BRL");

        verify(currencyRepository, times(1)).findByCode(currency.getCode());

        assertThat(currencyByCode).isEqualTo(currency);
    }

    @Test
    void findByCodeNotFound() {
        String code = "1000";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> currencyServiceJPA.findByCode(code));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Currency.class.getName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage().compareToIgnoreCase(Fields.CODE.toString()));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage().compareToIgnoreCase(code));
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> currencyServiceJPA.findById(id));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Currency.class.getName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}