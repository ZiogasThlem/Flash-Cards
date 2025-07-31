package com.tilem.flashcards.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tilem.flashcards.data.enums.YesNo;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FlashcardDTO(
        Long id,
        @NotNull(message = "Deck ID is mandatory")
        Long deckId,
        PromptDTO prompt,
        YesNo hasImageData
) {
}