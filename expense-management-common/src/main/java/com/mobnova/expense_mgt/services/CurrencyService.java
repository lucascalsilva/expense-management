package com.mobnova.expense_mgt.services;

import com.mobnova.expense_mgt.dto.v1.CurrencyDto;

public interface CurrencyService extends BaseService<CurrencyDto, Long>, NameCodeService<CurrencyDto, String> {
}
