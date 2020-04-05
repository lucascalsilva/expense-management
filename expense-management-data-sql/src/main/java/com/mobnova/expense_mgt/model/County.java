package com.mobnova.expense_mgt.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@ToString(callSuper = true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "COUNTIES")
public class County extends NameCodeEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="STATE_ID")
    private StateOrProvince stateOrProvince;
}
