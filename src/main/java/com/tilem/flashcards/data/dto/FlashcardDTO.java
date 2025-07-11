package com.tilem.flashcards.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tilem.flashcards.data.enums.YesNo;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlashcardDTO {

    private Long id;

    @NotNull(message = "Deck ID is mandatory")
    private Long deckId;

    private PromptDTO prompt;
    private YesNo hasImageData;
}
