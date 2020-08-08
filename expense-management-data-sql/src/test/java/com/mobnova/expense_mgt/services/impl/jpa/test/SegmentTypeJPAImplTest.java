package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.config.ModelMapperConfiguration;
import com.mobnova.expense_mgt.dto.v1.SegmentTypeDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.repositories.SegmentTypeRepository;
import com.mobnova.expense_mgt.services.impl.jpa.SegmentTypeServiceJPAImpl;
import org.assertj.core.api.AssertionsForClassTypes;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SegmentTypeJPAImplTest {

    private SegmentTypeServiceJPAImpl segmentTypeServiceJPA;

    @Mock
    private SegmentTypeRepository segmentTypeRepository;

    private ModelMapper modelMapper;

    @BeforeEach
    public void setup(){
        modelMapper = new ModelMapperConfiguration().globalMapper();
        segmentTypeServiceJPA = Mockito.spy(new SegmentTypeServiceJPAImpl(segmentTypeRepository, modelMapper));
    }

    @Test
    void save() {
        SegmentTypeDto segmentTypeDto = SegmentTypeDto.builder().id(1L).code("CC").name("Cost Center").build();
        SegmentType segmentType = modelMapper.map(segmentTypeDto, SegmentType.class);

        when(segmentTypeRepository.findById(1L)).thenReturn(Optional.of(segmentType));

        doAnswer(returnsFirstArg()).when(segmentTypeRepository).save(segmentType);

        segmentTypeServiceJPA.save(segmentTypeDto);

        verify(segmentTypeRepository).save(segmentType);
    }

    @Test
    void saveBulk() {
        SegmentTypeDto segmentTypeDto1 = SegmentTypeDto.builder().id(1L).code("CC").name("Cost Center").build();
        SegmentTypeDto segmentTypeDto2 = SegmentTypeDto.builder().id(2L).code("NC").name("Natural Account").build();
        SegmentType segmentType1 = modelMapper.map(segmentTypeDto1, SegmentType.class);
        SegmentType segmentType2 = modelMapper.map(segmentTypeDto2, SegmentType.class);

        Set<SegmentTypeDto> segmentTypeDtos = new HashSet<>();

        segmentTypeDtos.add(segmentTypeDto1);
        segmentTypeDtos.add(segmentTypeDto2);

        when(segmentTypeRepository.findById(1L)).thenReturn(Optional.of(segmentType1));
        when(segmentTypeRepository.findById(2L)).thenReturn(Optional.of(segmentType2));

        doAnswer(returnsFirstArg()).when(segmentTypeRepository).save(any(SegmentType.class));

        Set<SegmentTypeDto> savedSegmentTypeDtos = segmentTypeServiceJPA.saveBulk(segmentTypeDtos);
        assertThat(savedSegmentTypeDtos).hasSize(2);

        verify(segmentTypeRepository, times(2)).save(any(SegmentType.class));
        verify(segmentTypeServiceJPA, times(2)).save(any(SegmentTypeDto.class));
    }

    @Test
    void findById() {
        SegmentTypeDto segmentTypeDto = SegmentTypeDto.builder().id(1L).code("CC").name("Cost Center").build();
        SegmentType segmentType = modelMapper.map(segmentTypeDto, SegmentType.class);

        when(segmentTypeRepository.findById(segmentType.getId())).thenReturn(Optional.of(segmentType));

        SegmentTypeDto segmentTypeDtoById = segmentTypeServiceJPA.findById(1L);

        assertThat(segmentTypeDtoById.getId()).isEqualTo(segmentType.getId());
    }

    @Test
    void deleteById() {
        segmentTypeServiceJPA.deleteById(1L);

        verify(segmentTypeRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByCode() {
        SegmentTypeDto segmentTypeDto = SegmentTypeDto.builder().code("CC").name("Cost Center").build();
        SegmentType segmentType = modelMapper.map(segmentTypeDto, SegmentType.class);

        when(segmentTypeRepository.findByCode(segmentTypeDto.getCode())).thenReturn(Optional.of(segmentType));

        SegmentTypeDto segmentTypeDtoByCode = segmentTypeServiceJPA.findByCode("CC");

        verify(segmentTypeRepository, times(1)).findByCode(segmentType.getCode());

        assertThat(segmentTypeDtoByCode.getId()).isEqualTo(segmentType.getId());
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