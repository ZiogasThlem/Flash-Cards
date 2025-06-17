package com.tilem.flashcards.dto;

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
    public Long userId;
    public List<Long> flashcardIds;
}