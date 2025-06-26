package com.tilem.flashcards.controller;

import com.tilem.flashcards.data.dto.FlashcardDTO;
import com.tilem.flashcards.service.FlashcardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {

    private final FlashcardService flashcardService;

    public FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @GetMapping
    public List<FlashcardDTO> getAll() {
        return flashcardService.getAllFlashcards();
    }

    @GetMapping("/{id}")
    public FlashcardDTO getOne(@PathVariable Long id) {
        return flashcardService.getFlashcardById(id);
    }

    @PostMapping
    public FlashcardDTO create(@RequestBody FlashcardDTO dto) {
        return flashcardService.createFlashcard(dto);
    }

    @PutMapping("/{id}")
    public FlashcardDTO update(@PathVariable Long id, @RequestBody FlashcardDTO dto) {
        return flashcardService.updateFlashcard(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        flashcardService.deleteFlashcard(id);
    }

    @GetMapping("/deck/{deckId}")
    public List<FlashcardDTO> getByDeck(@PathVariable Long deckId) {
        return flashcardService.getFlashcardsByDeck(deckId);
    }
}
