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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Prompt mapToEntity(PromptDTO dto) {
	    log.info("Mapping PromptDTO to entity for DTO ID: " + dto.id());
	    Prompt prompt = mapper.toEntity(dto);
	    if (dto.answers() != null) {
		    dto.answers().forEach(answerDTO -> {
		        try {
			        Answer answer = answerService.mapToEntity(answerDTO);
			        prompt.addAnswer(answer);
		        } catch (Exception e) {
			        log.error(
					        "Error mapping answer DTO to entity for prompt DTO ID: " + dto.id(),
					        e);
			        throw e;
		        }
	        });
        }
	    log.info("Successfully mapped PromptDTO to entity for DTO ID: " + dto.id());
        return prompt;
    }

    @Override
    @Transactional
    public void updateEntity(Prompt entity, PromptDTO dto) {
        log.info("Updating Prompt entity with ID: " + entity.getId());
	    mapper.updateEntity(dto, entity);

	    if (dto.answers() != null) {
            List<Answer> existingAnswers = new ArrayList<>(entity.getAnswers());
            List<Answer> updatedAnswers = new ArrayList<>();

		    for (AnswerDTO answerDTO : dto.answers()) {
                try {
	                if (answerDTO.id() != null) {
                        existingAnswers.stream()
		                        .filter(a -> a.getUniqueID().equals(answerDTO.id()))
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
					        + entity.getUniqueID()
					        + ". Clearing existing answers.");
            entity.getAnswers().clear();
        }
    }
}
