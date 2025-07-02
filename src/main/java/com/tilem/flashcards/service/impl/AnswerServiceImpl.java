package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.entity.Answer;
import com.tilem.flashcards.data.entity.Prompt;
import com.tilem.flashcards.repository.AnswerRepository;
import com.tilem.flashcards.repository.PromptRepository;
import com.tilem.flashcards.service.AnswerService;
import org.springframework.stereotype.Service;

@Service
public class AnswerServiceImpl extends GenericServiceImpl<Answer, AnswerDTO> implements AnswerService {

    private final PromptRepository promptRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository, PromptRepository promptRepository) {
        super(answerRepository);
        this.promptRepository = promptRepository;
    }

    @Override
    protected AnswerDTO mapToDto(Answer entity) {
        return AnswerDTO.builder()
                .id(entity.getId())
                .promptId(entity.getPrompt().getId())
                .answerBody(entity.getAnswerBody())
                .notes(entity.getNotes())
                .build();
    }

    @Override
    public Answer mapToEntity(AnswerDTO dto) {
        Prompt prompt = promptRepository.findById(dto.getPromptId()).orElseThrow();
        Answer answer = new Answer();
        answer.setPrompt(prompt);
        answer.setAnswerBody(dto.getAnswerBody());
        answer.setNotes(dto.getNotes());
        return answer;
    }

    @Override
    public void updateEntity(Answer entity, AnswerDTO dto) {
        entity.setAnswerBody(dto.getAnswerBody());
        entity.setNotes(dto.getNotes());
    }
}
