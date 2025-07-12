package com.tilem.flashcards.controller;

import com.tilem.flashcards.data.dto.DeckDTO;
import com.tilem.flashcards.data.dto.FlashcardDTO;
import com.tilem.flashcards.data.entity.Deck;
import com.tilem.flashcards.service.DeckService;
import com.tilem.flashcards.service.FlashcardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/decks")
public class DeckController extends GenericController<Deck, DeckDTO> {

	private final FlashcardService flashcardService;

	public DeckController(DeckService deckService, FlashcardService flashcardService) {
		super(deckService);
		this.flashcardService = flashcardService;
	}

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
    public ResponseEntity<List<DeckDTO>> importDecks(@RequestParam("file") MultipartFile file)
		    throws IOException {
	    List<DeckDTO> importedDecks =
			    ((DeckService) service).importDecksFromFile(new String(file.getBytes()));
        return ResponseEntity.ok(importedDecks);
    }

	@GetMapping("/{deckId}/flashcards")
	public ResponseEntity<List<FlashcardDTO>> getFlashcardsByDeck(@PathVariable Long deckId) {
		return ResponseEntity.ok(flashcardService.getFlashcardsByDeck(deckId));
	}
}