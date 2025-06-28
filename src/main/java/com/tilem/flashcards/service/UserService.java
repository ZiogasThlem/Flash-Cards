package com.tilem.flashcards.service;

import com.tilem.flashcards.data.dto.UserDTO;
import com.tilem.flashcards.util.AppException;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id) throws AppException;
    UserDTO createUser(UserDTO dto) throws Exception;
    UserDTO updateUser(Long id, UserDTO dto) throws Exception;
    void deleteUser(Long id);
}
