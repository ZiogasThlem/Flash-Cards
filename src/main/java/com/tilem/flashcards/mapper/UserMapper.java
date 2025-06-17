package com.tilem.flashcards.mapper;

import com.tilem.flashcards.dto.UserDTO;
import com.tilem.flashcards.entity.Deck;
import com.tilem.flashcards.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserDTO toDto(User user) {
        return UserDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .deckIds(user.getDecks().stream().map(Deck::getId).collect(Collectors.toList()))
                    .build();
    }
}