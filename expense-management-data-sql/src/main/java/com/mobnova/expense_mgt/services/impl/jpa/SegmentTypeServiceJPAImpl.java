package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.dto.v1.SegmentTypeDto;
import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.repositories.SegmentTypeRepository;
import com.mobnova.expense_mgt.services.SegmentTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("jpa")
public class SegmentTypeServiceJPAImpl extends AbstractNameCodeEntityBaseServiceJPA<SegmentType, SegmentTypeDto, Long> implements SegmentTypeService {

    public SegmentTypeServiceJPAImpl(SegmentTypeRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, SegmentType.class, SegmentTypeDto.class);
    }
}
