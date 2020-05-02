package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.exceptions.InvalidDataException;
import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.model.SegmentValuePair;
import com.mobnova.expense_mgt.repositories.SegmentTypeRepository;
import com.mobnova.expense_mgt.repositories.SegmentValuePairRepository;
import com.mobnova.expense_mgt.services.SegmentValuePairService;
import com.mobnova.expense_mgt.validation.BeanValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class SegmentValuePairJPAImpl implements SegmentValuePairService {

    private final SegmentValuePairRepository segmentValuePairRepository;
    private final SegmentTypeRepository segmentTypeRepository;
    private final BeanValidator beanValidator;

    @Override
    public SegmentValuePair save(SegmentValuePair segmentValuePair) {
        beanValidator.validateObject(segmentValuePair);

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
    public Optional<SegmentValuePair> findById(Long id) {
        return segmentValuePairRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        segmentValuePairRepository.deleteById(id);
    }

    @Override
    public Optional<SegmentValuePair> findByValueAndSegmentTypeCode(String segmentValue, String segmentTypeCode) {
        return segmentValuePairRepository.findByValueAndSegmentTypeCode(segmentValue, segmentTypeCode);
    }
}
