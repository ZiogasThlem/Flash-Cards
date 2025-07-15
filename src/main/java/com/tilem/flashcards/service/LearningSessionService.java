package com.tilem.flashcards.service;

import com.tilem.flashcards.data.dto.LearningSessionDTO;
import com.tilem.flashcards.data.entity.LearningSession;

public interface LearningSessionService
		extends GenericService<LearningSession, LearningSessionDTO> {
    LearningSessionDTO recordReview(Long userId, Long flashcardId, int quality);
}