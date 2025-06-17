package com.tilem.flashcards.mapper;

import com.tilem.flashcards.dto.FlashcardDTO;
import com.tilem.flashcards.entity.Flashcard;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class FlashcardMapper {
    public FlashcardDTO toDto(Flashcard flashcard) {
        return FlashcardDTO.builder()
                            .id(flashcard.getId())
                            .question(flashcard.getQuestion())
                            .answer(flashcard.getAnswer())
                            .deckId(flashcard.getDeck().getId())
                            .build();
    }
}