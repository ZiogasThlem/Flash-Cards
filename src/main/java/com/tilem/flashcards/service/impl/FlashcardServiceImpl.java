package com.tilem.flashcards.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.dto.FlashcardDTO;
import com.tilem.flashcards.data.dto.PromptDTO;
import com.tilem.flashcards.data.entity.Flashcard;
import com.tilem.flashcards.data.enums.YesNo;
import com.tilem.flashcards.mapper.FlashcardMapper;
import com.tilem.flashcards.mapper.PromptMapper;
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
public class FlashcardServiceImpl
		extends GenericServiceImpl<Flashcard, FlashcardDTO, FlashcardRepository>
		implements FlashcardService {

	private static final LogWrapper log = LogWrapper.getLogger(MethodHandles.lookup().lookupClass());
	private final DeckRepository deckRepository;
	private final PromptService promptService;
	private final LearningSessionService learningSessionService;
	private final FlashcardMapper flashcardMapper;
	private final PromptMapper promptMapper;

	public FlashcardServiceImpl(
			FlashcardRepository flashcardRepository,
			DeckRepository deckRepository,
			PromptService promptService,
			LearningSessionService learningSessionService,
			FlashcardMapper flashcardMapper,
			PromptMapper promptMapper) {
		super(flashcardRepository, flashcardMapper);
		this.deckRepository = deckRepository;
		this.promptService = promptService;
		this.learningSessionService = learningSessionService;
		this.flashcardMapper = flashcardMapper;
		this.promptMapper = promptMapper;
	}

	@Override
	public List<FlashcardDTO> getFlashcardsByDeck(Long deckId) {
		log.info("Fetching flashcards for deck ID: " + deckId);
		List<FlashcardDTO> flashcards =
				repository.findByDeckId(deckId).stream().map(mapper::toDto).collect(Collectors.toList());
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
	@Transactional
	public List<FlashcardDTO> importFlashcardsFromFile(String fileContent) {
		List<FlashcardDTO> importedFlashcards = new ArrayList<>();
		try (CSVReader reader = new CSVReaderBuilder(new StringReader(fileContent)).build()) {
			List<String[]> records = reader.readAll();
			for (String[] record : records) {
				if (record.length > 0) {
					String promptBody = record[0];
					PromptDTO promptDTO =
							PromptDTO.builder()
									.promptBody(promptBody)
									.hasSingleAnswer(YesNo.Y)
									.answers(new ArrayList<>())
									.build();

					for (int i = 1; i < record.length; i++) {
						AnswerDTO answerDTO = AnswerDTO.builder().answerBody(record[i]).build();
						promptDTO.getAnswers().add(answerDTO);
					}

					FlashcardDTO flashcardDTO =
							FlashcardDTO.builder().prompt(promptDTO).hasImageData(YesNo.N).build();
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