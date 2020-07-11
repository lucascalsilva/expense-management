package com.mobnova.expense_mgt.services;

import com.mobnova.expense_mgt.model.SegmentValuePair;

public interface SegmentValuePairService extends BaseService<SegmentValuePair, Long> {

    SegmentValuePair findByValueAndSegmentTypeCode(String segmentValue, String segmentTypeCode);
}
