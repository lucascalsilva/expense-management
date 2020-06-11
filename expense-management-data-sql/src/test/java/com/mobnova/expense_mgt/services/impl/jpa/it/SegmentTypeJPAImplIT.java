package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.services.impl.jpa.SegmentTypeJPAImpl;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.text.Segment;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SegmentTypeJPAImplIT {

    @Autowired
    private SegmentTypeJPAImpl segmentTypeJPA;

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
        SegmentType segmentType = SegmentType.builder().code("CC").name("Cost Center").build();

        SegmentType savedSegmentType = segmentTypeJPA.save(segmentType);

        assertThat(savedSegmentType.getId()).isNotNull();
        assertThat(savedSegmentType.getCreationDate()).isNotNull();
    }

    @Test
    void saveBulk() {
        SegmentType segmentType1 = SegmentType.builder().code("LE").name("Legal Entity").build();
        SegmentType segmentType2 = SegmentType.builder().code("BU").name("Business Unit").build();

        Set<SegmentType> segmentTypes = new HashSet<>();
        segmentTypes.add(segmentType1);
        segmentTypes.add(segmentType2);

        Set<SegmentType> savedSegmentTypes = segmentTypeJPA.saveBulk(segmentTypes);

        for(SegmentType segmentType : savedSegmentTypes){
            assertThat(segmentType.getId()).isNotNull();
            assertThat(segmentType.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        SegmentType segmentType = SegmentType.builder().code("AC").name("Natural Account").build();

        SegmentType savedSegmentType = segmentTypeJPA.save(segmentType);
        Long segmentTypeId = savedSegmentType.getId();

        SegmentType segmentTypeById = segmentTypeJPA.findById(segmentTypeId);

        assertThat(segmentTypeById).isEqualTo(savedSegmentType);
    }

    @Test
    void deleteById() {
        SegmentType segmentType = SegmentType.builder().code("FUTURE1").name("Future 1").build();

        SegmentType savedSegmentType  = segmentTypeJPA.save(segmentType);
        Long savedSegmentTypeId = savedSegmentType.getId();

        segmentTypeJPA.deleteById(savedSegmentTypeId);

        assertThrows(DataNotFoundException.class, () -> segmentTypeJPA.findById(savedSegmentTypeId));
    }

    @Test
    void findByCode() {
        SegmentType segmentType = SegmentType.builder().code("FUTURE2").name("Future 2").build();

        SegmentType savedSegmentType  = segmentTypeJPA.save(segmentType);
        String segmentTypeCode = savedSegmentType.getCode();

        SegmentType segmentTypeByCode = segmentTypeJPA.findByCode(segmentTypeCode);

        assertThat(segmentTypeByCode).isEqualTo(savedSegmentType);
    }
}