package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.exceptions.InvalidDataException;
import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.model.SegmentValuePair;
import com.mobnova.expense_mgt.model.StateOrProvince;
import com.mobnova.expense_mgt.repositories.SegmentTypeRepository;
import com.mobnova.expense_mgt.repositories.SegmentValuePairRepository;
import com.mobnova.expense_mgt.services.SegmentValuePairService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class SegmentValuePairJPAImpl implements SegmentValuePairService {

    private final SegmentValuePairRepository segmentValuePairRepository;
    private final SegmentTypeRepository segmentTypeRepository;

    @Override
    public SegmentValuePair save(SegmentValuePair segmentValuePair) {
        if (segmentValuePair.getSegmentType() != null && segmentValuePair.getSegmentType().getCode() != null) {

            String segmentTypeCode = segmentValuePair.getSegmentType().getCode();
            segmentTypeRepository.findByCode(segmentTypeCode).ifPresentOrElse(segmentType -> {
                segmentValuePair.setSegmentType(segmentType);
            }, () -> {
                throw new InvalidDataException(SegmentType.class, "segmentTypeCode", segmentTypeCode);
            });
        }
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
}
