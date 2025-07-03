package com.tilem.flashcards.repository;

import com.tilem.flashcards.data.entity.LearningSession;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LearningSessionRepository extends GenericRepository<LearningSession> {
    Optional<LearningSession> findByUserIdAndFlashcardId(Long userId, Long flashcardId);
}
