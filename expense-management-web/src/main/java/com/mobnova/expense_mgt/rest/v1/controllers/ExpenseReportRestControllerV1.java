package com.mobnova.expense_mgt.rest.v1.controllers;

import com.mobnova.expense_mgt.model.ExpenseReport;
import com.mobnova.expense_mgt.rest.v1.dto.ExpenseReportDtoV1;
import com.mobnova.expense_mgt.services.ExpenseReportService;
import lombok.RequiredArgsConstructor;
import org.mockito.internal.util.collections.Sets;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

import static com.mobnova.expense_mgt.ApplicationConstants.REST_API_V1_BASEPATH;

@RestController
@RequestMapping(REST_API_V1_BASEPATH + "/expense-reports")
@RequiredArgsConstructor
public class ExpenseReportRestControllerV1 {

    private final ExpenseReportService expenseReportService;
    private final ModelMapper globalMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Set<ExpenseReportDtoV1> search(String search){
        Set<ExpenseReport> expenseReports = expenseReportService.search(search);
        return expenseReports.stream().map(expenseReport -> globalMapper.map(expenseReport, ExpenseReportDtoV1.class))
                .collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ExpenseReportDtoV1 findById(@PathVariable Long id){
        ExpenseReport expenseReport = expenseReportService.findById(id);
        if(expenseReport != null){
            return globalMapper.map(expenseReport, ExpenseReportDtoV1.class);
        }
        else {
            return null;
        }
    }

    @GetMapping("/byRefID/{referenceID}")
    @ResponseStatus(HttpStatus.OK)
    public ExpenseReportDtoV1 findByReferenceId(@PathVariable String referenceID){
        ExpenseReport expenseReport = expenseReportService.findByReferenceID(referenceID);
        if(expenseReport != null){
            return globalMapper.map(expenseReport, ExpenseReportDtoV1.class);
        }
        else {
            return null;
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseReportDtoV1 create(@RequestBody ExpenseReportDtoV1 expenseReportDtoV1){
        ExpenseReport newExpenseReport = globalMapper.map(expenseReportDtoV1, ExpenseReport.class);
        newExpenseReport = expenseReportService.save(newExpenseReport);
        return globalMapper.map(newExpenseReport, ExpenseReportDtoV1.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ExpenseReportDtoV1 update(@RequestBody ExpenseReportDtoV1 expenseReportDtoV1, @PathVariable Long id){
        ExpenseReport newExpenseReport = globalMapper.map(expenseReportDtoV1, ExpenseReport.class);
        newExpenseReport.setId(id);
        newExpenseReport = expenseReportService.save(newExpenseReport);
        return globalMapper.map(newExpenseReport, ExpenseReportDtoV1.class);
    }

    @PostMapping("/bulk")
    @ResponseStatus(HttpStatus.CREATED)
    public Set<String> saveBulk(@RequestBody Set<ExpenseReportDtoV1> expenseReportsDtoV1Set){
        Set<ExpenseReport> expenseReportSet = Sets.newSet(globalMapper.map(expenseReportsDtoV1Set.toArray(), ExpenseReport[].class));
        expenseReportSet = expenseReportService.saveBulk(expenseReportSet);
        return expenseReportSet.stream().map(ExpenseReport::getReferenceID).collect(Collectors.toSet());
    }


}
