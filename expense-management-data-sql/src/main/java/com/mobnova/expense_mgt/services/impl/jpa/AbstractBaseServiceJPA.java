package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.dto.v1.BaseDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.BaseEntity;
import com.mobnova.expense_mgt.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AbstractBaseServiceJPA<E extends BaseEntity, D extends BaseDto, ID> implements BaseService<D, ID> {

    protected final JpaRepository<E, ID> repository;
    protected final ModelMapper modelMapper;
    protected final Class<E> entityClass;
    protected final Class<D> dtoClass;

    @Override
    public D save(D object) {
        E objectEntity = modelMapper.map(object, entityClass);

        E savedEntity = repository.save(objectEntity);

        return modelMapper.map(findById((ID) savedEntity.getId()), dtoClass);
    }

    public Set<D> saveBulk(Set<D> objects) {
        return objects.stream().map(this::save).collect(Collectors.toSet());
    }

    @Override
    public D findById(ID id) {
        Optional<E> entityById = repository.findById(id);

        return entityById.map(e -> modelMapper.map(e, dtoClass))
                .orElseThrow(() -> new DataNotFoundException(entityClass, Fields.ID, id));
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }
}
