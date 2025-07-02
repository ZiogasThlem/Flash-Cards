package com.tilem.flashcards.data.dto;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    public String password;
    public List<Long> deckIds;
}