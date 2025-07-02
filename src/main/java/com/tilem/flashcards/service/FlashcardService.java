package com.tilem.flashcards.service;

import com.tilem.flashcards.data.dto.FlashcardDTO;
import com.tilem.flashcards.data.entity.Flashcard;

import java.util.List;

import com.tilem.flashcards.data.enums.YesNo;

public interface FlashcardService extends GenericService<Flashcard, FlashcardDTO> {
    List<FlashcardDTO> getFlashcardsByDeck(Long deckId);
    List<FlashcardDTO> getFlashcardsByHasManyCorrectAnswers(YesNo hasManyCorrectAnswers);
}
