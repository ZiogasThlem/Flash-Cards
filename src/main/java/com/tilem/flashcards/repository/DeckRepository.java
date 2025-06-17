package com.tilem.flashcards.repository;

import com.tilem.flashcards.entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<Deck, Long> {}