package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.model.ExpenseCategory;
import com.mobnova.expense_mgt.services.impl.jpa.CountryServiceJPAImpl;
import com.mobnova.expense_mgt.services.impl.jpa.ExpenseCategoryServiceJPAImpl;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ExpenseCategoryServiceJPAImplIT {

    @Autowired
    private ExpenseCategoryServiceJPAImpl expenseCategoryServiceJPA;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    private static Boolean dbInitialized = false;

    @BeforeEach
    public void init(){
        if(!dbInitialized) {
            integrationTestHelper.cleanAllData();
        }
    }

    @Test
    void save() {
        ExpenseCategory expenseCategory = ExpenseCategory.builder().code("MEAL").name("Meal").build();

        ExpenseCategory savedExpenseCategory = expenseCategoryServiceJPA.save(expenseCategory);

        assertThat(savedExpenseCategory.getId()).isNotNull();
        assertThat(savedExpenseCategory.getCreationDate()).isNotNull();
    }

    @Test
    void saveBulk() {
        ExpenseCategory expenseCategory1 = ExpenseCategory.builder().code("TAXI").name("Meal").build();
        ExpenseCategory expenseCategory2 = ExpenseCategory.builder().code("FLIGHT").name("Flight").build();

        Set<ExpenseCategory> expenseCategories = new HashSet<>();
        expenseCategories.add(expenseCategory1);
        expenseCategories.add(expenseCategory2);

        Set<ExpenseCategory> savedExpenseCategories = expenseCategoryServiceJPA.saveBulk(expenseCategories);

        for(ExpenseCategory expenseCategory : savedExpenseCategories){
            assertThat(expenseCategory.getId()).isNotNull();
            assertThat(expenseCategory.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        ExpenseCategory expenseCategory = ExpenseCategory.builder().code("HOTEL").name("Meal").build();

        ExpenseCategory savedExpenseCategory = expenseCategoryServiceJPA.save(expenseCategory);
        Long expenseCategoryId = savedExpenseCategory.getId();

        ExpenseCategory expenseCategoryById = expenseCategoryServiceJPA.findById(expenseCategoryId);

        assertThat(expenseCategoryById).isEqualTo(savedExpenseCategory);
    }

    @Test
    void deleteById() {
        ExpenseCategory expenseCategory = ExpenseCategory.builder().code("BUS").name("Meal").build();

        ExpenseCategory savedExpenseCategory = expenseCategoryServiceJPA.save(expenseCategory);
        Long expenseCategoryId = savedExpenseCategory.getId();

        expenseCategoryServiceJPA.deleteById(expenseCategoryId);

        assertThrows(DataNotFoundException.class, () -> expenseCategoryServiceJPA.findById(expenseCategoryId));
    }

    @Test
    void findByCode() {
        ExpenseCategory expenseCategory = ExpenseCategory.builder().code("OTHER").name("Meal").build();

        ExpenseCategory savedExpenseCategory = expenseCategoryServiceJPA.save(expenseCategory);
        String categoryCode = savedExpenseCategory.getCode();

        ExpenseCategory categoryByCode = expenseCategoryServiceJPA.findByCode(categoryCode);

        assertThat(categoryByCode).isEqualTo(savedExpenseCategory);
    }
}