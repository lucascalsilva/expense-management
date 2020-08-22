package com.mobnova.expense_mgt.rest.v1.controllers;

import com.mobnova.expense_mgt.dto.v1.CountryDto;
import com.mobnova.expense_mgt.services.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mobnova.expense_mgt.ApplicationConstants.REST_API_V1_BASEPATH;

@RestController
@RequestMapping(REST_API_V1_BASEPATH + "/countries")
@RequiredArgsConstructor
@Slf4j
public class CountryRestController {

    private final CountryService countryService;

    @GetMapping("/{id}")
    public ResponseEntity<CountryDto> findById(@PathVariable Long id){
        return new ResponseEntity<CountryDto>(countryService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CountryDto> create(@RequestBody @Valid CountryDto countryDto){
        return new ResponseEntity<CountryDto>(countryService.save(countryDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryDto> update(@RequestBody @Valid CountryDto countryDto, @PathVariable Long id){
        return new ResponseEntity<CountryDto>(countryService.save(countryService.findById(id)), HttpStatus.OK);
    }

    @PostMapping("/bulk")
    public ResponseEntity<Set<String>> saveBulk(@RequestBody Set<CountryDto> countryDtos){
        return new ResponseEntity<Set<String>>(countryService
                .saveBulk(countryDtos).stream().map(CountryDto::getCode).collect(Collectors.toSet()), HttpStatus.CREATED);
    }
}
