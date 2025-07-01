package com.tilem.flashcards.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "answers")
public class Answer {

    private long id;

    private Prompt prompt;

    private String body;

    private String notes;
}
