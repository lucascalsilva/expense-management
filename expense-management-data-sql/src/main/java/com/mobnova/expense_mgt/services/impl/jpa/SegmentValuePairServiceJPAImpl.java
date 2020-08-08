package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.dto.v1.SegmentTypeDto;
import com.mobnova.expense_mgt.dto.v1.SegmentValuePairDto;
import com.mobnova.expense_mgt.exception.ExceptionVariable;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.model.SegmentValuePair;
import com.mobnova.expense_mgt.repositories.SegmentTypeRepository;
import com.mobnova.expense_mgt.repositories.SegmentValuePairRepository;
import com.mobnova.expense_mgt.services.SegmentValuePairService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Profile("jpa")
public class SegmentValuePairServiceJPAImpl extends AbstractBaseServiceJPA<SegmentValuePair, SegmentValuePairDto, Long> implements SegmentValuePairService {

    private final SegmentValuePairRepository segmentValuePairRepository;
    private final SegmentTypeRepository segmentTypeRepository;

    public SegmentValuePairServiceJPAImpl(SegmentValuePairRepository segmentValuePairRepository, SegmentTypeRepository segmentTypeRepository, ModelMapper modelMapper) {
        super(segmentValuePairRepository, modelMapper, SegmentValuePair.class, SegmentValuePairDto.class);
        this.segmentValuePairRepository = segmentValuePairRepository;
        this.segmentTypeRepository = segmentTypeRepository;
    }

    @Override
    public SegmentValuePairDto save(SegmentValuePairDto segmentValuePairDto) {
        String segmentTypeCode = segmentValuePairDto.getSegmentType().getCode();
        segmentTypeRepository.findByCode(segmentTypeCode).ifPresentOrElse(segmentType -> {
            SegmentTypeDto segmentTypeDto = modelMapper.map(segmentType, SegmentTypeDto.class);
            segmentValuePairDto.setSegmentType(segmentTypeDto);
        }, () -> {
            throw new DataNotFoundException(SegmentType.class, Fields.SEGMENT_TYPE_CODE, segmentTypeCode);
        });

        SegmentValuePair segmentValuePair = modelMapper.map(segmentValuePairDto, SegmentValuePair.class);
        SegmentValuePair savedSegmentValuePair = segmentValuePairRepository.save(segmentValuePair);

        return modelMapper.map(findById(savedSegmentValuePair.getId()), SegmentValuePairDto.class);
    }

    @Override
    public SegmentValuePairDto findByValueAndSegmentTypeCode(String segmentValue, String segmentTypeCode) {
        SegmentValuePair segmentValuePair = segmentValuePairRepository.findByValueAndSegmentTypeCode(segmentValue, segmentTypeCode)
                .orElseThrow(() -> {
                    ExceptionVariable segmentValueEV = ExceptionVariable.builder().field(Fields.SEGMENT_VALUE).value(segmentValue).build();
                    ExceptionVariable segmentTypeCodeEV = ExceptionVariable.builder().field(Fields.SEGMENT_TYPE_CODE).value(segmentTypeCode).build();

                    throw new DataNotFoundException(SegmentValuePair.class, Arrays.asList(segmentValueEV, segmentTypeCodeEV));
                });

        return modelMapper.map(segmentValuePair, SegmentValuePairDto.class);
    }
}
