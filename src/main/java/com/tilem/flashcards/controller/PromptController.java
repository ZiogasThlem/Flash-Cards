package com.tilem.flashcards.controller;

import com.tilem.flashcards.data.dto.PromptDTO;
import com.tilem.flashcards.data.entity.Prompt;
import com.tilem.flashcards.data.dto.AnswerDTO;
import com.tilem.flashcards.data.entity.Answer;
import com.tilem.flashcards.service.PromptService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prompts")
public class PromptController extends GenericController<Prompt, PromptDTO> {

    public PromptController(PromptService promptService) {
        super(promptService);
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
}
