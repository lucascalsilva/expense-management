package com.mobnova.expense_mgt.services.dto;

import com.mobnova.expense_mgt.dto.SegmentValuePairDto;

public interface SegmentValuePairService extends BaseService<SegmentValuePairDto, Long> {

    SegmentValuePairDto findByValueAndSegmentTypeCode(String segmentValue, String segmentTypeCode);
}
