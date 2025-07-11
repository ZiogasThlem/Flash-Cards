package com.tilem.flashcards.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "answers")
public class Answer extends DbEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "prompt_id")
	private Prompt prompt;

	@NotBlank
	@Column(name = "answer_body")
	private String answerBody;

	private String notes;

	@Override
	public String getEntityTitle() {
		return answerBody;
	}

	@Override
	public Long getUniqueID() {
		return id;
	}
}
