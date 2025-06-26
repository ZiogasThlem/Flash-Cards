package com.tilem.flashcards.data.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDTO {
    public Long id;
    public String username;
    public List<Long> deckIds;
}