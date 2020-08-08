package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.dto.v1.ExpenseItemDto;
import com.mobnova.expense_mgt.dto.v1.ExpenseReportDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.*;
import com.mobnova.expense_mgt.repositories.*;
import com.mobnova.expense_mgt.services.impl.jpa.ExpenseReportServiceJPAImpl;
import com.mobnova.expense_mgt.util.AssertionUtils;
import com.mobnova.expense_mgt.util.ExpenseReportTestHelper;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
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

    @Autowired
    private ModelMapper modelMapper;

    private static ExpenseReportTestHelper expenseReportTestHelper;

    private final Long expenseReportQuantity = 3L;
    private final Long expenseItemQuantity = 3L;

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
            SegmentType segmentTypeCC = SegmentType.builder().code("CC").name("Cost Center").order(4L).build();
            SegmentType segmentTypeAC = SegmentType.builder().code("NA").name("Natural Account").order(5L).build();
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
        ExpenseReport expenseReport = expenseReportTestHelper.createDummyExpenseReport(null, expenseItemQuantity);
        ExpenseReportDto expenseReportDto = modelMapper.map(expenseReport, ExpenseReportDto.class);

        ExpenseReportDto savedExpenseReportDto = expenseReportServiceJPA.save(expenseReportDto);

        assertThat(savedExpenseReportDto.getId()).isNotNull();
        assertThat(savedExpenseReportDto.getCreationDate()).isNotNull();

        for(ExpenseItemDto expenseItemDto : savedExpenseReportDto.getExpenses()){
            assertThat(expenseItemDto.getExpenseCity().getId()).isNotNull();
            assertThat(expenseItemDto.getExpenseCity().getCreationDate()).isNotNull();
        }
    }

    @Test
    void saveValidationError() {
        ExpenseReport expenseReport = ExpenseReport.builder().referenceID("12345").build();
        ExpenseReportDto expenseReportDto = modelMapper.map(expenseReport, ExpenseReportDto.class);

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> expenseReportServiceJPA.save(expenseReportDto));
        Assertions.assertThat(constraintViolationException.getMessage()).contains("save.arg0.creator: must not be blank");
        Assertions.assertThat(constraintViolationException.getMessage()).contains("save.arg0.countryCode: must not be blank");
        Assertions.assertThat(constraintViolationException.getMessage()).contains("save.arg0.tripEndDate: must not be null");
        Assertions.assertThat(constraintViolationException.getMessage()).contains("save.arg0.tripDescription: must not be blank");
        Assertions.assertThat(constraintViolationException.getMessage()).contains("save.arg0.expenses: must not be empty");
        Assertions.assertThat(constraintViolationException.getMessage()).contains("save.arg0.tripStartDate: must not be null");
        Assertions.assertThat(constraintViolationException.getMessage()).contains("save.arg0.justification: must not be blank");
    }

    @Test
    void update() {
        ExpenseReport expenseReport = expenseReportTestHelper.createDummyExpenseReport(null, expenseItemQuantity);
        ExpenseReportDto expenseReportDto = modelMapper.map(expenseReport, ExpenseReportDto.class);

        expenseReportDto = expenseReportServiceJPA.save(expenseReportDto);

        assertThat(expenseReportDto).isNotNull();
        assertThat(expenseReportDto.getId()).isNotNull();

        ExpenseReportDto updatedExpenseReportDto = expenseReportServiceJPA.findById(expenseReportDto.getId());

        String newJustification = "Some other justification";
        updatedExpenseReportDto.setJustification(newJustification);

        updatedExpenseReportDto = expenseReportServiceJPA.save(updatedExpenseReportDto);

        AssertionUtils.dtoUpdateAssertions(expenseReportDto, updatedExpenseReportDto);
        assertThat(expenseReportDto.getJustification()).isNotEqualTo(updatedExpenseReportDto.getJustification());
    }

    @Test
    void saveBulk() {
        Set<ExpenseReport> expenseReports = expenseReportTestHelper
                .createDummyExpenseReports(expenseReportQuantity, expenseItemQuantity, false);
        Set<ExpenseReportDto> expenseReportDtos = new HashSet<>(Arrays.asList(modelMapper.map(expenseReports, ExpenseReportDto[].class)));

        Set<ExpenseReportDto> savedExpenseReportDtos = expenseReportServiceJPA.saveBulk(expenseReportDtos);

        for(ExpenseReportDto expenseReportDto : savedExpenseReportDtos){
            assertThat(expenseReportDto.getId()).isNotNull();
            assertThat(expenseReportDto.getCreationDate()).isNotNull();
        }
    }

    @Test
    void searchWithLikeByJustification() {
        Set<ExpenseReport> expenseReports = expenseReportTestHelper
                .createDummyExpenseReports(expenseReportQuantity, expenseItemQuantity, false);
        Set<ExpenseReportDto> expenseReportDtos = new HashSet<>(Arrays.asList(modelMapper.map(expenseReports, ExpenseReportDto[].class)));

        Set<ExpenseReportDto> savedExpenseReportDtos = expenseReportServiceJPA.saveBulk(expenseReportDtos);

        String queryReferenceID = savedExpenseReportDtos.stream().findFirst().get().getJustification()
                .replace("Justification ", "");

        Set<ExpenseReportDto> searchedExpenseReportDtos = expenseReportServiceJPA.search("justification:"+queryReferenceID);

        assertThat(searchedExpenseReportDtos).isNotEmpty();
    }

    @Test
    void findById() {
        ExpenseReport expenseReport = expenseReportTestHelper.createDummyExpenseReport(null, expenseItemQuantity);
        ExpenseReportDto expenseReportDto = modelMapper.map(expenseReport, ExpenseReportDto.class);

        ExpenseReportDto savedExpenseReportDto = expenseReportServiceJPA.save(expenseReportDto);

        Long expenseReportId = savedExpenseReportDto.getId();

        ExpenseReportDto expenseReportDtoById = expenseReportServiceJPA.findById(expenseReportId);

        assertThat(expenseReportDtoById).isEqualTo(savedExpenseReportDto);
    }

    @Test
    void deleteById() {
        ExpenseReport expenseReport = expenseReportTestHelper.createDummyExpenseReport(null, expenseItemQuantity);
        ExpenseReportDto expenseReportDto = modelMapper.map(expenseReport, ExpenseReportDto.class);

        ExpenseReportDto savedExpenseReportDto = expenseReportServiceJPA.save(expenseReportDto);

        Long expenseReportId = savedExpenseReportDto.getId();

        assertThat(expenseReportServiceJPA.findById(expenseReportId)).isNotNull();

        expenseReportServiceJPA.deleteById(expenseReportId);

        assertThrows(DataNotFoundException.class, () -> expenseReportServiceJPA.findById(expenseReportId));
    }

    @Test
    void findByReferenceID() {
        ExpenseReport expenseReport = expenseReportTestHelper.createDummyExpenseReport(null, expenseItemQuantity);
        ExpenseReportDto expenseReportDto = modelMapper.map(expenseReport, ExpenseReportDto.class);

        ExpenseReportDto savedExpenseReportDto = expenseReportServiceJPA.save(expenseReportDto);
        String expenseReportReferenceID = savedExpenseReportDto.getReferenceID();

        ExpenseReportDto expenseReportDtoByReferenceID = expenseReportServiceJPA
                .findByReferenceID(expenseReportReferenceID);

        assertThat(expenseReportDtoByReferenceID).isEqualTo(savedExpenseReportDto);
    }

    @Test
    void findByReferenceIDNotFound() {
        String referenceID = "1000";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> expenseReportServiceJPA.findByReferenceID(referenceID));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(ExpenseReport.class.getSimpleName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.REFERENCE_ID.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(referenceID);
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> expenseReportServiceJPA.findById(id));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(ExpenseReport.class.getSimpleName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}