package com.mobnova.expense_mgt.repositories;

import com.mobnova.expense_mgt.model.Currency;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends NameCodeBaseRepository<Currency> {
}
