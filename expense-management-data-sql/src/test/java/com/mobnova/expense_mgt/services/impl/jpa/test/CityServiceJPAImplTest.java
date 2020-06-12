package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.City;
import com.mobnova.expense_mgt.model.County;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.CityRepository;
import com.mobnova.expense_mgt.repositories.CountyRepository;
import com.mobnova.expense_mgt.repositories.StateOrProvinceRepository;
import com.mobnova.expense_mgt.services.impl.jpa.CityServiceJPAImpl;
import com.mobnova.expense_mgt.validation.BeanValidator;
import org.assertj.core.api.AssertionsForClassTypes;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceJPAImplTest {

    @InjectMocks
    @Spy
    private CityServiceJPAImpl cityServiceJPA;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountyRepository countyRepository;

    @Mock
    private StateOrProvinceRepository stateOrProvinceRepository;

    @Mock
    private BeanValidator beanValidator;

    private StateOrProvince stateOrProvince;
    private County county;

    @BeforeEach
    void init(){
        stateOrProvince = StateOrProvince.builder().id(1L).code("RS")
                .name("Rio Grande do Sul").build();
        county = County.builder().id(1L).code("POA").name("Porto Alegre").build();
    }


    @Test
    void save() {
        City city = City.builder().code("POA").name("Porto Alegre")
                .stateOrProvince(StateOrProvince.builder().code("RS").build())
                .county(County.builder().code("POA").build()).build();

        doAnswer(returnsFirstArg()).when(cityRepository).save(any(City.class));

        when(stateOrProvinceRepository.findByCode(stateOrProvince.getCode())).thenReturn(Optional.of(stateOrProvince));
        when(countyRepository.findByCode(county.getCode())).thenReturn(Optional.of(county));

        City savedCity = cityServiceJPA.save(city);

        verify(stateOrProvinceRepository, times(1)).findByCode(stateOrProvince.getCode());
        verify(countyRepository, times(1)).findByCode(county.getCode());
        verify(cityRepository, times(1)).save(city);

        assertThat(savedCity.getStateOrProvince().getId()).isEqualTo(1L);
        assertThat(savedCity.getCounty().getId()).isEqualTo(1L);
    }


    @Test
    void saveNoCounty() {
        City city = City.builder().code("POA").name("Porto Alegre")
                .stateOrProvince(StateOrProvince.builder().code("RS").build()).build();

        doAnswer(returnsFirstArg()).when(cityRepository).save(any(City.class));

        when(stateOrProvinceRepository.findByCode(stateOrProvince.getCode())).thenReturn(Optional.of(stateOrProvince));

        City savedCity = cityServiceJPA.save(city);

        verify(stateOrProvinceRepository, times(1)).findByCode(stateOrProvince.getCode());
        verify(countyRepository, times(0)).findByCode(county.getCode());
        verify(cityRepository, times(1)).save(city);

        assertThat(savedCity.getStateOrProvince().getId()).isEqualTo(1L);
        assertThat(savedCity.getCounty()).isNull();
    }

    @Test
    void saveBulk() {
        City city1 = City.builder().code("POA").name("Porto Alegre")
                .stateOrProvince(StateOrProvince.builder().code("RS").build())
                .county(County.builder().code("POA").build()).build();

        City city2 = City.builder().code("CAN").name("Canoas")
                .stateOrProvince(StateOrProvince.builder().code("RS").build())
                .county(County.builder().code("POA").build()).build();

        Set<City> cities = new HashSet<>();
        cities.add(city1);
        cities.add(city2);

        doAnswer(returnsFirstArg()).when(cityRepository).save(any(City.class));

        when(stateOrProvinceRepository.findByCode(stateOrProvince.getCode())).thenReturn(Optional.of(stateOrProvince));
        when(countyRepository.findByCode(county.getCode())).thenReturn(Optional.of(county));

        Set<City> savedCities = cityServiceJPA.saveBulk(cities);

        verify(stateOrProvinceRepository, times(cities.size())).findByCode(stateOrProvince.getCode());
        verify(countyRepository, times(cities.size())).findByCode(county.getCode());

        for(City city : savedCities){
            verify(cityRepository, times(1)).save(city);
            verify(cityServiceJPA, times(1)).save(city);

            assertThat(city.getStateOrProvince().getId()).isEqualTo(1L);
            assertThat(city.getCounty().getId()).isEqualTo(1L);
        }
    }

    @Test
    void findById() {
        City city = City.builder().id(1L).code("POA").name("Porto Alegre")
                .stateOrProvince(StateOrProvince.builder().code("RS").build()).build();

        when(cityRepository.findById(city.getId())).thenReturn(Optional.of(city));

        City cityById = cityServiceJPA.findById(1L);

        verify(cityRepository, times(1)).findById(1L);

        assertThat(cityById).isEqualTo(city);
    }

    @Test
    void deleteById() {
        cityServiceJPA.deleteById(1L);

        verify(cityRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByCode() {
        City city = City.builder().id(1L).code("POA").name("Porto Alegre")
                .stateOrProvince(StateOrProvince.builder().code("RS").build()).build();

        when(cityRepository.findByCode(city.getCode())).thenReturn(Optional.of(city));

        City cityByCode = cityServiceJPA.findByCode("POA");

        verify(cityRepository, times(1)).findByCode(city.getCode());

        assertThat(cityByCode).isEqualTo(city);
    }

    @Test
    void findByCodeNotFound() {
        String code = "1000";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> cityServiceJPA.findByCode(code));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(City.class.getName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.CODE.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(code);
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> cityServiceJPA.findById(id));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(City.class.getName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}