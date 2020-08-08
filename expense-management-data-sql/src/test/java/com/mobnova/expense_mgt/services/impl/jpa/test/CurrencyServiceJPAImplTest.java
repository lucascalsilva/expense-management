package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.config.ModelMapperConfiguration;
import com.mobnova.expense_mgt.dto.v1.CurrencyDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.Currency;
import com.mobnova.expense_mgt.repositories.CurrencyRepository;
import com.mobnova.expense_mgt.services.impl.jpa.CurrencyServiceJPAImpl;
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
class CurrencyServiceJPAImplTest {

    private CurrencyServiceJPAImpl currencyServiceJPA;

    @Mock
    private CurrencyRepository currencyRepository;

    private ModelMapper modelMapper;

    @BeforeEach
    public void setup(){
        modelMapper = new ModelMapperConfiguration().globalMapper();
        currencyServiceJPA = Mockito.spy(new CurrencyServiceJPAImpl(currencyRepository, modelMapper));
    }

    @Test
    void save() {
        CurrencyDto currencyDto = CurrencyDto.builder().id(1L).code("BRL").name("Brazilian Real").build();
        Currency currency = modelMapper.map(currencyDto, Currency.class);

        doAnswer(returnsFirstArg()).when(currencyRepository).save(currency);
        when(currencyRepository.findById(1L)).thenReturn(Optional.of(currency));

        currencyServiceJPA.save(currencyDto);

        verify(currencyRepository).save(currency);
    }

    @Test
    void saveBulk() {
        CurrencyDto currencyDto1 = CurrencyDto.builder().id(1L).code("BRL").name("Brazilian Real").build();
        CurrencyDto currencyDto2 = CurrencyDto.builder().id(2L).code("USD").name("American Dollar").build();
        Currency currency1 = modelMapper.map(currencyDto1, Currency.class);
        Currency currency2 = modelMapper.map(currencyDto2, Currency.class);

        Set<CurrencyDto> currencyDtos = new HashSet<>();

        currencyDtos.add(currencyDto1);
        currencyDtos.add(currencyDto2);

        when(currencyRepository.findById(1L)).thenReturn(Optional.of(currency1));
        when(currencyRepository.findById(2L)).thenReturn(Optional.of(currency2));

        doAnswer(returnsFirstArg()).when(currencyRepository).save(any(Currency.class));

        Set<CurrencyDto> savedCurrencyDtos = currencyServiceJPA.saveBulk(currencyDtos);
        assertThat(savedCurrencyDtos).hasSize(2);

        verify(currencyRepository, times(2)).save(any(Currency.class));
        verify(currencyServiceJPA, times(2)).save(any(CurrencyDto.class));
    }

    @Test
    void findById() {
        CurrencyDto currencyDto = CurrencyDto.builder().id(1L).code("BRL").name("Brazilian Real").build();
        Currency currency = modelMapper.map(currencyDto, Currency.class);

        when(currencyRepository.findById(currencyDto.getId())).thenReturn(Optional.of(currency));

        CurrencyDto currencyDtoById = currencyServiceJPA.findById(1L);

        assertThat(currencyDtoById.getId()).isEqualTo(currency.getId());
    }

    @Test
    void deleteById() {
        currencyServiceJPA.deleteById(1L);

        verify(currencyRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByCode() {
        CurrencyDto currencyDto = CurrencyDto.builder().id(1L).code("BRL").name("Brazilian Real").build();
        Currency currency = modelMapper.map(currencyDto, Currency.class);

        when(currencyRepository.findByCode(currency.getCode())).thenReturn(Optional.of(currency));

        CurrencyDto currencyDtoByCode = currencyServiceJPA.findByCode("BRL");

        verify(currencyRepository, times(1)).findByCode(currency.getCode());

        assertThat(currencyDtoByCode.getId()).isEqualTo(currency.getId());
    }

    @Test
    void findByCodeNotFound() {
        String code = "1000";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> currencyServiceJPA.findByCode(code));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Currency.class.getSimpleName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage().compareToIgnoreCase(Fields.CODE.toString()));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage().compareToIgnoreCase(code));
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> currencyServiceJPA.findById(id));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Currency.class.getSimpleName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}