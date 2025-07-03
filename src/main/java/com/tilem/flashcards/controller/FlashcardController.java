package com.tilem.flashcards.controller;

import com.tilem.flashcards.data.dto.FlashcardDTO;
import com.tilem.flashcards.data.entity.Flashcard;
import com.tilem.flashcards.data.enums.YesNo;
import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.dto.BlobDataDTO;
import com.tilem.flashcards.data.dto.PromptDTO;
import com.tilem.flashcards.data.entity.Prompt;
import com.tilem.flashcards.service.FlashcardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
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

    @GetMapping("/hasManyCorrectAnswers/{hasManyCorrectAnswers}")
    public List<FlashcardDTO> getByHasManyCorrectAnswers(@PathVariable YesNo hasManyCorrectAnswers) {
        return flashcardService.getFlashcardsByHasManyCorrectAnswers(hasManyCorrectAnswers);
    }

    @Override
    protected FlashcardDTO mapToDto(Flashcard entity) {
        PromptDTO promptDTO = null;
        if (entity.getPrompt() != null) {
            Prompt prompt = entity.getPrompt();
            List<AnswerDTO> answerDTOs = prompt.getAnswers().stream()
                    .map(answer -> AnswerDTO.builder()
                            .id(answer.getId())
                            .promptId(answer.getPrompt().getId())
                            .answerBody(answer.getAnswerBody())
                            .notes(answer.getNotes())
                            .build())
                    .collect(Collectors.toList());
            promptDTO = PromptDTO.builder()
                    .id(prompt.getId())
                    .promptBody(prompt.getPromptBody())
                    .hasSingleAnswer(prompt.getHasSingleAnswer())
                    .answers(answerDTOs)
                    .build();
        }

        BlobDataDTO blobDataDTO = null;
        if (entity.getImageData() != null) {
            blobDataDTO = BlobDataDTO.builder()
                    .id(entity.getImageData().getId())
                    .data(entity.getImageData().getData())
                    .mimeType(entity.getImageData().getMimeType())
                    .build();
        }

        return FlashcardDTO.builder()
                .id(entity.getId())
                .prompt(promptDTO)
                .hasManyCorrectAnswers(entity.getHasManyCorrectAnswers())
                .hasImageData(entity.getHasImageData())
                .imageData(blobDataDTO)
                .deckId(entity.getDeck().getId())
                .build();
    }
}
