package com.tilem.flashcards.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class SM2Algorithm {

    public static final int INITIAL_INTERVAL = 1; // days
    public static final int SECOND_INTERVAL = 6; // days

    public static LearningSessionData calculateNextReview(int quality, int repetitions, double easeFactor, int interval) {
        double newEaseFactor = easeFactor;
        int newRepetitions = repetitions;
        int newInterval = interval;

        if (quality >= 3) {
            // Correct answer
            if (repetitions == 0) {
                newInterval = INITIAL_INTERVAL;
            } else if (repetitions == 1) {
                newInterval = SECOND_INTERVAL;
            } else {
                newInterval = (int) Math.round(newInterval * newEaseFactor);
            }
            newRepetitions++;
        } else {
            // Incorrect answer
            newRepetitions = 0;
            newInterval = INITIAL_INTERVAL;
        }

        newEaseFactor = newEaseFactor + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));
        if (newEaseFactor < 1.3) {
            newEaseFactor = 1.3;
        }

        LocalDateTime nextReviewDate = LocalDateTime.now().plusDays(newInterval);

        return new LearningSessionData(nextReviewDate, newInterval, newEaseFactor, newRepetitions);
    }

    @Getter
    @AllArgsConstructor
    public static class LearningSessionData {
        private LocalDateTime nextReviewDate;
        private int interval;
        private double easeFactor;
        private int repetitions;
    }
}
