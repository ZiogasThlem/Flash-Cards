package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.dto.PromptDTO;
import com.tilem.flashcards.data.entity.Answer;
import com.tilem.flashcards.data.entity.Prompt;
import com.tilem.flashcards.mapper.PromptMapper;
import com.tilem.flashcards.repository.PromptRepository;
import com.tilem.flashcards.service.AnswerService;
import com.tilem.flashcards.service.PromptService;
import com.tilem.flashcards.util.LogWrapper;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Service
public class PromptServiceImpl extends GenericServiceImpl<Prompt, PromptDTO, PromptRepository>
		implements PromptService {

    private final AnswerService answerService;
	private final PromptMapper promptMapper;
    private static final LogWrapper log = LogWrapper.getLogger(MethodHandles.lookup().lookupClass());

	public PromptServiceImpl(
			PromptRepository promptRepository, AnswerService answerService, PromptMapper promptMapper) {
		super(promptRepository, promptMapper, Prompt.class);
        this.answerService = answerService;
		this.promptMapper = promptMapper;
    }

    @Override
    public Prompt mapToEntity(PromptDTO dto) {
        log.info("Mapping PromptDTO to entity for DTO ID: " + dto.getId());
	    Prompt prompt = mapper.toEntity(dto);
        if (dto.getAnswers() != null) {
	        dto.getAnswers().forEach(answerDTO -> {
		        try {
			        Answer answer = answerService.mapToEntity(answerDTO);
			        prompt.addAnswer(answer);
		        } catch (Exception e) {
			        log.error(
					        "Error mapping answer DTO to entity for prompt DTO ID: " + dto.getId(),
					        e);
			        throw e;
		        }
	        });
        }
        log.info("Successfully mapped PromptDTO to entity for DTO ID: " + dto.getId());
        return prompt;
    }

    @Override
    public void updateEntity(Prompt entity, PromptDTO dto) {
        log.info("Updating Prompt entity with ID: " + entity.getId());
	    mapper.updateEntity(dto, entity);

        if (dto.getAnswers() != null) {
            List<Answer> existingAnswers = new ArrayList<>(entity.getAnswers());
            List<Answer> updatedAnswers = new ArrayList<>();

            for (AnswerDTO answerDTO : dto.getAnswers()) {
                try {
                    if (answerDTO.getId() != null) {
                        existingAnswers.stream()
                                .filter(a -> a.getId().equals(answerDTO.getId()))
                                .findFirst()
		                        .ifPresent(
				                        answer -> {
					                        answerService.updateEntity(answer, answerDTO);
					                        updatedAnswers.add(answer);
				                        });
                    } else {
                        Answer newAnswer = answerService.mapToEntity(answerDTO);
	                    entity.addAnswer(newAnswer);
                        updatedAnswers.add(newAnswer);
                    }
                } catch (Exception e) {
                    log.error("Error updating or creating answer for prompt ID: " + entity.getId(), e);
                    throw e;
                }
            }

	        entity.getAnswers().clear();
	        updatedAnswers.forEach(entity::addAnswer);
        } else {
	        log.info(
			        "No answers provided in DTO for prompt ID: "
					        + entity.getId()
					        + ". Clearing existing answers.");
            entity.getAnswers().clear();
        }
        log.info("Successfully updated Prompt entity with ID: " + entity.getId());
    }
}
