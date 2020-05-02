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

    @Query("SELECT sv FROM SegmentValuePair sv " +
            "INNER JOIN SegmentType st ON(sv.segmentType = st.id) " +
            "WHERE 1=1 " +
            "AND UPPER(sv.segmentValue) = UPPER(:segmentValue) " +
            "AND UPPER(st.code) = UPPER(:segmentTypeCode)")
    Optional<SegmentValuePair> findByValueAndSegmentTypeCode(@Param("segmentValue") String segmentValue,
                                                         @Param("segmentTypeCode") String segmentTypeCode);
}
