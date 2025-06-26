package com.tilem.flashcards.controller;

import com.tilem.flashcards.data.dto.DeckDTO;
import com.tilem.flashcards.service.DeckService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/decks")
public class DeckController {

    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @GetMapping
    public List<DeckDTO> getAll() {
        return deckService.getAllDecks();
    }

    @GetMapping("/{id}")
    public DeckDTO getOne(@PathVariable Long id) {
        return deckService.getDeckById(id);
    }

    @PostMapping
    public DeckDTO create(@RequestBody DeckDTO dto) {
        return deckService.createDeck(dto);
    }

    @PutMapping("/{id}")
    public DeckDTO update(@PathVariable Long id, @RequestBody DeckDTO dto) {
        return deckService.updateDeck(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        deckService.deleteDeck(id);
    }

}
