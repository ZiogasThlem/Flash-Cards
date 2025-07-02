package com.tilem.flashcards.data.entity;

import com.tilem.flashcards.data.enums.YesNo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "prompts")
public class Prompt extends DbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "prompt_body")
    private String promptBody;

    @Column(name = "has_single_answer")
    private YesNo hasSingleAnswer;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Flashcard card;

    @Override
    public String getEntityTitle() {
        return "Ερώτηση";
    }

    @Override
    public Long getUniqueID() {
        return id;
    }
}
