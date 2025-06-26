package com.tilem.flashcards.data.entity;

import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "decks")
    @Builder.Default
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Flashcard> flashcards = new ArrayList<>();

    public void addFlashcard(Flashcard flashcard) {
        this.flashcards.add(flashcard);
        flashcard.setDeck(this);
    }

    public void removeFlashcard(Flashcard flashcard) {
        this.flashcards.remove(flashcard);
        flashcard.setDeck(null);
    }

    @Override
    public String getEntityTitle() {
        return name;
    }

    @Override
    public Long getUniqueID() {
        return id;
    }
}