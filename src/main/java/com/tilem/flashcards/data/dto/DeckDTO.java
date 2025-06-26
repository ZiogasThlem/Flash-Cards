package com.tilem.flashcards.data.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DeckDTO {
    public Long id;
    public String name;
    public List<String> users;
    public List<String> flashcards;
}