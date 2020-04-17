package com.mobnova.expense_mgt.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public class NameCodeEntity extends BaseEntity {

    @Column(name = "CODE", length = 100, nullable = false)
    @NaturalId
    @NotNull
    private String code;

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;
}
