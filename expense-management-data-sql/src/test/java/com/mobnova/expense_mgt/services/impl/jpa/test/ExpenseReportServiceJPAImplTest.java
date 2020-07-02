package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.config.CriteriaConfigBean;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.*;
import com.mobnova.expense_mgt.repositories.*;
import com.mobnova.expense_mgt.services.impl.jpa.ExpenseReportServiceJPAImpl;
import com.mobnova.expense_mgt.util.ExpenseReportTestHelper;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class ExpenseReportServiceJPAImplTest {

    @InjectMocks
    @Spy
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
    private CityRepository cityRepository;

    @Mock
    private ExpenseCategoryRepository expenseCategoryRepository;

    @Mock
    private SegmentTypeRepository segmentTypeRepository;

    @Mock
    private SegmentValuePairRepository segmentValuePairRepository;

    @Mock
    private CriteriaConfigBean criteriaConfigBean;

    private ExpenseReportTestHelper expenseReportTestHelper;
    private final Integer expenseReportQuantity = 3;
    private final Integer expenseItemQuantity = 2;

    private Country country;
    private County county;
    private StateOrProvince stateOrProvince;
    private City city;
    private Currency currency;
    private ExpenseCategory expenseCategory;
    private User user;
    private SegmentType segmentTypeCC;
    private SegmentType segmentTypeAC;
    private SegmentValuePair segmentValuePairCC;
    private SegmentValuePair segmentValuePairAC;

    @BeforeEach
    public void init(){
        country = Country.builder().id(1L).code("BR").name("Brazil").build();
        stateOrProvince = StateOrProvince.builder().id(1L).code("RS").name("Rio Grande do Sul").country(country).build();
        county = County.builder().id(1L).code("POA").name("Condado de Porto Alegre").stateOrProvince(stateOrProvince).build();
        city = City.builder().id(1L).code("POA").county(county).stateOrProvince(stateOrProvince).build();
        user = User.builder().id(1L).username("user_one").email("user_one@user.com").firstName("User").lastName("One").password("12345").build();
        expenseCategory = ExpenseCategory.builder().id(1L).code("MEAL").name("Meal").build();
        currency = Currency.builder().id(1L).code("BRL").name("Brazilian Real").build();
        segmentTypeCC = SegmentType.builder().id(1L).code("CC").name("Cost Center").build();
        segmentTypeAC = SegmentType.builder().id(2L).code("AC").name("Natural Account").build();
        segmentValuePairCC = SegmentValuePair.builder().id(1L).segmentValue("1000").segmentType(segmentTypeCC).build();
        segmentValuePairAC = SegmentValuePair.builder().id(2L).segmentValue("5000").segmentType(segmentTypeAC).build();

        expenseReportTestHelper = new ExpenseReportTestHelper();
    }

    @Test
    void save() {
        setupSaveMocks();

        doAnswer(returnsFirstArg()).when(expenseReportRepository).save(any(ExpenseReport.class));

        ExpenseReport expenseReport = expenseReportTestHelper.createDummyExpenseReport(2);

        ExpenseReport savedExpenseReport = expenseReportServiceJPA.save(expenseReport);

        expenseReportAssertions(Stream.of(savedExpenseReport).collect(Collectors.toSet()));
    }

    @Test
    void update() {
        setupSaveMocks();

        doAnswer(returnsFirstArg()).when(expenseReportRepository).save(any(ExpenseReport.class));

        ExpenseReport expenseReport = expenseReportTestHelper.createDummyExpenseReport(2);

        ExpenseReport savedExpenseReport = expenseReportServiceJPA.save(expenseReport);

        expenseReportAssertions(Stream.of(savedExpenseReport).collect(Collectors.toSet()));
    }

    @Test
    void saveBulk() {
        setupSaveMocks();

        doAnswer(returnsFirstArg()).when(expenseReportRepository).save(any(ExpenseReport.class));

        Set<ExpenseReport> expenseReports = expenseReportTestHelper.createDummyExpenseReports(expenseReportQuantity, expenseItemQuantity);

        Set<ExpenseReport> savedExpenseReports = expenseReportServiceJPA.saveBulk(expenseReports);

        for(ExpenseReport expenseReport : savedExpenseReports) {
            verify(expenseReportServiceJPA, times(1)).save(expenseReport);
            verify(expenseReportRepository, times(1)).save(expenseReport);
        }

        expenseReportAssertions(savedExpenseReports);
    }

    @Test
    void search() {
        List<ExpenseReport> expenseReports = new ArrayList<>();

        expenseReports.addAll(expenseReportTestHelper.createDummyExpenseReports(expenseReportQuantity, expenseItemQuantity));

        String search = "justification:12345";

        when(expenseReportRepository.findAll(any(Specification.class)))
                .thenReturn(expenseReports);

        expenseReportServiceJPA.search(search);
        verify(expenseReportRepository, times(1)).findAll(any(Specification.class   ));
    }

    @Test
    void findById() {
        ExpenseReport expenseReport = expenseReportTestHelper.createDummyExpenseReport(expenseItemQuantity);
        expenseReport.setId(1L);

        when(expenseReportRepository.findById(1L)).thenReturn(Optional.of(expenseReport));

        expenseReportServiceJPA.findById(1L);

        verify(expenseReportRepository, times(1)).findById(1L);
    }

    @Test
    void deleteById() {
        expenseReportServiceJPA.deleteById(1L);

        verify(expenseReportRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByReferenceID() {
        ExpenseReport expenseReport = expenseReportTestHelper.createDummyExpenseReport(expenseItemQuantity);
        String expenseReportReferenceID = expenseReport.getReferenceID();

        when(expenseReportRepository.findByReferenceID(expenseReportReferenceID)).thenReturn(Optional.of(expenseReport));
        expenseReportServiceJPA.findByReferenceID(expenseReportReferenceID);

        verify(expenseReportRepository).findByReferenceID(expenseReportReferenceID);

    }

    void setupSaveMocks(){
        when(countryRepository.findByCode(country.getCode())).thenReturn(Optional.of(country));
        when(cityRepository.findByCode(city.getCode())).thenReturn(Optional.of(city));
        when(currencyRepository.findByCode(currency.getCode())).thenReturn(Optional.of(currency));
        when(expenseCategoryRepository.findByCode(expenseCategory.getCode())).thenReturn(Optional.of(expenseCategory));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(segmentValuePairRepository.findByValueAndSegmentTypeCode(segmentValuePairAC.getSegmentValue(),
                segmentTypeAC.getCode())).thenReturn(Optional.of(segmentValuePairAC));
        when(segmentValuePairRepository.findByValueAndSegmentTypeCode(segmentValuePairCC.getSegmentValue(),
                segmentTypeCC.getCode())).thenReturn(Optional.of(segmentValuePairCC));
    }

    void expenseReportAssertions(Set<ExpenseReport> savedExpenseReports){
        verify(countryRepository, times(savedExpenseReports.size())).findByCode(country.getCode());
        verify(userRepository, times(savedExpenseReports.size())).findByUsername(user.getUsername());
        verify(expenseReportRepository, times(savedExpenseReports.size())).save(any(ExpenseReport.class));

        Integer totalExpenseItems = savedExpenseReports.stream().mapToInt(expenseReport ->
                expenseReport.getExpenses().size()).sum();

        verify(expenseCategoryRepository, times(totalExpenseItems)).findByCode(expenseCategory.getCode());
        verify(cityRepository, times(totalExpenseItems)).findByCode(city.getCode());

        for(ExpenseReport expenseReport : savedExpenseReports) {
            verify(expenseReportRepository, times(1)).save(expenseReport);
            assertThat(expenseReport.getCountry().getId()).isEqualTo(1L);
            assertThat(expenseReport.getUser().getId()).isEqualTo(1L);

            for (ExpenseItem expenseItem : expenseReport.getExpenses()) {
                assertThat(expenseItem.getExpenseCity().getId()).isEqualTo(1L);
                assertThat(expenseItem.getCategory().getId()).isEqualTo(1L);
                assertThat(expenseItem.getCurrency().getId()).isEqualTo(1L);

                for(SegmentValuePair segmentValuePair : expenseItem.getSegmentValuePairs()){
                    verify(segmentValuePairRepository, times(totalExpenseItems))
                            .findByValueAndSegmentTypeCode(segmentValuePair.getSegmentValue(), segmentValuePair.getSegmentType().getCode());
                    assertThat(segmentValuePair.getId()).isIn(1L, 2L);
                    assertThat(segmentValuePair.getSegmentType().getId()).isIn(1L, 2L);
                }
            }
        }
    }

    @Test
    void findByReferenceIDNotFound() {
        String referenceID = "1000";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> expenseReportServiceJPA.findByReferenceID(referenceID));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(ExpenseReport.class.getName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.REFERENCE_ID.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(referenceID);
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> expenseReportServiceJPA.findById(id));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(ExpenseReport.class.getName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}