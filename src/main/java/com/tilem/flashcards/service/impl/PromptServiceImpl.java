package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.dto.PromptDTO;
import com.tilem.flashcards.data.entity.Answer;
import com.tilem.flashcards.data.entity.Prompt;
import com.tilem.flashcards.repository.PromptRepository;
import com.tilem.flashcards.service.AnswerService;
import com.tilem.flashcards.service.PromptService;
import com.tilem.flashcards.util.LogWrapper;
import java.lang.invoke.MethodHandles;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromptServiceImpl extends GenericServiceImpl<Prompt, PromptDTO> implements PromptService {

    private final AnswerService answerService;
    private static final LogWrapper log = LogWrapper.getLogger(MethodHandles.lookup().lookupClass());

    public PromptServiceImpl(PromptRepository promptRepository, AnswerService answerService) {
        super(promptRepository);
        this.answerService = answerService;
    }

    @Override
    protected PromptDTO mapToDto(Prompt entity) {
        log.info("Mapping Prompt entity to DTO for entity ID: " + entity.getId());
        List<AnswerDTO> answerDTOs = entity.getAnswers().stream()
                .map(answer -> AnswerDTO.builder()
                        .id(answer.getId())
                        .promptId(answer.getPrompt().getId())
                        .answerBody(answer.getAnswerBody())
                        .notes(answer.getNotes())
                        .build())
                .collect(Collectors.toList());

        PromptDTO promptDTO = PromptDTO.builder()
                .id(entity.getId())
                .promptBody(entity.getPromptBody())
                .hasSingleAnswer(entity.getHasSingleAnswer())
                .answers(answerDTOs)
                .build();
        log.info("Successfully mapped Prompt entity to DTO for entity ID: " + entity.getId());
        return promptDTO;
    }

    @Override
    public Prompt mapToEntity(PromptDTO dto) {
        log.info("Mapping PromptDTO to entity for DTO ID: " + dto.getId());
        Prompt prompt = new Prompt();
        prompt.setPromptBody(dto.getPromptBody());
        prompt.setHasSingleAnswer(dto.getHasSingleAnswer());
        if (dto.getAnswers() != null) {
            List<Answer> answers = dto.getAnswers().stream()
                    .map(answerDTO -> {
                        try {
                            return answerService.mapToEntity(answerDTO);
                        } catch (Exception e) {
                            log.error("Error mapping answer DTO to entity for prompt DTO ID: " + dto.getId(), e);
                            throw e;
                        }
                    })
                    .collect(Collectors.toList());
            answers.forEach(answer -> answer.setPrompt(prompt));
            prompt.setAnswers(answers);
        }
        log.info("Successfully mapped PromptDTO to entity for DTO ID: " + dto.getId());
        return prompt;
    }

    @Override
    public void updateEntity(Prompt entity, PromptDTO dto) {
        log.info("Updating Prompt entity with ID: " + entity.getId());
        entity.setPromptBody(dto.getPromptBody());
        entity.setHasSingleAnswer(dto.getHasSingleAnswer());

        if (dto.getAnswers() != null) {
            List<Answer> existingAnswers = new ArrayList<>(entity.getAnswers());
            List<Answer> updatedAnswers = new ArrayList<>();

            for (AnswerDTO answerDTO : dto.getAnswers()) {
                try {
                    if (answerDTO.getId() != null) {
                        existingAnswers.stream()
                                .filter(a -> a.getId().equals(answerDTO.getId()))
                                .findFirst()
                                .ifPresent(answer -> {
                                    answerService.updateEntity(answer, answerDTO);
                                    updatedAnswers.add(answer);
                                });
                    } else {
                        Answer newAnswer = answerService.mapToEntity(answerDTO);
                        newAnswer.setPrompt(entity);
                        updatedAnswers.add(newAnswer);
                    }
                } catch (Exception e) {
                    log.error("Error updating or creating answer for prompt ID: " + entity.getId(), e);
                    throw e;
                }
            }

            entity.getAnswers().retainAll(updatedAnswers);
            updatedAnswers.stream()
                    .filter(answer -> !entity.getAnswers().contains(answer))
                    .forEach(entity.getAnswers()::add);
        } else {
            log.info("No answers provided in DTO for prompt ID: " + entity.getId() + ". Clearing existing answers.");
            entity.getAnswers().clear();
        }
        log.info("Successfully updated Prompt entity with ID: " + entity.getId());
    }
}
