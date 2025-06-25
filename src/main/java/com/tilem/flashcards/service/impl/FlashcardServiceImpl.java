package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.dto.FlashcardDTO;
import com.tilem.flashcards.entity.Deck;
import com.tilem.flashcards.entity.Flashcard;
import com.tilem.flashcards.repository.DeckRepository;
import com.tilem.flashcards.repository.FlashcardRepository;
import com.tilem.flashcards.service.FlashcardService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlashcardServiceImpl implements FlashcardService {

    private final FlashcardRepository flashcardRepo;
    private final DeckRepository deckRepo;

    public FlashcardServiceImpl(FlashcardRepository flashcardRepo, DeckRepository deckRepo) {
        this.flashcardRepo = flashcardRepo;
        this.deckRepo = deckRepo;
    }

    @Override
    public List<FlashcardDTO> getAllFlashcards() {
        return flashcardRepo.findAll().stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public FlashcardDTO getFlashcardById(Long id) {
        return map(flashcardRepo.findById(id).orElseThrow());
    }

    @Override
    public FlashcardDTO createFlashcard(FlashcardDTO dto) {
        Deck deck = deckRepo.findById(dto.getDeckId()).orElseThrow();
        Flashcard flashcard = new Flashcard();
        flashcard.setQuestion(dto.getQuestion());
        flashcard.setAnswer(dto.getAnswer());
        flashcard.setDeck(deck);
        return map(flashcardRepo.save(flashcard));
    }

    @Override
    public FlashcardDTO updateFlashcard(Long id, FlashcardDTO dto) {
        Flashcard flashcard = flashcardRepo.findById(id).orElseThrow();
        flashcard.setQuestion(dto.getQuestion());
        flashcard.setAnswer(dto.getAnswer());
        return map(flashcardRepo.save(flashcard));
    }

    @Override
    public void deleteFlashcard(Long id) {
        flashcardRepo.deleteById(id);
    }

    @Override
    public List<FlashcardDTO> getFlashcardsByDeck(Long deckId) {
        return flashcardRepo.findByDeckId(deckId).stream().map(this::map).collect(Collectors.toList());
    }

    private FlashcardDTO map(Flashcard f) {
        return FlashcardDTO.builder()
                .id(f.getId())
                .question(f.getQuestion())
                .answer(f.getAnswer())
                .deckId(f.getDeck().getId())
                .build();
    }
}
