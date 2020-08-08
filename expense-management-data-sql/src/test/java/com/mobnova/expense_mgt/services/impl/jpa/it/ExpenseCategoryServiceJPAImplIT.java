package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.dto.v1.ExpenseCategoryDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.ExpenseCategory;
import com.mobnova.expense_mgt.services.impl.jpa.ExpenseCategoryServiceJPAImpl;
import com.mobnova.expense_mgt.util.AssertionUtils;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ExpenseCategoryServiceJPAImplIT {

    @Autowired
    private ExpenseCategoryServiceJPAImpl expenseCategoryServiceJPA;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    private static final Boolean dbInitialized = false;

    @BeforeEach
    public void init(){
        if(!dbInitialized) {
            integrationTestHelper.cleanAllData();
        }
    }

    @Test
    void save() {
        ExpenseCategoryDto expenseCategoryDto = ExpenseCategoryDto.builder().code("MEAL").name("Meal").build();

        ExpenseCategoryDto savedExpenseCategoryDto = expenseCategoryServiceJPA.save(expenseCategoryDto);

        assertThat(savedExpenseCategoryDto.getId()).isNotNull();
        assertThat(savedExpenseCategoryDto.getCreationDate()).isNotNull();
    }

    @Test
    void saveValidationError() {
        ExpenseCategoryDto expenseCategoryDto = ExpenseCategoryDto.builder().code(null).name("Meal").build();

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> expenseCategoryServiceJPA.save(expenseCategoryDto));
        assertThat(constraintViolationException.getMessage()).contains("code");
        assertThat(constraintViolationException.getMessage()).contains("must not be blank");
    }

    @Test
    void update() {
        ExpenseCategoryDto expenseCategoryDto = ExpenseCategoryDto.builder().code("OTHER").name("Other").build();

        expenseCategoryDto = expenseCategoryServiceJPA.save(expenseCategoryDto);

        assertThat(expenseCategoryDto).isNotNull();
        assertThat(expenseCategoryDto.getId()).isNotNull();

        ExpenseCategoryDto updatedExpenseCategoryDto = expenseCategoryServiceJPA.findById(expenseCategoryDto.getId());

        updatedExpenseCategoryDto.setName("Other_1");

        updatedExpenseCategoryDto = expenseCategoryServiceJPA.save(updatedExpenseCategoryDto);

        AssertionUtils.dtoUpdateAssertions(expenseCategoryDto, updatedExpenseCategoryDto);
        assertThat(expenseCategoryDto.getName()).isNotEqualTo(updatedExpenseCategoryDto.getName());
    }

    @Test
    void saveBulk() {
        ExpenseCategoryDto expenseCategoryDto1 = ExpenseCategoryDto.builder().code("TAXI").name("Meal").build();
        ExpenseCategoryDto expenseCategoryDto2 = ExpenseCategoryDto.builder().code("FLIGHT").name("Flight").build();

        Set<ExpenseCategoryDto> expenseCategoryDtos = new HashSet<>();
        expenseCategoryDtos.add(expenseCategoryDto1);
        expenseCategoryDtos.add(expenseCategoryDto2);

        expenseCategoryDtos = expenseCategoryServiceJPA.saveBulk(expenseCategoryDtos);

        for(ExpenseCategoryDto expenseCategoryDto : expenseCategoryDtos){
            assertThat(expenseCategoryDto.getId()).isNotNull();
            assertThat(expenseCategoryDto.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        ExpenseCategoryDto expenseCategoryDto = ExpenseCategoryDto.builder().code("HOTEL").name("Meal").build();

        ExpenseCategoryDto savedExpenseCategoryDto = expenseCategoryServiceJPA.save(expenseCategoryDto);
        Long expenseCategoryId = savedExpenseCategoryDto.getId();

        ExpenseCategoryDto expenseCategoryByIdDto = expenseCategoryServiceJPA.findById(expenseCategoryId);

        assertThat(expenseCategoryByIdDto).isEqualTo(savedExpenseCategoryDto);
    }

    @Test
    void deleteById() {
        ExpenseCategoryDto expenseCategoryDto = ExpenseCategoryDto.builder().code("BUS").name("Meal").build();

        ExpenseCategoryDto savedExpenseCategoryDto = expenseCategoryServiceJPA.save(expenseCategoryDto);
        Long expenseCategoryId = savedExpenseCategoryDto.getId();

        expenseCategoryServiceJPA.deleteById(expenseCategoryId);

        assertThrows(DataNotFoundException.class, () -> expenseCategoryServiceJPA.findById(expenseCategoryId));
    }

    @Test
    void findByCode() {
        ExpenseCategoryDto expenseCategoryDto = ExpenseCategoryDto.builder().code("OTHER").name("Meal").build();

        ExpenseCategoryDto savedExpenseCategoryDto = expenseCategoryServiceJPA.save(expenseCategoryDto);
        String categoryCode = savedExpenseCategoryDto.getCode();

        ExpenseCategoryDto expenseCategoryDtoByCode = expenseCategoryServiceJPA.findByCode(categoryCode);

        assertThat(expenseCategoryDtoByCode).isEqualTo(savedExpenseCategoryDto);
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