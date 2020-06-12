package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.model.ExpenseReport;
import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.repositories.SegmentTypeRepository;
import com.mobnova.expense_mgt.services.SegmentTypeService;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.validation.BeanValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class SegmentTypeServiceJPAImpl implements SegmentTypeService {

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
    public SegmentType findById(Long id) {
        return segmentTypeRepository.findById(id).orElseThrow(() -> new DataNotFoundException(SegmentType.class, Fields.ID, id));
    }

    @Override
    public void deleteById(Long id) {
        segmentTypeRepository.deleteById(id);
    }

    @Override
    public SegmentType findByCode(String code) {
        return segmentTypeRepository.findByCode(code).orElseThrow(() -> new DataNotFoundException(SegmentType.class, Fields.CODE, code));
    }
}
