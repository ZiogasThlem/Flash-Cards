package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.dto.BlobDataDTO;
import com.tilem.flashcards.data.dto.FlashcardDTO;
import com.tilem.flashcards.data.dto.PromptDTO;
import com.tilem.flashcards.data.entity.Answer;
import com.tilem.flashcards.data.entity.BlobData;
import com.tilem.flashcards.data.entity.Deck;
import com.tilem.flashcards.data.entity.Flashcard;
import com.tilem.flashcards.data.entity.Prompt;
import com.tilem.flashcards.data.enums.YesNo;
import com.tilem.flashcards.repository.BlobDataRepository;
import com.tilem.flashcards.repository.DeckRepository;
import com.tilem.flashcards.repository.FlashcardRepository;
import com.tilem.flashcards.service.AnswerService;
import com.tilem.flashcards.service.FlashcardService;
import com.tilem.flashcards.service.PromptService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlashcardServiceImpl extends GenericServiceImpl<Flashcard, FlashcardDTO> implements FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final DeckRepository deckRepository;
    private final PromptService promptService;
    private final AnswerService answerService;
    private final BlobDataRepository blobDataRepository;

    public FlashcardServiceImpl(FlashcardRepository flashcardRepository, DeckRepository deckRepository,
                                PromptService promptService, AnswerService answerService, BlobDataRepository blobDataRepository) {
        super(flashcardRepository);
        this.flashcardRepository = flashcardRepository;
        this.deckRepository = deckRepository;
        this.promptService = promptService;
        this.answerService = answerService;
        this.blobDataRepository = blobDataRepository;
    }

    @Override
    public List<FlashcardDTO> getFlashcardsByDeck(Long deckId) {
        return flashcardRepository.findByDeckId(deckId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<FlashcardDTO> getFlashcardsByHasManyCorrectAnswers(YesNo hasManyCorrectAnswers) {
        return flashcardRepository.findByHasManyCorrectAnswers(hasManyCorrectAnswers).stream().map(this::mapToDto).collect(Collectors.toList());
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

    @Override
    protected Flashcard mapToEntity(FlashcardDTO dto) {
        Deck deck = deckRepository.findById(dto.getDeckId()).orElseThrow();
        Flashcard flashcard = new Flashcard();
        flashcard.setDeck(deck);
        flashcard.setHasManyCorrectAnswers(dto.getHasManyCorrectAnswers());
        flashcard.setHasImageData(dto.getHasImageData());

        if (dto.getPrompt() != null) {
            Prompt prompt;
            if (dto.getPrompt().getId() == null) {
                // New prompt
                prompt = promptService.mapToEntity(dto.getPrompt());
            } else {
                // Existing prompt
                prompt = promptService.mapToEntity(promptService.findById(dto.getPrompt().getId()));
                promptService.updateEntity(prompt, dto.getPrompt());
            }
            flashcard.setPrompt(prompt);
        } else {
            flashcard.setPrompt(null);
        }

        if (dto.getImageData() != null) {
            BlobData blobData = new BlobData();
            blobData.setData(dto.getImageData().getData());
            blobData.setMimeType(dto.getImageData().getMimeType());
            flashcard.setImageData(blobData);
        } else {
            flashcard.setImageData(null);
        }

        return flashcard;
    }

    @Override
    protected void updateEntity(Flashcard entity, FlashcardDTO dto) {
        entity.setHasManyCorrectAnswers(dto.getHasManyCorrectAnswers());
        entity.setHasImageData(dto.getHasImageData());

        if (dto.getPrompt() != null) {
            if (entity.getPrompt() == null) {
                // Assign new prompt
                Prompt prompt = promptService.mapToEntity(dto.getPrompt());
                entity.setPrompt(prompt);
            } else {
                // Update existing prompt
                promptService.updateEntity(entity.getPrompt(), dto.getPrompt());
            }
        } else {
            // Remove prompt association
            entity.setPrompt(null);
        }

        if (dto.getImageData() != null) {
            if (entity.getImageData() == null) {
                // New image data
                BlobData blobData = new BlobData();
                blobData.setData(dto.getImageData().getData());
                blobData.setMimeType(dto.getImageData().getMimeType());
                entity.setImageData(blobData);
            } else {
                // Update existing image data
                entity.getImageData().setData(dto.getImageData().getData());
                entity.getImageData().setMimeType(dto.getImageData().getMimeType());
            }
        } else {
            // Remove image data
            if (entity.getImageData() != null) {
                blobDataRepository.delete(entity.getImageData());
                entity.setImageData(null);
            }
        }
    }
}

