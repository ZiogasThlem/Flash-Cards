package com.tilem.flashcards.repository;

import com.tilem.flashcards.data.entity.User;

import java.util.Optional;

public interface UserRepository extends GenericRepository<User> {

	Optional<User> findByUsername(String username);
}