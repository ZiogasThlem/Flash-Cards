package com.tilem.flashcards.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Implements the SM-2 Spaced Repetition Algorithm for calculating flashcard review intervals.
 * This algorithm adjusts the review schedule of flashcards based on the user's recall quality,
 * aiming to optimize learning and retention.
 */
@Component
public class SM2Algorithm {

	public static final int INITIAL_INTERVAL = 1;
	public static final int SECOND_INTERVAL = 6;

    /**
     * Calculates the next review date, interval, ease factor, and repetitions for a flashcard
     * based on the user's recall quality.
     *
     * @param quality The quality of recall (0-5), where 5 is perfect recall and 0 is complete failure.
     * @param repetitions The number of times the flashcard has been successfully recalled in a row.
     * @param easeFactor The ease factor of the flashcard, which adjusts the growth of the interval.
     * @param interval The current interval in days between reviews.
     * @return A {@link LearningSessionData} object containing the updated review data.
     */
	public static LearningSessionData calculateNextReview(int quality, int repetitions, double easeFactor, int interval) {
        double newEaseFactor = easeFactor;
        int newRepetitions = repetitions;
        int newInterval = interval;

        if (quality >= 3) {
	        if (repetitions == 0) {
		        newInterval = INITIAL_INTERVAL;
	        } else if (repetitions == 1) {
		        newInterval = SECOND_INTERVAL;
	        } else {
		        newInterval = (int) Math.round(newInterval * newEaseFactor);
	        }
	        newRepetitions++;
        } else {
	        newRepetitions = 0;
	        newInterval = INITIAL_INTERVAL;
            newEaseFactor = 1.3;
        }

        newEaseFactor = newEaseFactor + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));
        if (newEaseFactor < 1.3) {
            newEaseFactor = 1.3;
        }

        LocalDateTime nextReviewDate = LocalDateTime.now().plusDays(newInterval);

        return new LearningSessionData(nextReviewDate, newInterval, newEaseFactor, newRepetitions);
    }

    /**
     * A static nested class to hold the results of the SM-2 algorithm calculation.
     */
    @Getter
    @AllArgsConstructor
    public static class LearningSessionData {
        private LocalDateTime nextReviewDate;
        private int interval;
        private double easeFactor;
        private int repetitions;
    }
}