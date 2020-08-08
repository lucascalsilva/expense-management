package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.config.CriteriaConfigBean;
import com.mobnova.expense_mgt.config.ModelMapperConfiguration;
import com.mobnova.expense_mgt.dto.v1.*;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.*;
import com.mobnova.expense_mgt.model.Currency;
import com.mobnova.expense_mgt.repositories.*;
import com.mobnova.expense_mgt.services.impl.jpa.ExpenseReportServiceJPAImpl;
import com.mobnova.expense_mgt.util.ExpenseReportTestHelper;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;
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

    private ModelMapper modelMapper;

    @Mock
    private CriteriaConfigBean criteriaConfigBean;

    private ExpenseReportTestHelper expenseReportTestHelper;
    private final Long expenseReportQuantity = 3L;
    private final Long expenseItemQuantity = 2L;

    private Country country;
    private County county;
    private StateOrProvince stateOrProvince;
    private City city;
    private Currency currency;
    private ExpenseCategory expenseCategory;
    private User user;
    private SegmentType segmentTypeCC;
    private SegmentType segmentTypeNA;
    private SegmentValuePair segmentValuePairCC;
    private SegmentValuePair segmentValuePairNA;

    private CountryDto countryDto;
    private CountyDto countyDto;
    private StateOrProvinceDto stateOrProvinceDto;
    private ExpenseItemCityDto expenseItemCityDto;
    private CurrencyDto currencyDto;
    private ExpenseCategoryDto expenseCategoryDto;
    private UserDto userDto;
    private SegmentTypeDto segmentTypeDtoCC;
    private SegmentTypeDto segmentTypeDtoNA;
    private SegmentValuePairDto segmentValuePairDtoCC;
    private SegmentValuePairDto segmentValuePairDtoNA;

    @BeforeEach
    public void init() {
        modelMapper = new ModelMapperConfiguration().globalMapper();

        country = Country.builder().id(1L).code("BR").name("Brazil").build();
        countryDto = modelMapper.map(country, CountryDto.class);

        stateOrProvince = StateOrProvince.builder().id(1L).code("RS").name("Rio Grande do Sul").country(country).build();
        stateOrProvinceDto = modelMapper.map(stateOrProvince, StateOrProvinceDto.class);

        county = County.builder().id(1L).code("POA").name("Condado de Porto Alegre").stateOrProvince(stateOrProvince).build();
        countyDto = modelMapper.map(county, CountyDto.class);

        city = City.builder().id(1L).code("POA").county(county).stateOrProvince(stateOrProvince).build();
        expenseItemCityDto = modelMapper.map(city, ExpenseItemCityDto.class);

        user = User.builder().id(1L).username("user_one").email("user_one@user.com").firstName("User").lastName("One").password("12345").build();
        userDto = modelMapper.map(user, UserDto.class);

        expenseCategory = ExpenseCategory.builder().id(1L).code("MEAL").name("Meal").build();
        expenseCategoryDto = modelMapper.map(expenseCategory, ExpenseCategoryDto.class);

        currency = Currency.builder().id(1L).code("BRL").name("Brazilian Real").build();
        currencyDto = modelMapper.map(currency, CurrencyDto.class);

        segmentTypeCC = SegmentType.builder().id(1L).order(4L).code("CC").name("Cost Center").build();
        segmentTypeDtoCC = modelMapper.map(segmentTypeCC, SegmentTypeDto.class);
        segmentTypeNA = SegmentType.builder().id(2L).order(5L).code("NA").name("Natural Account").build();
        segmentTypeDtoNA = modelMapper.map(segmentTypeNA, SegmentTypeDto.class);

        segmentValuePairCC = SegmentValuePair.builder().id(1L).segmentValue("1000").segmentType(segmentTypeCC).build();
        segmentValuePairDtoCC = modelMapper.map(segmentValuePairCC, SegmentValuePairDto.class);
        segmentValuePairNA = SegmentValuePair.builder().id(2L).segmentValue("5000").segmentType(segmentTypeNA).build();
        segmentValuePairDtoNA = modelMapper.map(segmentValuePairCC, SegmentValuePairDto.class);

        expenseReportTestHelper = new ExpenseReportTestHelper();

        expenseReportServiceJPA = Mockito.spy(new ExpenseReportServiceJPAImpl(expenseReportRepository, userRepository,
                countryRepository, currencyRepository, cityRepository, expenseCategoryRepository,
                segmentValuePairRepository, modelMapper, criteriaConfigBean));
    }

    @Test
    void save() {
        setupSaveMocks();

        ExpenseReport expenseReport = expenseReportTestHelper.createDummyExpenseReport(1L,expenseItemQuantity);
        ExpenseReportDto expenseReportDto = modelMapper.map(expenseReport, ExpenseReportDto.class);

        doAnswer(returnsFirstArg()).when(expenseReportRepository).save(any(ExpenseReport.class));
        when(expenseReportRepository.findById(anyLong())).thenReturn(Optional.of(expenseReport));


        ExpenseReportDto savedExpenseReportDto = expenseReportServiceJPA.save(expenseReportDto);

        expenseReportAssertions(Stream.of(savedExpenseReportDto).collect(Collectors.toSet()));
    }

    @Test
    void update() {
        setupSaveMocks();

        ExpenseReport expenseReport = expenseReportTestHelper.createDummyExpenseReport(1L, expenseItemQuantity);
        ExpenseReportDto expenseReportDto = modelMapper.map(expenseReport, ExpenseReportDto.class);

        doAnswer(returnsFirstArg()).when(expenseReportRepository).save(any(ExpenseReport.class));
        when(expenseReportRepository.findById(anyLong())).thenReturn(Optional.of(expenseReport));

        ExpenseReportDto savedExpenseReportDto = expenseReportServiceJPA.save(expenseReportDto);

        expenseReportAssertions(Stream.of(savedExpenseReportDto).collect(Collectors.toSet()));
    }

    @Test
    void saveBulk() {
        setupSaveMocks();

        doAnswer(returnsFirstArg()).when(expenseReportRepository).save(any(ExpenseReport.class));

        Set<ExpenseReport> expenseReports = expenseReportTestHelper.createDummyExpenseReports(expenseReportQuantity, expenseItemQuantity, true);
        Set<ExpenseReportDto> expenseReportDtos = new HashSet<ExpenseReportDto>(Arrays.asList(modelMapper.map(expenseReports, ExpenseReportDto[].class)));

        for(ExpenseReport expenseReport : expenseReports){
            when(expenseReportRepository.findById(expenseReport.getId())).thenReturn(Optional.of(expenseReport));
        }

        Set<ExpenseReportDto> savedExpenseReportDtos = expenseReportServiceJPA.saveBulk(expenseReportDtos);

        verify(expenseReportServiceJPA, times(3)).save(any(ExpenseReportDto.class));
        verify(expenseReportRepository, times(3)).save(any(ExpenseReport.class));

        expenseReportAssertions(savedExpenseReportDtos);
    }

    @Test
    void search() {
        List<ExpenseReport> expenseReports = new ArrayList<>();

        expenseReports.addAll(expenseReportTestHelper.createDummyExpenseReports(expenseReportQuantity, expenseItemQuantity, true));

        String search = "justification:12345";

        when(expenseReportRepository.findAll(any(Specification.class)))
                .thenReturn(expenseReports);

        Set<ExpenseReportDto> expenseReportDtos = expenseReportServiceJPA.search(search);
        assertThat(expenseReportDtos).hasSize(expenseReports.size());

        verify(expenseReportRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void findById() {
        ExpenseReport expenseReport = expenseReportTestHelper.createDummyExpenseReport(1L, expenseItemQuantity);
        expenseReport.setId(1L);

        when(expenseReportRepository.findById(1L)).thenReturn(Optional.of(expenseReport));

        ExpenseReportDto expenseReportDto = expenseReportServiceJPA.findById(1L);
        assertThat(expenseReportDto).isNotNull();

        verify(expenseReportRepository, times(1)).findById(1L);
    }

    @Test
    void deleteById() {
        expenseReportServiceJPA.deleteById(1L);

        verify(expenseReportRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByReferenceID() {
        ExpenseReport expenseReport = expenseReportTestHelper.createDummyExpenseReport(1L, expenseItemQuantity);
        String expenseReportReferenceID = expenseReport.getReferenceID();

        when(expenseReportRepository.findByReferenceID(expenseReportReferenceID)).thenReturn(Optional.of(expenseReport));

        ExpenseReportDto expenseReportDto = expenseReportServiceJPA.findByReferenceID(expenseReportReferenceID);
        assertThat(expenseReportDto).isNotNull();

        verify(expenseReportRepository).findByReferenceID(expenseReportReferenceID);

    }

    void setupSaveMocks() {
        when(countryRepository.findByCode(country.getCode())).thenReturn(Optional.of(country));
        when(cityRepository.findByCode(city.getCode())).thenReturn(Optional.of(city));
        when(currencyRepository.findByCode(currency.getCode())).thenReturn(Optional.of(currency));
        when(expenseCategoryRepository.findByCode(expenseCategory.getCode())).thenReturn(Optional.of(expenseCategory));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(segmentValuePairRepository.findByValueAndSegmentTypeOrder(segmentValuePairNA.getSegmentValue(),
                segmentTypeNA.getOrder())).thenReturn(Optional.of(segmentValuePairNA));
        when(segmentValuePairRepository.findByValueAndSegmentTypeOrder(segmentValuePairCC.getSegmentValue(),
                segmentTypeCC.getOrder())).thenReturn(Optional.of(segmentValuePairCC));
    }

    void expenseReportAssertions(Set<ExpenseReportDto> savedExpenseReportDtos) {
        verify(countryRepository, times(savedExpenseReportDtos.size())).findByCode(country.getCode());
        verify(userRepository, times(savedExpenseReportDtos.size())).findByUsername(user.getUsername());
        verify(expenseReportRepository, times(savedExpenseReportDtos.size())).save(any(ExpenseReport.class));

        Integer totalExpenseItems = savedExpenseReportDtos.stream().mapToInt(expenseReport ->
                expenseReport.getExpenses().size()).sum();

        verify(expenseCategoryRepository, times(totalExpenseItems)).findByCode(expenseCategory.getCode());
        verify(cityRepository, times(totalExpenseItems)).findByCode(city.getCode());

        for (ExpenseReportDto expenseReportDto : savedExpenseReportDtos) {
            ExpenseReport expenseReport = modelMapper.map(expenseReportDto, ExpenseReport.class);
            verify(expenseReportRepository, times(1)).save(expenseReport);

            for (ExpenseItemDto expenseItemDto : expenseReportDto.getExpenses()) {
                String segmentValuePairs = expenseItemDto.getSegmentValuePairs();

                String[] splitSegmentValues = expenseItemDto.getSegmentValuePairs().split("-");

                splitSegmentValues[0].equalsIgnoreCase(segmentTypeCC.getCode());
                splitSegmentValues[1].equalsIgnoreCase(segmentTypeNA.getCode());
            }
        }
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