package com.tilem.flashcards.repository;

import com.tilem.flashcards.data.entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<Deck, Long> {
}