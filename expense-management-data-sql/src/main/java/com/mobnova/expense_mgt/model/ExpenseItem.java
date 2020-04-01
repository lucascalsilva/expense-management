package com.mobnova.expense_mgt.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@ToString(callSuper = true)
@Entity
@Table(name = "EXPENSE_ITEMS")
@Data
public class ExpenseItem extends BaseEntity {

    @Column(name = "EXPENSE_ITEM_NUM", nullable = false)
    private Long expenseItemNumber;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="CURRENCY_ID")
    private Currency currency;

    @Column(name = "EXPENSE_DATE", nullable = false)
    private LocalDate expenseDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="CATEGORY_ID")
    private ExpenseCategory category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="EXPENSE_CITY_ID")
    private City expenseCity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "CODE_COMBINATION_ID")
    private Set<SegmentValuePair> segmentValuePair;

    @Column(name = "PICTURE")
    @Lob
    private String picture;
}
