package com.tilem.flashcards.mapper;

import com.tilem.flashcards.data.dto.GenParamDTO;
import com.tilem.flashcards.data.entity.GenParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
		componentModel = "spring",
		uses = {YesNoMapper.class})
public interface GenParamMapper extends GenericMapper<GenParam, GenParamDTO> {

	GenParamDTO toDto(GenParam genParam);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "tempUuid", ignore = true)
	@Mapping(target = "tempUniqueID", ignore = true)
	GenParam toEntity(GenParamDTO genParamDTO);

	@Mapping(target = "tempUuid", ignore = true)
	@Mapping(target = "tempUniqueID", ignore = true)
	void updateEntity(GenParamDTO genParamDTO, @MappingTarget GenParam genParam);
}