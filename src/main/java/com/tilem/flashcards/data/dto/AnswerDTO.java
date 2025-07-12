package com.tilem.flashcards.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerDTO {
    private Long id;
    private Long promptId;
    private String answerBody;
    private String notes;
}