package com.mobnova.expense_mgt.model;

import lombok.*;

import javax.persistence.*;

@ToString(callSuper = true)
@Entity
@Table(name = "SEGMENT_VALUE_PAIRS")
@Data
public class SegmentValuePair extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SEGMENT_TYPE_ID")
    private SegmentType segmentType;

    private String segmentValue;
}
