package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.config.ModelMapperConfiguration;
import com.mobnova.expense_mgt.dto.v1.CountryDto;
import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.repositories.CountryRepository;
import com.mobnova.expense_mgt.services.impl.jpa.CountryServiceJPAImpl;
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
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceJPAImplTest {

    private CountryServiceJPAImpl countryServiceJPA;

    @Mock
    private CountryRepository countryRepository;

    private ModelMapper modelMapper;

    @BeforeEach
    public void setup(){
        modelMapper = new ModelMapperConfiguration().globalMapper();
        countryServiceJPA = Mockito.spy(new CountryServiceJPAImpl(countryRepository, modelMapper));
    }

    @Test
    void save() {
        CountryDto countryDto = CountryDto.builder().id(1L).code("BR")
                .name("Brazil").build();
        Country country = modelMapper.map(countryDto, Country.class);

        doAnswer(returnsFirstArg()).when(countryRepository).save(country);
        when(countryRepository.findById(countryDto.getId())).thenReturn(Optional.of(country));

        countryServiceJPA.save(countryDto);

        verify(countryRepository).save(country);
        verify(countryRepository).findById(countryDto.getId());
    }

    @Test
    void saveBulk() {
        CountryDto countryDto1 = CountryDto.builder().id(1L).code("BR")
                .name("Brazil").build();
        Country country1 = modelMapper.map(countryDto1, Country.class);

        CountryDto countryDto2 = CountryDto.builder().id(2L).code("US")
                .name("United States of America").build();
        Country country2 = modelMapper.map(countryDto2, Country.class);

        Set<CountryDto> countryDtos = new HashSet<>();

        countryDtos.add(countryDto1);
        countryDtos.add(countryDto2);

        when(countryRepository.findById(1L)).thenReturn(Optional.of(country1));
        when(countryRepository.findById(2L)).thenReturn(Optional.of(country2));

        doAnswer(returnsFirstArg()).when(countryRepository).save(any(Country.class));

        Set<CountryDto> savedCountryDtos = countryServiceJPA.saveBulk(countryDtos);
        assertThat(savedCountryDtos).hasSize(2);

        verify(countryRepository, times(2)).save(any(Country.class));
        verify(countryServiceJPA, times(2)).save(any(CountryDto.class));
    }

    @Test
    void findById() {
        CountryDto countryDto = CountryDto.builder().id(1L).code("BR")
                .name("Brazil").build();
        Country country = modelMapper.map(countryDto, Country.class);

        when(countryRepository.findById(country.getId())).thenReturn(Optional.of(country));

        CountryDto countryDtoById = countryServiceJPA.findById(1L);

        assertThat(countryDtoById.getId()).isEqualTo(country.getId());
    }

    @Test
    void deleteById() {
        countryServiceJPA.deleteById(1L);

        verify(countryRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByCode() {
        CountryDto countryDto = CountryDto.builder().id(1L).code("BR")
                .name("Brazil").build();
        Country country = modelMapper.map(countryDto, Country.class);

        when(countryRepository.findByCode(country.getCode())).thenReturn(Optional.of(country));

        CountryDto countryDtoByCode = countryServiceJPA.findByCode("BR");

        verify(countryRepository, times(1)).findByCode(country.getCode());

        assertThat(countryDtoByCode.getId()).isEqualTo(country.getId());
    }
}