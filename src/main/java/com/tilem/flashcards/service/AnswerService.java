package com.tilem.flashcards.service;

import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.entity.Answer;

public interface AnswerService extends GenericService<Answer, AnswerDTO> {
    Answer mapToEntity(AnswerDTO dto);

	void updateEntity(Answer entity, AnswerDTO dto);
}