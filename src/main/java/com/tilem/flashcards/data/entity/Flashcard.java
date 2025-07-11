package com.tilem.flashcards.data.entity;

import com.tilem.flashcards.data.enums.YesNo;
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

    @ManyToOne
    @JoinColumn(name = "prompt_id")
    private Prompt prompt;

    @Enumerated(EnumType.STRING)
    private YesNo hasImageData;

    @ManyToOne
    @JoinColumn(name = "deck_id")
    private Deck deck;

    @Override
    public String getEntityTitle() {
        return "Flashcard";
    }

    @Override
    public String getSimpleLabel() {
        return "Flashcard " + id;
    }

    @Override
    public Long getUniqueID() {
        return id;
    }
}
