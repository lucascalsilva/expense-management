package com.mobnova.expense_mgt.services;

import com.mobnova.expense_mgt.dto.v1.SegmentValuePairDto;

public interface SegmentValuePairService extends BaseService<SegmentValuePairDto, Long> {

    SegmentValuePairDto findByValueAndSegmentTypeCode(String segmentValue, String segmentTypeCode);
}
