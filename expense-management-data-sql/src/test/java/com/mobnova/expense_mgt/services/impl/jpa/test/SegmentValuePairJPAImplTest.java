package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.model.SegmentValuePair;
import com.mobnova.expense_mgt.repositories.SegmentTypeRepository;
import com.mobnova.expense_mgt.repositories.SegmentValuePairRepository;
import com.mobnova.expense_mgt.services.impl.jpa.SegmentValuePairJPAImpl;
import com.mobnova.expense_mgt.validation.BeanValidator;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SegmentValuePairJPAImplTest {

    @InjectMocks
    @Spy
    private SegmentValuePairJPAImpl segmentValuePairJPA;

    @Mock
    private SegmentValuePairRepository segmentValuePairRepository;

    @Mock
    private SegmentTypeRepository segmentTypeRepository;

    @Mock
    private BeanValidator beanValidator;

    private SegmentType segmentType;

    @BeforeEach
    public void init(){
        segmentType = SegmentType.builder().id(1L).code("CC").name("Cost Center").build();
    }

    @Test
    void save() {
        SegmentValuePair segmentValuePair = SegmentValuePair.builder().segmentValue("1001")
                .segmentType(SegmentType.builder().code("CC").build()).build();

        doAnswer(returnsFirstArg()).when(segmentValuePairRepository).save(segmentValuePair);
        when(segmentTypeRepository.findByCode(segmentType.getCode())).thenReturn(Optional.of(segmentType));

        SegmentValuePair savedSegmentValuePair = segmentValuePairJPA.save(segmentValuePair);

        verify(segmentValuePairRepository).save(segmentValuePair);
        assertThat(savedSegmentValuePair.getSegmentType().getId()).isEqualTo(1L);
    }

    @Test
    void saveBulk() {
        SegmentValuePair segmentValuePair1 = SegmentValuePair.builder().segmentValue("1001")
                .segmentType(SegmentType.builder().code("CC").build()).build();
        SegmentValuePair segmentValuePair2 = SegmentValuePair.builder().segmentValue("1002")
                .segmentType(SegmentType.builder().code("CC").build()).build();

        Set<SegmentValuePair> segmentValuePairs = new HashSet<>();

        segmentValuePairs.add(segmentValuePair1);
        segmentValuePairs.add(segmentValuePair2);

        doAnswer(returnsFirstArg()).when(segmentValuePairRepository).save(any(SegmentValuePair.class));
        when(segmentTypeRepository.findByCode(segmentType.getCode())).thenReturn(Optional.of(segmentType));

        Set<SegmentValuePair> savedSegmentValuePairs = segmentValuePairJPA.saveBulk(segmentValuePairs);

        for(SegmentValuePair segmentValuePair : savedSegmentValuePairs){
            verify(segmentValuePairRepository, times(1)).save(segmentValuePair);
            verify(segmentValuePairJPA, times(1)).save(segmentValuePair);
        }
    }

    @Test
    void findById() {
        SegmentValuePair segmentValuePair = SegmentValuePair.builder().id(1L).segmentValue("1001")
                .segmentType(segmentType).build();

        when(segmentValuePairRepository.findById(segmentValuePair.getId())).thenReturn(Optional.of(segmentValuePair));

        Optional<SegmentValuePair> segmentValuePairById = segmentValuePairJPA.findById(1L);

        assertThat(segmentValuePairById.isPresent());
        assertThat(segmentValuePairById.get()).isEqualTo(segmentValuePair);
    }

    @Test
    void findByValueAndSegmentTypeCode(){
        SegmentValuePair segmentValuePair = SegmentValuePair.builder().id(1L).segmentValue("1001")
                .segmentType(segmentType).build();

        when(segmentValuePairRepository.findByValueAndSegmentTypeCode(segmentValuePair.getSegmentValue(),
                segmentValuePair.getSegmentType().getCode())).thenReturn(Optional.of(segmentValuePair));

        Optional<SegmentValuePair> segmentValuePairByQuery = segmentValuePairJPA
                .findByValueAndSegmentTypeCode(segmentValuePair.getSegmentValue(), segmentType.getCode());

        assertThat(segmentValuePairByQuery.isPresent());
        assertThat(segmentValuePairByQuery.get()).isEqualTo(segmentValuePair);
    }

    @Test
    void deleteById() {
        segmentValuePairJPA.deleteById(1L);

        verify(segmentValuePairRepository, times(1)).deleteById(1L);
    }
}