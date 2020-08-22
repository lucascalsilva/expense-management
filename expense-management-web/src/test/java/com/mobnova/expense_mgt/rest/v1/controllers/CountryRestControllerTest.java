package com.mobnova.expense_mgt.rest.v1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mobnova.expense_mgt.dto.v1.CountryDto;
import com.mobnova.expense_mgt.rest.v1.controllers.exception.GlobalControllerExceptionHandler;
import com.mobnova.expense_mgt.services.CountryService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.mobnova.expense_mgt.ApplicationConstants.REST_API_V1_BASEPATH;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CountryRestControllerTest {

    @InjectMocks
    private CountryRestController countryRestController;

    @Mock
    private CountryService countryService;

    private CountryDto countryDto;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(){
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        mockMvc = MockMvcBuilders.standaloneSetup(countryRestController).setControllerAdvice(new GlobalControllerExceptionHandler()).build();

        countryDto = CountryDto.builder().id(1L).code("BR").name("Brazil").build();
    }

    @Test
    public void findById() throws Exception {
        Long id = 1L;
        when(countryService.findById(id)).thenReturn(countryDto);

        ResultActions resultActions = mockMvc.perform(get(REST_API_V1_BASEPATH + "/countries/" + id.toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvcExpects(resultActions);

        verify(countryService, times(1)).findById(id);
    }

    @Test
    public void create() throws Exception {
        when(countryService.save(any())).thenReturn(countryDto);

        ResultActions resultActions = mockMvc.perform(post(REST_API_V1_BASEPATH + "/countries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(countryDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvcExpects(resultActions);

        verify(countryService, times(1)).save(any());
    }

    @Test
    public void createWithValidationErrors() throws Exception {
        CountryDto countryDtoInvalid = CountryDto.builder().name("Peru").build();

        ResultActions resultActions = mockMvc.perform(post(REST_API_V1_BASEPATH + "/countries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(countryDtoInvalid)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].object", Matchers.equalTo("countryDto")))
                .andExpect(jsonPath("$.[0].message", Matchers.equalTo("must not be blank")))
                .andExpect(jsonPath("$.[0].field", Matchers.equalTo("code")));

        verifyNoInteractions(countryService);
    }

    @Test
    public void update() throws Exception {
        when(countryService.save(any())).thenReturn(countryDto);

        ResultActions resultActions = mockMvc.perform(put(REST_API_V1_BASEPATH + "/countries/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(countryDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvcExpects(resultActions);

        verify(countryService, times(1)).save(any());
    }

    @Test
    public void saveBulk() throws Exception {
        CountryDto countryDto2 = CountryDto.builder().id(2L).code("US").name("United States").build();
        Set<CountryDto> countryDtos = new HashSet<CountryDto>(Arrays.asList(countryDto, countryDto2));

        when(countryService.saveBulk(anySet())).thenReturn(countryDtos);

        mockMvc.perform(post(REST_API_V1_BASEPATH + "/countries/bulk")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(countryDtos)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(countryService, times(1)).saveBulk(anySet());
    }

    public void mockMvcExpects(ResultActions resultActions) throws Exception {
        resultActions.andExpect(jsonPath("$.id", Matchers.equalTo(countryDto.getId().intValue())))
                .andExpect(jsonPath("$.code", Matchers.equalTo(countryDto.getCode())))
                .andExpect(jsonPath("$.name", Matchers.equalTo(countryDto.getName())));
    }
}