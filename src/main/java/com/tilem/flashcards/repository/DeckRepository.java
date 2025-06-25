package com.tilem.flashcards.repository;

import com.tilem.flashcards.entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeckRepository extends JpaRepository<Deck, Long> {
    List<Deck> findByUserId(Long userId);
}