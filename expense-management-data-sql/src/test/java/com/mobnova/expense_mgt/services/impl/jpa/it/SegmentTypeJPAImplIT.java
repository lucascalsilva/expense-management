package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.services.impl.jpa.SegmentTypeServiceJPAImpl;
import com.mobnova.expense_mgt.util.AssertionsUtil;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.assertj.core.api.Assertions;
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
        SegmentType segmentType = SegmentType.builder().code("CC").name("Cost Center").build();

        SegmentType savedSegmentType = segmentTypeServiceJPA.save(segmentType);

        assertThat(savedSegmentType.getId()).isNotNull();
        assertThat(savedSegmentType.getCreationDate()).isNotNull();
    }

    @Test
    void saveValidationError() {
        SegmentType segmentType = SegmentType.builder().code(null).name("Cost Center").build();

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> segmentTypeServiceJPA.save(segmentType));
        Assertions.assertThat(constraintViolationException.getMessage()).contains("save.arg0.code: must not be blank");
    }

    @Test
    void update() {
        SegmentType segmentType = SegmentType.builder().code("FUTURE3").name("Future 3").build();

        segmentType = segmentTypeServiceJPA.save(segmentType);

        assertThat(segmentType).isNotNull();
        assertThat(segmentType.getId()).isNotNull();

        SegmentType updatedSegmentType = segmentTypeServiceJPA.findById(segmentType.getId());

        updatedSegmentType.setName("Future 3_1");

        updatedSegmentType = segmentTypeServiceJPA.save(updatedSegmentType);

        AssertionsUtil.entityUpdateAssertions(segmentType, updatedSegmentType);
        assertThat(segmentType.getName()).isNotEqualTo(updatedSegmentType.getName());
    }

    @Test
    void saveBulk() {
        SegmentType segmentType1 = SegmentType.builder().code("LE").name("Legal Entity").build();
        SegmentType segmentType2 = SegmentType.builder().code("BU").name("Business Unit").build();

        Set<SegmentType> segmentTypes = new HashSet<>();
        segmentTypes.add(segmentType1);
        segmentTypes.add(segmentType2);

        Set<SegmentType> savedSegmentTypes = segmentTypeServiceJPA.saveBulk(segmentTypes);

        for(SegmentType segmentType : savedSegmentTypes){
            assertThat(segmentType.getId()).isNotNull();
            assertThat(segmentType.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        SegmentType segmentType = SegmentType.builder().code("AC").name("Natural Account").build();

        SegmentType savedSegmentType = segmentTypeServiceJPA.save(segmentType);
        Long segmentTypeId = savedSegmentType.getId();

        SegmentType segmentTypeById = segmentTypeServiceJPA.findById(segmentTypeId);

        assertThat(segmentTypeById).isEqualTo(savedSegmentType);
    }

    @Test
    void deleteById() {
        SegmentType segmentType = SegmentType.builder().code("FUTURE1").name("Future 1").build();

        SegmentType savedSegmentType  = segmentTypeServiceJPA.save(segmentType);
        Long savedSegmentTypeId = savedSegmentType.getId();

        segmentTypeServiceJPA.deleteById(savedSegmentTypeId);

        assertThrows(DataNotFoundException.class, () -> segmentTypeServiceJPA.findById(savedSegmentTypeId));
    }

    @Test
    void findByCode() {
        SegmentType segmentType = SegmentType.builder().code("FUTURE2").name("Future 2").build();

        SegmentType savedSegmentType  = segmentTypeServiceJPA.save(segmentType);
        String segmentTypeCode = savedSegmentType.getCode();

        SegmentType segmentTypeByCode = segmentTypeServiceJPA.findByCode(segmentTypeCode);

        assertThat(segmentTypeByCode).isEqualTo(savedSegmentType);
    }

    @Test
    void findByCodeNotFound() {
        String code = "1000";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> segmentTypeServiceJPA.findByCode(code));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(SegmentType.class.getName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.CODE.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(code);
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> segmentTypeServiceJPA.findById(id));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(SegmentType.class.getName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}