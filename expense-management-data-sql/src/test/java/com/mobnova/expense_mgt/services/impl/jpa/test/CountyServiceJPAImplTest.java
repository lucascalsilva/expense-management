package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.model.City;
import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.model.County;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.CountyRepository;
import com.mobnova.expense_mgt.repositories.StateOrProvinceRepository;
import com.mobnova.expense_mgt.services.impl.jpa.CountyServiceJPAImpl;
import com.mobnova.expense_mgt.validation.BeanValidator;
import org.assertj.core.api.Assertions;
import org.hibernate.annotations.SortComparator;
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
class CountyServiceJPAImplTest {

    @InjectMocks
    @Spy
    private CountyServiceJPAImpl countyServiceJPA;

    @Mock
    private CountyRepository countyRepository;

    @Mock
    private StateOrProvinceRepository stateOrProvinceRepository;

    @Mock
    private BeanValidator beanValidator;

    private StateOrProvince stateOrProvince;

    @BeforeEach
    void init(){
        stateOrProvince = StateOrProvince.builder().id(1L).code("RS")
                .name("Rio Grande do Sul").build();
    }

    @Test
    void save() {
        County county = County.builder().code("POA").name("Condado de Porto Alegre")
                .stateOrProvince(stateOrProvince).build();

        doAnswer(returnsFirstArg()).when(countyRepository).save(any(County.class));

        when(stateOrProvinceRepository.findByCode(stateOrProvince.getCode()))
                .thenReturn(Optional.of(stateOrProvince));

        County savedCounty = countyServiceJPA.save(county);

        verify(stateOrProvinceRepository, times(1)).findByCode(stateOrProvince.getCode());
        verify(countyRepository, times(1)).save(county);

        assertThat(savedCounty.getStateOrProvince().getId()).isEqualTo(1L);
    }

    @Test
    void saveBulk() {
        County county1 = County.builder().code("POA").name("Condado de Porto Alegre")
                .stateOrProvince(stateOrProvince).build();

        County county2 = County.builder().code("CAX").name("Condado de Caxias")
                .stateOrProvince(stateOrProvince).build();

        Set<County> counties = new HashSet<>();
        counties.add(county1);
        counties.add(county2);

        doAnswer(returnsFirstArg()).when(countyRepository).save(any(County.class));
        when(stateOrProvinceRepository.findByCode(stateOrProvince.getCode()))
                .thenReturn(Optional.of(stateOrProvince));

        Set<County> savedCounties = countyServiceJPA.saveBulk(counties);

        verify(stateOrProvinceRepository, times(counties.size())).findByCode(stateOrProvince.getCode());

        for(County county : savedCounties){
            verify(countyRepository, times(1)).save(county);
            verify(countyServiceJPA, times(1)).save(county);

            Assertions.assertThat(county.getStateOrProvince().getId()).isEqualTo(1L);
        }
    }

    @Test
    void findById() {
        County county = County.builder().id(1L).code("POA").name("Condado de Porto Alegre")
                .stateOrProvince(stateOrProvince).build();

        when(countyRepository.findById(county.getId())).thenReturn(Optional.of(county));

        Optional<County> countyById = countyServiceJPA.findById(1L);

        verify(countyRepository, times(1)).findById(1L);

        assertThat(countyById.isPresent());
        assertThat(countyById.get()).isEqualTo(county);
    }

    @Test
    void deleteById() {
        countyServiceJPA.deleteById(1L);

        verify(countyRepository, times(1)).deleteById(1L);

    }

    @Test
    void findByCode(){
        County county = County.builder().code("POA").name("Condado de Porto Alegre")
                .stateOrProvince(stateOrProvince).build();

        when(countyRepository.findByCode(county.getCode())).thenReturn(Optional.of(county));

        Optional<County> countyByCode = countyServiceJPA.findByCode("POA");

        verify(countyRepository, times(1)).findByCode(county.getCode());

        assertThat(countyByCode.isPresent());
        assertThat(countyByCode.get()).isEqualTo(county);
    }
}