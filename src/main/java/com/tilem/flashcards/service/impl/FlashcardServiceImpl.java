package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.dto.BlobDataDTO;
import com.tilem.flashcards.data.dto.FlashcardDTO;
import com.tilem.flashcards.data.dto.PromptDTO;
import com.tilem.flashcards.data.entity.BlobData;
import com.tilem.flashcards.data.entity.Deck;
import com.tilem.flashcards.data.entity.Flashcard;
import com.tilem.flashcards.data.entity.Prompt;
import com.tilem.flashcards.data.enums.YesNo;
import com.tilem.flashcards.repository.BlobDataRepository;
import com.tilem.flashcards.repository.DeckRepository;
import com.tilem.flashcards.repository.FlashcardRepository;
import com.tilem.flashcards.service.FlashcardService;
import com.tilem.flashcards.service.LearningSessionService;
import com.tilem.flashcards.service.PromptService;
import com.tilem.flashcards.util.LogWrapper;
import java.lang.invoke.MethodHandles;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlashcardServiceImpl extends GenericServiceImpl<Flashcard, FlashcardDTO> implements FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final DeckRepository deckRepository;
    private final PromptService promptService;
    private final BlobDataRepository blobDataRepository;
    private final LearningSessionService learningSessionService;
    private static final LogWrapper log = LogWrapper.getLogger(MethodHandles.lookup().lookupClass());

    public FlashcardServiceImpl(FlashcardRepository flashcardRepository, DeckRepository deckRepository,
                                PromptService promptService, BlobDataRepository blobDataRepository, LearningSessionService learningSessionService) {
        super(flashcardRepository);
        this.flashcardRepository = flashcardRepository;
        this.deckRepository = deckRepository;
        this.promptService = promptService;
        this.blobDataRepository = blobDataRepository;
        this.learningSessionService = learningSessionService;
    }

    @Override
    public List<FlashcardDTO> getFlashcardsByDeck(Long deckId) {
        log.info("Fetching flashcards for deck ID: " + deckId);
        List<FlashcardDTO> flashcards = flashcardRepository.findByDeckId(deckId).stream().map(this::mapToDto).collect(Collectors.toList());
        log.info("Found " + flashcards.size() + " flashcards for deck ID: " + deckId);
        return flashcards;
    }

    @Override
    public List<FlashcardDTO> getFlashcardsByHasManyCorrectAnswers(YesNo hasManyCorrectAnswers) {
        log.info("Fetching flashcards by hasManyCorrectAnswers: " + hasManyCorrectAnswers);
        List<FlashcardDTO> flashcards = flashcardRepository.findByHasManyCorrectAnswers(hasManyCorrectAnswers).stream().map(this::mapToDto).collect(Collectors.toList());
        log.info("Found " + flashcards.size() + " flashcards with hasManyCorrectAnswers: " + hasManyCorrectAnswers);
        return flashcards;
    }

    @Override
    public void recordFlashcardReview(Long flashcardId, Long userId, int quality) {
        log.info("Recording review for flashcard ID: " + flashcardId + ", user ID: " + userId + ", quality: " + quality);
        learningSessionService.recordReview(userId, flashcardId, quality);
        log.info("Review recorded for flashcard ID: " + flashcardId);
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
        log.info("Mapping FlashcardDTO to entity for DTO ID: " + dto.getId());
        Deck deck;
        try {
            deck = deckRepository.findById(dto.getDeckId()).orElseThrow(() -> new RuntimeException("Deck not found with ID: " + dto.getDeckId()));
        } catch (RuntimeException e) {
            log.error("Error finding deck with ID: " + dto.getDeckId(), e);
            throw e;
        }
        Flashcard flashcard = new Flashcard();
        flashcard.setDeck(deck);
        flashcard.setHasManyCorrectAnswers(dto.getHasManyCorrectAnswers());
        flashcard.setHasImageData(dto.getHasImageData());

        if (dto.getPrompt() != null) {
            Prompt promptToSet;
            if (dto.getPrompt().getId() == null) {
                log.info("Mapping new prompt for flashcard DTO ID: " + dto.getId());
                promptToSet = promptService.mapToEntity(dto.getPrompt());
            } else {
                log.info("Updating existing prompt for flashcard DTO ID: " + dto.getId());
                try {
                    promptToSet = promptService.findById(dto.getPrompt().getId());
                    promptService.updateEntity(promptToSet, dto.getPrompt());
                } catch (RuntimeException e) {
                    log.error("Error finding or updating prompt with ID: " + dto.getPrompt().getId(), e);
                    throw e;
                }
            }
            flashcard.setPrompt(promptToSet);
        } else {
            log.info("No prompt provided for flashcard DTO ID: " + dto.getId());
            flashcard.setPrompt(null);
        }

        if (dto.getImageData() != null) {
            log.info("Mapping image data for flashcard DTO ID: " + dto.getId());
            BlobData blobData = new BlobData();
            blobData.setData(dto.getImageData().getData());
            blobData.setMimeType(dto.getImageData().getMimeType());
            flashcard.setImageData(blobData);
        } else {
            log.info("No image data provided for flashcard DTO ID: " + dto.getId());
            flashcard.setImageData(null);
        }
        log.info("Successfully mapped FlashcardDTO to entity for DTO ID: " + dto.getId());
        return flashcard;
    }

    @Override
    protected void updateEntity(Flashcard entity, FlashcardDTO dto) {
        log.info("Updating flashcard entity with ID: " + entity.getId());
        entity.setHasManyCorrectAnswers(dto.getHasManyCorrectAnswers());
        entity.setHasImageData(dto.getHasImageData());

        if (dto.getPrompt() != null) {
            if (entity.getPrompt() == null) {
                // Assign new prompt
                log.info("Assigning new prompt to flashcard ID: " + entity.getId());
                Prompt prompt = promptService.mapToEntity(dto.getPrompt());
                entity.setPrompt(prompt);
            } else {
                // Update existing prompt
                log.info("Updating existing prompt for flashcard ID: " + entity.getId());
                try {
                    promptService.updateEntity(entity.getPrompt(), dto.getPrompt());
                } catch (RuntimeException e) {
                    log.error("Error updating prompt for flashcard ID: " + entity.getId(), e);
                    throw e;
                }
            }
        } else {
            // Remove prompt association
            log.info("Removing prompt association for flashcard ID: " + entity.getId());
            entity.setPrompt(null);
        }

        if (dto.getImageData() != null) {
            if (entity.getImageData() == null) {
                // New image data
                log.info("Assigning new image data to flashcard ID: " + entity.getId());
                BlobData blobData = new BlobData();
                blobData.setData(dto.getImageData().getData());
                blobData.setMimeType(dto.getImageData().getMimeType());
                entity.setImageData(blobData);
            } else {
                // Update existing image data
                log.info("Updating existing image data for flashcard ID: " + entity.getId());
                entity.getImageData().setData(dto.getImageData().getData());
                entity.getImageData().setMimeType(dto.getImageData().getMimeType());
            }
        } else {
            // Remove image data
            if (entity.getImageData() != null) {
                log.info("Removing image data for flashcard ID: " + entity.getId());
                try {
                    blobDataRepository.delete(entity.getImageData());
                    entity.setImageData(null);
                } catch (RuntimeException e) {
                    log.error("Error deleting image data for flashcard ID: " + entity.getId(), e);
                    throw e;
                }
            }
        }
        log.info("Successfully updated flashcard entity with ID: " + entity.getId());
    }
}

