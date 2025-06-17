package com.tilem.flashcards.repository;

import com.tilem.flashcards.entity.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {}