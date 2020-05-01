package com.mobnova.expense_mgt.jpa.it;

import com.mobnova.expense_mgt.config.CriteriaConfigBean;
import com.mobnova.expense_mgt.criteria.GeneralSpecification;
import com.mobnova.expense_mgt.criteria.SearchCriteria;
import com.mobnova.expense_mgt.model.ExpenseReport;
import com.mobnova.expense_mgt.repositories.*;
import com.mobnova.expense_mgt.util.ExpenseReportTestHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

//TODO write more tests to handle criteria with nested entities

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Slf4j
public class ExpenseReportSearchCriteriaIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private static ExpenseReportRepository expenseReportRepository;

    @Autowired
    private static CountryRepository countryRepository;

    @Autowired
    private static UserRepository userRepository;

    @Autowired
    private static CurrencyRepository currencyRepository;

    @Autowired
    private static StateOrProvinceRepository stateOrProvinceRepository;

    @Autowired
    private static ExpenseCategoryRepository expenseCategoryRepository;

    @Autowired
    private static CityRepository cityRepository;

    private ExpenseReportTestHelper expenseReportTestHelper;

    private CriteriaConfigBean criteriaConfigBean;
    private List<ExpenseReport> expenseReports;
    private Integer expenseReportQuantity = 5;
    private Integer expenseItemQuantity = 5;

    @BeforeEach
    public void init(){
        expenseReportTestHelper = new ExpenseReportTestHelper();

        countryRepository.save(expenseReportTestHelper.getCountry());
        stateOrProvinceRepository.save(expenseReportTestHelper.getStateOrProvince());
        cityRepository.save(expenseReportTestHelper.getCity());
        userRepository.save(expenseReportTestHelper.getUser());
        currencyRepository.save(expenseReportTestHelper.getCurrency());
        expenseCategoryRepository.save(expenseReportTestHelper.getExpenseCategory());

        expenseReports = new ArrayList<>();
        expenseReports.addAll(expenseReportTestHelper
                .createDummyExpenseReports(expenseReportQuantity, expenseItemQuantity));

        expenseReportRepository.saveAll(expenseReports);

        criteriaConfigBean = new CriteriaConfigBean();
    }

    @Test
    public void findExpenseReportByJustification(){
        GeneralSpecification generalSpecification = new GeneralSpecification(
                new SearchCriteria("justification", ":", "Justification"), criteriaConfigBean);

        List<ExpenseReport> expenseReportsByCriteria = expenseReportRepository.findAll(generalSpecification);

        assertThat(expenseReports).contains(expenseReportsByCriteria.toArray(new ExpenseReport[0]));
    }

    @Test
    public void findExpenseReportByReferenceID(){
        String referenceId = expenseReports.get(0).getReferenceID();
        GeneralSpecification generalSpecification = new GeneralSpecification(
                new SearchCriteria("referenceID", ":", referenceId), criteriaConfigBean);

        List<ExpenseReport> expenseReportsByCriteria = expenseReportRepository.findAll(generalSpecification);

        assertThat(expenseReports).contains(expenseReportsByCriteria.toArray(new ExpenseReport[0]));
    }

    @Test
    public void findNoExpenseReportWhenUsingNonExistingData(){
        GeneralSpecification generalSpecification = new GeneralSpecification(
                new SearchCriteria("tripDescription", ":", "XYZ"), criteriaConfigBean);

        List<ExpenseReport> expenseReportsByCriteria = expenseReportRepository.findAll(generalSpecification);

        assertThat(expenseReportsByCriteria).isEmpty();
    }
}

