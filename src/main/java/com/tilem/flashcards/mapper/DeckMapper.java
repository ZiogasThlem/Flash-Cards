package com.tilem.flashcards.mapper;

import com.tilem.flashcards.dto.DeckDTO;
import com.tilem.flashcards.entity.Deck;
import com.tilem.flashcards.entity.Flashcard;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class DeckMapper {
    public DeckDTO toDto(Deck deck) {
        return DeckDTO.builder()
                        .id (deck.getId())
                        .name(deck.getName())
                        .userId(deck.getUser().getId())
                        .flashcardIds(deck.getFlashcards().stream().map(Flashcard::getId).collect(Collectors.toList()))
                        .build();
    }
}