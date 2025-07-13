package com.tilem.flashcards.mapper;

import com.tilem.flashcards.data.dto.FlashcardDTO;
import com.tilem.flashcards.data.entity.Flashcard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
		componentModel = "spring",
		uses = {PromptMapper.class, YesNoMapper.class})
public interface FlashcardMapper extends GenericMapper<Flashcard, FlashcardDTO> {

	@Mapping(target = "deckId", source = "deck.id")
	FlashcardDTO toDto(Flashcard flashcard);

	@Mapping(target = "deck", ignore = true)
	Flashcard toEntity(FlashcardDTO flashcardDTO);

	@Mapping(target = "deck", ignore = true)
	void updateEntity(FlashcardDTO flashcardDTO, @MappingTarget Flashcard flashcard);
}