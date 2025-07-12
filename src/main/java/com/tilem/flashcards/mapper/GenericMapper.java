package com.tilem.flashcards.mapper;

import com.tilem.flashcards.data.entity.DbEntity;
import org.mapstruct.MappingTarget;

public interface GenericMapper<T extends DbEntity, U> {
	U toDto(T entity);

	T toEntity(U dto);

	void updateEntity(U dto, @MappingTarget T entity);
}