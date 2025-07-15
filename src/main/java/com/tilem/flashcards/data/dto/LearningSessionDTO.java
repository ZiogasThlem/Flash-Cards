package com.tilem.flashcards.data.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LearningSessionDTO {
    private Long id;
    private Long userId;
    private Long flashcardId;
    private LocalDateTime lastReviewedAt;
    private LocalDateTime nextReviewAt;
    private int repetitions;
    private double easeFactor;
    private int interval;
    private String isActive;
}