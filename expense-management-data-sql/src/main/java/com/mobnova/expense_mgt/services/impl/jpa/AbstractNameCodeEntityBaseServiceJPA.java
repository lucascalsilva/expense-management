package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.dto.v1.BaseDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.BaseEntity;
import com.mobnova.expense_mgt.repositories.NameCodeBaseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public abstract class AbstractNameCodeEntityBaseServiceJPA<E extends BaseEntity, D extends BaseDto, ID> extends AbstractBaseServiceJPA<E, D, ID> {

    private final NameCodeBaseRepository<E> repository;

    public AbstractNameCodeEntityBaseServiceJPA(NameCodeBaseRepository<E> repository, ModelMapper modelMapper, Class<E> entityClass, Class<D> dtoClass) {
        super((JpaRepository<E, ID>) repository, modelMapper, entityClass, dtoClass);
        this.repository = repository;
    }

    public D findByCode(String code) {
        Optional<E> countryByCode = repository.findByCode(code);
        return countryByCode.map(e -> modelMapper.map(e, dtoClass))
                .orElseThrow(() -> new DataNotFoundException(entityClass, Fields.CODE, code));
    }

}
