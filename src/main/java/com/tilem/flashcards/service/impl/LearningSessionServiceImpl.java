package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.LearningSessionDTO;
import com.tilem.flashcards.data.entity.Flashcard;
import com.tilem.flashcards.data.entity.LearningSession;
import com.tilem.flashcards.data.entity.User;
import com.tilem.flashcards.repository.FlashcardRepository;
import com.tilem.flashcards.repository.LearningSessionRepository;
import com.tilem.flashcards.repository.UserRepository;
import com.tilem.flashcards.service.LearningSessionService;
import com.tilem.flashcards.util.LogWrapper;
import com.tilem.flashcards.util.SM2Algorithm;
import java.lang.invoke.MethodHandles;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LearningSessionServiceImpl extends GenericServiceImpl<LearningSession, LearningSessionDTO> implements LearningSessionService {

    private final LearningSessionRepository learningSessionRepository;
    private final UserRepository userRepository;
    private final FlashcardRepository flashcardRepository;
    private static final LogWrapper log = LogWrapper.getLogger(MethodHandles.lookup().lookupClass());

    public LearningSessionServiceImpl(LearningSessionRepository learningSessionRepository, UserRepository userRepository, FlashcardRepository flashcardRepository) {
        super(learningSessionRepository);
        this.learningSessionRepository = learningSessionRepository;
        this.userRepository = userRepository;
        this.flashcardRepository = flashcardRepository;
    }

    @Override
    protected LearningSessionDTO mapToDto(LearningSession entity) {
        return LearningSessionDTO.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .flashcardId(entity.getFlashcard().getId())
                .lastReviewedAt(entity.getLastReviewedAt())
                .nextReviewAt(entity.getNextReviewAt())
                .interval(entity.getInterval())
                .ease(entity.getEase())
                .build();
    }

    @Override
    protected LearningSession mapToEntity(LearningSessionDTO dto) {
        log.info("Mapping LearningSessionDTO to entity for DTO ID: " + dto.getId());
        User user;
        Flashcard flashcard;
        try {
            user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            flashcard = flashcardRepository.findById(dto.getFlashcardId()).orElseThrow(() -> new RuntimeException("Flashcard not found"));
        } catch (RuntimeException e) {
            log.error("Error finding user or flashcard for LearningSessionDTO ID: " + dto.getId(), e);
            throw e;
        }

        LearningSession learningSession = LearningSession.builder()
                .id(dto.getId())
                .user(user)
                .flashcard(flashcard)
                .lastReviewedAt(dto.getLastReviewedAt())
                .nextReviewAt(dto.getNextReviewAt())
                .interval(dto.getInterval())
                .ease(dto.getEase())
                .build();
        log.info("Successfully mapped LearningSessionDTO to entity for DTO ID: " + dto.getId());
        return learningSession;
    }

    @Override
    protected void updateEntity(LearningSession entity, LearningSessionDTO dto) {
        log.info("Updating LearningSession entity with ID: " + entity.getId());
        // For learning sessions, we primarily update based on the SM-2 algorithm, not direct DTO updates
        // This method might not be directly used for typical updates, but is required by GenericServiceImpl
        if (dto.getLastReviewedAt() != null) entity.setLastReviewedAt(dto.getLastReviewedAt());
        if (dto.getNextReviewAt() != null) entity.setNextReviewAt(dto.getNextReviewAt());
        if (dto.getInterval() != null) entity.setInterval(dto.getInterval());
        if (dto.getEase() != null) entity.setEase(dto.getEase());
        log.info("Finished updating LearningSession entity with ID: " + entity.getId());
    }

    @Override
    @Transactional
    public LearningSessionDTO recordReview(Long userId, Long flashcardId, int quality) {
        log.info("Recording review for user ID: " + userId + ", flashcard ID: " + flashcardId + ", quality: " + quality);
        LearningSession learningSession = learningSessionRepository.findByUserIdAndFlashcardId(userId, flashcardId)
                .orElseGet(() -> {
                    log.info("No existing learning session found. Creating new session for user ID: " + userId + ", flashcard ID: " + flashcardId);
                    User user;
                    Flashcard flashcard;
                    try {
                        user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
                        flashcard = flashcardRepository.findById(flashcardId).orElseThrow(() -> new RuntimeException("Flashcard not found"));
                    } catch (RuntimeException e) {
                        log.error("Error finding user or flashcard when creating new learning session for user ID: " + userId + ", flashcard ID: " + flashcardId, e);
                        throw e;
                    }
                    return LearningSession.builder()
                            .user(user)
                            .flashcard(flashcard)
                            .interval(0) // Initial interval
                            .ease(2.5) // Initial ease factor
                            .repetitions(0) // Initial repetitions
                            .build();
                });

        SM2Algorithm.LearningSessionData srsData = SM2Algorithm.calculateNextReview(
                quality,
                learningSession.getRepetitions(),
                learningSession.getEase(),
                learningSession.getInterval()
        );

        learningSession.setLastReviewedAt(LocalDateTime.now());
        learningSession.setNextReviewAt(srsData.getNextReviewDate());
        learningSession.setInterval(srsData.getInterval());
        learningSession.setEase(srsData.getEaseFactor());
        learningSession.setRepetitions(srsData.getRepetitions());

        LearningSession savedSession = learningSessionRepository.save(learningSession);
        log.info("Review recorded and session updated for user ID: " + userId + ", flashcard ID: " + flashcardId + ". Next review: " + savedSession.getNextReviewAt());
        return mapToDto(savedSession);
    }
}
