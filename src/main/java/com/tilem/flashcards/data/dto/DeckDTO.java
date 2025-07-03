package com.tilem.flashcards.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tilem.flashcards.data.enums.DeckCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeckDTO {

    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String description;
    private DeckCategory category;
    private List<String> users;
    private List<String> flashcards;

}
