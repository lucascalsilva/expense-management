package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.config.CriteriaConfigBean;
import com.mobnova.expense_mgt.criteria.CriteriaUtil;
import com.mobnova.expense_mgt.exceptions.InvalidDataException;
import com.mobnova.expense_mgt.model.*;
import com.mobnova.expense_mgt.repositories.*;
import com.mobnova.expense_mgt.services.ExpenseReportService;
import com.mobnova.expense_mgt.validation.BeanValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class ExpenseReportServiceJPAImpl implements ExpenseReportService {

    private final ExpenseReportRepository expenseReportRepository;
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;
    private final CityRepository cityRepository;
    private final ExpenseCategoryRepository categoryRepository;
    private final BeanValidator beanValidator;
    private final CriteriaConfigBean criteriaConfigBean;

    @Override
    public ExpenseReport save(ExpenseReport expenseReport) {
        beanValidator.validateObject(expenseReport);
        beanValidator.validateObjects(expenseReport.getExpenses());

        //TODO should I allow save to update as well?
        /*if (expenseReport.getId() != null) {
            expenseReportRepository.findById(expenseReport.getId())
                    .ifPresent(currentObject -> {
                        expenseReport.setId(currentObject.getId());
                        expenseReport.setVersion(currentObject.getVersion());
                    });
        }*/

        String username = expenseReport.getUser().getUsername();
        userRepository.findByUsername(username)
                .ifPresentOrElse(expenseReport::setUser,
                        () -> {
                            throw new InvalidDataException(User.class, "username", username);
                        });


        String countryCode = expenseReport.getCountry().getCode();
        countryRepository.findByCode(countryCode)
                .ifPresentOrElse(expenseReport::setCountry,
                        () -> {
                            throw new InvalidDataException(Country.class, "countryCode", countryCode);
                        });


        for (ExpenseItem expenseItem : expenseReport.getExpenses()) {
            String currencyCode = expenseItem.getCurrency().getCode();
            currencyRepository.findByCode(currencyCode)
                    .ifPresentOrElse(expenseItem::setCurrency,
                            () -> {
                                throw new InvalidDataException(Currency.class, "currencyCode", currencyCode);
                            });

            if(expenseItem.getExpenseCity() != null && expenseItem.getExpenseCity().getCode() != null) {
                String expenseCityCode = expenseItem.getExpenseCity().getCode();
                cityRepository.findByCode(expenseCityCode)
                        .ifPresentOrElse(expenseItem::setExpenseCity,
                                () -> {
                                    throw new InvalidDataException(City.class, "expenseCityCode", expenseCityCode);
                                });

            }

            String categoryCode = expenseItem.getCategory().getCode();
            categoryRepository.findByCode(categoryCode)
                    .ifPresentOrElse(expenseItem::setCategory,
                            () -> {
                                throw new InvalidDataException(ExpenseCategory.class, "expenseCategoryCode", categoryCode);
                            });
        }

        return expenseReportRepository.save(expenseReport);
    }

    @Override
    @Transactional
    public Set<ExpenseReport> saveBulk(Set<ExpenseReport> expenseReports) {
        return expenseReports.stream().map(this::save).collect(Collectors.toSet());
    }

    @Override
    public Set<ExpenseReport> search(String search) {
        Specification specification = CriteriaUtil.extractSpecification(search, criteriaConfigBean);
        return new HashSet(expenseReportRepository.findAll(specification));
    }

    @Override
    public Optional<ExpenseReport> findById(Long id) {
        return expenseReportRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        expenseReportRepository.deleteById(id);
    }

    @Override
    public Optional<ExpenseReport> findByReferenceID(String referenceID) {
        return expenseReportRepository.findByReferenceID(referenceID);
    }
}
