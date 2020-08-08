package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.config.ModelMapperConfiguration;
import com.mobnova.expense_mgt.dto.v1.SegmentTypeDto;
import com.mobnova.expense_mgt.dto.v1.SegmentValuePairDto;
import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.model.SegmentValuePair;
import com.mobnova.expense_mgt.repositories.SegmentTypeRepository;
import com.mobnova.expense_mgt.repositories.SegmentValuePairRepository;
import com.mobnova.expense_mgt.services.impl.jpa.SegmentValuePairServiceJPAImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SegmentValuePairJPAImplTest {

    private SegmentValuePairServiceJPAImpl segmentValuePairServiceJPA;

    @Mock
    private SegmentValuePairRepository segmentValuePairRepository;

    @Mock
    private SegmentTypeRepository segmentTypeRepository;

    private ModelMapper modelMapper;

    private SegmentType segmentType;
    private SegmentTypeDto segmentTypeDto;

    @BeforeEach
    public void init(){
        modelMapper = new ModelMapperConfiguration().globalMapper();
        segmentValuePairServiceJPA = Mockito.spy(new SegmentValuePairServiceJPAImpl(segmentValuePairRepository, segmentTypeRepository, modelMapper));

        segmentTypeDto = SegmentTypeDto.builder().id(1L).code("CC").name("Cost Center").build();
        segmentType = modelMapper.map(segmentTypeDto, SegmentType.class);
    }

    @Test
    void save() {
        SegmentValuePairDto segmentValuePairDto = SegmentValuePairDto.builder().id(1L).segmentValue("1001")
                .segmentType(segmentTypeDto).build();
        SegmentValuePair segmentValuePair = modelMapper.map(segmentValuePairDto, SegmentValuePair.class);

        doAnswer(returnsFirstArg()).when(segmentValuePairRepository).save(segmentValuePair);
        when(segmentTypeRepository.findByCode(segmentTypeDto.getCode())).thenReturn(Optional.of(segmentType));
        when(segmentValuePairRepository.findById(1L)).thenReturn(Optional.of(segmentValuePair));

        SegmentValuePairDto savedSegmentValuePair = segmentValuePairServiceJPA.save(segmentValuePairDto);

        verify(segmentValuePairRepository).save(segmentValuePair);
        assertThat(savedSegmentValuePair.getSegmentType().getId()).isEqualTo(1L);
    }

    @Test
    void saveBulk() {
        SegmentValuePairDto segmentValuePairDto1 = SegmentValuePairDto.builder().id(1L).segmentValue("1001")
                .segmentType(segmentTypeDto).build();
        SegmentValuePairDto segmentValuePairDto2 = SegmentValuePairDto.builder().id(2L).segmentValue("1002")
                .segmentType(segmentTypeDto).build();

        SegmentValuePair segmentValuePair1 = modelMapper.map(segmentValuePairDto1, SegmentValuePair.class);
        SegmentValuePair segmentValuePair2 = modelMapper.map(segmentValuePairDto2, SegmentValuePair.class);

        Set<SegmentValuePairDto> segmentValuePairDtos = new HashSet<>();

        segmentValuePairDtos.add(segmentValuePairDto1);
        segmentValuePairDtos.add(segmentValuePairDto2);

        when(segmentValuePairRepository.findById(1L)).thenReturn(Optional.of(segmentValuePair1));
        when(segmentValuePairRepository.findById(2L)).thenReturn(Optional.of(segmentValuePair2));

        doAnswer(returnsFirstArg()).when(segmentValuePairRepository).save(any(SegmentValuePair.class));
        when(segmentTypeRepository.findByCode(segmentTypeDto.getCode())).thenReturn(Optional.of(segmentType));

        Set<SegmentValuePairDto> savedSegmentValuePairDtos = segmentValuePairServiceJPA.saveBulk(segmentValuePairDtos);
        assertThat(savedSegmentValuePairDtos).hasSize(2);

        verify(segmentValuePairRepository, times(2)).save(any(SegmentValuePair.class));
        verify(segmentValuePairServiceJPA, times(2)).save(any(SegmentValuePairDto.class));
    }

    @Test
    void findById() {
        SegmentValuePairDto segmentValuePairDto = SegmentValuePairDto.builder().id(1L).segmentValue("1001")
                .segmentType(segmentTypeDto).build();
        SegmentValuePair segmentValuePair = modelMapper.map(segmentTypeDto, SegmentValuePair.class);

        when(segmentValuePairRepository.findById(segmentValuePairDto.getId())).thenReturn(Optional.of(segmentValuePair));

        SegmentValuePairDto segmentValuePairByIdDto = segmentValuePairServiceJPA.findById(1L);

        assertThat(segmentValuePairByIdDto).isEqualTo(segmentValuePairDto);
    }

    @Test
    void findByValueAndSegmentTypeCode(){
        SegmentValuePairDto segmentValuePairDto = SegmentValuePairDto.builder().id(1L).segmentValue("1001")
                .segmentType(segmentTypeDto).build();
        SegmentValuePair segmentValuePair = modelMapper.map(segmentValuePairDto, SegmentValuePair.class);

        when(segmentValuePairRepository.findByValueAndSegmentTypeCode(segmentValuePair.getSegmentValue(),
                segmentValuePair.getSegmentType().getCode())).thenReturn(Optional.of(segmentValuePair));

        SegmentValuePairDto segmentValuePairByQueryDto = segmentValuePairServiceJPA
                .findByValueAndSegmentTypeCode(segmentValuePairDto.getSegmentValue(), segmentTypeDto.getCode());

        assertThat(segmentValuePairByQueryDto).isEqualTo(segmentValuePairDto);
    }

    @Test
    void deleteById() {
        segmentValuePairServiceJPA.deleteById(1L);

        verify(segmentValuePairRepository, times(1)).deleteById(1L);
    }


}