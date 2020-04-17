package com.mobnova.expense_mgt.repositories;

import com.mobnova.expense_mgt.model.ExpenseCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseCategoryRepository extends NameCodeBaseRepository<ExpenseCategory> {
}
