package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.mapper.BaseMapper;
import com.mobnova.expense_mgt.model.BaseEntity;
import com.mobnova.expense_mgt.services.dto.BaseService;
import com.mobnova.expense_mgt.validation.BeanValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class BaseAbstractServiceJPA<E extends BaseEntity, D, ID> implements BaseService<D, ID> {

    protected final JpaRepository<E, ID> mainRepository;
    protected final BaseMapper<E, D> baseMapper;
    protected final BeanValidator beanValidator;
    protected final Class mainObjectClass;

    @Override
    public D save(D object) {
        E objectEntity = baseMapper.toEntity(object);
        beanValidator.validateObject(objectEntity);

        E savedEntity = mainRepository.save(objectEntity);

        return baseMapper.toDto(objectEntity);
    }

    public Set<D> saveBulk(Set<D> objects) {
        beanValidator.validateObjects(objects);
        return objects.stream().map(this::save).collect(Collectors.toSet());
    }

    @Override
    public D findById(ID id) {
        Optional<E> entityById = mainRepository.findById(id);

        if(entityById.isEmpty()){
            throw new RuntimeException("Entity with id " + mainObjectClass.getName() + " not found");
        }
        else{
            return baseMapper.toDto(entityById.get());
        }
    }

    @Override
    public void deleteById(ID id) {
        mainRepository.deleteById(id);
    }
}
