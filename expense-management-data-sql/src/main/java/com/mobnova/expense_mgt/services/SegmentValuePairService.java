package com.mobnova.expense_mgt.services;

import com.mobnova.expense_mgt.model.SegmentValuePair;

import java.util.Optional;

public interface SegmentValuePairService extends BaseService<SegmentValuePair, Long> {

    Optional<SegmentValuePair> findByValueAndSegmentTypeCode(String segmentValue, String segmentTypeCode);
}
