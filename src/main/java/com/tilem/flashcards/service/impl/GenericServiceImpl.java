package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.entity.DbEntity;
import com.tilem.flashcards.mapper.GenericMapper;
import com.tilem.flashcards.repository.GenericRepository;
import com.tilem.flashcards.service.GenericService;
import com.tilem.flashcards.util.LogWrapper;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericServiceImpl<T extends DbEntity, U, R extends GenericRepository<T>>
		implements GenericService<T, U> {

	protected final R repository;
	protected final LogWrapper log = LogWrapper.getLogger(MethodHandles.lookup().lookupClass());
	protected final GenericMapper<T, U> mapper;
	protected final Class<T> entityClass;

	public GenericServiceImpl(R repository, GenericMapper<T, U> mapper, Class<T> entityClass) {
		this.entityClass = entityClass;
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

	@Override
	@Transactional(readOnly = true)
	public List<U> findAll() {
		log.info("Fetching all " + entityClass.getSimpleName() + " entities.");
		List<U> entities =
				repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
		log.info("Found " + entities.size() + " " + entityClass.getSimpleName() + " entities.");
		return entities;
	}

	@Override
	@Transactional(readOnly = true)
	public U findById(Long id) {
		log.info("Fetching " + entityClass.getSimpleName() + " by ID: " + id);
		try {
			T entity =
					repository
							.findById(id)
							.orElseThrow(() -> new RuntimeException("Entity not found with ID: " + id));
			log.info("Found " + entityClass.getSimpleName() + " with ID: " + id);
			return mapper.toDto(entity);
		} catch (RuntimeException e) {
			log.error("Error finding " + entityClass.getSimpleName() + " with ID: " + id, e);
			throw e;
		}
	}

	@Override
	@Transactional
	public U create(U dto) {
		log.info("Creating new " + entityClass.getSimpleName() + ".");
		try {
			T entity = mapper.toEntity(dto);
			U createdDto = mapper.toDto(repository.save(entity));
			log.info("Successfully created " + entityClass.getSimpleName() + " with ID: " + entity.getUniqueID());
			return createdDto;
		} catch (Exception e) {
			log.error("Error creating " + entityClass.getSimpleName() + ".", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public U update(Long id, U dto) {
		log.info("Updating " + entityClass.getSimpleName() + " with ID: " + id);
		try {
			T entity =
					repository
							.findById(id)
							.orElseThrow(() -> new RuntimeException("Entity not found with ID: " + id));
			mapper.updateEntity(dto, entity);
			U updatedDto = mapper.toDto(repository.save(entity));
			log.info("Successfully updated " + entityClass.getSimpleName() + " with ID: " + id);
			return updatedDto;
		} catch (RuntimeException e) {
			log.error("Error updating " + entityClass.getSimpleName() + " with ID: " + id, e);
			throw e;
		}
	}

	@Override
	@Transactional
	public void delete(Long id) {
		log.info("Deleting " + entityClass.getSimpleName() + " with ID: " + id);
		try {
			repository.deleteById(id);
			log.info("Successfully deleted " + entityClass.getSimpleName() + " with ID: " + id);
		} catch (Exception e) {
			log.error("Error deleting " + entityClass.getSimpleName() + " with ID: " + id, e);
			throw e;
		}
	}
}
