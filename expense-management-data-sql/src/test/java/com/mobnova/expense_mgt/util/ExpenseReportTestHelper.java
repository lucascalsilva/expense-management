package com.mobnova.expense_mgt.util;

import com.mobnova.expense_mgt.model.*;
import com.mobnova.expense_mgt.repositories.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static com.mobnova.expense_mgt.number.NumberUtil.getRandomNumberInRange;

@Getter
public class ExpenseReportTestHelper {

    private List<ExpenseCategory> expenseCategories = new ArrayList<>();
    private List<Currency> currencies = new ArrayList<>();
    private List<Country> countries = new ArrayList<>();
    private List<StateOrProvince> stateOrProvinces = new ArrayList<>();
    private List<City> cities = new ArrayList<>();
    private List<County> counties = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    public ExpenseReportTestHelper() {
        init();
    }

    public void init() {
        expenseCategories.add(ExpenseCategory.builder().code("MEAL").name("Meal").build());
        expenseCategories.add(ExpenseCategory.builder().code("FLIGHT").name("Flight").build());

        currencies.add(Currency.builder().code("BRL").name("Brazilian Real").build());
        currencies.add(Currency.builder().code("USD").name("US Dollar").build());

        Country brCountry = Country.builder().code("BR").name("Brazil").build();
        Country usCountry = Country.builder().code("US").name("United States").build();

        countries.add(brCountry);
        countries.add(usCountry);

        StateOrProvince rsStateOrProvince = StateOrProvince.builder().code("RS").name("Rio Grande do Sul")
                .country(brCountry).build();
        StateOrProvince txStateOrProvince = StateOrProvince.builder().code("TX").name("Texas")
                .country(usCountry).build();

        stateOrProvinces.add(rsStateOrProvince);
        stateOrProvinces.add(txStateOrProvince);

        County poaCounty = County.builder().code("POA").name("Condado de Porto Alegre")
                .stateOrProvince(rsStateOrProvince).build();
        County dallasCounty = County.builder().code("DAL").name("Dallas County")
                .stateOrProvince(rsStateOrProvince).build();

        cities.add(City.builder().code("POA").name("Porto Alegre")
                .stateOrProvince(rsStateOrProvince).county(poaCounty).build());
        cities.add(City.builder().code("DAL").name("Dallas")
                .stateOrProvince(txStateOrProvince).county(dallasCounty).build());

        users.add(User.builder().username("user_one").email("user_one@user.com").firstName("User")
                .lastName("One").password("12345").build());
        users.add(User.builder().username("user_two").email("user_two@user.com").firstName("User")
                .lastName("Two").password("12345").build());
    }

    public List<ExpenseReport> createRandomExpenseReports(Integer expenseReportQuantity, Integer expenseItemQuantity) {
        List<ExpenseReport> expenseReports = new ArrayList<>();
        IntStream.range(0, expenseReportQuantity).forEach(number_ -> {
            expenseReports.add(createRandomExpenseReport(expenseItemQuantity));
        });

        return expenseReports;
    }

    public ExpenseReport createRandomExpenseReport(Integer expenseItemQuantity) {
        ExpenseReport expenseReport = ExpenseReport.builder().referenceID(UUID.randomUUID().toString())
                .tripStartDate(LocalDate.now().minusDays(getRandomNumberInRange(1, 100)))
                .tripEndDate(LocalDate.now().plusDays(getRandomNumberInRange(1, 100)))
                .justification("Justification " + UUID.randomUUID().toString())
                .country(countries.get(getRandomNumberInRange(0, 1)))
                .tripDescription("Trip description " + UUID.randomUUID().toString())
                .expenses(new HashSet<>())
                .user(users.get(getRandomNumberInRange(0, 1))).build();

        IntStream.range(0, expenseItemQuantity).forEach(value -> {
            ExpenseItem expenseItem = ExpenseItem.builder().expenseItemNumber((long) value)
                    .amount(new BigDecimal(getRandomNumberInRange(1, 1000)))
                    .currency(currencies.get(getRandomNumberInRange(0, 1)))
                    .expenseCity(cities.get(getRandomNumberInRange(0, 1)))
                    .category(expenseCategories.get(getRandomNumberInRange(0, 1)))
                    .expenseDate(LocalDate.now().plusDays(getRandomNumberInRange(1, 100))).build();

            expenseReport.getExpenses().add(expenseItem);

            //expenseReport.addExpense(expenseItem);
        });

        return expenseReport;
    }
}
