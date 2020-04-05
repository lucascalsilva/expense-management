package com.mobnova.expense_mgt.jpa;

import com.mobnova.expense_mgt.config.CriteriaConfigBean;
import com.mobnova.expense_mgt.criteria.GeneralSpecification;
import com.mobnova.expense_mgt.criteria.SearchCriteria;
import com.mobnova.expense_mgt.model.ExpenseCategory;
import com.mobnova.expense_mgt.repositories.CountryRepository;
import com.mobnova.expense_mgt.repositories.ExpenseReportRepository;
import com.mobnova.expense_mgt.model.ExpenseReport;
import com.mobnova.expense_mgt.model.User;
import com.mobnova.expense_mgt.model.Country;
import com.mobnova.expense_mgt.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class JPASpecificationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ExpenseReportRepository expenseReportRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private UserRepository userRepository;

    private CriteriaConfigBean criteriaConfigBean;
    private ExpenseReport expenseReport1;
    private ExpenseReport expenseReport2;

    @Before
    public void init(){
        ExpenseCategory expenseCategory = ExpenseCategory.builder().code("MEAL").build();
        Country country = Country.builder().code("BR").build();

        countryRepository.save(country);

        User user = User.builder().username("Lucas").build();

        userRepository.save(user);

        expenseReport1 = new ExpenseReport();
        expenseReport1.setJustification("Justification 1234...");
        expenseReport1.setCountry(country);
        expenseReport1.setTripDescription("Some description 1234...");
        expenseReport1.setUser(user);

        expenseReport2 = new ExpenseReport();
        expenseReport2.setJustification("Justification 5678...");
        expenseReport2.setCountry(country);
        expenseReport2.setTripDescription("Some description 5678...");
        expenseReport2.setUser(user);

        expenseReportRepository.save(expenseReport1);
        expenseReportRepository.save(expenseReport2);

        criteriaConfigBean = new CriteriaConfigBean();
    }

    @Test
    public void givenExistingJustification_whenGettingExpenseReports_thenGetCorrectExpenseReport(){
        GeneralSpecification generalSpecification = new GeneralSpecification(
                new SearchCriteria("justification", ":", "Justification 5678..."), criteriaConfigBean);

        List<ExpenseReport> expenseReports = expenseReportRepository.findAll(generalSpecification);

        assertThat(expenseReport2).isIn(expenseReports);
        assertThat(expenseReport1).isNotIn(expenseReports);
    }

    @Test
    public void givenExistingDescription_whenGettingExpenseReports_thenGetCorrectExpenseReport(){
        GeneralSpecification generalSpecification = new GeneralSpecification(
                new SearchCriteria("tripDescription", ":", "Some description 1234..."), criteriaConfigBean);

        List<ExpenseReport> expenseReports = expenseReportRepository.findAll(generalSpecification);

        assertThat(expenseReport1).isIn(expenseReports);
        assertThat(expenseReport2).isNotIn(expenseReports);
    }

    @Test
    public void givenNotDescription_whenGettingExpenseReports_thenGetNoExpenseReport(){
        GeneralSpecification generalSpecification = new GeneralSpecification(
                new SearchCriteria("tripDescription", ":", "XYZ"), criteriaConfigBean);

        List<ExpenseReport> expenseReports = expenseReportRepository.findAll(generalSpecification);

        assertThat(expenseReports.size()).isEqualTo(0);
    }
}

