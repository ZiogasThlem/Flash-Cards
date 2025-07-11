package com.tilem.flashcards.service;

import com.tilem.flashcards.data.dto.PromptDTO;
import com.tilem.flashcards.data.entity.Prompt;

public interface PromptService extends GenericService<Prompt, PromptDTO> {
    Prompt mapToEntity(PromptDTO dto);

	void updateEntity(Prompt entity, PromptDTO dto);
}
