package com.tilem.flashcards.service;

import java.util.List;

public interface GenericService<T, U> {
	Class<T> getEntityClass();

	List<U> findAll();

	U findById(Long id);

	U create(U dto);

	U update(Long id, U dto);

	void delete(Long id);
}