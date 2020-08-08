package com.mobnova.expense_mgt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

@NoRepositoryBean
public interface SearchRepository<T> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

}
