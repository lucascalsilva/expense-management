package com.mobnova.expense_mgt.model;

import lombok.*;

import javax.persistence.*;

@ToString(callSuper = true)
@Entity
@Table(name = "CITIES")
@Data
public class City extends NameCodeEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="STATE_ID")
    private StateOrProvince stateOrProvince;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="COUNTY_ID")
    private County county;
}
