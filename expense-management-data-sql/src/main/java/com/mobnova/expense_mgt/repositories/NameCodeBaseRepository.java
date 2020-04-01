package com.mobnova.expense_mgt.repositories;

import com.mobnova.expense_mgt.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface NameCodeBaseRepository<T> extends JpaRepository<T, Long> {

    Optional<T> findByCode(String code);
}
