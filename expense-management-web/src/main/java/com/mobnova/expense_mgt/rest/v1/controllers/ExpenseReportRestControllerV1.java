package com.mobnova.expense_mgt.rest.v1.controllers;

import com.mobnova.expense_mgt.model.ExpenseReport;
import com.mobnova.expense_mgt.rest.v1.dto.ExpenseReportDtoV1;
import com.mobnova.expense_mgt.services.ExpenseReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mobnova.expense_mgt.ApplicationConstants.REST_API_V1_BASEPATH;

@RestController
@RequestMapping(REST_API_V1_BASEPATH + "/expense-reports")
@RequiredArgsConstructor
@Slf4j
public class ExpenseReportRestControllerV1 {

    private final ExpenseReportService expenseReportService;
    private final ModelMapper globalMapper;

    @GetMapping
    public ResponseEntity<Set<ExpenseReportDtoV1>> search(@RequestParam String search){
        Set<ExpenseReport> expenseReports = expenseReportService.search(search);
        return ResponseEntity.ok(expenseReports.stream().map(expenseReport -> globalMapper.map(expenseReport, ExpenseReportDtoV1.class))
                .collect(Collectors.toSet()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseReportDtoV1> findById(@PathVariable Long id){
        ExpenseReport expenseReport = expenseReportService.findById(id);
        ExpenseReportDtoV1 expenseReportDtoV1 = globalMapper.map(expenseReport, ExpenseReportDtoV1.class);
        return ResponseEntity.ok(expenseReportDtoV1);
    }

    @GetMapping("/byRefID/{referenceID}")
    public ResponseEntity<ExpenseReportDtoV1> findByReferenceId(@PathVariable String referenceID){
        ExpenseReport expenseReport = expenseReportService.findByReferenceID(referenceID);
        ExpenseReportDtoV1 expenseReportDtoV1 = globalMapper.map(expenseReport, ExpenseReportDtoV1.class);
        return ResponseEntity.ok(expenseReportDtoV1);
    }

    @PostMapping
    public ResponseEntity<ExpenseReportDtoV1> create(@RequestBody @Valid ExpenseReportDtoV1 expenseReportDtoV1){
        ExpenseReport newExpenseReport = globalMapper.map(expenseReportDtoV1, ExpenseReport.class);
        newExpenseReport = expenseReportService.save(newExpenseReport);
        return new ResponseEntity(globalMapper.map(newExpenseReport, ExpenseReportDtoV1.class), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseReportDtoV1> update(@RequestBody @Valid ExpenseReportDtoV1 expenseReportDtoV1, @PathVariable Long id){
        ExpenseReport expenseReportById = expenseReportService.findById(id);

        ExpenseReport updateExpenseReport = globalMapper.map(expenseReportDtoV1, ExpenseReport.class);
        updateExpenseReport.setId(expenseReportById.getId());
        updateExpenseReport.setVersion(expenseReportById.getVersion());

        updateExpenseReport = expenseReportService.save(updateExpenseReport);
        return new ResponseEntity(globalMapper.map(updateExpenseReport, ExpenseReportDtoV1.class), HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    public ResponseEntity<Set<String>> saveBulk(@RequestBody Set<ExpenseReportDtoV1> expenseReportsDtoV1Set){
        Set<ExpenseReport> expenseReportSet = Set.of(globalMapper.map(expenseReportsDtoV1Set.toArray(), ExpenseReport[].class));
        expenseReportSet = expenseReportService.saveBulk(expenseReportSet);
        return new ResponseEntity(expenseReportSet.stream().map(ExpenseReport::getReferenceID).collect(Collectors.toSet()), HttpStatus.CREATED);
    }
}