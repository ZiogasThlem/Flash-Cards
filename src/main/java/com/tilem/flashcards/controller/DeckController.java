package com.tilem.flashcards.controller;

import com.tilem.flashcards.data.dto.DeckDTO;
import com.tilem.flashcards.data.entity.Deck;
import com.tilem.flashcards.data.entity.Flashcard;
import com.tilem.flashcards.data.entity.User;
import com.tilem.flashcards.service.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/decks")
public class DeckController extends GenericController<Deck, DeckDTO> {

    @Autowired
    private DeckService deckService;

    @GetMapping
    public ResponseEntity<List<DeckDTO>> getAll() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeckDTO> getById(@PathVariable Long id) {
        return super.getById(id);
    }

    @PostMapping
    public ResponseEntity<DeckDTO> create(@RequestBody DeckDTO dto) {
        return super.create(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeckDTO> update(@PathVariable Long id, @RequestBody DeckDTO dto) {
        return super.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @PostMapping("/import")
    public ResponseEntity<List<DeckDTO>> importDecks(@RequestParam("file") MultipartFile file) throws IOException {
        List<DeckDTO> importedDecks = deckService.importDecksFromFile(new String(file.getBytes()));
        return ResponseEntity.ok(importedDecks);
    }

    @Override
    protected DeckDTO mapToDto(Deck entity) {
        return DeckDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .users(entity.getUsers().stream().map(User::getSimpleLabel).collect(Collectors.toList()))
                .flashcards(entity.getFlashcards().stream().map(Flashcard::getSimpleLabel).collect(Collectors.toList()))
                .build();
    }
}
