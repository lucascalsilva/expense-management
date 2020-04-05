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
@Table(name = "STATES_OR_PROVINCES")
public class StateOrProvince extends NameCodeEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="COUNTRY_ID")
    private Country country;
}
