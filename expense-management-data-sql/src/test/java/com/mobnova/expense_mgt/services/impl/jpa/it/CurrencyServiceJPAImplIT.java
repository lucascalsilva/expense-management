package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.dto.v1.CurrencyDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.Currency;
import com.mobnova.expense_mgt.services.impl.jpa.CurrencyServiceJPAImpl;
import com.mobnova.expense_mgt.util.AssertionUtils;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CurrencyServiceJPAImplIT {

    @Autowired
    private CurrencyServiceJPAImpl currencyServiceJPA;

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
        CurrencyDto currencyDto = CurrencyDto.builder().id(1L).code("BRL").name("Brazilian Real").build();

        CurrencyDto savedCurrencyDto  = currencyServiceJPA.save(currencyDto);

        assertThat(savedCurrencyDto.getId()).isNotNull();
        assertThat(savedCurrencyDto.getCreationDate()).isNotNull();
    }

    @Test
    void saveValidationError() {
        CurrencyDto currencyDto = CurrencyDto.builder().code(null).name("Brazilian Real").build();

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> currencyServiceJPA.save(currencyDto));
        Assertions.assertThat(constraintViolationException.getMessage()).contains("code");
        Assertions.assertThat(constraintViolationException.getMessage()).contains("must not be blank");
    }

    @Test
    void update() {
        CurrencyDto currencyDto = CurrencyDto.builder().code("CHF").name("Swiss Frank").build();

        currencyDto = currencyServiceJPA.save(currencyDto);

        assertThat(currencyDto).isNotNull();
        assertThat(currencyDto.getId()).isNotNull();

        CurrencyDto updatedCurrencyDto = currencyServiceJPA.findById(currencyDto.getId());

        updatedCurrencyDto.setName("Condado de Torres_1");

        updatedCurrencyDto = currencyServiceJPA.save(updatedCurrencyDto);

        AssertionUtils.dtoUpdateAssertions(currencyDto, updatedCurrencyDto);
        assertThat(currencyDto.getName()).isNotEqualTo(updatedCurrencyDto.getName());
    }

    @Test
    void saveBulk() {
        CurrencyDto currencyDto1 = CurrencyDto.builder().id(1L).code("USD").name("American Dollar").build();
        CurrencyDto currencyDto2 = CurrencyDto.builder().id(1L).code("EUR").name("Euro").build();

        Set<CurrencyDto> currencyDtos = new HashSet<>();
        currencyDtos.add(currencyDto1);
        currencyDtos.add(currencyDto2);

        Set<CurrencyDto> savedCurrencyDtos = currencyServiceJPA.saveBulk(currencyDtos);

        for(CurrencyDto currencyDto : savedCurrencyDtos){
            assertThat(currencyDto.getId()).isNotNull();
            assertThat(currencyDto.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        CurrencyDto currencyDto = CurrencyDto.builder().code("GBP").name("British Pound").build();

        CurrencyDto savedCurrencyDto  = currencyServiceJPA.save(currencyDto);
        Long currencyId = savedCurrencyDto.getId();

        CurrencyDto currencyDtoById = currencyServiceJPA.findById(currencyId);

        assertThat(currencyDtoById).isEqualTo(savedCurrencyDto);
    }

    @Test
    void deleteById() {
        CurrencyDto currencyDto = CurrencyDto.builder().code("GBP").name("British Pound").build();

        CurrencyDto savedCurrencyDto  = currencyServiceJPA.save(currencyDto);
        Long currencyId = savedCurrencyDto.getId();

        currencyServiceJPA.deleteById(currencyId);

        assertThrows(DataNotFoundException.class, () -> currencyServiceJPA.findById(currencyId));
    }

    @Test
    void findByCode() {
        CurrencyDto currencyDto = CurrencyDto.builder().code("GBP").name("British Pound").build();

        CurrencyDto savedCurrencyDto  = currencyServiceJPA.save(currencyDto);
        Long currencyId = savedCurrencyDto.getId();

        CurrencyDto currencyDtoById = currencyServiceJPA.findById(currencyId);

        assertThat(currencyDtoById).isEqualTo(savedCurrencyDto);
    }

    @Test
    void findByCodeNotFound() {
        String code = "1000";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> currencyServiceJPA.findByCode(code));
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Currency.class.getSimpleName());
        assertThat(dataNotFoundException.getMessage().compareToIgnoreCase(Fields.CODE.toString()));
        assertThat(dataNotFoundException.getMessage().compareToIgnoreCase(code));
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> currencyServiceJPA.findById(id));
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Currency.class.getSimpleName());
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}