package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.dto.v1.ExpenseCategoryDto;
import com.mobnova.expense_mgt.model.ExpenseCategory;
import com.mobnova.expense_mgt.repositories.ExpenseCategoryRepository;
import com.mobnova.expense_mgt.services.ExpenseCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("jpa")
public class ExpenseCategoryServiceJPAImpl extends AbstractNameCodeEntityBaseServiceJPA<ExpenseCategory, ExpenseCategoryDto, Long> implements ExpenseCategoryService {

    @Autowired
    public ExpenseCategoryServiceJPAImpl(ExpenseCategoryRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, ExpenseCategory.class, ExpenseCategoryDto.class);
    }
}
