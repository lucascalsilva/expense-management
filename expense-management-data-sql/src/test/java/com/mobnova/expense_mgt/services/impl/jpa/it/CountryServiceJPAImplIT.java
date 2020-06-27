package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.services.impl.jpa.CountryServiceJPAImpl;
import com.mobnova.expense_mgt.util.AssertionsUtil;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void update() {
        Country country = Country.builder().code("EG").name("Egypt").build();

        country = countryServiceJPA.save(country);

        assertThat(country).isNotNull();
        assertThat(country.getId()).isNotNull();

        Country updatedCountry = countryServiceJPA.findById(country.getId());

        updatedCountry.setName("Egypt_1");

        updatedCountry = countryServiceJPA.save(updatedCountry);

        AssertionsUtil.entityUpdateAssertions(country, updatedCountry);
        assertThat(country.getName()).isNotEqualTo(updatedCountry.getName());
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

        Country countryById = countryServiceJPA.findById(countryId);

        assertThat(countryById).isEqualTo(savedCountry);
    }

    @Test
    void deleteById() {
        Country country = Country.builder().code("DE").name("Germany").build();

        Country savedCountry = countryServiceJPA.save(country);
        Long countryId = savedCountry.getId();

        countryServiceJPA.deleteById(countryId);

        assertThrows(DataNotFoundException.class, () -> countryServiceJPA.findById(countryId));
    }

    @Test
    void findByCode() {
        Country country = Country.builder().code("PE").name("Peru").build();

        Country savedCountry = countryServiceJPA.save(country);
        String countryCode = savedCountry.getCode();

        Country countryByCode = countryServiceJPA.findByCode(countryCode);

        assertThat(countryByCode).isEqualTo(country);
    }

    @Test
    void findByCodeNotFound() {
        String code = "1000";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> countryServiceJPA.findByCode(code));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Country.class.getName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage().compareToIgnoreCase(Fields.CODE.toString()));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage().compareToIgnoreCase(code));
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> countryServiceJPA.findById(id));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Country.class.getName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}