package com.tilem.flashcards.data.entity;

import com.tilem.flashcards.data.enums.YesNo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
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

	@NotBlank
    @Column(name = "password")
    private String password;

	@Column(name = "is_active")
	@Enumerated(EnumType.STRING)
	private YesNo isActive;

	@Column(name = "last_login")
	private LocalDateTime lastLogin;

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "lastname")
	private String lastname;

	@Column(name = "email")
	private String email;

	@ManyToMany(
			cascade = {CascadeType.PERSIST, CascadeType.MERGE},
			fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_decks",
            joinColumns = @JoinColumn(name = "user_id"),
		    inverseJoinColumns = @JoinColumn(name = "deck_id"))
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
        return "Χρήστης";
    }

    @Override
    public Long getUniqueID() {
        return id;
    }

    @Override
    public String getSimpleLabel() {
        return username;
    }
}
