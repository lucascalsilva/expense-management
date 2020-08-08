package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.dto.v1.CountyDto;
import com.mobnova.expense_mgt.dto.v1.StateOrProvinceDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.model.County;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.CountryRepository;
import com.mobnova.expense_mgt.repositories.StateOrProvinceRepository;
import com.mobnova.expense_mgt.services.impl.jpa.CountyServiceJPAImpl;
import com.mobnova.expense_mgt.util.AssertionUtils;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.assertj.core.api.Assertions;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CountyServiceJPAImplIT {

    @Autowired
    private CountyServiceJPAImpl countyServiceJPA;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateOrProvinceRepository stateOrProvinceRepository;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    @Autowired
    private ModelMapper modelMapper;

    private StateOrProvince stateOrProvince;
    private StateOrProvinceDto stateOrProvinceDto;

    private static Boolean dbInitialized = false;

    @BeforeEach
    public void init(){
        Country country = Country.builder().code("BR").name("Brazil").build();
        stateOrProvince = StateOrProvince.builder().code("RS").name("Rio Grande do Sul")
                .country(country).build();
        stateOrProvinceDto = modelMapper.map(stateOrProvince, StateOrProvinceDto.class);

        if(!dbInitialized) {
            integrationTestHelper.cleanAllData();
            countryRepository.save(country);
            stateOrProvinceRepository.save(stateOrProvince);
            dbInitialized = true;
        }
    }

    @Test
    void saveValidationError() {
        CountyDto countyDto = CountyDto.builder().code(null).name("Condado de Porto Alegre").build();

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> countyServiceJPA.save(countyDto));
        Assertions.assertThat(constraintViolationException.getMessage()).contains("code");
        Assertions.assertThat(constraintViolationException.getMessage()).contains("must not be blank");
        Assertions.assertThat(constraintViolationException.getMessage()).contains("stateOrProvince");
        Assertions.assertThat(constraintViolationException.getMessage()).contains("must not be null");
    }

    @Test
    void save() {
        CountyDto countyDto = CountyDto.builder().code("POA").name("Condado de Porto Alegre")
                .stateOrProvince(stateOrProvinceDto).build();

        CountyDto savedCountyDto = countyServiceJPA.save(countyDto);

        assertThat(savedCountyDto.getId()).isNotNull();
        assertThat(savedCountyDto.getCreationDate()).isNotNull();

        assertThat(savedCountyDto.getStateOrProvince().getId()).isNotNull();
        assertThat(savedCountyDto.getStateOrProvince().getCreationDate()).isNotNull();
    }

    @Test
    void update() {
        CountyDto countyDto = CountyDto.builder().code("TOR").name("Condado de Torres")
                .stateOrProvince(stateOrProvinceDto).build();

        countyDto = countyServiceJPA.save(countyDto);

        assertThat(countyDto).isNotNull();
        assertThat(countyDto.getId()).isNotNull();

        CountyDto updateCountyDto = countyServiceJPA.findById(countyDto.getId());

        updateCountyDto.setName("Condado de Torres_1");

        updateCountyDto = countyServiceJPA.save(updateCountyDto);

        AssertionUtils.dtoUpdateAssertions(countyDto, updateCountyDto);
        assertThat(countyDto.getName()).isNotEqualTo(updateCountyDto.getName());
    }

    @Test
    void saveBulk() {
        CountyDto countyDto1 = CountyDto.builder().code("CAX").name("Condado de Caxias")
                .stateOrProvince(stateOrProvinceDto).build();
        CountyDto countyDto2 = CountyDto.builder().code("PEL").name("Condado de Pelotas")
                .stateOrProvince(stateOrProvinceDto).build();

        Set<CountyDto> countyDtos = new HashSet<>();

        countyDtos.add(countyDto1);
        countyDtos.add(countyDto2);

        Set<CountyDto> savedCountyDtos = countyServiceJPA.saveBulk(countyDtos);

        for(CountyDto countyDto : savedCountyDtos){
            assertThat(countyDto.getId()).isNotNull();
            assertThat(countyDto.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        CountyDto countyDto = CountyDto.builder().code("ALG").name("Condado do Alegrete")
                .stateOrProvince(stateOrProvinceDto).build();

        CountyDto savedCountyDto = countyServiceJPA.save(countyDto);
        Long countyId = savedCountyDto.getId();

        CountyDto countyByIdDto = countyServiceJPA.findById(countyId);

        assertThat(countyByIdDto).isEqualTo(savedCountyDto);
    }

    @Test
    void deleteById() {
        CountyDto county = CountyDto.builder().code("CHU").name("Condado do Chui")
                .stateOrProvince(stateOrProvinceDto).build();

        CountyDto savedCountyDto = countyServiceJPA.save(county);
        Long countyId = savedCountyDto.getId();

        countyServiceJPA.deleteById(countyId);

        assertThrows(DataNotFoundException.class, () -> countyServiceJPA.findById(countyId));
    }

    @Test
    void findByCode() {
        CountyDto countyDto = CountyDto.builder().code("STC").name("Condado do Santa Cruz")
                .stateOrProvince(stateOrProvinceDto).build();

        CountyDto savedCountyDto = countyServiceJPA.save(countyDto);
        String countyCode = savedCountyDto.getCode();

        CountyDto countyByCodeDto = countyServiceJPA.findByCode(countyCode);

        assertThat(countyByCodeDto).isEqualTo(savedCountyDto);
    }

    @Test
    void findByCodeNotFound() {
        String code = "1000";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> countyServiceJPA.findByCode(code));
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(County.class.getSimpleName());
        assertThat(dataNotFoundException.getMessage().compareToIgnoreCase(Fields.CODE.toString()));
        assertThat(dataNotFoundException.getMessage().compareToIgnoreCase(code));
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> countyServiceJPA.findById(id));
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(County.class.getSimpleName());
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}