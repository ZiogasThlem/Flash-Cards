package com.tilem.flashcards.data.entity;

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
@Table(name = "users")
public class User extends DbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, name = "username")
    @NotBlank
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_decks",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "deck_id")
    )
    @Builder.Default
    private List<Deck> decks = new ArrayList<>();

    public void addDeck(Deck deck) {
        this.decks.add(deck);
        deck.getUsers().add(this);
    }

    public void removeDeck(Deck deck) {
        this.decks.remove(deck);
        deck.getUsers().remove(this);
    }

    @Override
    public String getEntityTitle() {
        return username;
    }

    @Override
    public Long getUniqueID() {
        return id;
    }
}