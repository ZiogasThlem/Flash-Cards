package com.tilem.flashcards.data.dto;

import com.tilem.flashcards.data.enums.YesNo;

import java.util.List;

public record PromptDTO(
		Long id,
		String promptBody,
		YesNo hasSingleAnswer,
		List<AnswerDTO> answers
) {
}