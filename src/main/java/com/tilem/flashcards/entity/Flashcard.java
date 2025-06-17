package com.tilem.flashcards.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Flashcard {
    @Id
    @GeneratedValue
    private Long id;

    private String question;

    private String answer;

    @ManyToOne
    @JoinColumn(name = "deck_id")
    private Deck deck;
}