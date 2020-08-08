package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.config.ModelMapperConfiguration;
import com.mobnova.expense_mgt.dto.v1.CountryDto;
import com.mobnova.expense_mgt.dto.v1.StateOrProvinceDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.CountryRepository;
import com.mobnova.expense_mgt.repositories.StateOrProvinceRepository;
import com.mobnova.expense_mgt.services.impl.jpa.StateOrProvinceServiceJPAImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StateOrProvinceServiceJPAImplTest {

    private StateOrProvinceServiceJPAImpl stateOrProvinceServiceJPA;

    @Mock
    private StateOrProvinceRepository stateOrProvinceRepository;

    @Mock
    private CountryRepository countryRepository;

    private ModelMapper modelMapper;

    private Country country;
    private CountryDto countryDto;

    @BeforeEach
    void init() {
        modelMapper = new ModelMapperConfiguration().globalMapper();
        countryDto = CountryDto.builder().id(1L).code("BR").name("Brazil").build();
        country = modelMapper.map(countryDto, Country.class);
        stateOrProvinceServiceJPA = Mockito.spy(new StateOrProvinceServiceJPAImpl(stateOrProvinceRepository, countryRepository, modelMapper));
    }


    @Test
    void save() {
        StateOrProvinceDto stateOrProvinceDto = StateOrProvinceDto.builder().id(1L).code("RS")
                .name("Rio Grande do Sul").country(countryDto)
                .build();
        StateOrProvince stateOrProvince = modelMapper.map(stateOrProvinceDto, StateOrProvince.class);

        doAnswer(returnsFirstArg()).when(stateOrProvinceRepository).save(any(StateOrProvince.class));
        when(stateOrProvinceRepository.findById(stateOrProvince.getId())).thenReturn(Optional.of(stateOrProvince));
        when(countryRepository.findByCode(country.getCode())).thenReturn(Optional.of(country));

        StateOrProvinceDto savedStateOrProvinceDto = stateOrProvinceServiceJPA.save(stateOrProvinceDto);

        verify(countryRepository, times(1)).findByCode(country.getCode());
        verify(stateOrProvinceRepository, times(1)).save(stateOrProvince);

        assertThat(savedStateOrProvinceDto.getCountry().getId()).isEqualTo(1L);
    }

    @Test
    void saveBulk() {
        StateOrProvinceDto stateOrProvinceDto1 = StateOrProvinceDto.builder().id(1L).code("RS")
                .name("Rio Grande do Sul").country(countryDto)
                .build();
        StateOrProvince stateOrProvince1 = modelMapper.map(stateOrProvinceDto1, StateOrProvince.class);

        StateOrProvinceDto stateOrProvinceDto2 = StateOrProvinceDto.builder().id(2L).code("SC")
                .name("Santa Catarina").country(countryDto)
                .build();
        StateOrProvince stateOrProvince2 = modelMapper.map(stateOrProvinceDto2, StateOrProvince.class);

        Set<StateOrProvinceDto> stateOrProvinceDtos = new HashSet<>();
        stateOrProvinceDtos.add(stateOrProvinceDto1);
        stateOrProvinceDtos.add(stateOrProvinceDto2);

        when(stateOrProvinceRepository.findById(1L)).thenReturn(Optional.of(stateOrProvince1));
        when(stateOrProvinceRepository.findById(2L)).thenReturn(Optional.of(stateOrProvince2));

        doAnswer(returnsFirstArg()).when(stateOrProvinceRepository).save(any(StateOrProvince.class));

        when(countryRepository.findByCode(country.getCode())).thenReturn(Optional.of(country));

        Set<StateOrProvinceDto> savedStateOrProvinceDtos = stateOrProvinceServiceJPA.saveBulk(stateOrProvinceDtos);
        assertThat(savedStateOrProvinceDtos).hasSize(2);

        verify(countryRepository, times(savedStateOrProvinceDtos.size())).findByCode(country.getCode());
        verify(stateOrProvinceRepository, times(2)).save(any(StateOrProvince.class));
        verify(stateOrProvinceServiceJPA, times(2)).save(any(StateOrProvinceDto.class));

        for (StateOrProvinceDto stateOrProvinceDto : savedStateOrProvinceDtos) {
            assertThat(stateOrProvinceDto.getCountry().getId()).isEqualTo(1L);
        }
    }

    @Test
    void findById() {
        StateOrProvinceDto stateOrProvinceDto = StateOrProvinceDto.builder().id(1L).code("RS")
                .name("Rio Grande do Sul").country(countryDto)
                .build();
        StateOrProvince stateOrProvince = modelMapper.map(stateOrProvinceDto, StateOrProvince.class);

        when(stateOrProvinceRepository.findById(stateOrProvinceDto.getId())).thenReturn(Optional.of(stateOrProvince));

        StateOrProvinceDto stateOrProvinceByDtoId = stateOrProvinceServiceJPA.findById(1L);

        verify(stateOrProvinceRepository, times(1)).findById(1L);

        assertThat(stateOrProvinceByDtoId).isEqualTo(stateOrProvinceDto);
    }

    @Test
    void deleteById() {
        stateOrProvinceServiceJPA.deleteById(1L);

        verify(stateOrProvinceRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByCode() {
        StateOrProvinceDto stateOrProvinceDto = StateOrProvinceDto.builder().id(1L).code("RS")
                .name("Rio Grande do Sul").country(countryDto)
                .build();
        StateOrProvince stateOrProvince = modelMapper.map(stateOrProvinceDto, StateOrProvince.class);

        when(stateOrProvinceRepository.findByCode(stateOrProvinceDto.getCode())).thenReturn(Optional.of(stateOrProvince));

        StateOrProvinceDto stateOrProvinceDtoByCode = stateOrProvinceServiceJPA.findByCode("RS");

        verify(stateOrProvinceRepository, times(1)).findByCode(stateOrProvinceDto.getCode());

        assertThat(stateOrProvinceDtoByCode).isEqualTo(stateOrProvinceDto);
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