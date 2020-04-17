package com.mobnova.expense_mgt.repositories;

import com.mobnova.expense_mgt.model.Country;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends NameCodeBaseRepository<Country> {

}
