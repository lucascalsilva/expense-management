package com.mobnova.expense_mgt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NaturalId;

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
    @NaturalId
    private Country country;
}
