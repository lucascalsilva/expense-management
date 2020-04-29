package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.model.ExpenseCategory;
import com.mobnova.expense_mgt.repositories.ExpenseCategoryRepository;
import com.mobnova.expense_mgt.services.impl.jpa.ExpenseCategoryServiceJPAImpl;
import com.mobnova.expense_mgt.validation.BeanValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseCategoryServiceJPAImplTest {

    @InjectMocks
    @Spy
    private ExpenseCategoryServiceJPAImpl expenseCategoryServiceJPA;

    @Mock
    private ExpenseCategoryRepository expenseCategoryRepository;

    @Mock
    private BeanValidator beanValidator;

    @Test
    void save() {
        ExpenseCategory expenseCategory = ExpenseCategory.builder().code("MEAL").name("Meal").build();

        doAnswer(returnsFirstArg()).when(expenseCategoryRepository).save(expenseCategory);

        expenseCategoryServiceJPA.save(expenseCategory);

        verify(expenseCategoryServiceJPA).save(expenseCategory);
    }

    @Test
    void saveBulk() {
        ExpenseCategory expenseCategory1 = ExpenseCategory.builder().code("MEAL").name("Meal").build();
        ExpenseCategory expenseCategory2 = ExpenseCategory.builder().code("FLIGHT").name("Flight").build();

        Set<ExpenseCategory> expenseCategories = new HashSet<>();

        expenseCategories.add(expenseCategory1);
        expenseCategories.add(expenseCategory2);

        doAnswer(returnsFirstArg()).when(expenseCategoryRepository).save(any(ExpenseCategory.class));

        Set<ExpenseCategory> savedExpenseCategories = expenseCategoryServiceJPA.saveBulk(expenseCategories);

        for(ExpenseCategory expenseCategory: savedExpenseCategories){
            verify(expenseCategoryRepository, times(1)).save(expenseCategory);
            verify(expenseCategoryServiceJPA, times(1)).save(expenseCategory);
        }
    }

    @Test
    void findById() {
        ExpenseCategory expenseCategory = ExpenseCategory.builder().id(1L).code("MEAL").name("Meal").build();

        when(expenseCategoryRepository.findById(expenseCategory.getId())).thenReturn(Optional.of(expenseCategory));

        Optional<ExpenseCategory> expenseCategoryById = expenseCategoryServiceJPA.findById(1L);

        assertThat(expenseCategoryById.isPresent());
        assertThat(expenseCategoryById.get()).isEqualTo(expenseCategory);
    }

    @Test
    void deleteById() {
        expenseCategoryServiceJPA.deleteById(1L);

        verify(expenseCategoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByCode() {
        ExpenseCategory expenseCategory = ExpenseCategory.builder().id(1L).code("MEAL").name("Meal").build();

        when(expenseCategoryRepository.findByCode(expenseCategory.getCode())).thenReturn(Optional.of(expenseCategory));

        Optional<ExpenseCategory> expenseCategoryByCode = expenseCategoryServiceJPA.findByCode("MEAL");

        verify(expenseCategoryRepository, times(1)).findByCode(expenseCategory.getCode());

        assertThat(expenseCategoryByCode.isPresent());
        assertThat(expenseCategoryByCode.get()).isEqualTo(expenseCategory);
    }
}