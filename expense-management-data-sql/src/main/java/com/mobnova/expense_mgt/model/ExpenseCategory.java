package com.mobnova.expense_mgt.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;


@ToString(callSuper = true)
@Entity
@Table(name = "EXPENSE_CATEGORIES")
@Data
public class ExpenseCategory extends NameCodeEntity {

}
