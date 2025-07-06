package com.tilem.flashcards.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.dto.FlashcardDTO;
import com.tilem.flashcards.data.dto.PromptDTO;
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
import com.tilem.flashcards.util.AppException;
import com.tilem.flashcards.util.LogWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringReader;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
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

    public FlashcardServiceImpl(FlashcardRepository flashcardRepository,
                                DeckRepository deckRepository,
                                PromptService promptService,
                                BlobDataRepository blobDataRepository,
                                LearningSessionService learningSessionService) {
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
    public void recordFlashcardReview(Long flashcardId, Long userId) {
        log.info("Recording review for flashcard ID: " + flashcardId + ", user ID: " + userId);
        learningSessionService.recordReview(userId, flashcardId);
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

        

        return FlashcardDTO.builder()
                .id(entity.getId())
                .prompt(promptDTO)
                .hasImageData(entity.getHasImageData())
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
        flashcard.setHasImageData(dto.getHasImageData());

        if (dto.getPrompt() != null) {
            if (dto.getPrompt().getAnswers() == null || dto.getPrompt().getAnswers().isEmpty()) {
                throw new AppException("Flashcard prompt must have at least one answer.");
            }
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

        
        log.info("Successfully mapped FlashcardDTO to entity for DTO ID: " + dto.getId());
        return flashcard;
    }

    @Override
    protected void updateEntity(Flashcard entity, FlashcardDTO dto) {
        log.info("Updating flashcard entity with ID: " + entity.getId());
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

        
        log.info("Successfully updated flashcard entity with ID: " + entity.getId());
    }

    @Override
    @Transactional
    public List<FlashcardDTO> importFlashcardsFromFile(String fileContent) {
        List<FlashcardDTO> importedFlashcards = new ArrayList<>();
        try (CSVReader reader = new CSVReaderBuilder(new StringReader(fileContent)).build()) {
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                if (record.length > 0) {
                    String promptBody = record[0];
                    PromptDTO promptDTO = PromptDTO.builder()
                            .promptBody(promptBody)
                            .hasSingleAnswer(YesNo.Y)
                            .answers(new ArrayList<>())
                            .build();

                    for (int i = 1; i < record.length; i++) {
                        AnswerDTO answerDTO = AnswerDTO.builder()
                                .answerBody(record[i])
                                .build();
                        promptDTO.getAnswers().add(answerDTO);
                    }

                    FlashcardDTO flashcardDTO = FlashcardDTO.builder()
                            .prompt(promptDTO)
                            .hasImageData(YesNo.N)
                            .build();
                    importedFlashcards.add(create(flashcardDTO));
                }
            }
        } catch (IOException | CsvException e) {
            log.error("Error processing CSV file for flashcards: " + e.getMessage());
            throw new AppException("Error reading CSV file for flashcards import.", e);
        }
        return importedFlashcards;
    }
}

