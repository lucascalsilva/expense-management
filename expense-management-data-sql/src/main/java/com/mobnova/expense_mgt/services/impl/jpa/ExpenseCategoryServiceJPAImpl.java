package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.model.ExpenseCategory;
import com.mobnova.expense_mgt.repositories.ExpenseCategoryRepository;
import com.mobnova.expense_mgt.services.ExpenseCategoryService;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.validation.BeanValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class ExpenseCategoryServiceJPAImpl implements ExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final BeanValidator beanValidator;

    @Override
    public ExpenseCategory save(ExpenseCategory category) {
        beanValidator.validateObject(category);
        return expenseCategoryRepository.save(category);
    }

    @Override
    public Set<ExpenseCategory> saveBulk(Set<ExpenseCategory> categories) {
        return categories.stream().map(this::save).collect(Collectors.toSet());
    }

    @Override
    public ExpenseCategory findById(Long id) {
        return expenseCategoryRepository.findById(id).orElseThrow(() -> new DataNotFoundException(ExpenseCategory.class, Fields.ID, id));
    }

    @Override
    public void deleteById(Long id) {
        expenseCategoryRepository.deleteById(id);
    }

    @Override
    public ExpenseCategory findByCode(String code) {
        return expenseCategoryRepository.findByCode(code).orElseThrow(() -> new DataNotFoundException(ExpenseCategory.class, Fields.CODE, code));
    }
}
