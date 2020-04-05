package com.mobnova.expense_mgt.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;


@ToString(callSuper = true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "EXPENSE_CATEGORIES")
public class ExpenseCategory extends NameCodeEntity {


}
