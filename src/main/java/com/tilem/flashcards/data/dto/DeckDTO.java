package com.tilem.flashcards.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tilem.flashcards.data.enums.DeckCategory;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DeckDTO(
		Long id,
		@NotBlank(message = "Name is mandatory")
		String name,
		String description,
		DeckCategory category,
		List<String> flashcards
) {
}