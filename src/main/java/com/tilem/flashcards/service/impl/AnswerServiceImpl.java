package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.entity.Answer;
import com.tilem.flashcards.mapper.AnswerMapper;
import com.tilem.flashcards.repository.AnswerRepository;
import com.tilem.flashcards.repository.PromptRepository;
import com.tilem.flashcards.service.AnswerService;
import com.tilem.flashcards.util.LogWrapper;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class AnswerServiceImpl extends GenericServiceImpl<Answer, AnswerDTO, AnswerRepository>
		implements AnswerService {

    private final PromptRepository promptRepository;
	private final AnswerMapper answerMapper;
    private static final LogWrapper log = LogWrapper.getLogger(MethodHandles.lookup().lookupClass());

	public AnswerServiceImpl(
			AnswerRepository answerRepository,
			PromptRepository promptRepository,
			AnswerMapper answerMapper) {
		super(answerRepository, answerMapper);
        this.promptRepository = promptRepository;
		this.answerMapper = answerMapper;
    }

    @Override
    public Answer mapToEntity(AnswerDTO dto) {
	    return answerMapper.toEntity(dto);
    }

    @Override
    public void updateEntity(Answer entity, AnswerDTO dto) {
	    answerMapper.updateEntity(dto, entity);
    }
}