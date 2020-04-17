package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.model.*;
import com.mobnova.expense_mgt.repositories.*;
import com.mobnova.expense_mgt.util.DemoDataGenerator;
import com.mobnova.expense_mgt.validation.BeanValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class ExpenseReportServiceJPAImplTest {

    @InjectMocks
    private ExpenseReportServiceJPAImpl expenseReportServiceJPA;

    @Mock
    private ExpenseReportRepository expenseReportRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private StateOrProvinceRepository stateOrProvinceRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private ExpenseCategoryRepository expenseCategoryRepository;

    @Mock
    private BeanValidator beanValidator;

    private DemoDataGenerator demoDataGenerator;

    private Integer expenseReportQuantity = 3;
    private Integer expenseItemQuantity = 3;

    @BeforeEach
    public void init(){
        demoDataGenerator = new DemoDataGenerator();
    }

    @Test
    void save() {
        ExpenseReport expenseReport = getDummyExpenseReportAndSetupSaveMocks();

        expenseReportServiceJPA.save(expenseReport);

        verify(countryRepository, times(1)).findByCode(expenseReport.getCountry().getCode());
        verify(expenseReportRepository, times(1)).save(expenseReport);
        verify(userRepository, times(1)).findByUsername(expenseReport.getUser().getUsername());

        for(ExpenseItem expenseItem : expenseReport.getExpenses()){
            verify(expenseCategoryRepository, atLeastOnce()).findByCode(expenseItem.getCategory().getCode());
            verify(cityRepository, atLeastOnce()).findByCode(expenseItem.getExpenseCity().getCode());
        }
    }

    @Test
    void saveBulk() {
        getDummyExpenseReportAndSetupSaveMocks();

        List<ExpenseReport> randomExpenseReports = demoDataGenerator
                .createRandomExpenseReports(expenseReportQuantity, expenseItemQuantity);

        for(ExpenseReport expenseReport : randomExpenseReports) {
            when(expenseReportRepository.save(expenseReport)).thenReturn(expenseReport);
        }
        Set<ExpenseReport> expenseReportsFromService = expenseReportServiceJPA.saveBulk(new HashSet<>(randomExpenseReports));
        verify(expenseReportRepository, times(expenseItemQuantity)).save(any());
        assertThat(expenseReportsFromService).contains(randomExpenseReports.toArray(new ExpenseReport[0]));
    }

    @Test
    void search() {
        when(expenseReportRepository.findAll(any(Specification.class)))
                .thenReturn(demoDataGenerator.createRandomExpenseReports(expenseReportQuantity, expenseItemQuantity));
        expenseReportServiceJPA.search("justification:12345");
        verify(expenseReportRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void findById() {

    }

    @Test
    void deleteById() {
    }

    @Test
    void findByReferenceID() {

    }

    ExpenseReport getDummyExpenseReportAndSetupSaveMocks(){
        ExpenseReport expenseReport = demoDataGenerator.createRandomExpenseReport(expenseItemQuantity);

        ExpenseItem oneExpenseItem = expenseReport.getExpenses().stream().findFirst().get();

        Country country = expenseReport.getCountry();
        User user = expenseReport.getUser();
        City expenseCity = oneExpenseItem.getExpenseCity();
        Currency currency = oneExpenseItem.getCurrency();
        ExpenseCategory expenseCategory = oneExpenseItem.getCategory();

        when(countryRepository.findByCode(anyString())).thenReturn(Optional.of(country));
        when(cityRepository.findByCode(anyString())).thenReturn(Optional.of(expenseCity));
        when(currencyRepository.findByCode(anyString())).thenReturn(Optional.of(currency));
        when(expenseCategoryRepository.findByCode(anyString())).thenReturn(Optional.of(expenseCategory));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(expenseReportRepository.save(any())).thenReturn(expenseReport);

        return expenseReport;
    }
}