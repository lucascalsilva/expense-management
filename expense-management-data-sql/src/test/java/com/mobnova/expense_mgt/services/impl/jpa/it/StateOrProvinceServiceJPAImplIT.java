package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.dto.v1.CountryDto;
import com.mobnova.expense_mgt.dto.v1.StateOrProvinceDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.CountryRepository;
import com.mobnova.expense_mgt.services.impl.jpa.StateOrProvinceServiceJPAImpl;
import com.mobnova.expense_mgt.util.AssertionUtils;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class StateOrProvinceServiceJPAImplIT {

    @Autowired
    private StateOrProvinceServiceJPAImpl stateOrProvinceServiceJPA;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    @Autowired
    private ModelMapper modelMapper;

    private Country country;
    private CountryDto countryDto;

    private static Boolean dbInitialized = false;

    @BeforeEach
    public void init(){
        country = Country.builder().code("BR").name("Brazil").build();
        countryDto = modelMapper.map(country, CountryDto.class);

        if(!dbInitialized) {
            integrationTestHelper.cleanAllData();
            countryRepository.save(country);
            dbInitialized = true;
        }
    }

    @Test
    void save() {
        StateOrProvinceDto stateOrProvinceDto = StateOrProvinceDto.builder().code("RS").name("Rio Grande do Sul")
                .country(countryDto).build();

        StateOrProvinceDto savedStateOrProvince = stateOrProvinceServiceJPA.save(stateOrProvinceDto);

        assertThat(savedStateOrProvince.getId()).isNotNull();
        assertThat(savedStateOrProvince.getCreationDate()).isNotNull();

        assertThat(savedStateOrProvince.getCountry()).isNotNull();
        assertThat(savedStateOrProvince.getCountry().getCode()).isEqualTo("BR");
    }

    @Test
    void saveValidationError() {
        StateOrProvinceDto stateOrProvinceDto = StateOrProvinceDto.builder().code(null).name("Rio Grande do Sul").build();

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> stateOrProvinceServiceJPA.save(stateOrProvinceDto));
        Assertions.assertThat(constraintViolationException.getMessage()).contains("save.arg0.code: must not be blank");
    }

    @Test
    void update() {
        StateOrProvinceDto stateOrProvinceDto = StateOrProvinceDto.builder().code("CE").name("Ceara")
                .country(countryDto).build();

        stateOrProvinceDto = stateOrProvinceServiceJPA.save(stateOrProvinceDto);

        Assertions.assertThat(stateOrProvinceDto).isNotNull();
        Assertions.assertThat(stateOrProvinceDto.getId()).isNotNull();

        StateOrProvinceDto updatedStateOrProvinceDto = stateOrProvinceServiceJPA.findById(stateOrProvinceDto.getId());

        updatedStateOrProvinceDto.setName("Ceara_1");

        updatedStateOrProvinceDto = stateOrProvinceServiceJPA.save(updatedStateOrProvinceDto);

        AssertionUtils.dtoUpdateAssertions(stateOrProvinceDto, updatedStateOrProvinceDto);
        Assertions.assertThat(stateOrProvinceDto.getName()).isNotEqualTo(updatedStateOrProvinceDto.getName());
    }

    @Test
    void saveBulk() {
        StateOrProvinceDto stateOrProvinceDto1 = StateOrProvinceDto.builder().code("PR").name("Parana")
                .country(countryDto).build();
        StateOrProvinceDto stateOrProvinceDto2 = StateOrProvinceDto.builder().code("SC").name("Santa Catarina")
                .country(countryDto).build();
        Set<StateOrProvinceDto> stateOrProvinceDtos = new HashSet<>();

        stateOrProvinceDtos.add(stateOrProvinceDto1);
        stateOrProvinceDtos.add(stateOrProvinceDto2);

        Set<StateOrProvinceDto> savedStateOrProvince = stateOrProvinceServiceJPA.saveBulk(stateOrProvinceDtos);

        for(StateOrProvinceDto stateOrProvinceDto : savedStateOrProvince){
            assertThat(stateOrProvinceDto.getId()).isNotNull();
            assertThat(stateOrProvinceDto.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        StateOrProvinceDto stateOrProvinceDto = StateOrProvinceDto.builder().code("SP").name("Sao Paulo")
                .country(countryDto).build();

        StateOrProvinceDto savedStateOrProvinceDto = stateOrProvinceServiceJPA.save(stateOrProvinceDto);
        Long stateOrProvinceId = savedStateOrProvinceDto.getId();

        StateOrProvinceDto stateOrProvinceById = stateOrProvinceServiceJPA.findById(stateOrProvinceId);

        assertThat(stateOrProvinceById).isEqualTo(savedStateOrProvinceDto);
    }

    @Test
    void deleteById() {
        StateOrProvinceDto stateOrProvinceDto = StateOrProvinceDto.builder().code("RJ").name("Rio de Janeiro")
                .country(countryDto).build();

        StateOrProvinceDto savedStateOrProvinceDto = stateOrProvinceServiceJPA.save(stateOrProvinceDto);
        Long stateOrProvinceId = savedStateOrProvinceDto.getId();

        stateOrProvinceServiceJPA.deleteById(stateOrProvinceId);

        assertThrows(DataNotFoundException.class, () -> stateOrProvinceServiceJPA.findById(stateOrProvinceId));

    }

    @Test
    void findByCode() {
        StateOrProvinceDto stateOrProvinceDto = StateOrProvinceDto.builder().code("MG").name("Minas Gerais")
                .country(countryDto).build();

        StateOrProvinceDto savedStateOrProvince = stateOrProvinceServiceJPA.save(stateOrProvinceDto);
        String stateOrProvinceCode = savedStateOrProvince.getCode();

        StateOrProvinceDto stateOrProvinceDtoByCode = stateOrProvinceServiceJPA.findByCode(stateOrProvinceCode);

        assertThat(stateOrProvinceDtoByCode).isEqualTo(savedStateOrProvince);
    }

    @Test
    void findByCodeNotFound() {
        String code = "1000";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> stateOrProvinceServiceJPA.findByCode(code));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(StateOrProvince.class.getSimpleName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.CODE.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(code);
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> stateOrProvinceServiceJPA.findById(id));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(StateOrProvince.class.getSimpleName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}