package com.tilem.flashcards.data.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PromptDTO {
    private Long id;
    private String promptBody;
    private String hasSingleAnswer;
    private List<AnswerDTO> answers;
}
