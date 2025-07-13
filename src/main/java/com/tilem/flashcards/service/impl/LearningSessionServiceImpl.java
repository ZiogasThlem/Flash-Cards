package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.LearningSessionDTO;
import com.tilem.flashcards.data.entity.Flashcard;
import com.tilem.flashcards.data.entity.LearningSession;
import com.tilem.flashcards.data.entity.User;
import com.tilem.flashcards.data.enums.YesNo;
import com.tilem.flashcards.mapper.LearningSessionMapper;
import com.tilem.flashcards.repository.FlashcardRepository;
import com.tilem.flashcards.repository.LearningSessionRepository;
import com.tilem.flashcards.repository.UserRepository;
import com.tilem.flashcards.service.LearningSessionService;
import com.tilem.flashcards.util.LogWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;

@Service
public class LearningSessionServiceImpl
		extends GenericServiceImpl<LearningSession, LearningSessionDTO, LearningSessionRepository>
		implements LearningSessionService {

    private final UserRepository userRepository;
    private final FlashcardRepository flashcardRepository;
	private final LearningSessionMapper learningSessionMapper;
    private static final LogWrapper log = LogWrapper.getLogger(MethodHandles.lookup().lookupClass());

	public LearningSessionServiceImpl(
			LearningSessionRepository learningSessionRepository,
			UserRepository userRepository,
			FlashcardRepository flashcardRepository,
			LearningSessionMapper learningSessionMapper) {
		super(learningSessionRepository, learningSessionMapper, LearningSession.class);
        this.userRepository = userRepository;
        this.flashcardRepository = flashcardRepository;
		this.learningSessionMapper = learningSessionMapper;
    }

    @Override
    @Transactional
    public LearningSessionDTO recordReview(Long userId, Long flashcardId) {
        log.info("Recording review for user ID: " + userId + ", flashcard ID: " + flashcardId);
	    LearningSession learningSession =
			    repository
					    .findByUserIdAndFlashcardId(userId, flashcardId)
					    .orElseGet(
							    () -> {
								    log.info(
										    "No existing learning session found. Creating new session for user ID: "
												    + userId
												    + ", flashcard ID: "
												    + flashcardId);
								    User user;
								    Flashcard flashcard;
								    try {
									    user =
											    userRepository
													    .findById(userId)
													    .orElseThrow(() -> new RuntimeException("User not found"));
									    flashcard =
											    flashcardRepository
													    .findById(flashcardId)
													    .orElseThrow(() -> new RuntimeException("Flashcard not found"));
								    } catch (RuntimeException e) {
									    log.error(
											    "Error finding user or flashcard when creating new learning session for user ID: "
													    + userId
													    + ", flashcard ID: "
													    + flashcardId,
											    e);
									    throw e;
								    }
								    return LearningSession.builder()
										    .user(user)
										    .flashcard(flashcard)
										    .isActive(YesNo.Y)
										    .build();
                });

        learningSession.setLastReviewedAt(LocalDateTime.now());
	    learningSession.setNextReviewAt(
			    LocalDateTime.now().plusDays(1));
	    learningSession.setIsActive(YesNo.Y);

	    LearningSession savedSession = repository.save(learningSession);
	    log.info(
			    "Review recorded and session updated for user ID: "
					    + userId
					    + ", flashcard ID: "
					    + flashcardId
					    + ". Next review: "
					    + savedSession.getNextReviewAt());
	    return mapper.toDto(savedSession);
    }
}