package com.mobnova.expense_mgt.rest.v1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mobnova.expense_mgt.config.ModelMapperConfiguration;
import com.mobnova.expense_mgt.dto.v1.ExpenseItemDto;
import com.mobnova.expense_mgt.dto.v1.ExpenseReportDto;
import com.mobnova.expense_mgt.model.ExpenseReport;
import com.mobnova.expense_mgt.model.SegmentValuePair;
import com.mobnova.expense_mgt.rest.v1.controllers.exception.GlobalControllerExceptionHandler;
import com.mobnova.expense_mgt.services.ExpenseReportService;
import com.mobnova.expense_mgt.util.ExpenseReportTestHelper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.mobnova.expense_mgt.ApplicationConstants.REST_API_V1_BASEPATH;
import static com.mobnova.expense_mgt.ApplicationConstants.VALIDATION_ERRORS_MESSAGE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ExpenseReportRestControllerTest {

    @InjectMocks
    private ExpenseReportRestController expenseReportRestController;

    @Mock
    private ExpenseReportService expenseReportService;

    private MockMvc mockMvc;

    private ExpenseReport expenseReport;
    private ExpenseReportDto expenseReportDto;
    private ExpenseItemDto expenseItemDto;
    private SegmentValuePair segmentValuePairNA;
    private SegmentValuePair segmentValuePairCC;

    private ExpenseReportTestHelper expenseReportTestHelper;

    private ModelMapper modelMapper;

    private ObjectMapper objectMapper;


    @BeforeEach
    public void setup() {
        ModelMapperConfiguration modelMapperConfiguration = new ModelMapperConfiguration();
        modelMapper = modelMapperConfiguration.globalMapper();
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        mockMvc = MockMvcBuilders.standaloneSetup(expenseReportRestController).setControllerAdvice(new GlobalControllerExceptionHandler()).build();

        expenseReportTestHelper = new ExpenseReportTestHelper();
        expenseReport = expenseReportTestHelper.createDummyExpenseReport(1L, 2L);
        expenseReportDto = modelMapper.map(expenseReport, ExpenseReportDto.class);

        expenseItemDto = new ArrayList<ExpenseItemDto>(expenseReportDto.getExpenses()).get(0);
    }

    @Test
    public void search() throws Exception {
        Set<ExpenseReport> expenseReports = expenseReportTestHelper.createDummyExpenseReports(5L, 2L, true);
        Set<ExpenseReportDto> expenseReportDtos = new HashSet<>(Arrays.asList(modelMapper.map(expenseReports, ExpenseReportDto[].class)));

        String searchString = "referenceID:123456";

        when(expenseReportService.search(anyString())).thenReturn(expenseReportDtos);

        ResultActions resultActions = mockMvc.perform(get(REST_API_V1_BASEPATH + "/expense-reports")
                .param("search", searchString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(expenseReportService, times(1)).search(searchString);

    }

    @Test
    public void findById() throws Exception {
        Long id = 1L;
        when(expenseReportService.findById(id)).thenReturn(expenseReportDto);

        ResultActions resultActions = mockMvc.perform(get(REST_API_V1_BASEPATH + "/expense-reports/" + id.toString()))
                .andExpect(status().isOk());

        mockMvcExpects(resultActions);


        verify(expenseReportService, times(1)).findById(id);
    }

    @Test
    public void findByReferenceId() throws Exception {
        String referenceId = "12345";
        when(expenseReportService.findByReferenceID(anyString())).thenReturn(expenseReportDto);

        ResultActions resultActions = mockMvc.perform(get(REST_API_V1_BASEPATH + "/expense-reports/byRefID/" + referenceId))
                .andExpect(status().isOk());
        mockMvcExpects(resultActions);

        verify(expenseReportService, times(1)).findByReferenceID(referenceId);
    }

    @Test
    public void create() throws Exception {
        when(expenseReportService.save(any())).thenReturn(expenseReportDto);

        ResultActions resultActions = mockMvc.perform(post(REST_API_V1_BASEPATH + "/expense-reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(expenseReportDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvcExpects(resultActions);

        verify(expenseReportService, times(1)).save(any());
    }

    @Test
    public void createWithValidationErrors() throws Exception {
        ExpenseReportDto expenseReportDtoInvalid = ExpenseReportDto.builder().referenceID("12345")
                .tripStartDate(LocalDate.now()).tripEndDate(LocalDate.now().plusDays(10)).countryCode("BR").build();

        ResultActions resultActions = mockMvc.perform(post(REST_API_V1_BASEPATH + "/expense-reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(expenseReportDtoInvalid)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", Matchers.equalTo("BAD_REQUEST")))
                .andExpect(jsonPath("$.timestamp", Matchers.notNullValue()))
                .andExpect(jsonPath("$.message", Matchers.equalTo(VALIDATION_ERRORS_MESSAGE)))
                .andExpect(jsonPath("$.debugMessage", Matchers.notNullValue()))
                .andExpect(jsonPath("$.subErrors", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.subErrors.[0].object", Matchers.equalTo("expenseReportDto")))
                .andExpect(jsonPath("$.subErrors.[0].field", Matchers.equalTo("creator")))
                .andExpect(jsonPath("$.subErrors.[0].message", Matchers.equalTo("must not be blank")))
                .andExpect(jsonPath("$.subErrors.[1].object", Matchers.equalTo("expenseReportDto")))
                .andExpect(jsonPath("$.subErrors.[1].field", Matchers.equalTo("expenses")))
                .andExpect(jsonPath("$.subErrors.[1].message", Matchers.equalTo("must not be empty")))
                .andExpect(jsonPath("$.subErrors.[2].object", Matchers.equalTo("expenseReportDto")))
                .andExpect(jsonPath("$.subErrors.[2].field", Matchers.equalTo("justification")))
                .andExpect(jsonPath("$.subErrors.[2].message", Matchers.equalTo("must not be blank")))
                .andExpect(jsonPath("$.subErrors.[3].object", Matchers.equalTo("expenseReportDto")))
                .andExpect(jsonPath("$.subErrors.[3].field", Matchers.equalTo("tripDescription")))
                .andExpect(jsonPath("$.subErrors.[3].message", Matchers.equalTo("must not be blank")));

        verifyNoInteractions(expenseReportService);
    }

    @Test
    public void update() throws Exception {
        ExpenseReportDto expenseReportDtoWithId = ExpenseReportDto.builder().id(123L).version(0).build();
        ArgumentCaptor<ExpenseReportDto> argumentCaptor = ArgumentCaptor.forClass(ExpenseReportDto.class);

        when(expenseReportService.save(any())).thenReturn(expenseReportDto);
        when(expenseReportService.findById(123L)).thenReturn(expenseReportDtoWithId);

        ExpenseReportDto expenseReportDtoV1 = modelMapper.map(expenseReport, ExpenseReportDto.class);

        ResultActions resultActions = mockMvc.perform(put(REST_API_V1_BASEPATH + "/expense-reports/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(expenseReportDtoV1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvcExpects(resultActions);

        verify(expenseReportService, times(1)).findById(eq(123L));
        verify(expenseReportService, times(1)).save(argumentCaptor.capture());

        ExpenseReportDto savedExpenseReportDto = argumentCaptor.getValue();
        assertThat(savedExpenseReportDto.getId()).isEqualTo(123L);
        assertThat(savedExpenseReportDto.getVersion()).isEqualTo(0);
    }

    @Test
    public void saveBulk() throws Exception {
        Set<ExpenseReport> expenseReports = expenseReportTestHelper.createDummyExpenseReports(5L, 2L, true);
        Set<ExpenseReportDto> expenseReportDtos = new HashSet<>(Arrays.asList(modelMapper.map(expenseReports, ExpenseReportDto[].class)));

        when(expenseReportService.saveBulk(anySet())).thenReturn(expenseReportDtos);

        ResultActions resultActions = mockMvc.perform(post(REST_API_V1_BASEPATH + "/expense-reports/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(expenseReportDtos)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(expenseReportService, times(1)).saveBulk(anySet());
    }

    public void mockMvcExpects(ResultActions resultActions) throws Exception {
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id",
                        Matchers.equalTo(expenseReportDto.getId().intValue())))
                .andExpect(jsonPath("$.referenceID",
                        Matchers.equalTo(expenseReportDto.getReferenceID())))
                .andExpect(jsonPath("$.tripStartDate",
                        Matchers.equalTo(expenseReportDto.getTripStartDate().toString())))
                .andExpect(jsonPath("$.tripEndDate",
                        Matchers.equalTo(expenseReportDto.getTripEndDate().toString())))
                .andExpect(jsonPath("$.justification",
                        Matchers.equalTo(expenseReportDto.getJustification())))
                .andExpect(jsonPath("$.tripDescription",
                        Matchers.equalTo(expenseReportDto.getTripDescription())))
                .andExpect(jsonPath("$.creator",
                        Matchers.equalTo(expenseReportDto.getCreator())))
                .andExpect(jsonPath("$.expenses",
                        Matchers.hasSize(expenseReportDto.getExpenses().size())))
                .andExpect(jsonPath("$.expenses[0].expenseItemNumber",
                        Matchers.equalTo(expenseItemDto.getExpenseItemNumber().intValue())))
                .andExpect(jsonPath("$.expenses[0].amount",
                        Matchers.equalTo(expenseItemDto.getAmount().intValue())))
                .andExpect(jsonPath("$.expenses[0].currencyCode",
                        Matchers.equalTo(expenseItemDto.getCurrencyCode())))
                .andExpect(jsonPath("$.expenses[0].expenseDate",
                        Matchers.equalTo(expenseItemDto.getExpenseDate().toString())))
                .andExpect(jsonPath("$.expenses[0].expenseCategoryCode",
                        Matchers.equalTo(expenseItemDto.getExpenseCategoryCode())))
                .andExpect(jsonPath("$.expenses[0].expenseCity.cityCode",
                        Matchers.equalTo(expenseItemDto.getExpenseCity().getCityCode())))
                .andExpect(jsonPath("$.expenses[0].expenseCity.countyCode",
                        Matchers.equalTo(expenseItemDto.getExpenseCity().getCountyCode())))
                .andExpect(jsonPath("$.expenses[0].expenseCity.stateOrProvinceCode",
                        Matchers.equalTo(expenseItemDto.getExpenseCity().getStateOrProvinceCode())))
                .andExpect(jsonPath("$.expenses[0].expenseCity.countryCode",
                        Matchers.equalTo(expenseItemDto.getExpenseCity().getCountryCode())))
                .andExpect(jsonPath("$.expenses[0].segmentValuePairs",
                        Matchers.equalTo(expenseItemDto.getSegmentValuePairs())));
    }
}