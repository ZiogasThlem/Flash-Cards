package com.tilem.flashcards.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tilem.flashcards.data.enums.YesNo;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDTO(
        Long id,
        @NotBlank(message = "Username is mandatory")
        String username,
        String password,
        YesNo isActive,
        LocalDateTime lastLogin,
        String firstname,
        String lastname,
        String email,
        List<DeckDTO> decks
) {
}