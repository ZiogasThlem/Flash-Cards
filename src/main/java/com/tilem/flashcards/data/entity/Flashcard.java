package com.tilem.flashcards.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "flashcards")
public class Flashcard extends DbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private String answer;

    @ManyToOne
    @JoinColumn(name = "deck_id")
    private Deck deck;

    @Override
    public String getEntityTitle() {
        return question;
    }

    @Override
    public String getSimpleLabel() {
        return question;
    }

    @Override
    public Long getUniqueID() {
        return id;
    }
}