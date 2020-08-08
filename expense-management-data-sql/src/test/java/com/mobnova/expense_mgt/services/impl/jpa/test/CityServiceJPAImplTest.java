package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.config.ModelMapperConfiguration;
import com.mobnova.expense_mgt.dto.v1.CityDto;
import com.mobnova.expense_mgt.dto.v1.CountyDto;
import com.mobnova.expense_mgt.dto.v1.StateOrProvinceDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.City;
import com.mobnova.expense_mgt.model.County;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.CityRepository;
import com.mobnova.expense_mgt.repositories.CountyRepository;
import com.mobnova.expense_mgt.repositories.StateOrProvinceRepository;
import com.mobnova.expense_mgt.services.impl.jpa.CityServiceJPAImpl;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceJPAImplTest {

    private CityServiceJPAImpl cityServiceJPA;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountyRepository countyRepository;

    @Mock
    private StateOrProvinceRepository stateOrProvinceRepository;

    private ModelMapper modelMapper;

    private StateOrProvince stateOrProvince;
    private StateOrProvinceDto stateOrProvinceDto;

    private County county;
    private CountyDto countyDto;

    @BeforeEach
    void init(){
        modelMapper = new ModelMapperConfiguration().globalMapper();

        stateOrProvinceDto = StateOrProvinceDto.builder().id(1L).code("RS")
                .name("Rio Grande do Sul").build();
        stateOrProvince = modelMapper.map(stateOrProvinceDto, StateOrProvince.class);

        countyDto = CountyDto.builder().id(1L).code("POA").name("Porto Alegre").build();
        county = modelMapper.map(countyDto, County.class);

        cityServiceJPA = Mockito.spy(new CityServiceJPAImpl(cityRepository, countyRepository, stateOrProvinceRepository, modelMapper));
    }


    @Test
    void save() {
        CityDto cityDto = CityDto.builder().code("POA").name("Porto Alegre")
                .stateOrProvince(stateOrProvinceDto)
                .county(countyDto).build();
        City city = modelMapper.map(cityDto, City.class);

        doAnswer(returnsFirstArg()).when(cityRepository).save(any(City.class));
        when(stateOrProvinceRepository.findByCode(stateOrProvince.getCode())).thenReturn(Optional.of(stateOrProvince));
        when(countyRepository.findByCode(county.getCode())).thenReturn(Optional.of(county));
        when(cityRepository.findById(city.getId())).thenReturn(Optional.of(city));

        CityDto savedCityDto = cityServiceJPA.save(cityDto);

        verify(stateOrProvinceRepository, times(1)).findByCode(stateOrProvince.getCode());
        verify(countyRepository, times(1)).findByCode(county.getCode());
        verify(cityRepository, times(1)).save(any(City.class));

        assertThat(savedCityDto.getStateOrProvince().getId()).isEqualTo(1L);
        assertThat(savedCityDto.getCounty().getId()).isEqualTo(1L);
    }


    @Test
    void saveNoCounty() {
        CityDto cityDto = CityDto.builder().id(1L).code("POA").name("Porto Alegre")
                .stateOrProvince(stateOrProvinceDto).build();
        City city = modelMapper.map(cityDto, City.class);

        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        doAnswer(returnsFirstArg()).when(cityRepository).save(any(City.class));

        when(stateOrProvinceRepository.findByCode(stateOrProvince.getCode())).thenReturn(Optional.of(stateOrProvince));

        CityDto savedCityDto = cityServiceJPA.save(cityDto);

        verify(stateOrProvinceRepository, times(1)).findByCode(stateOrProvince.getCode());
        verify(countyRepository, times(0)).findByCode(county.getCode());
        verify(cityRepository, times(1)).save(city);

        assertThat(savedCityDto.getStateOrProvince().getId()).isEqualTo(1L);
        assertThat(savedCityDto.getCounty()).isNull();
    }

    @Test
    void saveBulk() {
        CityDto cityDto1 = CityDto.builder().id(1L).code("POA").name("Porto Alegre")
                .stateOrProvince(stateOrProvinceDto)
                .county(countyDto).build();

        CityDto cityDto2 = CityDto.builder().id(2L).code("CAN").name("Canoas")
                .stateOrProvince(stateOrProvinceDto)
                .county(countyDto).build();

        City city1 = modelMapper.map(cityDto1, City.class);
        City city2 = modelMapper.map(cityDto2, City.class);

        Set<CityDto> cityDtos = new HashSet<>();
        cityDtos.add(cityDto1);
        cityDtos.add(cityDto2);

        when(cityRepository.findById(1L)).thenReturn(Optional.of(city1));
        when(cityRepository.findById(2L)).thenReturn(Optional.of(city2));

        doAnswer(returnsFirstArg()).when(cityRepository).save(any(City.class));

        when(stateOrProvinceRepository.findByCode(stateOrProvince.getCode())).thenReturn(Optional.of(stateOrProvince));
        when(countyRepository.findByCode(county.getCode())).thenReturn(Optional.of(county));

        Set<CityDto> savedCityDtos = cityServiceJPA.saveBulk(cityDtos);

        assertThat(savedCityDtos).hasSize(2);
        verify(stateOrProvinceRepository, times(savedCityDtos.size())).findByCode(stateOrProvince.getCode());
        verify(countyRepository, times(savedCityDtos.size())).findByCode(county.getCode());

        verify(cityRepository, times(savedCityDtos.size())).save(any(City.class));
        verify(cityServiceJPA, times(savedCityDtos.size())).save(any(CityDto.class));

        for(CityDto cityDto : savedCityDtos){
            assertThat(cityDto.getStateOrProvince().getId()).isEqualTo(1L);
            assertThat(cityDto.getCounty().getId()).isEqualTo(1L);
        }
    }

    @Test
    void findById() {
        CityDto cityDto = CityDto.builder().id(1L).code("POA").name("Porto Alegre")
                .stateOrProvince(stateOrProvinceDto).build();
        City city = modelMapper.map(cityDto, City.class);

        when(cityRepository.findById(city.getId())).thenReturn(Optional.of(city));

        CityDto cityByIdDto = cityServiceJPA.findById(1L);

        verify(cityRepository, times(1)).findById(1L);

        assertThat(cityByIdDto).isEqualTo(cityDto);
    }

    @Test
    void deleteById() {
        cityServiceJPA.deleteById(1L);

        verify(cityRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByCode() {
        CityDto cityDto = CityDto.builder().id(1L).code("POA").name("Porto Alegre")
                .stateOrProvince(stateOrProvinceDto).build();
        City city = modelMapper.map(cityDto,City.class);

        when(cityRepository.findByCode(city.getCode())).thenReturn(Optional.of(city));

        CityDto cityByCodeDto = cityServiceJPA.findByCode("POA");

        verify(cityRepository, times(1)).findByCode(city.getCode());

        assertThat(cityByCodeDto).isEqualTo(cityDto);
    }

    @Test
    void findByCodeNotFound() {
        String code = "1000";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> cityServiceJPA.findByCode(code));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(City.class.getSimpleName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.CODE.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(code);
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> cityServiceJPA.findById(id));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(City.class.getSimpleName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}