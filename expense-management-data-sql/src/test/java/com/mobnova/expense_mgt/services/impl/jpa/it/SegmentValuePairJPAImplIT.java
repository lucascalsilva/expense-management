package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.dto.v1.SegmentTypeDto;
import com.mobnova.expense_mgt.dto.v1.SegmentValuePairDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.model.SegmentValuePair;
import com.mobnova.expense_mgt.repositories.SegmentTypeRepository;
import com.mobnova.expense_mgt.services.impl.jpa.SegmentValuePairServiceJPAImpl;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

    private SegmentType segmentType;

    private SegmentTypeDto segmentTypeDto;

    private static Boolean dbInitialized = false;

    @BeforeEach
    public void init(){
        segmentType = SegmentType.builder().code("CC").name("Cost Center").order(4L).build();
        segmentTypeDto = modelMapper.map(segmentType, SegmentTypeDto.class);

        if(!dbInitialized) {
            integrationTestHelper.cleanAllData();
            segmentTypeRepository.save(segmentType);
            dbInitialized = true;
        }
    }

    @Test
    void save() {
        SegmentValuePairDto segmentValuePairDto = SegmentValuePairDto.builder().segmentValue("1000").segmentType(segmentTypeDto).build();
        SegmentValuePairDto savedSegmentValuePairDto = segmentValuePairServiceJPA.save(segmentValuePairDto);

        assertThat(savedSegmentValuePairDto.getId()).isNotNull();
        assertThat(savedSegmentValuePairDto.getCreationDate()).isNotNull();
    }

    @Test
    void saveValidationError() {
        SegmentValuePairDto segmentValuePairDto = SegmentValuePairDto.builder().segmentValue(null).segmentType(segmentTypeDto).build();

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> segmentValuePairServiceJPA.save(segmentValuePairDto));
        assertThat(constraintViolationException.getMessage()).contains("segmentValue");
        assertThat(constraintViolationException.getMessage()).contains("must not be blank");
    }

    @Test
    void saveBulk() {
        SegmentValuePairDto segmentValuePairDto1 = SegmentValuePairDto.builder().segmentValue("1001").segmentType(segmentTypeDto).build();
        SegmentValuePairDto segmentValuePairDto2 = SegmentValuePairDto.builder().segmentValue("1002").segmentType(segmentTypeDto).build();

        Set<SegmentValuePairDto> segmentValuePairDtos = new HashSet<>();
        segmentValuePairDtos.add(segmentValuePairDto1);
        segmentValuePairDtos.add(segmentValuePairDto2);

        Set<SegmentValuePairDto> savedSegmentValuePairDtos = segmentValuePairServiceJPA.saveBulk(segmentValuePairDtos);

        for(SegmentValuePairDto segmentValuePairDto : savedSegmentValuePairDtos){
            assertThat(segmentValuePairDto.getId()).isNotNull();
            assertThat(segmentValuePairDto.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        SegmentValuePairDto segmentValuePairDto = SegmentValuePairDto.builder().segmentValue("1003").segmentType(segmentTypeDto).build();

        SegmentValuePairDto savedSegmentValuePairDto = segmentValuePairServiceJPA.save(segmentValuePairDto);
        Long segmentValuePairId = savedSegmentValuePairDto.getId();

        SegmentValuePairDto segmentValuePairByIdDto = segmentValuePairServiceJPA.findById(segmentValuePairId);

        assertThat(segmentValuePairByIdDto).isEqualTo(savedSegmentValuePairDto);
    }

    @Test
    void deleteById() {
        SegmentValuePairDto segmentValuePairDto = SegmentValuePairDto.builder().segmentValue("1004").segmentType(segmentTypeDto).build();

        SegmentValuePairDto savedSegmentValuePairDto = segmentValuePairServiceJPA.save(segmentValuePairDto);
        Long segmentValuePairId = savedSegmentValuePairDto.getId();

        segmentValuePairServiceJPA.deleteById(segmentValuePairId);

        assertThrows(DataNotFoundException.class, () -> segmentValuePairServiceJPA.findById(segmentValuePairId));
    }

    @Test
    void findByValueAndSegmentTypeCode() {
        SegmentValuePairDto segmentValuePairDto = SegmentValuePairDto.builder().segmentValue("1005").segmentType(segmentTypeDto).build();

        SegmentValuePairDto savedSegmentValuePairDto = segmentValuePairServiceJPA.save(segmentValuePairDto);
        String segmentTypeCode = segmentValuePairDto.getSegmentType().getCode();
        String segmentValuePairValue = segmentValuePairDto.getSegmentValue();

        SegmentValuePairDto segmentValuePairByIdDto = segmentValuePairServiceJPA.findByValueAndSegmentTypeCode(segmentValuePairValue, segmentTypeCode);

        assertThat(segmentValuePairByIdDto).isEqualTo(savedSegmentValuePairDto);
    }

    @Test
    void findByValueAndSegmentTypeCodeNotFound() {
        String segmentCode = "1000";
        String segmentValue = "AA";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> segmentValuePairServiceJPA.findByValueAndSegmentTypeCode(segmentValue, segmentCode));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(SegmentValuePair.class.getSimpleName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.SEGMENT_VALUE.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.SEGMENT_TYPE_CODE.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(segmentValue);
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(segmentCode);
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> segmentValuePairServiceJPA.findById(id));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(SegmentValuePair.class.getSimpleName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}