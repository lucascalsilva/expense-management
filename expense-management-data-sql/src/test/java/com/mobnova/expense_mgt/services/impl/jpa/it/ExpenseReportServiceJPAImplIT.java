package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.*;
import com.mobnova.expense_mgt.repositories.*;
import com.mobnova.expense_mgt.services.impl.jpa.ExpenseReportServiceJPAImpl;
import com.mobnova.expense_mgt.util.ExpenseReportTestHelper;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ExpenseReportServiceJPAImplIT {

    @Autowired
    private ExpenseReportServiceJPAImpl expenseReportServiceJPA;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateOrProvinceRepository stateOrProvinceRepository;

    @Autowired
    private CountyRepository countyRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;

    @Autowired
    private SegmentTypeRepository segmentTypeRepository;

    @Autowired
    private SegmentValuePairRepository segmentValuePairRepository;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    private static ExpenseReportTestHelper expenseReportTestHelper;

    private Integer expenseReportQuantity = 3;
    private Integer expenseItemQuantity = 3;

    private static Boolean dbInitialized = false;

    @BeforeEach
    public void init(){
        expenseReportTestHelper = new ExpenseReportTestHelper();

        if(!dbInitialized) {
            integrationTestHelper.cleanAllData();
            Country country = Country.builder().code("BR").name("Brazil").build();
            StateOrProvince stateOrProvince = StateOrProvince.builder().code("RS").name("Rio Grande do Sul")
                    .country(country).build();
            County county = County.builder().code("POA").name("Condado de Porto Alegre")
                    .stateOrProvince(stateOrProvince).build();
            City city = City.builder().code("POA").name("Porto Alegre").stateOrProvince(stateOrProvince)
                    .county(county).build();
            User user = User.builder().username("user_one").password("123456").email("lucas.silva@domain.com")
                    .firstName("Lucas").lastName("Silva").build();
            ExpenseCategory expenseCategory = ExpenseCategory.builder().code("MEAL").name("Meal").build();
            Currency currency = Currency.builder().code("BRL").name("Real").build();
            SegmentType segmentTypeCC = SegmentType.builder().code("CC").name("Cost Center").build();
            SegmentType segmentTypeAC = SegmentType.builder().code("AC").name("Natural Account").build();
            SegmentValuePair segmentValuePairCC = SegmentValuePair.builder().segmentValue("1000").segmentType(segmentTypeCC).build();
            SegmentValuePair segmentValuePairAC = SegmentValuePair.builder().segmentValue("5000").segmentType(segmentTypeAC).build();

            countryRepository.save(country);
            stateOrProvinceRepository.save(stateOrProvince);
            countyRepository.save(county);
            cityRepository.save(city);
            currencyRepository.save(currency);
            expenseCategoryRepository.save(expenseCategory);
            userRepository.save(user);
            segmentTypeRepository.save(segmentTypeCC);
            segmentTypeRepository.save(segmentTypeAC);
            segmentValuePairRepository.save(segmentValuePairCC);
            segmentValuePairRepository.save(segmentValuePairAC);

            dbInitialized = true;
        }
    }

    @Test
    void save() {
        ExpenseReport expenseReport = expenseReportTestHelper.createDummyExpenseReport(expenseItemQuantity);
        ExpenseReport savedExpenseReport = expenseReportServiceJPA.save(expenseReport);
        assertThat(savedExpenseReport.getId()).isNotNull();
        assertThat(savedExpenseReport.getCreationDate()).isNotNull();
        assertThat(savedExpenseReport.getCountry().getId()).isNotNull();
        assertThat(savedExpenseReport.getUser().getId()).isNotNull();

        for(ExpenseItem expenseItem : savedExpenseReport.getExpenses()){
            assertThat(expenseItem.getExpenseReport()).isNotNull();
            assertThat(expenseItem.getExpenseCity().getId()).isNotNull();
            assertThat(expenseItem.getExpenseCity().getCreationDate()).isNotNull();

            assertThat(expenseItem.getCategory().getId()).isNotNull();
            assertThat(expenseItem.getCategory().getCreationDate()).isNotNull();

            assertThat(expenseItem.getCurrency().getId()).isNotNull();
            assertThat(expenseItem.getCurrency().getCreationDate()).isNotNull();

            for(SegmentValuePair segmentValuePair : expenseItem.getSegmentValuePairs()){
                assertThat(segmentValuePair.getId()).isNotNull();
                assertThat(segmentValuePair.getCreationDate()).isNotNull();
            }
        }
    }

    @Test
    void saveBulk() {
        Set<ExpenseReport> expenseReports = expenseReportTestHelper
                .createDummyExpenseReports(expenseReportQuantity, expenseItemQuantity);

        Set<ExpenseReport> savedExpenseReports = expenseReportServiceJPA.saveBulk(new HashSet<>(expenseReports));

        for(ExpenseReport expenseReport : savedExpenseReports){
            assertThat(expenseReport.getId()).isNotNull();
            assertThat(expenseReport.getCreationDate()).isNotNull();
        }
    }

    @Test
    void searchWithLikeByJustification() {
        Set<ExpenseReport> expenseReports = expenseReportTestHelper
                .createDummyExpenseReports(expenseReportQuantity, expenseItemQuantity);

        Set<ExpenseReport> savedExpenseReports = expenseReportServiceJPA.saveBulk(new HashSet<>(expenseReports));

        String queryReferenceID = savedExpenseReports.stream().findFirst().get().getJustification()
                .replace("Justification ", "");

        Set<ExpenseReport> searchedExpenseReports = expenseReportServiceJPA.search("justification:"+queryReferenceID);

        assertThat(searchedExpenseReports).isNotEmpty();
    }

    @Test
    void findById() {
        ExpenseReport expenseReport = expenseReportTestHelper.createDummyExpenseReport(expenseItemQuantity);
        ExpenseReport savedExpenseReport = expenseReportServiceJPA.save(expenseReport);

        Long expenseReportId = savedExpenseReport.getId();

        ExpenseReport expenseReportById = expenseReportServiceJPA.findById(expenseReportId);

        assertThat(expenseReportById).isEqualTo(savedExpenseReport);
    }

    @Test
    void deleteById() {
        ExpenseReport expenseReport = expenseReportTestHelper.createDummyExpenseReport(expenseItemQuantity);
        ExpenseReport savedExpenseReport = expenseReportServiceJPA.save(expenseReport);

        Long expenseReportId = savedExpenseReport.getId();

        assertThat(expenseReportServiceJPA.findById(expenseReportId)).isNotNull();

        expenseReportServiceJPA.deleteById(expenseReportId);

        assertThrows(DataNotFoundException.class, () -> expenseReportServiceJPA.findById(expenseReportId));
    }

    @Test
    void findByReferenceID() {
        ExpenseReport expenseReport = expenseReportTestHelper.createDummyExpenseReport(expenseItemQuantity);
        ExpenseReport savedExpenseReport = expenseReportServiceJPA.save(expenseReport);
        String expenseReportReferenceID = savedExpenseReport.getReferenceID();

        ExpenseReport expenseReportByReferenceID = expenseReportServiceJPA
                .findByReferenceID(expenseReportReferenceID);

        assertThat(expenseReportByReferenceID).isEqualTo(savedExpenseReport);
    }
}