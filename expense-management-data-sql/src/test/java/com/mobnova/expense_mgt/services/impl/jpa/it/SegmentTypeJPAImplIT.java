package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.dto.v1.SegmentTypeDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.services.impl.jpa.SegmentTypeServiceJPAImpl;
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
class SegmentTypeJPAImplIT {

    @Autowired
    private SegmentTypeServiceJPAImpl segmentTypeServiceJPA;

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
        SegmentTypeDto segmentTypeDto = SegmentTypeDto.builder().code("CC").name("Cost Center").order(4L).build();

        SegmentTypeDto savedSegmentDtoType = segmentTypeServiceJPA.save(segmentTypeDto);

        assertThat(savedSegmentDtoType.getId()).isNotNull();
        assertThat(savedSegmentDtoType.getCreationDate()).isNotNull();
    }

    @Test
    void saveValidationError() {
        SegmentTypeDto segmentTypeDto = SegmentTypeDto.builder().code(null).name("Cost Center").order(4L).build();

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> segmentTypeServiceJPA.save(segmentTypeDto));
        assertThat(constraintViolationException.getMessage()).contains("code");
        assertThat(constraintViolationException.getMessage()).contains("must not be blank");
    }

    @Test
    void update() {
        SegmentTypeDto segmentTypeDto = SegmentTypeDto.builder().code("FUTURE3").name("Future 3").order(8L).build();

        segmentTypeDto = segmentTypeServiceJPA.save(segmentTypeDto);

        assertThat(segmentTypeDto).isNotNull();
        assertThat(segmentTypeDto.getId()).isNotNull();

        SegmentTypeDto updatedSegmentTypeDto = segmentTypeServiceJPA.findById(segmentTypeDto.getId());

        updatedSegmentTypeDto.setName("Future 3_1");

        updatedSegmentTypeDto = segmentTypeServiceJPA.save(updatedSegmentTypeDto);

        AssertionUtils.dtoUpdateAssertions(segmentTypeDto, updatedSegmentTypeDto);
        assertThat(segmentTypeDto.getName()).isNotEqualTo(updatedSegmentTypeDto.getName());
    }

    @Test
    void saveBulk() {
        SegmentTypeDto segmentTypeDto1 = SegmentTypeDto.builder().code("LE").name("Legal Entity").order(2L).build();
        SegmentTypeDto segmentTypeDto2 = SegmentTypeDto.builder().code("BU").name("Business Unit").order(3L).build();

        Set<SegmentTypeDto> segmentTypeDtos = new HashSet<>();
        segmentTypeDtos.add(segmentTypeDto1);
        segmentTypeDtos.add(segmentTypeDto2);

        Set<SegmentTypeDto> savedSegmentTypeDtos = segmentTypeServiceJPA.saveBulk(segmentTypeDtos);

        for(SegmentTypeDto segmentTypeDto : savedSegmentTypeDtos){
            assertThat(segmentTypeDto.getId()).isNotNull();
            assertThat(segmentTypeDto.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        SegmentTypeDto segmentTypeDto = SegmentTypeDto.builder().code("NA").name("Natural Account").order(5L).build();

        SegmentTypeDto savedSegmentTypeDto = segmentTypeServiceJPA.save(segmentTypeDto);
        Long segmentTypeId = savedSegmentTypeDto.getId();

        SegmentTypeDto segmentTypeDtoById = segmentTypeServiceJPA.findById(segmentTypeId);

        assertThat(segmentTypeDtoById).isEqualTo(segmentTypeDtoById);
    }

    @Test
    void deleteById() {
        SegmentTypeDto segmentTypeDto = SegmentTypeDto.builder().code("FUTURE1").name("Future 1").order(6L).build();

        SegmentTypeDto savedSegmentTypeDto  = segmentTypeServiceJPA.save(segmentTypeDto);
        Long savedSegmentTypeId = savedSegmentTypeDto.getId();

        segmentTypeServiceJPA.deleteById(savedSegmentTypeId);

        assertThrows(DataNotFoundException.class, () -> segmentTypeServiceJPA.findById(savedSegmentTypeId));
    }

    @Test
    void findByCode() {
        SegmentTypeDto segmentTypeDto = SegmentTypeDto.builder().code("FUTURE2").name("Future 2").order(7L).build();

        SegmentTypeDto savedSegmentTypeDto  = segmentTypeServiceJPA.save(segmentTypeDto);
        String segmentTypeCode = savedSegmentTypeDto.getCode();

        SegmentTypeDto segmentTypeDtoByCode = segmentTypeServiceJPA.findByCode(segmentTypeCode);

        assertThat(segmentTypeDtoByCode).isEqualTo(savedSegmentTypeDto);
    }

    @Test
    void findByCodeNotFound() {
        String code = "1000";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> segmentTypeServiceJPA.findByCode(code));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(SegmentType.class.getSimpleName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.CODE.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(code);
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> segmentTypeServiceJPA.findById(id));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(SegmentType.class.getSimpleName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}