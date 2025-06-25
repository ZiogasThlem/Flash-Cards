package com.tilem.flashcards.service;

import com.tilem.flashcards.dto.FlashcardDTO;
import java.util.List;

public interface FlashcardService {
    List<FlashcardDTO> getAllFlashcards();
    FlashcardDTO getFlashcardById(Long id);
    FlashcardDTO createFlashcard(FlashcardDTO dto);
    FlashcardDTO updateFlashcard(Long id, FlashcardDTO dto);
    void deleteFlashcard(Long id);
    List<FlashcardDTO> getFlashcardsByDeck(Long deckId);
}
