package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.DeckDTO;
import com.tilem.flashcards.data.entity.Deck;
import com.tilem.flashcards.data.entity.Flashcard;
import com.tilem.flashcards.data.entity.User;
import com.tilem.flashcards.repository.DeckRepository;
import com.tilem.flashcards.service.DeckService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class DeckServiceImpl extends GenericServiceImpl<Deck, DeckDTO> implements DeckService {

    public DeckServiceImpl(DeckRepository deckRepo) {
        super(deckRepo);
    }

    @Override
    protected DeckDTO mapToDto(Deck entity) {
        return DeckDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .users(entity.getUsers().stream().map(User::getDetailedLabel).collect(Collectors.toList()))
                .flashcards(entity.getFlashcards().stream().map(Flashcard::getSimpleLabel).collect(Collectors.toList()))
                .build();
    }

    @Override
    protected Deck mapToEntity(DeckDTO dto) {
        Deck deck = new Deck();
        deck.setName(dto.getName());
        return deck;
    }

    @Override
    protected void updateEntity(Deck entity, DeckDTO dto) {
        entity.setName(dto.getName());
    }
}
