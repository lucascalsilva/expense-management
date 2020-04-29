package com.mobnova.expense_mgt.repositories;

import com.mobnova.expense_mgt.model.SegmentValuePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SegmentValuePairRepository extends JpaRepository<SegmentValuePair, Long> {

    @Query("SELECT SV SEGMENT_VALUES SV, SEGMENT_TYPES ST " +
            "WHERE SV.SEGMENT_TYPE_ID = ST.ID" +
            "AND UPPER(SV.SEGMENT_VALUE) = :segmentValue" +
            "AND UPPER(ST.CODE) = UPPER(:segmentTypeCode)")
    Optional<SegmentValuePair> findByValueAndSegmentTypeCode(@Param("segmentValue") String segmentValue,
                                                             @Param("segmentTypeCode") String segmentTypeCode);
}
