package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.repositories.SegmentTypeRepository;
import com.mobnova.expense_mgt.services.impl.jpa.SegmentTypeServiceJPAImpl;
import org.assertj.core.api.AssertionsForClassTypes;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SegmentTypeJPAImplTest {

    @InjectMocks
    @Spy
    private SegmentTypeServiceJPAImpl segmentTypeServiceJPA;

    @Mock
    private SegmentTypeRepository segmentTypeRepository;

    @Test
    void save() {
        SegmentType segmentType = SegmentType.builder().code("CC").name("Cost Center").build();

        doAnswer(returnsFirstArg()).when(segmentTypeRepository).save(segmentType);

        segmentTypeServiceJPA.save(segmentType);

        verify(segmentTypeRepository).save(segmentType);
    }

    @Test
    void saveBulk() {
        SegmentType segmentType1 = SegmentType.builder().code("CC").name("Cost Center").build();
        SegmentType segmentType2 = SegmentType.builder().code("NC").name("Natural Account").build();

        Set<SegmentType> segmentTypes = new HashSet<>();

        segmentTypes.add(segmentType1);
        segmentTypes.add(segmentType2);

        doAnswer(returnsFirstArg()).when(segmentTypeRepository).save(any(SegmentType.class));

        Set<SegmentType> savedSegmentTypes = segmentTypeServiceJPA.saveBulk(segmentTypes);

        for(SegmentType segmentType : savedSegmentTypes){
            verify(segmentTypeRepository, times(1)).save(segmentType);
            verify(segmentTypeServiceJPA, times(1)).save(segmentType);
        }
    }

    @Test
    void findById() {
        SegmentType segmentType = SegmentType.builder().id(1L).code("CC").name("Cost Center").build();

        when(segmentTypeRepository.findById(segmentType.getId())).thenReturn(Optional.of(segmentType));

        Optional<SegmentType> segmentTypeById = segmentTypeRepository.findById(1L);

        assertThat(segmentTypeById.isPresent());
        assertThat(segmentTypeById.get()).isEqualTo(segmentType);
    }

    @Test
    void deleteById() {
        segmentTypeServiceJPA.deleteById(1L);

        verify(segmentTypeRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByCode() {
        SegmentType segmentType = SegmentType.builder().code("CC").name("Cost Center").build();

        when(segmentTypeRepository.findByCode(segmentType.getCode())).thenReturn(Optional.of(segmentType));

        SegmentType segmentTypeByCode = segmentTypeServiceJPA.findByCode("CC");

        verify(segmentTypeRepository, times(1)).findByCode(segmentType.getCode());

        assertThat(segmentTypeByCode).isEqualTo(segmentType);
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