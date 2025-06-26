package com.tilem.flashcards.repository;

import com.tilem.flashcards.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}