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
    public String question;
    public String answer;
    public Long deckId;
}