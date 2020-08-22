package com.mobnova.expense_mgt.util;

import com.mobnova.expense_mgt.model.Currency;
import com.mobnova.expense_mgt.model.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.LongStream;

import static com.mobnova.expense_mgt.number.NumberUtil.getRandomNumberInRange;

@Getter
public class ExpenseReportTestHelper {

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

    public ExpenseReportTestHelper() {
        init();
    }

    public void init() {
        country = Country.builder().code("BR").build();
        stateOrProvince = StateOrProvince.builder().code("RS").country(country).build();
        county = County.builder().code("POA").build();
        city = City.builder().code("POA").stateOrProvince(stateOrProvince).county(county).build();
        user = User.builder().username("user_one").build();
        expenseCategory = ExpenseCategory.builder().code("MEAL").build();
        currency = Currency.builder().code("BRL").build();
        segmentTypeCC = SegmentType.builder().code("CC").order(4L).build();
        segmentTypeAC = SegmentType.builder().code("NA").order(5L).build();
        segmentValuePairCC = SegmentValuePair.builder().segmentValue("1000").segmentType(segmentTypeCC).build();
        segmentValuePairAC = SegmentValuePair.builder().segmentValue("5000").segmentType(segmentTypeAC).build();
    }

    public Set<ExpenseReport> createDummyExpenseReports(Long expenseReportQuantity, Long expenseItemQuantity, Boolean withIds) {
        Set<ExpenseReport> expenseReports = new HashSet<ExpenseReport>();
        LongStream.range(0, expenseReportQuantity).forEach(number_ -> {
            Long id = withIds ? number_ + 1 : null;
            expenseReports.add(createDummyExpenseReport(id, expenseItemQuantity));
        });

        return expenseReports;
    }

    public ExpenseReport createDummyExpenseReport(Long id, Long expenseItemQuantity) {
        ExpenseReport expenseReport = ExpenseReport.builder().id(id).referenceID(UUID.randomUUID().toString())
                .tripStartDate(LocalDate.now().minusDays(getRandomNumberInRange(1, 100)))
                .tripEndDate(LocalDate.now().plusDays(getRandomNumberInRange(1, 100)))
                .justification("Justification " + UUID.randomUUID().toString())
                .country(country)
                .tripDescription("Trip description " + UUID.randomUUID().toString())
                .expenses(new HashSet<>())
                .user(user).build();

        LongStream.range(0, expenseItemQuantity).forEach(value -> {
            List<SegmentValuePair> segmentValuePairs = new ArrayList<>();
            segmentValuePairs.add(segmentValuePairCC);
            segmentValuePairs.add(segmentValuePairAC);

            ExpenseItem expenseItem = ExpenseItem.builder().expenseItemNumber(value + 1L)
                    .amount(new BigDecimal(1000))
                    .currency(currency)
                    .expenseCity(city)
                    .category(expenseCategory)
                    .segmentValuePairs(segmentValuePairs)
                    .expenseDate(LocalDate.now().plusDays(getRandomNumberInRange(1, 100))).build();

            expenseReport.getExpenses().add(expenseItem);
        });

        return expenseReport;
    }
}
