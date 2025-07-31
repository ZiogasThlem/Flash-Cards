package com.tilem.flashcards.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDTO(
        Long id,
        @NotBlank(message = "Username is mandatory")
        String username,
        String password,
        List<DeckDTO> decks
) {
}