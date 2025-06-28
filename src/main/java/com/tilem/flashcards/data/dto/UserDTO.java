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
    public String password;
    public List<Long> deckIds;
}