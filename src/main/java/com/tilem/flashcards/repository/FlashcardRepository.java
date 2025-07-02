package com.tilem.flashcards.repository;

import com.tilem.flashcards.data.entity.Flashcard;

import java.util.List;

import com.tilem.flashcards.data.enums.YesNo;

public interface FlashcardRepository extends GenericRepository<Flashcard> {
    List<Flashcard> findByDeckId(Long deckId);
    List<Flashcard> findByHasManyCorrectAnswers(YesNo hasManyCorrectAnswers);
}