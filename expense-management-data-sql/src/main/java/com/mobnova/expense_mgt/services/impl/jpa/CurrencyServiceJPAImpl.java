package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.dto.v1.CurrencyDto;
import com.mobnova.expense_mgt.model.Currency;
import com.mobnova.expense_mgt.repositories.CurrencyRepository;
import com.mobnova.expense_mgt.services.CurrencyService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("jpa")
public class CurrencyServiceJPAImpl extends AbstractNameCodeEntityBaseServiceJPA<Currency, CurrencyDto, Long> implements CurrencyService {

    public CurrencyServiceJPAImpl(CurrencyRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, Currency.class, CurrencyDto.class);
    }
}
