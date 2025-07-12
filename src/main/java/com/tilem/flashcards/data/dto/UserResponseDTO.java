package com.tilem.flashcards.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO {

    private Long id;
    private String username;
    private List<DeckDTO> decks;
}