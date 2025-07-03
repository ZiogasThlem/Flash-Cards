package com.tilem.flashcards.data.entity;

import com.tilem.flashcards.data.enums.YesNo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

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
    @Enumerated(EnumType.STRING)
    private YesNo hasSingleAnswer;

    @OneToMany(mappedBy = "prompt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;

    @Override
    public String getEntityTitle() {
        return promptBody;
    }

    @Override
    public Long getUniqueID() {
        return id;
    }
}
