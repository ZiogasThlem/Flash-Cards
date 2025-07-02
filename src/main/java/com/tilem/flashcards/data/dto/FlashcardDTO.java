package com.tilem.flashcards.data.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FlashcardDTO {
    public Long id;
    public PromptDTO prompt;
    public String hasManyCorrectAnswers;
    public String hasImageData;
    public BlobDataDTO imageData;
    public Long deckId;
}