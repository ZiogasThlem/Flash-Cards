package com.tilem.flashcards.service;

import com.tilem.flashcards.dto.DeckDTO;

import java.util.List;

public interface DeckService {
    List<DeckDTO> getAllDecks();
    DeckDTO getDeckById(Long id);
    DeckDTO createDeck(DeckDTO dto);
    DeckDTO updateDeck(Long id, DeckDTO dto);
    void deleteDeck(Long id);
    List<DeckDTO> getDecksByUser(Long userId);
}
