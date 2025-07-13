package com.tilem.flashcards.mapper;

import com.tilem.flashcards.data.entity.DbEntity;
import org.mapstruct.MappingTarget;

import org.mapstruct.Mapping;

public interface GenericMapper<T extends DbEntity, U> {
	U toDto(T entity);

	@Mapping(target = "tempUuid", ignore = true)
	@Mapping(target = "tempUniqueID", ignore = true)
	T toEntity(U dto);

	@Mapping(target = "tempUuid", ignore = true)
	@Mapping(target = "tempUniqueID", ignore = true)
	void updateEntity(U dto, @MappingTarget T entity);
}