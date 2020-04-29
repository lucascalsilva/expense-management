package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.model.City;
import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.repositories.SegmentTypeRepository;
import com.mobnova.expense_mgt.services.SegmentTypeService;
import com.mobnova.expense_mgt.validation.BeanValidator;
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
    private final BeanValidator beanValidator;

    @Override
    public SegmentType save(SegmentType segmentType) {
        beanValidator.validateObject(segmentType);
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

    @Override
    public Optional<SegmentType> findByCode(String code) {
        return segmentTypeRepository.findByCode(code);
    }
}
