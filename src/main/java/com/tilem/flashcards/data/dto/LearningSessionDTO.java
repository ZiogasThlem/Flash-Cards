package com.tilem.flashcards.data.dto;

import java.time.LocalDateTime;

public record LearningSessionDTO(
		Long id,
		Long userId,
		Long flashcardId,
		LocalDateTime lastReviewedAt,
		LocalDateTime nextReviewAt,
		int repetitions,
		double easeFactor,
		int interval,
		String isActive
) {
}