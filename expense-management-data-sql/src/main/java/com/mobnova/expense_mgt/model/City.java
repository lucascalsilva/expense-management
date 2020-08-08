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
@Table(name = "CITIES")
public class City extends NameCodeEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="STATE_ID")
    @NaturalId
    private StateOrProvince stateOrProvince;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="COUNTY_ID")
    @NaturalId
    private County county;
}
