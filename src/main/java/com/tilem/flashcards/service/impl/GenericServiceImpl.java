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

	public GenericServiceImpl(R repository, GenericMapper<T, U> mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<U> findAll() {
		log.info("Fetching all entities.");
		List<U> entities =
				repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
		log.info("Found " + entities.size() + " entities.");
		return entities;
	}

	@Override
	@Transactional(readOnly = true)
	public U findById(Long id) {
		log.info("Fetching entity by ID: " + id);
		try {
			T entity =
					repository
							.findById(id)
							.orElseThrow(() -> new RuntimeException("Entity not found with ID: " + id));
			log.info("Found entity with ID: " + id);
			return mapper.toDto(entity);
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
			T entity = mapper.toEntity(dto);
			U createdDto = mapper.toDto(repository.save(entity));
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
			T entity =
					repository
							.findById(id)
							.orElseThrow(() -> new RuntimeException("Entity not found with ID: " + id));
			mapper.updateEntity(dto, entity);
			U updatedDto = mapper.toDto(repository.save(entity));
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
}
