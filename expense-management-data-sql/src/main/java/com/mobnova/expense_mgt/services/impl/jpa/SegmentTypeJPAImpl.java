package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.repositories.SegmentTypeRepository;
import com.mobnova.expense_mgt.services.SegmentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class SegmentTypeJPAImpl implements SegmentTypeService {

    private final SegmentTypeRepository segmentTypeRepository;

    @Override
    public SegmentType save(SegmentType segmentType) {
        return segmentTypeRepository.save(segmentType);
    }

    @Override
    public Set<SegmentType> saveBulk(Set<SegmentType> segmentTypes) {
        return segmentTypes.stream().map(this::save).collect(Collectors.toSet());
    }

    @Override
    public Optional<SegmentType> findById(Long id) {
        return segmentTypeRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        segmentTypeRepository.deleteById(id);
    }
}
