package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.model.SegmentValuePair;
import com.mobnova.expense_mgt.repositories.SegmentTypeRepository;
import com.mobnova.expense_mgt.repositories.SegmentValuePairRepository;
import com.mobnova.expense_mgt.services.impl.jpa.SegmentTypeJPAImpl;
import com.mobnova.expense_mgt.services.impl.jpa.SegmentValuePairJPAImpl;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.text.html.Option;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SegmentValuePairJPAImplIT {

    @Autowired
    private SegmentValuePairJPAImpl segmentValuePairJPA;

    @Autowired
    private SegmentTypeRepository segmentTypeRepository;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    private SegmentType segmentType;

    private static Boolean dbInitialized = false;

    @BeforeEach
    public void init(){
        if(!dbInitialized) {
            integrationTestHelper.cleanAllData();
            segmentType = SegmentType.builder().code("CC").name("Cost Center").build();
            segmentTypeRepository.save(segmentType);
        }
    }

    @Test
    void save() {
        SegmentValuePair segmentValuePair = SegmentValuePair.builder().segmentValue("1000").segmentType(segmentType).build();

        SegmentValuePair savedSegmentValuePair = segmentValuePairJPA.save(segmentValuePair);

        assertThat(savedSegmentValuePair.getId()).isNotNull();
        assertThat(savedSegmentValuePair.getCreationDate()).isNotNull();
    }

    @Test
    void saveBulk() {
        SegmentValuePair segmentValuePair1 = SegmentValuePair.builder().segmentValue("1001").segmentType(segmentType).build();
        SegmentValuePair segmentValuePair2 = SegmentValuePair.builder().segmentValue("1002").segmentType(segmentType).build();

        Set<SegmentValuePair> segmentValuePairs = new HashSet<>();
        segmentValuePairs.add(segmentValuePair1);
        segmentValuePairs.add(segmentValuePair2);

        Set<SegmentValuePair> savedSegmentValuePairs = segmentValuePairJPA.saveBulk(segmentValuePairs);

        for(SegmentValuePair segmentValuePair : savedSegmentValuePairs){
            assertThat(segmentValuePair.getId()).isNotNull();
            assertThat(segmentValuePair.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        SegmentValuePair segmentValuePair = SegmentValuePair.builder().segmentValue("1003").segmentType(segmentType).build();

        SegmentValuePair savedSegmentValuePair = segmentValuePairJPA.save(segmentValuePair);
        Long segmentValuePairId = savedSegmentValuePair.getId();

        Optional<SegmentValuePair> segmentValuePairById = segmentValuePairJPA.findById(segmentValuePairId);

        assertThat(segmentValuePairById).isPresent();
        assertThat(segmentValuePairById.get()).isEqualTo(savedSegmentValuePair);
    }

    @Test
    void deleteById() {
        SegmentValuePair segmentValuePair = SegmentValuePair.builder().segmentValue("1004").segmentType(segmentType).build();

        SegmentValuePair savedSegmentValuePair = segmentValuePairJPA.save(segmentValuePair);
        Long segmentValuePairId = savedSegmentValuePair.getId();

        segmentValuePairJPA.deleteById(segmentValuePairId);

        Optional<SegmentValuePair> segmentValuePairById = segmentValuePairJPA.findById(segmentValuePairId);

        assertThat(segmentValuePairById).isEmpty();
    }

    @Test
    void findByValueAndSegmentTypeCode() {
        SegmentValuePair segmentValuePair = SegmentValuePair.builder().segmentValue("1005").segmentType(segmentType).build();

        SegmentValuePair savedSegmentValuePair = segmentValuePairJPA.save(segmentValuePair);
        String segmentTypeCode = savedSegmentValuePair.getSegmentType().getCode();
        String segmentValuePairValue = savedSegmentValuePair.getSegmentValue();

        Optional<SegmentValuePair> segmentValuePairById = segmentValuePairJPA.findByValueAndSegmentTypeCode(segmentValuePairValue, segmentTypeCode);

        assertThat(segmentValuePairById).isPresent();
        assertThat(segmentValuePairById.get()).isEqualTo(savedSegmentValuePair);
    }
}