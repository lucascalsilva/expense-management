package com.mobnova.expense_mgt.repositories;

import com.mobnova.expense_mgt.model.County;
import com.mobnova.expense_mgt.model.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseCategoryRepository extends NameCodeBaseRepository<ExpenseCategory> {
}
