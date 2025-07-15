package com.tilem.flashcards.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SM2AlgorithmTest {

    @Test
    void calculateNextReview_quality5_initialRepetition() {
        // First repetition, quality 5
        SM2Algorithm.LearningSessionData data = SM2Algorithm.calculateNextReview(5, 0, 2.5, 0);
        assertEquals(1, data.getRepetitions());
        assertEquals(1, data.getInterval());
        assertEquals(2.6, data.getEaseFactor(), 0.001);
        assertNotNull(data.getNextReviewDate());
    }

    @Test
    void calculateNextReview_quality5_secondRepetition() {
        // Second repetition, quality 5
        SM2Algorithm.LearningSessionData data = SM2Algorithm.calculateNextReview(5, 1, 2.6, 1);
        assertEquals(2, data.getRepetitions());
        assertEquals(6, data.getInterval());
        assertEquals(2.7, data.getEaseFactor(), 0.001);
        assertNotNull(data.getNextReviewDate());
    }

    @Test
    void calculateNextReview_quality5_subsequentRepetition() {
        // Subsequent repetition, quality 5
        SM2Algorithm.LearningSessionData data = SM2Algorithm.calculateNextReview(5, 2, 2.7, 6);
        assertEquals(3, data.getRepetitions());
        assertEquals(16, data.getInterval()); // 6 * 2.7 = 16.2, rounded to 16
        assertEquals(2.8, data.getEaseFactor(), 0.001);
        assertNotNull(data.getNextReviewDate());
    }

    @Test
    void calculateNextReview_quality3() {
        // Quality 3, subsequent repetition
        SM2Algorithm.LearningSessionData data = SM2Algorithm.calculateNextReview(3, 3, 2.8, 16);
        assertEquals(4, data.getRepetitions());
        assertEquals(45, data.getInterval()); // 16 * 2.8 = 44.8, rounded to 45
        assertEquals(2.66, data.getEaseFactor(), 0.001); // 2.8 + (0.1 - (5-3)*(0.08 + (5-3)*0.02)) = 2.8 + (0.1 - 2*(0.08+0.04)) = 2.8 + (0.1 - 2*0.12) = 2.8 + (0.1 - 0.24) = 2.8 - 0.14 = 2.66
        assertNotNull(data.getNextReviewDate());
    }

    @Test
    void calculateNextReview_quality0() {
        // Quality 0, reset repetitions and interval
        SM2Algorithm.LearningSessionData data = SM2Algorithm.calculateNextReview(0, 3, 2.8, 16);
        assertEquals(0, data.getRepetitions());
        assertEquals(1, data.getInterval());
        assertEquals(1.3, data.getEaseFactor(), 0.001); // Ease factor reset to 1.3 if it goes below
        assertNotNull(data.getNextReviewDate());
    }

    @Test
    void calculateNextReview_qualityLessThan3_resetsRepetitionsAndInterval() {
        // Quality 2, should reset repetitions and interval
        SM2Algorithm.LearningSessionData data = SM2Algorithm.calculateNextReview(2, 5, 2.0, 30);
        assertEquals(0, data.getRepetitions());
        assertEquals(1, data.getInterval());
        assertEquals(1.3, data.getEaseFactor(), 0.001); // Ease factor should be at least 1.3
        assertNotNull(data.getNextReviewDate());
    }

    @Test
    void calculateNextReview_easeFactorBelow1_3_resetsTo1_3() {
        // Test case where ease factor would drop below 1.3
        SM2Algorithm.LearningSessionData data = SM2Algorithm.calculateNextReview(0, 0, 1.5, 1);
        assertEquals(0, data.getRepetitions());
        assertEquals(1, data.getInterval());
        assertEquals(1.3, data.getEaseFactor(), 0.001);
        assertNotNull(data.getNextReviewDate());
    }
}
