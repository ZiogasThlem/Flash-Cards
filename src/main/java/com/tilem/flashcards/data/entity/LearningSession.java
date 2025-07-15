package com.tilem.flashcards.data.entity;

import com.tilem.flashcards.data.enums.YesNo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "learning_session")
public class LearningSession extends DbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flashcard_id")
    @NotNull
    private Flashcard flashcard;

    @Column(name = "last_reviewed_at")
    @NotNull
    private LocalDateTime lastReviewedAt;

    @Column(name = "next_review_at")
    @NotNull
    private LocalDateTime nextReviewAt;

    @Column(name = "repetitions")
    @NotNull
    private Integer repetitions;

    @Column(name = "ease_factor")
    @NotNull
    private Double easeFactor;

    @Column(name = "interval")
    @NotNull
    private Integer interval;

    @Column(name = "is_active")
    @Enumerated(EnumType.STRING)
    private YesNo isActive;

    @Override
    public String getEntityTitle() {
        return "Learning Session " + id;
    }

    @Override
    public Long getUniqueID() {
        return id;
    }
}