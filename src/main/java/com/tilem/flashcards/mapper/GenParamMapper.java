package com.tilem.flashcards.mapper;

import com.tilem.flashcards.data.dto.GenParamDTO;
import com.tilem.flashcards.data.entity.GenParam;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
		componentModel = "spring",
		uses = {YesNoMapper.class})
public interface GenParamMapper extends GenericMapper<GenParam, GenParamDTO> {

	GenParamDTO toDto(GenParam genParam);

	GenParam toEntity(GenParamDTO genParamDTO);

	void updateEntity(GenParamDTO genParamDTO, @MappingTarget GenParam genParam);
}