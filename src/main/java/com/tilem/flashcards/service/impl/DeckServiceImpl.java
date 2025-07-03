package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.DeckDTO;
import com.tilem.flashcards.data.entity.Deck;
import com.tilem.flashcards.data.entity.Flashcard;
import com.tilem.flashcards.data.entity.User;
import com.tilem.flashcards.repository.DeckRepository;
import com.tilem.flashcards.service.DeckService;
import com.tilem.flashcards.util.LogWrapper;
import java.lang.invoke.MethodHandles;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class DeckServiceImpl extends GenericServiceImpl<Deck, DeckDTO> implements DeckService {

    private static final LogWrapper log = LogWrapper.getLogger(MethodHandles.lookup().lookupClass());

    public DeckServiceImpl(DeckRepository deckRepo) {
        super(deckRepo);
    }

    @Override
    protected DeckDTO mapToDto(Deck entity) {
        log.info("Mapping Deck entity to DTO for entity ID: " + entity.getId());
        DeckDTO deckDTO = DeckDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .users(entity.getUsers().stream().map(User::getDetailedLabel).collect(Collectors.toList()))
                .flashcards(entity.getFlashcards().stream().map(Flashcard::getSimpleLabel).collect(Collectors.toList()))
                .build();
        log.info("Successfully mapped Deck entity to DTO for entity ID: " + entity.getId());
        return deckDTO;
    }

    @Override
    protected Deck mapToEntity(DeckDTO dto) {
        log.info("Mapping DeckDTO to entity for DTO name: " + dto.getName());
        Deck deck = new Deck();
        deck.setName(dto.getName());
        log.info("Successfully mapped DeckDTO to entity for DTO name: " + dto.getName());
        return deck;
    }

    @Override
    protected void updateEntity(Deck entity, DeckDTO dto) {
        log.info("Updating Deck entity with ID: " + entity.getId());
        entity.setName(dto.getName());
        log.info("Successfully updated Deck entity with ID: " + entity.getId());
    }
}
