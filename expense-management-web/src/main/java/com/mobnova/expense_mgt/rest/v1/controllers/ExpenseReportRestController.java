package com.mobnova.expense_mgt.rest.v1.controllers;

import com.mobnova.expense_mgt.dto.v1.ExpenseReportDto;
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
public class ExpenseReportRestController {

    private final ExpenseReportService expenseReportService;
    private final ModelMapper globalMapper;

    @GetMapping
    public ResponseEntity<Set<ExpenseReportDto>> search(@RequestParam String search){
        return ResponseEntity.ok(expenseReportService.search(search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseReportDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(expenseReportService.findById(id));
    }

    @GetMapping("/byRefID/{referenceID}")
    public ResponseEntity<ExpenseReportDto> findByReferenceId(@PathVariable String referenceID){
        return ResponseEntity.ok(expenseReportService.findByReferenceID(referenceID));
    }

    @PostMapping
    public ResponseEntity<ExpenseReportDto> create(@RequestBody @Valid ExpenseReportDto expenseReportDto){
        return new ResponseEntity(expenseReportService.save(expenseReportDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseReportDto> update(@RequestBody @Valid ExpenseReportDto expenseReportDto, @PathVariable Long id){
        ExpenseReportDto expenseReportDtoById = expenseReportService.findById(id);

        expenseReportDtoById.setId(expenseReportDtoById.getId());
        expenseReportDtoById.setVersion(expenseReportDtoById.getVersion());

        return new ResponseEntity(expenseReportService.save(expenseReportDtoById), HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    public ResponseEntity<Set<String>> saveBulk(@RequestBody Set<ExpenseReportDto> expenseReportDtos){
        return new ResponseEntity(expenseReportService.saveBulk(expenseReportDtos).stream().map(ExpenseReportDto::getReferenceID).collect(Collectors.toSet()), HttpStatus.CREATED);
    }
}