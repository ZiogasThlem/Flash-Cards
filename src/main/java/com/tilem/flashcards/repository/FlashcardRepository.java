package com.tilem.flashcards.repository;

import com.tilem.flashcards.data.entity.Flashcard;

import java.util.List;

public interface FlashcardRepository extends GenericRepository<Flashcard> {
	List<Flashcard> findByDeckId(Long deckId);
}
