package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.model.*;
import com.mobnova.expense_mgt.util.DemoDataGenerator;
import com.mobnova.expense_mgt.util.DemoDataPersistence;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ExpenseReportServiceJPAImplIT {

    @Autowired
    private ExpenseReportServiceJPAImpl expenseReportServiceJPA;

    @Autowired
    private DemoDataPersistence demoDataPersistence;

    private DemoDataGenerator demoDataGenerator;

    private Integer expenseReportQuantity = 3;
    private Integer expenseItemQuantity = 3;

    private static Boolean baseDataPersisted = false;

    @BeforeEach
    public void init(){
        demoDataGenerator = new DemoDataGenerator();
        if(!baseDataPersisted) {
            demoDataPersistence.persistAllBaseData(demoDataGenerator);
            baseDataPersisted = true;
        }
    }

    @Test
    void save() {
        ExpenseReport expenseReport = demoDataGenerator.createRandomExpenseReport(expenseItemQuantity);
        ExpenseReport savedExpenseReport = expenseReportServiceJPA.save(expenseReport);
        assertThat(savedExpenseReport.getId()).isNotNull();
        assertThat(savedExpenseReport.getCreationDate()).isNotNull();
    }

    @Test
    void saveBulk() {
        List<ExpenseReport> randomExpenseReports = demoDataGenerator
                .createRandomExpenseReports(expenseReportQuantity, expenseItemQuantity);

        Set<ExpenseReport> savedExpenseReports = expenseReportServiceJPA.saveBulk(new HashSet<>(randomExpenseReports));
        for(ExpenseReport expenseReport : savedExpenseReports){
            assertThat(expenseReport.getId()).isNotNull();
        }

        List<String> randomExpenseReportsReferenceIds = savedExpenseReports.stream()
                .map(ExpenseReport::getReferenceID).collect(Collectors.toList());
        List<String> savedExpenseReportsReferenceIds = savedExpenseReports.stream()
                .map(ExpenseReport::getReferenceID).collect(Collectors.toList());

        assertThat(randomExpenseReportsReferenceIds).contains(savedExpenseReportsReferenceIds.toArray(new String[0]));
    }

    @Test
    void searchWithLikeByJustification() {
        List<ExpenseReport> randomExpenseReports = demoDataGenerator
                .createRandomExpenseReports(expenseReportQuantity, expenseItemQuantity);

        Set<ExpenseReport> savedExpenseReports = expenseReportServiceJPA.saveBulk(new HashSet<>(randomExpenseReports));

        String queryReferenceID = savedExpenseReports.stream().findFirst().get().getJustification()
                .replace("Justification ", "");

        Set<ExpenseReport> searchedExpenseReports = expenseReportServiceJPA.search("justification:"+queryReferenceID);

        assertThat(searchedExpenseReports).isNotEmpty();
    }

    @Test
    void findById() {
        ExpenseReport expenseReport = demoDataGenerator.createRandomExpenseReport(expenseItemQuantity);
        ExpenseReport savedExpenseReport = expenseReportServiceJPA.save(expenseReport);

        Long expenseReportId = savedExpenseReport.getId();

        Optional<ExpenseReport> expenseReportById = expenseReportServiceJPA.findById(expenseReportId);

        assertThat(expenseReportById).isPresent();
        assertThat(expenseReportById.get()).isEqualTo(savedExpenseReport);
    }

    @Test
    void deleteById() {
        ExpenseReport savedExpenseReport = expenseReportServiceJPA.save(demoDataGenerator
                .createRandomExpenseReport(expenseItemQuantity));

        Long expenseReportId = savedExpenseReport.getId();

        assertThat(expenseReportServiceJPA.findById(expenseReportId)).isPresent();

        expenseReportServiceJPA.deleteById(expenseReportId);

        assertThat(expenseReportServiceJPA.findById(expenseReportId)).isNotPresent();
    }

    @Test
    void findByReferenceID() {
        ExpenseReport savedExpenseReport = expenseReportServiceJPA.save(demoDataGenerator
                .createRandomExpenseReport(expenseItemQuantity));
        String expenseReportReferenceID = savedExpenseReport.getReferenceID();

        Optional<ExpenseReport> expenseReportByReferenceID = expenseReportServiceJPA
                .findByReferenceID(expenseReportReferenceID);

        assertThat(expenseReportByReferenceID).isPresent();
        assertThat(expenseReportByReferenceID.get()).isEqualTo(savedExpenseReport);
    }
}