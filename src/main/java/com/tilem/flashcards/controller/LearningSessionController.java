package com.tilem.flashcards.controller;

import com.tilem.flashcards.data.dto.LearningSessionDTO;
import com.tilem.flashcards.data.entity.LearningSession;
import com.tilem.flashcards.service.LearningSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/learning-sessions")
public class LearningSessionController extends GenericController<LearningSession, LearningSessionDTO> {

    private final LearningSessionService learningSessionService;

    public LearningSessionController(LearningSessionService learningSessionService) {
        super(learningSessionService);
        this.learningSessionService = learningSessionService;
    }

    @PostMapping("/review")
    public ResponseEntity<LearningSessionDTO> recordReview(@RequestParam Long userId, @RequestParam Long flashcardId, @RequestParam int quality) {
        LearningSessionDTO updatedSession = learningSessionService.recordReview(userId, flashcardId, quality);
        return ResponseEntity.ok(updatedSession);
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
}
