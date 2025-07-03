package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.entity.Answer;
import com.tilem.flashcards.data.entity.Prompt;
import com.tilem.flashcards.repository.AnswerRepository;
import com.tilem.flashcards.repository.PromptRepository;
import com.tilem.flashcards.service.AnswerService;
import com.tilem.flashcards.util.LogWrapper;
import java.lang.invoke.MethodHandles;
import org.springframework.stereotype.Service;

@Service
public class AnswerServiceImpl extends GenericServiceImpl<Answer, AnswerDTO> implements AnswerService {

    private final PromptRepository promptRepository;
    private static final LogWrapper log = LogWrapper.getLogger(MethodHandles.lookup().lookupClass());

    public AnswerServiceImpl(AnswerRepository answerRepository, PromptRepository promptRepository) {
        super(answerRepository);
        this.promptRepository = promptRepository;
    }

    @Override
    protected AnswerDTO mapToDto(Answer entity) {
        log.info("Mapping Answer entity to DTO for entity ID: " + entity.getId());
        AnswerDTO answerDTO = AnswerDTO.builder()
                .id(entity.getId())
                .promptId(entity.getPrompt().getId())
                .answerBody(entity.getAnswerBody())
                .notes(entity.getNotes())
                .build();
        log.info("Successfully mapped Answer entity to DTO for entity ID: " + entity.getId());
        return answerDTO;
    }

    @Override
    public Answer mapToEntity(AnswerDTO dto) {
        log.info("Mapping AnswerDTO to entity for DTO ID: " + dto.getId());
        Prompt prompt;
        try {
            prompt = promptRepository.findById(dto.getPromptId()).orElseThrow(() -> new RuntimeException("Prompt not found with ID: " + dto.getPromptId()));
        } catch (RuntimeException e) {
            log.error("Error finding prompt with ID: " + dto.getPromptId(), e);
            throw e;
        }
        Answer answer = new Answer();
        answer.setPrompt(prompt);
        answer.setAnswerBody(dto.getAnswerBody());
        answer.setNotes(dto.getNotes());
        log.info("Successfully mapped AnswerDTO to entity for DTO ID: " + dto.getId());
        return answer;
    }

    @Override
    public void updateEntity(Answer entity, AnswerDTO dto) {
        log.info("Updating Answer entity with ID: " + entity.getId());
        entity.setAnswerBody(dto.getAnswerBody());
        entity.setNotes(dto.getNotes());
        log.info("Successfully updated Answer entity with ID: " + entity.getId());
    }
}
