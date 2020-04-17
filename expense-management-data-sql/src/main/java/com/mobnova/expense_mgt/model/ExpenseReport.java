package com.mobnova.expense_mgt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@ToString(callSuper = true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "EXPENSE_REPORTS")
public class ExpenseReport extends BaseEntity {

    @Column(name = "REFERENCE_ID")
    @NaturalId
    @NotNull
    private String referenceID;

    @Column(name = "TRIP_DESCRIPTION", nullable = false)
    @NotNull
    private String tripDescription;

    @Column(name = "JUSTIFICATION")
    @NotNull
    private String justification;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COUNTRY_ID")
    @NotNull
    private Country country;

    @Column(name = "TRIP_START_DATE")
    @NotNull
    private LocalDate tripStartDate;

    @Column(name = "TRIP_END_DATE")
    @NotNull
    private LocalDate tripEndDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="USER_ID")
    @NotNull
    private User user;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="EXPENSE_ITEM_ID")
    @NotEmpty
    private Set<ExpenseItem> expenses = new HashSet<ExpenseItem>();

    /*public void addExpense(ExpenseItem expenseItem){
        this.expenses.add(expenseItem);
        expenseItem.setExpenseReport(this);
    }*/

}
