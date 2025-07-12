package com.tilem.flashcards.mapper;

import com.tilem.flashcards.data.dto.PromptDTO;
import com.tilem.flashcards.data.entity.Prompt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
		componentModel = "spring",
		uses = {AnswerMapper.class})
public interface PromptMapper extends GenericMapper<Prompt, PromptDTO> {

	PromptDTO toDto(Prompt prompt);

	@Mapping(target = "answers", ignore = true)
	Prompt toEntity(PromptDTO promptDTO);

	@Mapping(target = "tempUuid", ignore = true)
	@Mapping(target = "tempUniqueID", ignore = true)
	void updateEntity(PromptDTO promptDTO, @MappingTarget Prompt prompt);
}