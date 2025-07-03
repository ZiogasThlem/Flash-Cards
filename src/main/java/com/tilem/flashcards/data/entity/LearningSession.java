package com.tilem.flashcards.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "learning_sessions")
public class LearningSession extends DbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flashcard_id", nullable = false)
    private Flashcard flashcard;

    @Column(name = "last_reviewed_at", nullable = false)
    private LocalDateTime lastReviewedAt;

    @Column(name = "next_review_at", nullable = false)
    private LocalDateTime nextReviewAt;

    @Column(nullable = false)
    private Integer interval;

    @Column(nullable = false)
    private Double ease;

    @Column(nullable = false)
    private Integer repetitions;

    @Override
    public String getEntityTitle() {
        return "Learning Session " + id;
    }

    @Override
    public Long getUniqueID() {
        return id;
    }
}
