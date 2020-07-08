package com.mobnova.expense_mgt.util;

import com.mobnova.expense_mgt.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntegrationTestHelper {

    @Autowired
    private ExpenseReportRepository expenseReportRepository;

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
    private ExpenseCategoryRepository expenseCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SegmentTypeRepository segmentTypeRepository;

    @Autowired
    private SegmentValuePairRepository segmentValuePairRepository;

    public void cleanAllData(){
        expenseReportRepository.deleteAll();
        cityRepository.deleteAll();
        countyRepository.deleteAll();
        stateOrProvinceRepository.deleteAll();
        countryRepository.deleteAll();
        currencyRepository.deleteAll();
        expenseCategoryRepository.deleteAll();
        userRepository.deleteAll();
        segmentValuePairRepository.deleteAll();
        segmentTypeRepository.deleteAll();
    }


}
