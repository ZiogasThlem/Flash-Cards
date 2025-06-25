package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.dto.DeckDTO;
import com.tilem.flashcards.entity.Deck;
import com.tilem.flashcards.repository.DeckRepository;
import com.tilem.flashcards.repository.UserRepository;
import com.tilem.flashcards.service.DeckService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeckServiceImpl implements DeckService {

    private final DeckRepository deckRepo;
    private final UserRepository userRepo;

    public DeckServiceImpl(DeckRepository deckRepo, UserRepository userRepo) {
        this.deckRepo = deckRepo;
        this.userRepo = userRepo;
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
        deck.setUser(userRepo.findById(dto.getUserId()).orElseThrow());
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

    @Override
    public List<DeckDTO> getDecksByUser(Long userId) {
        return deckRepo.findByUserId(userId).stream().map(this::map).collect(Collectors.toList());
    }

    private DeckDTO map(Deck deck) {
        return DeckDTO.builder()
                .id(deck.getId())
                .name(deck.getName())
                .userId(deck.getUser().getId())
                .build();
    }
}
