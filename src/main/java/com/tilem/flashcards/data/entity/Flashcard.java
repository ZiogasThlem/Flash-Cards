package com.tilem.flashcards.data.entity;

import com.tilem.flashcards.data.enums.YesNo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @Column(name = "question")
    private String question;

    @NotBlank
    private String answer;

    private YesNo hasManyCorrectAnswers;

    private YesNo hasImageData;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_data_id", referencedColumnName = "blob_id")
    private BlobData imageData;

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