package com.mobnova.expense_mgt.util;

import com.mobnova.expense_mgt.model.*;
import com.mobnova.expense_mgt.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DemoDataPersistence {

    @Autowired
    private ExpenseReportRepository expenseReportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private StateOrProvinceRepository stateOrProvinceRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;

    public void persistAllBaseData(DemoDataGenerator demoDataGenerator){
        persistCountries(demoDataGenerator.getCountries());
        persistStateOrProvince(demoDataGenerator.getStateOrProvinces());
        persistCities(demoDataGenerator.getCities());
        persistExpenseCategories(demoDataGenerator.getExpenseCategories());
        persistUsers(demoDataGenerator.getUsers());
        persistCurrencies(demoDataGenerator.getCurrencies());
    }

    public void persistCountries(List<Country> countries){
        countryRepository.saveAll(countries);
    }

    public void persistStateOrProvince(List<StateOrProvince> stateOrProvinces){
        stateOrProvinceRepository.saveAll(stateOrProvinces);
    }

    public void persistCities(List<City> cities){
        cityRepository.saveAll(cities);
    }

    public void persistExpenseCategories(List<ExpenseCategory> expenseCategories){
        expenseCategoryRepository.saveAll(expenseCategories);
    }

    public void persistUsers(List<User> users){
        userRepository.saveAll(users);
    }

    public void persistCurrencies(List<Currency> currencies){
        currencyRepository.saveAll(currencies);
    }
}
