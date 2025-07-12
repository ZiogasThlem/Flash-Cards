package com.tilem.flashcards.mapper;

import com.tilem.flashcards.data.dto.UserDTO;
import com.tilem.flashcards.data.dto.UserResponseDTO;
import com.tilem.flashcards.data.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
		componentModel = "spring",
		uses = {DeckMapper.class})
public interface UserMapper extends GenericMapper<User, UserDTO> {

	@Mapping(target = "decks", source = "decks")
	UserDTO toDto(User user);

	@Mapping(target = "decks", source = "decks")
	UserResponseDTO toResponseDto(User user);

	@Mapping(target = "decks", ignore = true)
	User toEntity(UserDTO userDTO);

	@Mapping(target = "tempUuid", ignore = true)
	@Mapping(target = "tempUniqueID", ignore = true)
	void updateEntity(UserDTO userDTO, @MappingTarget User user);
}