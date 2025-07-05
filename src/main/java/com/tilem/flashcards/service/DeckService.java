package com.tilem.flashcards.service;

import com.tilem.flashcards.data.dto.DeckDTO;
import com.tilem.flashcards.data.entity.Deck;

import java.util.List;

public interface DeckService extends GenericService<Deck, DeckDTO> {
    List<DeckDTO> importDecksFromFile(String fileContent);
}
