package com.mobnova.expense_mgt.repositories;

import com.mobnova.expense_mgt.model.County;
import com.mobnova.expense_mgt.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends NameCodeBaseRepository<Currency> {
}
