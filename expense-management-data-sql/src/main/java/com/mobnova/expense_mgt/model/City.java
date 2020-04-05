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
@Table(name = "CITIES")
public class City extends NameCodeEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="STATE_ID")
    private StateOrProvince stateOrProvince;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="COUNTY_ID")
    private County county;
}
