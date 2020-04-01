package com.mobnova.expense_mgt.model;

import lombok.*;

import javax.persistence.*;

@ToString(callSuper = true)
@Entity
@Table(name = "COUNTIES")
@Data
public class County extends NameCodeEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="STATE_ID")
    private StateOrProvince stateOrProvince;
}
