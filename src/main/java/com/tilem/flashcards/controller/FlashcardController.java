package com.tilem.flashcards.controller;

import com.tilem.flashcards.data.dto.FlashcardDTO;
import com.tilem.flashcards.data.entity.Flashcard;
import com.tilem.flashcards.service.FlashcardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flashcards")
public class FlashcardController extends GenericController<Flashcard, FlashcardDTO> {

	private final FlashcardService flashcardService;

	public FlashcardController(FlashcardService flashcardService) {
		super(flashcardService);
		this.flashcardService = flashcardService;
	}

	@GetMapping
	public ResponseEntity<List<FlashcardDTO>> getAll() {
		return super.getAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<FlashcardDTO> getById(@PathVariable Long id) {
		return super.getById(id);
	}

	@PostMapping
	public ResponseEntity<FlashcardDTO> create(@RequestBody FlashcardDTO dto) {
		return super.create(dto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<FlashcardDTO> update(@PathVariable Long id, @RequestBody FlashcardDTO dto) {
		return super.update(id, dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		return super.delete(id);
	}

	@GetMapping("/deck/{deckId}")
	public List<FlashcardDTO> getByDeck(@PathVariable Long deckId) {
		return flashcardService.getFlashcardsByDeck(deckId);
	}

	@PostMapping("/{flashcardId}/review/{userId}")
	public ResponseEntity<Void> recordFlashcardReview(@PathVariable Long flashcardId, @PathVariable Long userId, @RequestParam int quality) {
		flashcardService.recordFlashcardReview(flashcardId, userId, quality);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/decks/{deckId}/import")
	public ResponseEntity<List<FlashcardDTO>> importFlashcards(@PathVariable Long deckId, @RequestBody String fileContent) {
		return ResponseEntity.ok(flashcardService.importFlashcardsFromFile(deckId, fileContent));
	}
}