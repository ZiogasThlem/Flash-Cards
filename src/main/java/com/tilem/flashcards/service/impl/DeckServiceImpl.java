package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.DeckDTO;
import com.tilem.flashcards.data.entity.Deck;
import com.tilem.flashcards.data.entity.Flashcard;
import com.tilem.flashcards.data.entity.User;
import com.tilem.flashcards.repository.DeckRepository;
import com.tilem.flashcards.repository.UserRepository;
import com.tilem.flashcards.service.DeckService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeckServiceImpl implements DeckService {

    private final DeckRepository deckRepo;

    public DeckServiceImpl(DeckRepository deckRepo, UserRepository userRepo) {
        this.deckRepo = deckRepo;
    }

    @Override
    public List<DeckDTO> getAllDecks() {
        return deckRepo.findAll().stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public DeckDTO getDeckById(Long id) {
        return map(deckRepo.findById(id).orElseThrow());
    }

    @Override
    public DeckDTO createDeck(DeckDTO dto) {
        Deck deck = new Deck();
        deck.setName(dto.getName());
        return map(deckRepo.save(deck));
    }

    @Override
    public DeckDTO updateDeck(Long id, DeckDTO dto) {
        Deck deck = deckRepo.findById(id).orElseThrow();
        deck.setName(dto.getName());
        return map(deckRepo.save(deck));
    }

    @Override
    public void deleteDeck(Long id) {
        deckRepo.deleteById(id);
    }

    private DeckDTO map(Deck deck) {
        return DeckDTO.builder()
                .id (deck.getId())
                .name(deck.getName())
                .users(deck.getUsers().stream().map(User::getDetailedLabel).collect(Collectors.toList()))
                .flashcards(deck.getFlashcards().stream().map(Flashcard::getSimpleLabel).collect(Collectors.toList()))
                .build();
    }
}
