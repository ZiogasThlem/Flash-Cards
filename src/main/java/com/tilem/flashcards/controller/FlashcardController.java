package com.tilem.flashcards.controller;

import com.tilem.flashcards.data.dto.FlashcardDTO;
import com.tilem.flashcards.data.entity.Flashcard;
import com.tilem.flashcards.data.enums.YesNo;
import com.tilem.flashcards.service.FlashcardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController extends GenericController<Flashcard, FlashcardDTO> {

    private final FlashcardService flashcardService;

    public FlashcardController(FlashcardService flashcardService) {
        super(flashcardService);
        this.flashcardService = flashcardService;
    }

    @GetMapping("/deck/{deckId}")
    public List<FlashcardDTO> getByDeck(@PathVariable Long deckId) {
        return flashcardService.getFlashcardsByDeck(deckId);
    }

    @GetMapping("/hasManyCorrectAnswers/{hasManyCorrectAnswers}")
    public List<FlashcardDTO> getByHasManyCorrectAnswers(@PathVariable YesNo hasManyCorrectAnswers) {
        return flashcardService.getFlashcardsByHasManyCorrectAnswers(hasManyCorrectAnswers);
    }
}
