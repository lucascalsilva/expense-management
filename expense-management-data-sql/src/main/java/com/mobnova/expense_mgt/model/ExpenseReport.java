package com.mobnova.expense_mgt.model;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@ToString(callSuper = true)
@Entity
@Table(name = "EXPENSE_REPORTS")
@Data
public class ExpenseReport extends BaseEntity {

    @Column(name = "REFERENCE_ID")
    @NaturalId
    private String referenceID;

    @Column(name = "TRIP_DESCRIPTION", nullable = false)
    private String tripDescription;

    @Column(name = "JUSTIFICATION")
    private String justification;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COUNTRY_ID")
    private Country country;

    @Column(name = "TRIP_START_DATE")
    private LocalDate tripStartDate;

    @Column(name = "TRIP_END_DATE")
    private LocalDate tripEndDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="USER_ID")
    private User user;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="EXPENSE_ITEM_ID")
    private Set<ExpenseItem> expenses;

}
