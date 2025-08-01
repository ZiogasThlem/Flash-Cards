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
	@Mapping(target = "isActive", source = "isActive")
	@Mapping(target = "lastLogin", source = "lastLogin")
	@Mapping(target = "firstname", source = "firstname")
	@Mapping(target = "lastname", source = "lastname")
	@Mapping(target = "email", source = "email")
	UserDTO toDto(User user);

	@Mapping(target = "decks", source = "decks")
	@Mapping(target = "isActive", source = "isActive")
	@Mapping(target = "lastLogin", source = "lastLogin")
	@Mapping(target = "firstname", source = "firstname")
	@Mapping(target = "lastname", source = "lastname")
	@Mapping(target = "email", source = "email")
	UserResponseDTO toResponseDto(User user);

	@Mapping(target = "decks", ignore = true)
	User toEntity(UserDTO userDTO);

	@Mapping(target = "tempUuid", ignore = true)
	@Mapping(target = "tempUniqueID", ignore = true)
	void updateEntity(UserDTO userDTO, @MappingTarget User user);
}