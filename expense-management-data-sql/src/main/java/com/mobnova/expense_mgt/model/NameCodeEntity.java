package com.mobnova.expense_mgt.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@ToString(callSuper = true)
@MappedSuperclass
@Data
public class NameCodeEntity extends BaseEntity {

    @Column(name = "CODE")
    @NaturalId
    private String code;

    @Column(name = "NAME")
    private String name;
}
