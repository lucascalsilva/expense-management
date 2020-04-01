package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.model.ExpenseCategory;
import com.mobnova.expense_mgt.repositories.ExpenseCategoryRepository;
import com.mobnova.expense_mgt.services.ExpenseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class ExpenseCategoryServiceJPAImpl implements ExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;

    @Override
    public ExpenseCategory save(ExpenseCategory category) {
        return expenseCategoryRepository.save(category);
    }

    @Override
    public Set<ExpenseCategory> saveBulk(Set<ExpenseCategory> categories) {
        return categories.stream().map(this::save).collect(Collectors.toSet());
    }

    @Override
    public Optional<ExpenseCategory> findById(Long id) {
        return expenseCategoryRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        expenseCategoryRepository.deleteById(id);
    }

    @Override
    public Optional<ExpenseCategory> findByCode(String code) {
        return expenseCategoryRepository.findByCode(code);
    }
}
