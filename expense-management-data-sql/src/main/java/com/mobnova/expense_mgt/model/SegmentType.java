package com.mobnova.expense_mgt.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;


@ToString(callSuper = true)
@Entity
@Table(name = "SEGMENT_TYPES")
@Data
public class SegmentType extends NameCodeEntity {

}
