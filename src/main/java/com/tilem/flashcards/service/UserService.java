package com.tilem.flashcards.service;

import com.tilem.flashcards.dto.UserDTO;
import com.tilem.flashcards.exception.AppException;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id) throws AppException;
}