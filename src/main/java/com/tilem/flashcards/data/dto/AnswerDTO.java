package com.tilem.flashcards.data.dto;

public record AnswerDTO(Long id, Long promptId, String answerBody, String notes) {
}