package com.mobnova.expense_mgt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@ToString(callSuper = true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "SEGMENT_TYPES")
public class SegmentType extends NameCodeEntity {

    @Column(name = "SEGMENT_TYPE_ORDER", length = 20, nullable = false)
    @Range(min = 1, max = 20)
    private Long order;

}
