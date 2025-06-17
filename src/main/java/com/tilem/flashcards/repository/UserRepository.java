package com.tilem.flashcards.repository;

import com.tilem.flashcards.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}