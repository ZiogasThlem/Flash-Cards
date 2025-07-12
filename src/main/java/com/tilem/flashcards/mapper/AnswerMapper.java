package com.tilem.flashcards.mapper;

import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AnswerMapper extends GenericMapper<Answer, AnswerDTO> {

	@Mapping(target = "promptId", source = "prompt.id")
	AnswerDTO toDto(Answer answer);

	@Mapping(target = "prompt", ignore = true)
	Answer toEntity(AnswerDTO answerDTO);

	@Mapping(target = "prompt", ignore = true)
	@Mapping(target = "tempUuid", ignore = true)
	@Mapping(target = "tempUniqueID", ignore = true)
	void updateEntity(AnswerDTO answerDTO, @MappingTarget Answer answer);
}