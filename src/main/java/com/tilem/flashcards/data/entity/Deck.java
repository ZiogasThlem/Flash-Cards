package com.tilem.flashcards.data.entity;

import com.tilem.flashcards.data.enums.DeckCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "decks")
public class Deck extends DbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private DeckCategory category;

    @ManyToMany(mappedBy = "decks")
    @Builder.Default
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Flashcard> flashcards = new ArrayList<>();

    public void addFlashcard(Flashcard flashcard) {
        flashcards.add(flashcard);
        flashcard.setDeck(this);
    }

    public void removeFlashcard(Flashcard flashcard) {
        flashcards.remove(flashcard);
        flashcard.setDeck(null);
    }

    @Override
    public String getEntityTitle() {
        return "Deck";
    }

    @Override
    public Long getUniqueID() {
        return id;
    }

    @Override
    public String getSimpleLabel() {
        return id + " " + name;
    }
}