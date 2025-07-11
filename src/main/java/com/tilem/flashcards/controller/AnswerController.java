package com.tilem.flashcards.controller;

import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.entity.Answer;
import com.tilem.flashcards.service.AnswerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/answers")
public class AnswerController extends GenericController<Answer, AnswerDTO> {

	public AnswerController(AnswerService answerService) {
		super(answerService);
	}
}
