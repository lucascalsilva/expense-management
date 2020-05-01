package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.model.City;
import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.model.County;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.CountryRepository;
import com.mobnova.expense_mgt.repositories.CountyRepository;
import com.mobnova.expense_mgt.repositories.StateOrProvinceRepository;
import com.mobnova.expense_mgt.services.impl.jpa.CityServiceJPAImpl;
import org.junit.jupiter.api.BeforeAll;
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


@ExtendWith(SpringExtension.class)
@SpringBootTest
class CityServiceJPAImplIT {

    @Autowired
    private CityServiceJPAImpl cityServiceJPA;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateOrProvinceRepository stateOrProvinceRepository;

    @Autowired
    private CountyRepository countyRepository;

    private static Boolean dbInitialized = false;


    @BeforeEach
    public void init(){
        if(!dbInitialized) {
            Country country = Country.builder().code("BR").name("Brazil").build();
            StateOrProvince stateOrProvince = StateOrProvince.builder().code("RS").name("Rio Grande do Sul")
                    .country(country).build();
            County county = County.builder().code("POA").name("Condado de Porto Alegre")
                    .stateOrProvince(stateOrProvince).build();

            countryRepository.save(country);
            stateOrProvinceRepository.save(stateOrProvince);
            countyRepository.save(county);
            dbInitialized = true;
        }
    }

    @Test
    void save() {
        City city = City.builder().code("POA").name("Porto Alegre")
                .stateOrProvince(StateOrProvince.builder().code("RS").build()).build();

        City citySaved = cityServiceJPA.save(city);

        assertThat(citySaved.getId()).isNotNull();
        assertThat(citySaved.getCreationDate()).isNotNull();

        assertThat(citySaved.getStateOrProvince()).isNotNull();
        assertThat(citySaved.getStateOrProvince().getCode()).isEqualTo("RS");
    }

    @Test
    void saveBulk() {
        City city1 = City.builder().code("EST").name("Esteio")
                .stateOrProvince(StateOrProvince.builder().code("RS").build()).build();
        City city2 = City.builder().code("CAN").name("Canoas")
                .stateOrProvince(StateOrProvince.builder().code("RS").build()).build();
        Set<City> cities = new HashSet<>();

        cities.add(city1);
        cities.add(city2);

        Set<City> savedCities = cityServiceJPA.saveBulk(cities);

        for(City city : savedCities){
            assertThat(city.getId()).isNotNull();
            assertThat(city.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        City city = City.builder().code("CAC").name("Cachoerinha")
                .stateOrProvince(StateOrProvince.builder().code("RS").build()).build();

        City savedCity = cityServiceJPA.save(city);
        Long cityId = savedCity.getId();

        Optional<City> cityById = cityServiceJPA.findById(cityId);

        assertThat(cityById).isPresent();
        assertThat(cityById.get()).isEqualTo(city);
    }

    @Test
    void deleteById() {
        City city = City.builder().code("GRV").name("Gravatai")
                .stateOrProvince(StateOrProvince.builder().code("RS").build()).build();

        City savedCity = cityServiceJPA.save(city);
        Long cityId = savedCity.getId();

        cityServiceJPA.deleteById(cityId);

        Optional<City> cityById = cityServiceJPA.findById(cityId);

        assertThat(cityById.isEmpty());

    }

    @Test
    void findByCode() {
        City city = City.builder().code("ALV").name("Alvorada")
                .stateOrProvince(StateOrProvince.builder().code("RS").build()).build();

        City savedCity = cityServiceJPA.save(city);
        String cityCode = savedCity.getCode();

        Optional<City> cityByCode = cityServiceJPA.findByCode(cityCode);

        assertThat(cityByCode).isPresent();
        assertThat(cityByCode.get()).isEqualTo(city);
    }
}