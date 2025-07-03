package com.tilem.flashcards.service.impl;

import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.dto.PromptDTO;
import com.tilem.flashcards.data.entity.Answer;
import com.tilem.flashcards.data.entity.Prompt;
import com.tilem.flashcards.data.enums.YesNo;
import com.tilem.flashcards.repository.PromptRepository;
import com.tilem.flashcards.service.AnswerService;
import com.tilem.flashcards.service.PromptService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromptServiceImpl extends GenericServiceImpl<Prompt, PromptDTO> implements PromptService {

    private final AnswerService answerService;

    public PromptServiceImpl(PromptRepository promptRepository, AnswerService answerService) {
        super(promptRepository);
        this.answerService = answerService;
    }

    @Override
    protected PromptDTO mapToDto(Prompt entity) {
        List<AnswerDTO> answerDTOs = entity.getAnswers().stream()
                .map(answer -> AnswerDTO.builder()
                        .id(answer.getId())
                        .promptId(answer.getPrompt().getId())
                        .answerBody(answer.getAnswerBody())
                        .notes(answer.getNotes())
                        .build())
                .collect(Collectors.toList());

        return PromptDTO.builder()
                .id(entity.getId())
                .promptBody(entity.getPromptBody())
                .hasSingleAnswer(entity.getHasSingleAnswer())
                .answers(answerDTOs)
                .build();
    }

    @Override
    public Prompt mapToEntity(PromptDTO dto) {
        Prompt prompt = new Prompt();
        prompt.setPromptBody(dto.getPromptBody());
        prompt.setHasSingleAnswer(dto.getHasSingleAnswer());
        if (dto.getAnswers() != null) {
            List<Answer> answers = dto.getAnswers().stream()
                    .map(answerDTO -> answerService.mapToEntity(answerDTO))
                    .collect(Collectors.toList());
            answers.forEach(answer -> answer.setPrompt(prompt));
            prompt.setAnswers(answers);
        }
        return prompt;
    }

    @Override
    public void updateEntity(Prompt entity, PromptDTO dto) {
        entity.setPromptBody(dto.getPromptBody());
        entity.setHasSingleAnswer(dto.getHasSingleAnswer());

        // Update answers
        if (dto.getAnswers() != null) {
            List<Answer> existingAnswers = new ArrayList<>(entity.getAnswers());
            List<Answer> updatedAnswers = new ArrayList<>();

            for (AnswerDTO answerDTO : dto.getAnswers()) {
                if (answerDTO.getId() != null) {
                    // Existing answer, update it
                    existingAnswers.stream()
                            .filter(a -> a.getId().equals(answerDTO.getId()))
                            .findFirst()
                            .ifPresent(answer -> {
                                answerService.updateEntity(answer, answerDTO);
                                updatedAnswers.add(answer);
                            });
                } else {
                    // New answer, create it
                    Answer newAnswer = answerService.mapToEntity(answerDTO);
                    newAnswer.setPrompt(entity);
                    updatedAnswers.add(newAnswer);
                }
            }

            // Remove answers not present in the DTO
            entity.getAnswers().retainAll(updatedAnswers);
            // Add new answers
            updatedAnswers.stream()
                    .filter(answer -> !entity.getAnswers().contains(answer))
                    .forEach(entity.getAnswers()::add);
        } else {
            entity.getAnswers().clear();
        }
    }
}
