package com.tilem.flashcards.data.dto;

import com.tilem.flashcards.data.enums.YesNo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PromptDTO {
    private Long id;
    private String promptBody;
    private YesNo hasSingleAnswer;
    private List<AnswerDTO> answers;
}