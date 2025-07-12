package com.tilem.flashcards.service;

import com.tilem.flashcards.data.dto.UserDTO;
import com.tilem.flashcards.data.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends GenericService<User, UserDTO>, UserDetailsService {
	void assignDeckToUser(Long userId, Long deckId);
}