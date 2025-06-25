package com.tilem.flashcards.repository;

import com.tilem.flashcards.entity.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
    List<Flashcard> findByDeckId(Long deckId);
}