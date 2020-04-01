package com.mobnova.expense_mgt.model;

import lombok.*;

import javax.persistence.*;


@ToString(callSuper = true)
@Entity
@Table(name = "STATES_OR_PROVINCES")
@Data
public class StateOrProvince extends NameCodeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="COUNTRY_ID")
    private Country country;
}
