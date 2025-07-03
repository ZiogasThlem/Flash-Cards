package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.entity.DbEntity;
import com.tilem.flashcards.repository.GenericRepository;
import com.tilem.flashcards.service.GenericService;
import com.tilem.flashcards.util.LogWrapper;
import java.lang.invoke.MethodHandles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericServiceImpl<T extends DbEntity, U> implements GenericService<T, U> {

    protected final GenericRepository<T> repository;
    protected final LogWrapper log = LogWrapper.getLogger(MethodHandles.lookup().lookupClass());

    public GenericServiceImpl(GenericRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<U> findAll() {
        log.info("Fetching all entities.");
        List<U> entities = repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
        log.info("Found " + entities.size() + " entities.");
        return entities;
    }

    @Override
    @Transactional(readOnly = true)
    public T findById(Long id) {
        log.info("Fetching entity by ID: " + id);
        try {
            T entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Entity not found with ID: " + id));
            log.info("Found entity with ID: " + id);
            return entity;
        } catch (RuntimeException e) {
            log.error("Error finding entity with ID: " + id, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public U create(U dto) {
        log.info("Creating new entity.");
        try {
            T entity = mapToEntity(dto);
            U createdDto = mapToDto(repository.save(entity));
            log.info("Successfully created entity with ID: " + entity.getUniqueID());
            return createdDto;
        } catch (Exception e) {
            log.error("Error creating entity.", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public U update(Long id, U dto) {
        log.info("Updating entity with ID: " + id);
        try {
            T entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Entity not found with ID: " + id));
            updateEntity(entity, dto);
            U updatedDto = mapToDto(repository.save(entity));
            log.info("Successfully updated entity with ID: " + id);
            return updatedDto;
        } catch (RuntimeException e) {
            log.error("Error updating entity with ID: " + id, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting entity with ID: " + id);
        try {
            repository.deleteById(id);
            log.info("Successfully deleted entity with ID: " + id);
        } catch (Exception e) {
            log.error("Error deleting entity with ID: " + id, e);
            throw e;
        }
    }

    protected abstract U mapToDto(T entity);

    protected abstract T mapToEntity(U dto);

    protected abstract void updateEntity(T entity, U dto);
}
