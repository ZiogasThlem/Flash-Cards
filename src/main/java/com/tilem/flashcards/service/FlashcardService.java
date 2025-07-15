package com.tilem.flashcards.service;

import com.tilem.flashcards.data.dto.FlashcardDTO;
import com.tilem.flashcards.data.entity.Flashcard;

import java.util.List;

public interface FlashcardService extends GenericService<Flashcard, FlashcardDTO> {
	List<FlashcardDTO> getFlashcardsByDeck(Long deckId);

	void recordFlashcardReview(Long flashcardId, Long userId, int quality);

	List<FlashcardDTO> importFlashcardsFromFile(String fileContent);
}