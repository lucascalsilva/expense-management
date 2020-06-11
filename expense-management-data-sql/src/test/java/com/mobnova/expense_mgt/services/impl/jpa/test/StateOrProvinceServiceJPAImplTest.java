package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.model.City;
import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.model.County;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.CountryRepository;
import com.mobnova.expense_mgt.repositories.StateOrProvinceRepository;
import com.mobnova.expense_mgt.services.impl.jpa.StateOrProvinceServiceJPAImpl;
import com.mobnova.expense_mgt.validation.BeanValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StateOrProvinceServiceJPAImplTest {

    @InjectMocks
    @Spy
    private StateOrProvinceServiceJPAImpl stateOrProvinceServiceJPA;

    @Mock
    private StateOrProvinceRepository stateOrProvinceRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private BeanValidator beanValidator;

    private Country country;

    @BeforeEach
    void init(){
        country = Country.builder().id(1L).code("BR").name("Brazil").build();
    }


    @Test
    void save() {
        StateOrProvince stateOrProvince = StateOrProvince.builder().code("RS")
                .name("Rio Grande do Sul").country(Country.builder().code("BR").build())
                .build();

        doAnswer(returnsFirstArg()).when(stateOrProvinceRepository).save(any(StateOrProvince.class));

        when(countryRepository.findByCode(country.getCode())).thenReturn(Optional.of(country));

        StateOrProvince savedStateOrProvince = stateOrProvinceServiceJPA.save(stateOrProvince);

        verify(countryRepository, times(1)).findByCode(country.getCode());
        verify(stateOrProvinceRepository, times(1)).save(stateOrProvince);

        assertThat(savedStateOrProvince.getCountry().getId()).isEqualTo(1L);
    }

    @Test
    void saveBulk() {
        StateOrProvince stateOrProvince1 = StateOrProvince.builder().code("RS")
                .name("Rio Grande do Sul").country(Country.builder().code("BR").build())
                .build();

        StateOrProvince stateOrProvince2 = StateOrProvince.builder().code("SC")
                .name("Santa Catarina").country(Country.builder().code("BR").build())
                .build();

        Set<StateOrProvince> stateOrProvinces = new HashSet<>();
        stateOrProvinces.add(stateOrProvince1);
        stateOrProvinces.add(stateOrProvince2);

        doAnswer(returnsFirstArg()).when(stateOrProvinceRepository).save(any(StateOrProvince.class));

        when(countryRepository.findByCode(country.getCode())).thenReturn(Optional.of(country));

        Set<StateOrProvince> savedStateOrProvinces = stateOrProvinceServiceJPA.saveBulk(stateOrProvinces);

        verify(countryRepository, times(savedStateOrProvinces.size())).findByCode(country.getCode());

        for(StateOrProvince stateOrProvince : savedStateOrProvinces){
            verify(stateOrProvinceRepository, times(1)).save(stateOrProvince);
            verify(stateOrProvinceServiceJPA, times(1)).save(stateOrProvince);

            assertThat(stateOrProvince.getCountry().getId()).isEqualTo(1L);
        }
    }

    @Test
    void findById() {
        StateOrProvince stateOrProvince = StateOrProvince.builder().id(1L).code("RS")
                .name("Rio Grande do Sul").country(Country.builder().code("BR").build())
                .build();

        when(stateOrProvinceRepository.findById(stateOrProvince.getId())).thenReturn(Optional.of(stateOrProvince));

        StateOrProvince stateOrProvinceById = stateOrProvinceServiceJPA.findById(1L);

        verify(stateOrProvinceRepository, times(1)).findById(1L);

        assertThat(stateOrProvinceById).isEqualTo(stateOrProvince);
    }

    @Test
    void deleteById() {
        stateOrProvinceServiceJPA.deleteById(1L);

        verify(stateOrProvinceRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByCode() {
        StateOrProvince stateOrProvince = StateOrProvince.builder().code("RS")
                .name("Rio Grande do Sul").country(Country.builder().code("BR").build())
                .build();

        when(stateOrProvinceRepository.findByCode(stateOrProvince.getCode())).thenReturn(Optional.of(stateOrProvince));

        StateOrProvince stateOrProvinceByCode = stateOrProvinceServiceJPA.findByCode("RS");

        verify(stateOrProvinceRepository, times(1)).findByCode(stateOrProvince.getCode());

        assertThat(stateOrProvinceByCode).isEqualTo(stateOrProvince);
    }
}