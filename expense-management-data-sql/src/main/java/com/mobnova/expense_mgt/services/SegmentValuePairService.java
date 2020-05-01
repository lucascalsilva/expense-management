package com.mobnova.expense_mgt.services;

import com.mobnova.expense_mgt.model.SegmentValuePair;

import java.util.Optional;
import java.util.Set;

public interface SegmentValuePairService extends BaseService<SegmentValuePair, Long> {

    Set<SegmentValuePair> findByValueAndSegmentTypeCode(String segmentValue, String segmentTypeCode);
}
