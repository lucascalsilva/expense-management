package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.config.ModelMapperConfiguration;
import com.mobnova.expense_mgt.dto.v1.ExpenseCategoryDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.ExpenseCategory;
import com.mobnova.expense_mgt.repositories.ExpenseCategoryRepository;
import com.mobnova.expense_mgt.services.impl.jpa.ExpenseCategoryServiceJPAImpl;
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
class ExpenseCategoryServiceJPAImplTest {

    private ExpenseCategoryServiceJPAImpl expenseCategoryServiceJPA;

    @Mock
    private ExpenseCategoryRepository expenseCategoryRepository;

    private ModelMapper modelMapper;

    @BeforeEach
    public void setup(){
        modelMapper = new ModelMapperConfiguration().globalMapper();
        expenseCategoryServiceJPA = Mockito.spy(new ExpenseCategoryServiceJPAImpl(expenseCategoryRepository, modelMapper));
    }

    @Test
    void save() {
        ExpenseCategoryDto expenseCategoryDto = ExpenseCategoryDto.builder().id(1L).code("MEAL").name("Meal").build();
        ExpenseCategory expenseCategory = modelMapper.map(expenseCategoryDto, ExpenseCategory.class);

        doAnswer(returnsFirstArg()).when(expenseCategoryRepository).save(any());
        when(expenseCategoryRepository.findById(1L)).thenReturn(Optional.of(expenseCategory));

        expenseCategoryServiceJPA.save(expenseCategoryDto);

        verify(expenseCategoryServiceJPA).save(expenseCategoryDto);
    }

    @Test
    void saveBulk() {
        ExpenseCategoryDto expenseCategoryDto1 = ExpenseCategoryDto.builder().id(1L).code("MEAL").name("Meal").build();
        ExpenseCategoryDto expenseCategoryDto2 = ExpenseCategoryDto.builder().id(2L).code("FLIGHT").name("Flight").build();
        ExpenseCategory expenseCategory1 = modelMapper.map(expenseCategoryDto1, ExpenseCategory.class);
        ExpenseCategory expenseCategory2 = modelMapper.map(expenseCategoryDto2, ExpenseCategory.class);

        Set<ExpenseCategoryDto> expenseCategoryDtos = new HashSet<>();

        expenseCategoryDtos.add(expenseCategoryDto1);
        expenseCategoryDtos.add(expenseCategoryDto2);

        when(expenseCategoryRepository.findById(1L)).thenReturn(Optional.of(expenseCategory1));
        when(expenseCategoryRepository.findById(2L)).thenReturn(Optional.of(expenseCategory2));

        doAnswer(returnsFirstArg()).when(expenseCategoryRepository).save(any(ExpenseCategory.class));

        Set<ExpenseCategoryDto> savedExpenseCategoryDtos = expenseCategoryServiceJPA.saveBulk(expenseCategoryDtos);
        assertThat(savedExpenseCategoryDtos).hasSize(2);

        verify(expenseCategoryRepository, times(2)).save(any());
        verify(expenseCategoryServiceJPA, times(2)).save(any());
    }

    @Test
    void findById() {
        ExpenseCategoryDto expenseCategoryDto = ExpenseCategoryDto.builder().id(1L).code("MEAL").name("Meal").build();
        ExpenseCategory expenseCategory = modelMapper.map(expenseCategoryDto, ExpenseCategory.class);

        when(expenseCategoryRepository.findById(expenseCategory.getId())).thenReturn(Optional.of(expenseCategory));

        ExpenseCategoryDto expenseCategoryDtoById = expenseCategoryServiceJPA.findById(1L);

        assertThat(expenseCategoryDtoById.getId()).isEqualTo(expenseCategoryDto.getId());
    }

    @Test
    void deleteById() {
        expenseCategoryServiceJPA.deleteById(1L);

        verify(expenseCategoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByCode() {
        ExpenseCategoryDto expenseCategoryDto = ExpenseCategoryDto.builder().id(1L).code("MEAL").name("Meal").build();
        ExpenseCategory expenseCategory = modelMapper.map(expenseCategoryDto, ExpenseCategory.class);

        when(expenseCategoryRepository.findByCode(expenseCategory.getCode())).thenReturn(Optional.of(expenseCategory));

        ExpenseCategoryDto expenseCategoryDtoByCode = expenseCategoryServiceJPA.findByCode("MEAL");

        verify(expenseCategoryRepository, times(1)).findByCode(expenseCategoryDto.getCode());

        assertThat(expenseCategoryDtoByCode.getId()).isEqualTo(expenseCategory.getId());
    }

    @Test
    void findByCodeNotFound() {
        String code = "1000";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> expenseCategoryServiceJPA.findByCode(code));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(ExpenseCategory.class.getSimpleName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage().compareToIgnoreCase(Fields.CODE.toString()));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage().compareToIgnoreCase(code));
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> expenseCategoryServiceJPA.findById(id));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(ExpenseCategory.class.getSimpleName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}