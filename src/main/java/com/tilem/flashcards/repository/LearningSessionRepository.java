package com.tilem.flashcards.repository;

import com.tilem.flashcards.data.entity.LearningSession;

import java.util.Optional;

public interface LearningSessionRepository extends GenericRepository<LearningSession> {

	Optional<LearningSession> findByUserIdAndFlashcardId(Long userId, Long flashcardId);
}