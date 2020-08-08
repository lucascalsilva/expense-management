package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.dto.v1.CountryDto;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.services.impl.jpa.CountryServiceJPAImpl;
import com.mobnova.expense_mgt.util.AssertionUtils;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;
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
        CountryDto countryDto = CountryDto.builder().code(null).build();

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> countryServiceJPA.save(countryDto));
        assertThat(constraintViolationException.getMessage()).contains("code");
        assertThat(constraintViolationException.getMessage()).contains("name");
        assertThat(StringUtils.countOccurrencesOf(constraintViolationException.getMessage(), "must not be blank")).isEqualTo(2);
    }

    @Test
    void update() {
        CountryDto countryDto = CountryDto.builder().code("EG").name("Egypt").build();

        countryDto = countryServiceJPA.save(countryDto);

        assertThat(countryDto).isNotNull();
        assertThat(countryDto.getId()).isNotNull();

        CountryDto updatedCountryDto = countryServiceJPA.findById(countryDto.getId());

        updatedCountryDto.setName("Egypt_1");

        updatedCountryDto = countryServiceJPA.save(updatedCountryDto);

        AssertionUtils.dtoUpdateAssertions(countryDto, updatedCountryDto);
        assertThat(countryDto.getName()).isNotEqualTo(updatedCountryDto.getName());
    }

    @Test
    void saveBulk() {
        CountryDto countryDto1 = CountryDto.builder().code("US").name("United States").build();
        CountryDto countryDto2 = CountryDto.builder().code("UK").name("United Kingdom").build();

        Set<CountryDto> countriesDto = new HashSet<>();
        countriesDto.add(countryDto1);
        countriesDto.add(countryDto2);

        Set<CountryDto> savedCountriesDto = countryServiceJPA.saveBulk(countriesDto);

        for(CountryDto countryDto : savedCountriesDto){
            assertThat(countryDto.getId()).isNotNull();
            assertThat(countryDto.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        CountryDto countryDto = CountryDto.builder().code("AR").name("Argentina").build();

        CountryDto savedCountryDto = countryServiceJPA.save(countryDto);
        Long countryId = savedCountryDto.getId();

        CountryDto countryDtoById = countryServiceJPA.findById(countryId);

        assertThat(countryDtoById).isEqualTo(savedCountryDto);
    }

    @Test
    void deleteById() {
        CountryDto countryDto = CountryDto.builder().code("DE").name("Germany").build();

        CountryDto savedCountryDto = countryServiceJPA.save(countryDto);
        Long countryId = savedCountryDto.getId();

        countryServiceJPA.deleteById(countryId);

        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> countryServiceJPA.findById(countryId));

        assertThat(dataNotFoundException).hasMessageContaining("Country");
        assertThat(dataNotFoundException).hasMessageContaining("ID");
        assertThat(dataNotFoundException).hasMessageContaining(countryId.toString());
    }

    @Test
    void findByCode() {
        CountryDto countryDto = CountryDto.builder().code("PE").name("Peru").build();

        CountryDto savedCountryDto = countryServiceJPA.save(countryDto);
        String countryCode = savedCountryDto.getCode();

        CountryDto countryDtoByCode = countryServiceJPA.findByCode(countryCode);

        assertThat(countryDtoByCode).isEqualTo(savedCountryDto);
    }
}