package com.tilem.flashcards.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tilem.flashcards.data.enums.YesNo;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GenParamDTO(
		Long id,
		String value,
		YesNo mandatory,
		String notes
) {
}