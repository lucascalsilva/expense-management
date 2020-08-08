package com.mobnova.expense_mgt.repositories;

import com.mobnova.expense_mgt.model.ExpenseReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenseReportRepository extends SearchRepository<ExpenseReport>  {

    Optional<ExpenseReport> findByReferenceID(String referenceID);

}
