package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.entity.DbEntity;
import com.tilem.flashcards.repository.GenericRepository;
import com.tilem.flashcards.service.GenericService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericServiceImpl<T extends DbEntity, U> implements GenericService<T, U> {

    protected final GenericRepository<T> repository;

    public GenericServiceImpl(GenericRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<U> findAll() {
        return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public T findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public U create(U dto) {
        T entity = mapToEntity(dto);
        return mapToDto(repository.save(entity));
    }

    @Override
    @Transactional
    public U update(Long id, U dto) {
        T entity = repository.findById(id).orElseThrow();
        updateEntity(entity, dto);
        return mapToDto(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    protected abstract U mapToDto(T entity);

    protected abstract T mapToEntity(U dto);

    protected abstract void updateEntity(T entity, U dto);
}
