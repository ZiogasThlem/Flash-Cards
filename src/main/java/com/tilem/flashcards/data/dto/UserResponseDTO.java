package com.tilem.flashcards.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponseDTO(
		Long id,
		String username,
		List<DeckDTO> decks
) {
}