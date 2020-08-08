package com.mobnova.expense_mgt.rest.v1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mobnova.expense_mgt.config.ModelMapperConfiguration;
import com.mobnova.expense_mgt.dto.v1.ExpenseReportDto;
import com.mobnova.expense_mgt.model.ExpenseItem;
import com.mobnova.expense_mgt.model.ExpenseReport;
import com.mobnova.expense_mgt.model.SegmentValuePair;
import com.mobnova.expense_mgt.services.ExpenseReportService;
import com.mobnova.expense_mgt.util.ExpenseReportTestHelper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mobnova.expense_mgt.ApplicationConstants.REST_API_V1_BASEPATH;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ExpenseReportRestControllerTest {

    private ExpenseReportRestController expenseReportRestController;

    @Mock
    private ExpenseReportService expenseReportService;

    private MockMvc mockMvc;

    private ExpenseItem expenseItem;

    private SegmentValuePair segmentValuePairNA;

    private SegmentValuePair segmentValuePairCC;

    private ExpenseReport expenseReport;
    private ExpenseReportDto expenseReportDto;

    private ExpenseReportTestHelper expenseReportTestHelper;

    private ModelMapper modelMapper;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setup() {
        ModelMapperConfiguration modelMapperConfiguration = new ModelMapperConfiguration();
        modelMapper = modelMapperConfiguration.globalMapper();
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        expenseReportRestController = new ExpenseReportRestController(expenseReportService, modelMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(expenseReportRestController).setControllerAdvice(new GlobalControllerErrorHandler()).build();

        expenseReportTestHelper = new ExpenseReportTestHelper();
        expenseReport = expenseReportTestHelper.createDummyExpenseReport(2);
        expenseReportDto = modelMapper.map(expenseReport, ExpenseReportDto.class);

        expenseItem = new ArrayList<ExpenseItem>(expenseReport.getExpenses()).get(0);

        segmentValuePairNA = expenseItem.getSegmentValuePairs().stream()
                .filter(segmentValuePair -> segmentValuePair.getSegmentType().getCode().equals("NA")).findFirst().get();

        segmentValuePairCC = expenseItem.getSegmentValuePairs().stream()
                .filter(segmentValuePair -> segmentValuePair.getSegmentType().getCode().equals("CC")).findFirst().get();
    }

    @Test
    void search() throws Exception {
        Set<ExpenseReport> expenseReports = expenseReportTestHelper.createDummyExpenseReports(5, 2);
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
    void findById() throws Exception {
        Long id = 1L;
        when(expenseReportService.findById(id)).thenReturn(expenseReportDto);

        ResultActions resultActions = mockMvc.perform(get(REST_API_V1_BASEPATH + "/expense-reports/" + id.toString()))
                .andExpect(status().isOk());

        mockMvcExpects(resultActions);


        verify(expenseReportService, times(1)).findById(id);
    }

    @Test
    void findByReferenceId() throws Exception {
        String referenceId = "12345";
        when(expenseReportService.findByReferenceID(anyString())).thenReturn(expenseReportDto);

        ResultActions resultActions = mockMvc.perform(get(REST_API_V1_BASEPATH + "/expense-reports/byRefID/" + referenceId))
                .andExpect(status().isOk());
        mockMvcExpects(resultActions);

        verify(expenseReportService, times(1)).findByReferenceID(referenceId);
    }

    @Test
    void create() throws Exception {
        when(expenseReportService.save(any())).thenReturn(expenseReportDto);
        ExpenseReportDto expenseReportDtoV1 = modelMapper.map(expenseReport, ExpenseReportDto.class);

        ResultActions resultActions = mockMvc.perform(post(REST_API_V1_BASEPATH + "/expense-reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(expenseReportDtoV1)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvcExpects(resultActions);

        verify(expenseReportService, times(1)).save(any());
    }

    @Test
    void createWithValidationErrors() throws Exception {
        ExpenseReport expenseReportInvalid = ExpenseReport.builder().referenceID("12345").tripStartDate(LocalDate.now()).build();
        ExpenseReportDto expenseReportDtoInvalid = modelMapper.map(expenseReportInvalid, ExpenseReportDto.class);

        ResultActions resultActions = mockMvc.perform(post(REST_API_V1_BASEPATH + "/expense-reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(expenseReportDtoInvalid)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].object", Matchers.equalTo("expenseReportDto")))
                .andExpect(jsonPath("$.[0].message", Matchers.equalTo("must not be blank")))
                .andExpect(jsonPath("$.[0].field", Matchers.equalTo("countryCode")))
                .andExpect(jsonPath("$.[1].object", Matchers.equalTo("expenseReportDto")))
                .andExpect(jsonPath("$.[1].message", Matchers.equalTo("must not be blank")))
                .andExpect(jsonPath("$.[1].field", Matchers.equalTo("creator")))
                .andExpect(jsonPath("$.[2].object", Matchers.equalTo("expenseReportDto")))
                .andExpect(jsonPath("$.[2].message", Matchers.equalTo("must not be empty")))
                .andExpect(jsonPath("$.[2].field", Matchers.equalTo("expenses")))
                .andExpect(jsonPath("$.[3].object", Matchers.equalTo("expenseReportDto")))
                .andExpect(jsonPath("$.[3].message", Matchers.equalTo("must not be blank")))
                .andExpect(jsonPath("$.[3].field", Matchers.equalTo("justification")))
                .andExpect(jsonPath("$.[4].object", Matchers.equalTo("expenseReportDto")))
                .andExpect(jsonPath("$.[4].message", Matchers.equalTo("must not be blank")))
                .andExpect(jsonPath("$.[4].field", Matchers.equalTo("tripDescription")))
                .andExpect(jsonPath("$.[5].object", Matchers.equalTo("expenseReportDto")))
                .andExpect(jsonPath("$.[5].message", Matchers.equalTo("must not be null")))
                .andExpect(jsonPath("$.[5].field", Matchers.equalTo("tripEndDate")));

        verifyNoInteractions(expenseReportService);
    }

    @Test
    void update() throws Exception {
        ExpenseReportDto expenseReportDtoWithId = ExpenseReportDto.builder().id(123L).version(0).build();
        ArgumentCaptor<ExpenseReportDto> argumentCaptor = ArgumentCaptor.forClass(ExpenseReportDto.class);
        when(expenseReportService.save(any())).thenReturn(expenseReportDto);
        when(expenseReportService.findById(123L)).thenReturn(expenseReportDtoWithId);

        ExpenseReportDto expenseReportDtoV1 = modelMapper.map(expenseReport, ExpenseReportDto.class);

        ResultActions resultActions = mockMvc.perform(put(REST_API_V1_BASEPATH + "/expense-reports/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(expenseReportDtoV1)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvcExpects(resultActions);

        verify(expenseReportService, times(1)).findById(eq(123L));
        verify(expenseReportService, times(1)).save(argumentCaptor.capture());

        ExpenseReportDto savedExpenseReportDto = argumentCaptor.getValue();
        assertThat(savedExpenseReportDto.getId()).isEqualTo(123L);
        assertThat(savedExpenseReportDto.getVersion()).isEqualTo(0);
    }

    @Test
    void saveBulk() throws Exception {
        Set<ExpenseReport> expenseReports = expenseReportTestHelper.createDummyExpenseReports(5, 2);
        Set<ExpenseReportDto> expenseReportDtos = new HashSet<>(Arrays.asList(modelMapper.map(expenseReports, ExpenseReportDto[].class)));

        when(expenseReportService.saveBulk(anySet())).thenReturn(expenseReportDtos);

        ResultActions resultActions = mockMvc.perform(post(REST_API_V1_BASEPATH + "/expense-reports/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(expenseReportDtos)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(expenseReportService, times(1)).saveBulk(anySet());
    }

    void mockMvcExpects(ResultActions resultActions) throws Exception {
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.referenceID",
                        Matchers.equalTo(expenseReport.getReferenceID())))
                .andExpect(jsonPath("$.tripStartDate",
                        Matchers.equalTo(expenseReport.getTripStartDate().toString())))
                .andExpect(jsonPath("$.tripEndDate",
                        Matchers.equalTo(expenseReport.getTripEndDate().toString())))
                .andExpect(jsonPath("$.justification",
                        Matchers.equalTo(expenseReport.getJustification())))
                .andExpect(jsonPath("$.tripDescription",
                        Matchers.equalTo(expenseReport.getTripDescription())))
                .andExpect(jsonPath("$.creator",
                        Matchers.equalTo(expenseReport.getUser().getUsername())))
                .andExpect(jsonPath("$.expenses",
                        Matchers.hasSize(expenseReport.getExpenses().size())))
                .andExpect(jsonPath("$.expenses[0].expenseItemNumber",
                        Matchers.equalTo(expenseItem.getExpenseItemNumber().intValue())))
                .andExpect(jsonPath("$.expenses[0].amount",
                        Matchers.equalTo(expenseItem.getAmount().intValue())))
                .andExpect(jsonPath("$.expenses[0].currencyCode",
                        Matchers.equalTo(expenseItem.getCurrency().getCode())))
                .andExpect(jsonPath("$.expenses[0].expenseDate",
                        Matchers.equalTo(expenseItem.getExpenseDate().toString())))
                .andExpect(jsonPath("$.expenses[0].expenseCategoryCode",
                        Matchers.equalTo(expenseItem.getCategory().getCode())))
                .andExpect(jsonPath("$.expenses[0].expenseCity.cityCode",
                        Matchers.equalTo(expenseItem.getExpenseCity().getCode())))
                .andExpect(jsonPath("$.expenses[0].expenseCity.countyCode",
                        Matchers.equalTo(expenseItem.getExpenseCity().getCode())))
                .andExpect(jsonPath("$.expenses[0].expenseCity.stateOrProvinceCode",
                        Matchers.equalTo(expenseItem.getExpenseCity().getStateOrProvince().getCode())))
                .andExpect(jsonPath("$.expenses[0].expenseCity.countryCode",
                        Matchers.equalTo(expenseItem.getExpenseCity().getStateOrProvince().getCountry().getCode())))
                .andExpect(jsonPath("$.expenses[0].segmentValuePairs",
                        Matchers.containsString(segmentValuePairCC.getSegmentValue() + "-" + segmentValuePairNA.getSegmentValue())));
    }
}