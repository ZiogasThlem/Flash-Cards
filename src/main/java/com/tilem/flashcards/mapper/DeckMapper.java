package com.tilem.flashcards.mapper;

import com.tilem.flashcards.data.dto.DeckDTO;
import com.tilem.flashcards.data.entity.Deck;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DeckMapper extends GenericMapper<Deck, DeckDTO> {

	@Mapping(
			target = "flashcards",
			expression =
					"java(entity.getFlashcards().stream().map(com.tilem.flashcards.data.entity.Flashcard::getSimpleLabel).collect(java.util.stream.Collectors.toList()))")
	DeckDTO toDto(Deck entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "flashcards", ignore = true)
	Deck toEntity(DeckDTO dto);

	@Mapping(target = "flashcards", ignore = true)
	@Mapping(target = "tempUuid", ignore = true)
	@Mapping(target = "tempUniqueID", ignore = true)
	void updateEntity(DeckDTO dto, @MappingTarget Deck entity);
}
