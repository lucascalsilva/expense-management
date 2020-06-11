package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.repositories.CountryRepository;
import com.mobnova.expense_mgt.services.impl.jpa.CountryServiceJPAImpl;
import com.mobnova.expense_mgt.validation.BeanValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceJPAImplTest {

    @InjectMocks
    @Spy
    private CountryServiceJPAImpl countryServiceJPA;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private BeanValidator beanValidator;

    @Test
    void save() {
        Country country = Country.builder().id(1L).code("BR")
                .name("Brazil").build();

        doAnswer(returnsFirstArg()).when(countryRepository).save(country);

        countryServiceJPA.save(country);

        verify(countryRepository).save(country);
    }

    @Test
    void saveBulk() {
        Country country1 = Country.builder().code("BR")
                .name("Brazil").build();
        Country country2 = Country.builder().code("US")
                .name("United States of America").build();

        Set<Country> countries = new HashSet<>();

        countries.add(country1);
        countries.add(country2);

        doAnswer(returnsFirstArg()).when(countryRepository).save(any(Country.class));

        Set<Country> savedCountries = countryServiceJPA.saveBulk(countries);

        for(Country country : savedCountries){
            verify(countryRepository, times(1)).save(country);
            verify(countryServiceJPA, times(1)).save(country);
        }
    }

    @Test
    void findById() {
        Country country = Country.builder().id(1L).code("BR")
                .name("Brazil").build();

        when(countryRepository.findById(country.getId())).thenReturn(Optional.of(country));

        Country countryById = countryServiceJPA.findById(1L);

        assertThat(countryById).isEqualTo(country);
    }

    @Test
    void deleteById() {
        countryServiceJPA.deleteById(1L);

        verify(countryRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByCode() {
        Country country = Country.builder().id(1L).code("BR")
                .name("Brazil").build();

        when(countryRepository.findByCode(country.getCode())).thenReturn(Optional.of(country));

        Country countryByCode = countryServiceJPA.findByCode("BR");

        verify(countryRepository, times(1)).findByCode(country.getCode());

        assertThat(countryByCode).isEqualTo(country);
    }
}