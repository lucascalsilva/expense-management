package com.mobnova.expense_mgt.services;

import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.model.County;

public interface CountryService extends BaseService<Country, Long>, NameCodeService<Country, String> {
}
