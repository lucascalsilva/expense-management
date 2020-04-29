package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.repositories.SegmentTypeRepository;
import com.mobnova.expense_mgt.services.impl.jpa.CurrencyServiceJPAImpl;
import com.mobnova.expense_mgt.services.impl.jpa.SegmentTypeJPAImpl;
import com.mobnova.expense_mgt.validation.BeanValidator;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SegmentTypeJPAImplTest {

    @InjectMocks
    @Spy
    private SegmentTypeJPAImpl segmentTypeJPA;

    @Mock
    private SegmentTypeRepository segmentTypeRepository;

    @Mock
    private BeanValidator beanValidator;

    @Test
    void save() {
        SegmentType segmentType = SegmentType.builder().code("CC").name("Cost Center").build();

        doAnswer(returnsFirstArg()).when(segmentTypeRepository).save(segmentType);

        segmentTypeJPA.save(segmentType);

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

        Set<SegmentType> savedSegmentTypes = segmentTypeJPA.saveBulk(segmentTypes);

        for(SegmentType segmentType : savedSegmentTypes){
            verify(segmentTypeRepository, times(1)).save(segmentType);
            verify(segmentTypeJPA, times(1)).save(segmentType);
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
        segmentTypeJPA.deleteById(1L);

        verify(segmentTypeRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByCode() {
        SegmentType segmentType = SegmentType.builder().code("CC").name("Cost Center").build();

        when(segmentTypeRepository.findByCode(segmentType.getCode())).thenReturn(Optional.of(segmentType));

        Optional<SegmentType> segmentTypeByCode = segmentTypeJPA.findByCode("CC");

        verify(segmentTypeRepository, times(1)).findByCode(segmentType.getCode());

        assertThat(segmentTypeByCode.isPresent());
        assertThat(segmentTypeByCode.get()).isEqualTo(segmentType);
    }
}