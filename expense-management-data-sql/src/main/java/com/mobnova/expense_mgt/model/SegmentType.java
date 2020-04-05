package com.mobnova.expense_mgt.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;

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

}
