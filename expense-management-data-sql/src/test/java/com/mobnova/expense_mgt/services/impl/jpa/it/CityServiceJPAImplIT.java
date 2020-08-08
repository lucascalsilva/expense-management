package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.dto.v1.CityDto;
import com.mobnova.expense_mgt.dto.v1.CountryDto;
import com.mobnova.expense_mgt.dto.v1.CountyDto;
import com.mobnova.expense_mgt.dto.v1.StateOrProvinceDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.*;
import com.mobnova.expense_mgt.repositories.CountryRepository;
import com.mobnova.expense_mgt.repositories.CountyRepository;
import com.mobnova.expense_mgt.repositories.StateOrProvinceRepository;
import com.mobnova.expense_mgt.services.impl.jpa.CityServiceJPAImpl;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertThrows;


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

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    @Autowired
    private ModelMapper modelMapper;

    private StateOrProvince stateOrProvince;
    private StateOrProvinceDto stateOrProvinceDto;

    private Country country;

    private County county;
    private CountyDto countyDto;

    private static Boolean dbInitialized = false;

    @BeforeEach
    public void init(){
        country = Country.builder().code("BR").name("Brazil").build();
        Country country1 = Country.builder().code("DE").name("Deutschland").build();
        Country country2 = Country.builder().code("USA").name("United States of America").build();

        stateOrProvince = StateOrProvince.builder().code("RS").name("Rio Grande do Sul")
                .country(country).build();
        StateOrProvince stateOrProvince1 = StateOrProvince.builder().code("BE").name("Berlin")
                .country(country1).build();
        StateOrProvince stateOrProvince2 = StateOrProvince.builder().code("NH").name("New Hampshire")
                .country(country2).build();

        stateOrProvinceDto = modelMapper.map(stateOrProvince, StateOrProvinceDto.class);

        county = County.builder().code("POA").name("Condado de Porto Alegre")
                .stateOrProvince(stateOrProvince).build();
        countyDto = modelMapper.map(county, CountyDto.class);

        if(!dbInitialized) {
            integrationTestHelper.cleanAllData();
            countryRepository.save(country);
            countryRepository.save(country1);
            countryRepository.save(country2);
            stateOrProvinceRepository.save(stateOrProvince);
            stateOrProvinceRepository.save(stateOrProvince1);
            stateOrProvinceRepository.save(stateOrProvince2);
            countyRepository.save(county);
            dbInitialized = true;
        }
    }

    @Test
    void save() {
        CityDto cityDto = CityDto.builder().code("POA").name("Porto Alegre")
                .stateOrProvince(stateOrProvinceDto).build();

        CityDto citySavedDto = cityServiceJPA.save(cityDto);

        assertThat(citySavedDto.getId()).isNotNull();
        assertThat(citySavedDto.getCreationDate()).isNotNull();

        assertThat(citySavedDto.getStateOrProvince()).isNotNull();
        assertThat(citySavedDto.getStateOrProvince().getCode()).isEqualTo("RS");
    }

    @Test
    void saveValidationError() {
        CityDto cityDto = CityDto.builder().code(null).name("Porto Alegre").build();

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> cityServiceJPA.save(cityDto));
        Assertions.assertThat(constraintViolationException.getMessage()).contains("code");
        Assertions.assertThat(constraintViolationException.getMessage()).contains("must not be blank");
        Assertions.assertThat(constraintViolationException.getMessage()).contains("stateOrProvince");
        Assertions.assertThat(constraintViolationException.getMessage()).contains("must not be null");
    }

    @Test
    void update() {
        CityDto cityDto = CityDto.builder().code("VM").name("Viamao")
                .stateOrProvince(stateOrProvinceDto).build();

        cityDto = cityServiceJPA.save(cityDto);

        assertThat(cityDto).isNotNull();
        assertThat(cityDto.getId()).isNotNull();

        CityDto updatedCityDto = cityServiceJPA.findById(cityDto.getId());

        updatedCityDto.setName("Vimao_1");

        updatedCityDto = cityServiceJPA.save(updatedCityDto);

        AssertionUtils.dtoUpdateAssertions(cityDto, updatedCityDto);
        assertThat(cityDto.getName()).isNotEqualTo(updatedCityDto.getName());
    }

    @Test
    void saveBulk() {
        CityDto cityDto1 = CityDto.builder().code("BE").name("Berlin")
                .stateOrProvince(StateOrProvinceDto.builder().name("Berlin").code("BE")
                        .country(CountryDto.builder().name("Deutschland").code("DE").build())
                        .build()).build();

        CityDto cityDto2 = CityDto.builder().code("BE").name("Berlin")
                .stateOrProvince(StateOrProvinceDto.builder().name("New Hampshire").code("NH")
                        .country(CountryDto.builder().name("United States of America").code("USA").build())
                        .build()).build();

        Set<CityDto> cityDtos = new HashSet<>();
        cityDtos.add(cityDto1);
        cityDtos.add(cityDto2);

        Set<CityDto> savedCityDtos = cityServiceJPA.saveBulk(cityDtos);

        for(CityDto cityDto : savedCityDtos){
            assertThat(cityDto.getId()).isNotNull();
            assertThat(cityDto.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        CityDto cityDto = CityDto.builder().code("CAC").name("Cachoerinha")
                .stateOrProvince(stateOrProvinceDto).build();

        CityDto savedCityDto = cityServiceJPA.save(cityDto);
        Long cityId = savedCityDto.getId();

        CityDto cityByIdDto = cityServiceJPA.findById(cityId);

        assertThat(cityByIdDto).isEqualTo(savedCityDto);
    }

    @Test
    void deleteById() {
        CityDto cityDto = CityDto.builder().code("GRV").name("Gravatai")
                .stateOrProvince(stateOrProvinceDto).build();

        CityDto savedCity = cityServiceJPA.save(cityDto);
        Long cityId = savedCity.getId();

        cityServiceJPA.deleteById(cityId);

        assertThrows(DataNotFoundException.class, () -> cityServiceJPA.findById(cityId));

    }

    @Test
    void findByCode() {
        CityDto cityDto = CityDto.builder().code("ALV").name("Alvorada")
                .stateOrProvince(stateOrProvinceDto).build();

        CityDto savedCityDto = cityServiceJPA.save(cityDto);
        String cityCode = savedCityDto.getCode();

        CityDto cityByCodeDto = cityServiceJPA.findByCode(cityCode);

        assertThat(cityByCodeDto).isEqualTo(savedCityDto);
    }

    @Test
    void findByCodeNotFound() {
        String code = "1000";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> cityServiceJPA.findByCode(code));
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(City.class.getSimpleName());
        assertThat(dataNotFoundException.getMessage().compareToIgnoreCase(Fields.CODE.toString()));
        assertThat(dataNotFoundException.getMessage().compareToIgnoreCase(code));
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> cityServiceJPA.findById(id));
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(City.class.getSimpleName());
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }

}