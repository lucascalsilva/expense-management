package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.config.ModelMapperConfiguration;
import com.mobnova.expense_mgt.dto.v1.CountyDto;
import com.mobnova.expense_mgt.dto.v1.StateOrProvinceDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.County;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.CountyRepository;
import com.mobnova.expense_mgt.repositories.StateOrProvinceRepository;
import com.mobnova.expense_mgt.services.impl.jpa.CountyServiceJPAImpl;
import org.assertj.core.api.Assertions;
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
class CountyServiceJPAImplTest {

    private CountyServiceJPAImpl countyServiceJPA;

    @Mock
    private CountyRepository countyRepository;

    @Mock
    private StateOrProvinceRepository stateOrProvinceRepository;

    private ModelMapper modelMapper;

    private StateOrProvince stateOrProvince;
    private StateOrProvinceDto stateOrProvinceDto;

    @BeforeEach
    void init() {
        modelMapper = new ModelMapperConfiguration().globalMapper();
        countyServiceJPA = Mockito.spy(new CountyServiceJPAImpl(countyRepository, stateOrProvinceRepository, modelMapper));

        stateOrProvinceDto = StateOrProvinceDto.builder().id(1L).code("RS")
                .name("Rio Grande do Sul").build();
        stateOrProvince = modelMapper.map(stateOrProvinceDto, StateOrProvince.class);
    }

    @Test
    void save() {
        CountyDto countyDto = CountyDto.builder().id(1L).code("POA").name("Condado de Porto Alegre")
                .stateOrProvince(stateOrProvinceDto).build();
        County county = modelMapper.map(countyDto, County.class);

        doAnswer(returnsFirstArg()).when(countyRepository).save(county);
        when(stateOrProvinceRepository.findByCode(stateOrProvince.getCode()))
                .thenReturn(Optional.of(stateOrProvince));
        when(countyRepository.findById(1L))
                .thenReturn(Optional.of(county));

        CountyDto savedCountyDto = countyServiceJPA.save(countyDto);

        verify(stateOrProvinceRepository, times(1)).findByCode(stateOrProvince.getCode());
        verify(countyRepository, times(1)).save(county);

        assertThat(savedCountyDto.getStateOrProvince().getId()).isEqualTo(1L);
    }

    @Test
    void saveBulk() {
        CountyDto countyDto1 = CountyDto.builder().id(1L).code("POA").name("Condado de Porto Alegre")
                .stateOrProvince(stateOrProvinceDto).build();
        CountyDto countyDto2 = CountyDto.builder().id(2L).code("CAX").name("Condado de Caxias")
                .stateOrProvince(stateOrProvinceDto).build();

        County county1 = modelMapper.map(countyDto1, County.class);
        County county2 = modelMapper.map(countyDto2, County.class);

        Set<CountyDto> countyDtos = new HashSet<>();
        countyDtos.add(countyDto1);
        countyDtos.add(countyDto2);

        when(countyRepository.findById(1L)).thenReturn(Optional.of(county1));
        when(countyRepository.findById(2L)).thenReturn(Optional.of(county2));

        doAnswer(returnsFirstArg()).when(countyRepository).save(any(County.class));
        when(stateOrProvinceRepository.findByCode(stateOrProvince.getCode()))
                .thenReturn(Optional.of(stateOrProvince));

        Set<CountyDto> savedCountyDtos = countyServiceJPA.saveBulk(countyDtos);
        assertThat(savedCountyDtos).hasSize(2);

        verify(stateOrProvinceRepository, times(savedCountyDtos.size())).findByCode(stateOrProvince.getCode());


        verify(countyRepository, times(2)).save(any(County.class));
        verify(countyServiceJPA, times(2)).save(any(CountyDto.class));

        for (CountyDto countyDto : savedCountyDtos) {
            Assertions.assertThat(countyDto.getStateOrProvince().getId()).isEqualTo(1L);
        }
    }

    @Test
    void findById() {
        CountyDto countyDto = CountyDto.builder().id(1L).code("POA").name("Condado de Porto Alegre")
                .stateOrProvince(stateOrProvinceDto).build();
        County county = modelMapper.map(countyDto, County.class);

        when(countyRepository.findById(county.getId())).thenReturn(Optional.of(county));

        CountyDto countyByIdDto = countyServiceJPA.findById(1L);

        verify(countyRepository, times(1)).findById(1L);

        assertThat(countyByIdDto).isEqualTo(countyDto);
    }

    @Test
    void deleteById() {
        countyServiceJPA.deleteById(1L);

        verify(countyRepository, times(1)).deleteById(1L);

    }

    @Test
    void findByCode() {
        CountyDto countyDto = CountyDto.builder().id(1L).code("POA").name("Condado de Porto Alegre")
                .stateOrProvince(stateOrProvinceDto).build();
        County county = modelMapper.map(countyDto, County.class);

        when(countyRepository.findByCode(county.getCode())).thenReturn(Optional.of(county));

        CountyDto countyByCodeDto = countyServiceJPA.findByCode("POA");

        verify(countyRepository, times(1)).findByCode(county.getCode());

        assertThat(countyByCodeDto).isEqualTo(countyDto);
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