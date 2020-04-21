package com.mobnova.expense_mgt.jpa;

import com.mobnova.expense_mgt.config.CriteriaConfigBean;
import com.mobnova.expense_mgt.criteria.GeneralSpecification;
import com.mobnova.expense_mgt.criteria.SearchCriteria;
import com.mobnova.expense_mgt.model.ExpenseReport;
import com.mobnova.expense_mgt.repositories.*;
import com.mobnova.expense_mgt.util.DemoDataGenerator;
import com.mobnova.expense_mgt.util.DemoDataPersistence;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

//TODO write more tests to handle criteria with nested entities

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Slf4j
public class ExpenseReportSearchCriteriaIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ExpenseReportRepository expenseReportRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private StateOrProvinceRepository stateOrProvinceRepository;

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DemoDataPersistence demoDataPersistence;

    private DemoDataGenerator demoDataGenerator;

    private CriteriaConfigBean criteriaConfigBean;
    private List<ExpenseReport> expenseReports;
    private Integer expenseReportQuantity = 5;
    private Integer expenseItemQuantity = 5;

    @BeforeEach
    public void init(){
        demoDataGenerator = new DemoDataGenerator();

        demoDataPersistence.persistAllBaseData(demoDataGenerator);

        expenseReports = demoDataGenerator.createRandomExpenseReports(expenseReportQuantity, expenseItemQuantity);

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

