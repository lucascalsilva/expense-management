package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.exception.ExceptionVariable;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.exceptions.InvalidDataException;
import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.model.SegmentValuePair;
import com.mobnova.expense_mgt.repositories.SegmentTypeRepository;
import com.mobnova.expense_mgt.repositories.SegmentValuePairRepository;
import com.mobnova.expense_mgt.services.SegmentValuePairService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class SegmentValuePairServiceJPAImpl implements SegmentValuePairService {

    private final SegmentValuePairRepository segmentValuePairRepository;
    private final SegmentTypeRepository segmentTypeRepository;

    @Override
    public SegmentValuePair save(SegmentValuePair segmentValuePair) {
        String segmentTypeCode = segmentValuePair.getSegmentType().getCode();
        segmentTypeRepository.findByCode(segmentTypeCode).ifPresentOrElse(segmentType -> {
            segmentValuePair.setSegmentType(segmentType);
        }, () -> {
            throw new InvalidDataException(SegmentType.class, "segmentTypeCode", segmentTypeCode);
        });

        return segmentValuePairRepository.save(segmentValuePair);
    }

    @Override
    public Set<SegmentValuePair> saveBulk(Set<SegmentValuePair> segmentValuePairs) {
        return segmentValuePairs.stream().map(this::save).collect(Collectors.toSet());
    }

    @Override
    public SegmentValuePair findById(Long id) {
        return segmentValuePairRepository.findById(id).orElseThrow(() -> new DataNotFoundException(SegmentValuePair.class, Fields.ID, id));
    }

    @Override
    public void deleteById(Long id) {
        segmentValuePairRepository.deleteById(id);
    }

    @Override
    public SegmentValuePair findByValueAndSegmentTypeCode(String segmentValue, String segmentTypeCode) {
        return segmentValuePairRepository.findByValueAndSegmentTypeCode(segmentValue, segmentTypeCode)
                .orElseThrow(() -> {
                    ExceptionVariable segmentValueEV = ExceptionVariable.builder().field(Fields.SEGMENT_VALUE).value(segmentValue).build();
                    ExceptionVariable segmentTypeCodeEV = ExceptionVariable.builder().field(Fields.SEGMENT_TYPE_CODE).value(segmentTypeCode).build();

                    throw new DataNotFoundException(SegmentValuePair.class, Arrays.asList(segmentValueEV, segmentTypeCodeEV));
                });
    }
}
