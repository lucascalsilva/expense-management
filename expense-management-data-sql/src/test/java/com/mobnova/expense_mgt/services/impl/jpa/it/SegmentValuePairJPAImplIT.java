package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.model.SegmentValuePair;
import com.mobnova.expense_mgt.repositories.SegmentTypeRepository;
import com.mobnova.expense_mgt.services.impl.jpa.SegmentValuePairServiceJPAImpl;
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
class SegmentValuePairJPAImplIT {

    @Autowired
    private SegmentValuePairServiceJPAImpl segmentValuePairServiceJPA;

    @Autowired
    private SegmentTypeRepository segmentTypeRepository;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    private SegmentType segmentType;

    private static final Boolean dbInitialized = false;

    @BeforeEach
    public void init(){
        if(!dbInitialized) {
            integrationTestHelper.cleanAllData();
            segmentType = SegmentType.builder().code("CC").name("Cost Center").order(4L).build();
            segmentTypeRepository.save(segmentType);
        }
    }

    @Test
    void save() {
        SegmentValuePair segmentValuePair = SegmentValuePair.builder().segmentValue("1000").segmentType(segmentType).build();

        SegmentValuePair savedSegmentValuePair = segmentValuePairServiceJPA.save(segmentValuePair);

        assertThat(savedSegmentValuePair.getId()).isNotNull();
        assertThat(savedSegmentValuePair.getCreationDate()).isNotNull();
    }

    @Test
    void saveValidationError() {
        SegmentValuePair segmentValuePair = SegmentValuePair.builder().segmentValue(null).segmentType(segmentType).build();

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> segmentValuePairServiceJPA.save(segmentValuePair));
        Assertions.assertThat(constraintViolationException.getMessage()).contains("save.arg0.segmentValue: must not be blank");
    }

    @Test
    void saveBulk() {
        SegmentValuePair segmentValuePair1 = SegmentValuePair.builder().segmentValue("1001").segmentType(segmentType).build();
        SegmentValuePair segmentValuePair2 = SegmentValuePair.builder().segmentValue("1002").segmentType(segmentType).build();

        Set<SegmentValuePair> segmentValuePairs = new HashSet<>();
        segmentValuePairs.add(segmentValuePair1);
        segmentValuePairs.add(segmentValuePair2);

        Set<SegmentValuePair> savedSegmentValuePairs = segmentValuePairServiceJPA.saveBulk(segmentValuePairs);

        for(SegmentValuePair segmentValuePair : savedSegmentValuePairs){
            assertThat(segmentValuePair.getId()).isNotNull();
            assertThat(segmentValuePair.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        SegmentValuePair segmentValuePair = SegmentValuePair.builder().segmentValue("1003").segmentType(segmentType).build();

        SegmentValuePair savedSegmentValuePair = segmentValuePairServiceJPA.save(segmentValuePair);
        Long segmentValuePairId = savedSegmentValuePair.getId();

        SegmentValuePair segmentValuePairById = segmentValuePairServiceJPA.findById(segmentValuePairId);

        assertThat(segmentValuePairById).isEqualTo(savedSegmentValuePair);
    }

    @Test
    void deleteById() {
        SegmentValuePair segmentValuePair = SegmentValuePair.builder().segmentValue("1004").segmentType(segmentType).build();

        SegmentValuePair savedSegmentValuePair = segmentValuePairServiceJPA.save(segmentValuePair);
        Long segmentValuePairId = savedSegmentValuePair.getId();

        segmentValuePairServiceJPA.deleteById(segmentValuePairId);

        assertThrows(DataNotFoundException.class, () -> segmentValuePairServiceJPA.findById(segmentValuePairId));
    }

    @Test
    void findByValueAndSegmentTypeCode() {
        SegmentValuePair segmentValuePair = SegmentValuePair.builder().segmentValue("1005").segmentType(segmentType).build();

        SegmentValuePair savedSegmentValuePair = segmentValuePairServiceJPA.save(segmentValuePair);
        String segmentTypeCode = savedSegmentValuePair.getSegmentType().getCode();
        String segmentValuePairValue = savedSegmentValuePair.getSegmentValue();

        SegmentValuePair segmentValuePairById = segmentValuePairServiceJPA.findByValueAndSegmentTypeCode(segmentValuePairValue, segmentTypeCode);

        assertThat(segmentValuePairById).isEqualTo(savedSegmentValuePair);
    }

    @Test
    void findByValueAndSegmentTypeCodeNotFound() {
        String segmentCode = "1000";
        String segmentValue = "AA";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> segmentValuePairServiceJPA.findByValueAndSegmentTypeCode(segmentValue, segmentCode));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(SegmentValuePair.class.getName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.SEGMENT_VALUE.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.SEGMENT_TYPE_CODE.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(segmentValue);
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(segmentCode);
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> segmentValuePairServiceJPA.findById(id));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(SegmentValuePair.class.getName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}