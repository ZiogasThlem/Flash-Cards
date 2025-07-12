package com.tilem.flashcards.repository;

import com.tilem.flashcards.data.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends GenericRepository<User> {

	Optional<User> findByUsername(String username);
}