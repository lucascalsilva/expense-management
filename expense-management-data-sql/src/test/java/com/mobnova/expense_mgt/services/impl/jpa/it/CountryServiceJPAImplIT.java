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

    private static final Boolean dbInitialized = false;

    @BeforeEach
    public void init(){
        if(!dbInitialized) {
            integrationTestHelper.cleanAllData();
        }
    }

    @Test
    void save() {
        CountryDto countryDto = CountryDto.builder().code("BR").name("Brazil").build();

        CountryDto savedCountryDto = countryServiceJPA.save(countryDto);

        assertThat(savedCountryDto.getId()).isNotNull();
        assertThat(savedCountryDto.getCreationDate()).isNotNull();
    }

    @Test
    void saveValidationError() {
        Country country = Country.builder().code(null).build();

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> countryServiceJPA.save(country));
        assertThat(constraintViolationException.getMessage()).contains("save.arg0.code: must not be blank");
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
        CountryDto countryDto1 = CountryDto.builder().code("US").name("United States").build();
        CountryDto countryDto2 = CountryDto.builder().code("UK").name("United Kingdom").build();

        Set<CountryDto> countriesDto = new HashSet<>();
        countriesDto.add(countryDto1);
        countriesDto.add(countryDto2);

        Set<CountryDto> savedCountriesDto = countryServiceJPA.saveBulk(countriesDto);

        for(CountryDto countryDto : countriesDto){
            assertThat(countryDto.getId()).isNotNull();
            assertThat(countryDto.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        CountryDto countryDto = CountryDto.builder().code("AR").name("Argentina").build();

        CountryDto savedCountryDto = countryServiceJPA.save(countryDto);
        Long countryId = savedCountryDto.getId();

        Optional<Country> countryById = countryServiceJPA.findById(countryId);

        assertThat(countryById).isPresent();
        assertThat(countryById.get()).isEqualTo(savedCountry);
    }

    @Test
    void deleteById() {
        CountryDto countryDto = CountryDto.builder().code("DE").name("Germany").build();

        CountryDto savedCountryDto = countryServiceJPA.save(countryDto);
        Long countryId = savedCountryDto.getId();

        countryServiceJPA.deleteById(countryId);

        Optional<Country> countryById = countryServiceJPA.findById(countryId);

        assertThat(countryById).isEmpty();
    }

    @Test
    void findByCode() {
        CountryDto countryDto = CountryDto.builder().code("PE").name("Peru").build();

        CountryDto savedCountryDto = countryServiceJPA.save(countryDto);
        String countryCode = savedCountryDto.getCode();

        Optional<Country> countryByCode = countryServiceJPA.findByCode(countryCode);

        assertThat(countryByCode).isPresent();
        assertThat(countryByCode.get()).isEqualTo(country);
    }
}