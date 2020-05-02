package com.mobnova.expense_mgt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@ToString(callSuper = true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "EXPENSE_ITEMS")
public class ExpenseItem extends BaseEntity {

    @Column(name = "EXPENSE_ITEM_NUM", nullable = false)
    @NotNull
    private Long expenseItemNumber;

    @Column(name = "AMOUNT", nullable = false)
    @NotNull
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="CURRENCY_ID")
    @NotNull
    private Currency currency;

    @Column(name = "EXPENSE_DATE", nullable = false)
    @NotNull
    private LocalDate expenseDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="CATEGORY_ID")
    @NotNull
    private ExpenseCategory category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="EXPENSE_CITY_ID")
    private City expenseCity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "CODE_COMBINATION_ID")
    @NotEmpty
    private List<SegmentValuePair> segmentValuePairs = new ArrayList<>();

    @Column(name = "PICTURE")
    @Lob
    private String picture;

    /*@ManyToOne
    @JoinColumn(name="EXPENSE_ITEM_ID")
    @NotNull
    @ToString.Exclude
    private ExpenseReport expenseReport;*/
}
