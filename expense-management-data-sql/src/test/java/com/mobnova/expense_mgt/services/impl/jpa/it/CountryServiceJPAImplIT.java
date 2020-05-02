package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.services.impl.jpa.CountryServiceJPAImpl;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CountryServiceJPAImplIT {

    @Autowired
    private CountryServiceJPAImpl countryServiceJPA;

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
        Country country = Country.builder().code("BR").name("Brazil").build();

        Country savedCountry = countryServiceJPA.save(country);

        assertThat(savedCountry.getId()).isNotNull();
        assertThat(savedCountry.getCreationDate()).isNotNull();
    }

    @Test
    void saveBulk() {
        Country country1 = Country.builder().code("US").name("United States").build();
        Country country2 = Country.builder().code("UK").name("United Kingdom").build();

        Set<Country> countries = new HashSet<>();
        countries.add(country1);
        countries.add(country2);

        Set<Country> savedCountries = countryServiceJPA.saveBulk(countries);

        for(Country country : countries){
            assertThat(country.getId()).isNotNull();
            assertThat(country.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        Country country = Country.builder().code("AR").name("Argentina").build();

        Country savedCountry = countryServiceJPA.save(country);
        Long countryId = savedCountry.getId();

        Optional<Country> countryById = countryServiceJPA.findById(countryId);

        assertThat(countryById).isPresent();
        assertThat(countryById.get()).isEqualTo(savedCountry);
    }

    @Test
    void deleteById() {
        Country country = Country.builder().code("DE").name("Germany").build();

        Country savedCountry = countryServiceJPA.save(country);
        Long countryId = savedCountry.getId();

        countryServiceJPA.deleteById(countryId);

        Optional<Country> countryById = countryServiceJPA.findById(countryId);

        assertThat(countryById).isEmpty();
    }

    @Test
    void findByCode() {
        Country country = Country.builder().code("PE").name("Peru").build();

        Country savedCountry = countryServiceJPA.save(country);
        String countryCode = savedCountry.getCode();

        Optional<Country> countryByCode = countryServiceJPA.findByCode(countryCode);

        assertThat(countryByCode).isPresent();
        assertThat(countryByCode.get()).isEqualTo(country);
    }
}