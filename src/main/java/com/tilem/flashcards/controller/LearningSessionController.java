package com.tilem.flashcards.controller;

import com.tilem.flashcards.data.dto.LearningSessionDTO;
import com.tilem.flashcards.data.entity.LearningSession;
import com.tilem.flashcards.service.LearningSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/learning-sessions")
public class LearningSessionController
		extends GenericController<LearningSession, LearningSessionDTO> {

	private final LearningSessionService learningSessionService;

	public LearningSessionController(LearningSessionService learningSessionService) {
		super(learningSessionService);
		this.learningSessionService = learningSessionService;
	}

    @PostMapping("/review")
    public ResponseEntity<LearningSessionDTO> recordReview(
		    @RequestParam Long userId, @RequestParam Long flashcardId) {
        LearningSessionDTO updatedSession = learningSessionService.recordReview(userId, flashcardId);
        return ResponseEntity.ok(updatedSession);
    }
}
