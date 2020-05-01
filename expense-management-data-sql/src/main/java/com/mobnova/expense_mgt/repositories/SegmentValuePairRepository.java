package com.mobnova.expense_mgt.repositories;

import com.mobnova.expense_mgt.model.SegmentValuePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SegmentValuePairRepository extends JpaRepository<SegmentValuePair, Long> {

    @Query("SELECT SV FROM SegmentValuePair SV, SegmentType ST " +
            "WHERE SV.SEGMENT_TYPE_ID = ST.ID " +
            "AND SV.SEGMENT_VALUE = ?#{:segmentValue.toUpperCase()} " +
            "AND ST.CODE = ?#{:segmentTypeCode.toUpperCase()}")
    List<SegmentValuePair> findByValueAndSegmentTypeCode(@Param("segmentValue") String segmentValue,
                                                         @Param("segmentTypeCode") String segmentTypeCode);
}
